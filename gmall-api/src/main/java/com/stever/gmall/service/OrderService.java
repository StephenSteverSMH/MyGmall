package com.stever.gmall.service;

public interface OrderService {
    String checkTradeCode(String memberId, String tradeCode);

    String generateTradeCode(String memberId);
}
