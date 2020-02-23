package com.stever.gmall.user.controller;


import com.stever.gmall.bean.UmsMember;
import com.stever.gmall.bean.UmsMemberReceiveAddress;
import com.stever.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
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
