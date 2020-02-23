package com.stever.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.stever.gmall.bean.OmsCartItem;
import com.stever.gmall.cart.mapper.OmsCartItemMapper;
import com.stever.gmall.service.CartService;
import com.stever.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;
    @Override
    public OmsCartItem ifCartExistedByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem;
    }
    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if(StringUtils.isNotBlank(omsCartItem.getMemberId())){
            omsCartItemMapper.insert(omsCartItem);
        }
    }
    @Override
    public void updateCart(OmsCartItem cartItem) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id", cartItem.getId());
        omsCartItemMapper.updateByExampleSelective(cartItem, e);
    }
    @Override
    public void flushCartCache(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems =  omsCartItemMapper.select(omsCartItem);
        Map<String ,String> map = new HashMap<>();
        for(OmsCartItem cartItem:omsCartItems){
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }


        //同步到redis缓存中
        Jedis jedis = redisUtil.getJedis();
        jedis.hmset("user:"+memberId+":cart", map);
    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
//        Jedis jedis = redisUtil.getJedis();
        List<OmsCartItem> omsCartItems;
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);
        omsCartItems = omsCartItemMapper.select(omsCartItem);
//        List<String> hvals = jedis.hvals("user:"+userId+":cart");
//        for(String hval : hvals){
//            OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
//            omsCartItems.add(omsCartItem);
//        }
//        jedis.close();
        return omsCartItems;
    }
}
