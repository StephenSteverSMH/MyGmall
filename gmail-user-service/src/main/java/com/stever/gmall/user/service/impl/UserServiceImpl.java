package com.stever.gmall.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.stever.gmall.bean.UmsMember;
import com.stever.gmall.bean.UmsMemberReceiveAddress;
import com.stever.gmall.service.UserService;
import com.stever.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.stever.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {

        return userMapper.selectAll();
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressesByMemberId(String memberId) {
        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().andEqualTo("memberId", memberId);
        return  umsMemberReceiveAddressMapper.selectByExample(e);
    }
}
