package com.stever.gmall.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.UmsMember;
import com.stever.gmall.bean.UmsMemberReceiveAddress;
import com.stever.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping(value = "index")
    public String index(){
        return "hello user";
    }

    @RequestMapping("getAllUser")
    public List<UmsMember> getAllUser(){
        List<UmsMember> umsMembers = userService.getAllUser();

        return umsMembers;
    }

    @RequestMapping(value = "getReceiveAddressByMemeberId")
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemeberId(@RequestParam("memberId") String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = userService.getReceiveAddressesByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }


}
