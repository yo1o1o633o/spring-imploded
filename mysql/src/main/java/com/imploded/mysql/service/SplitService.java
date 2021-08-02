package com.imploded.mysql.service;

import com.imploded.mysql.entity.User;
import com.imploded.mysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author shuai.yang
 */
public class SplitService {
    @Autowired
    UserRepository userRepository;

    public void doSplit() {
        List<User> userList = userRepository.findAll();
        System.out.println(userList);
    }
}
