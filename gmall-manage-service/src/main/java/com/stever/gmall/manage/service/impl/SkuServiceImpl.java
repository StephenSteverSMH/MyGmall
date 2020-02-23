package com.stever.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.stever.gmall.bean.PmsSkuAttrValue;
import com.stever.gmall.bean.PmsSkuInfo;
import com.stever.gmall.bean.PmsSkuSaleAttrValue;
import com.stever.gmall.manage.mapper.PmsSKuInfoMapper;
import com.stever.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.stever.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.stever.gmall.service.SkuService;
import com.stever.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSKuInfoMapper pmsSKuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    RedisUtil redisUtil;

    ReentrantLock reentrantLock = new ReentrantLock();
    Condition waitCondition = reentrantLock.newCondition();


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // 插入skuinfo
        pmsSKuInfoMapper.insertSelective(pmsSkuInfo);
        //插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for(PmsSkuAttrValue pmsSkuAttrValue:skuAttrValueList){
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        //插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList){
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
        //插入图片信息
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuid){
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuid);
        pmsSkuInfo = pmsSKuInfoMapper.selectOne(pmsSkuInfo);
        return pmsSkuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuid) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuid);
        //连接缓存
        Jedis jedis = redisUtil.getJedis();
        //查询缓存
        String skuKey = "sku:"+skuid+":info";
        String skuJson = jedis.get(skuKey);
        if(StringUtils.isNotBlank(skuJson)){
            System.out.println(Thread.currentThread().getId()+"获取到缓存");
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        }else {
            String uuid = UUID.randomUUID().toString();
            String ok = jedis.set("sku:"+skuid+":lock", uuid, "nx", "ex", 10);
            if(StringUtils.isNotBlank(ok)&&ok.equals("OK")){
                System.out.println(Thread.currentThread().getId()+"成功获得到锁");
                pmsSkuInfo = getSkuByIdFromDb(skuid);
                //如果缓存没有，查询mysql
                pmsSkuInfo = getSkuByIdFromDb(skuid);
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                if(pmsSkuInfo!=null){
                    //将查询结果存入redis
                    jedis.setex("sku:"+skuid+":info",4,  JSON.toJSONString(pmsSkuInfo));
                }else{
                    //处理 '缓存穿透'
                    jedis.setex("sku:"+skuid+":info", 5 * 60, JSON.toJSONString(""));
                }
                //防止误删锁
                String saved = jedis.get("sku:"+skuid+":lock");//特殊情况，如果刚刚get到，突然过期了，也会误删，需要用lua脚本保证查询和删除的原子性
                if(StringUtils.isNotBlank(saved)&&saved.equals(uuid)){
                    jedis.del("sku:"+skuid+":lock");
                }
                reentrantLock.lock();
                waitCondition.signalAll();
                reentrantLock.unlock();
            }else{
                System.out.println(Thread.currentThread().getId()+"等待");
                 //设置失败，自选（线程睡眠几秒后，重新尝试访问本方法）
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                reentrantLock.lock();
                try {
                    waitCondition.await(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLock.unlock();
                jedis.close();
                return getSkuById(skuid);
            }
        }
        jedis.close();
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfos =  pmsSKuInfoMapper.getSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSKuInfoMapper.selectAll();
        for(PmsSkuInfo pmsSkuInfo : pmsSkuInfos){
            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }
}
