package com.app.hazelcast.service;

import com.app.hazelcast.entity.UserEntity;
import org.apache.catalina.User;

public interface UserService {
    public UserEntity saveUser(UserEntity user);

    UserEntity findUser(Integer userId);

    UserEntity updateUser(UserEntity userEntity);
}
