package com.stever.gmall.service;



import com.stever.gmall.bean.UmsMember;
import com.stever.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;


public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressesByMemberId(String memberId);
}
