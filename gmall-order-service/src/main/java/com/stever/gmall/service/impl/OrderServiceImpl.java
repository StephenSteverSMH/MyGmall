package com.stever.gmall.service.impl;

import com.stever.gmall.service.OrderService;
import com.stever.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    @Autowired
    RedisUtil redisUtil;


    @Override
    public String checkTradeCode(String memberId, String tradeCode) {
        Jedis jedis = redisUtil.getJedis();
        String tradeKey="user:"+memberId+":tradeCode";
        String tradeCodeFromCache = jedis.get(tradeKey);
        if(tradeCodeFromCache!=null&&tradeCodeFromCache.equals(tradeCode)){
            jedis.close();
            return "success";
        }else {
            jedis.close();
            return "failed";
        }
    }

    @Override
    public String generateTradeCode(String memberId) {
        Jedis jedis = redisUtil.getJedis();
        String tradeCode = UUID.randomUUID().toString();
        String tradeKey="user:"+memberId+":tradeCode";
        jedis.setex(tradeKey,60*15,  tradeCode);
        jedis.close();
        return tradeCode;
    }
}
