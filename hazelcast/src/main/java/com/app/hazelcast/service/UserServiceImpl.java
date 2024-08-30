package com.app.hazelcast.service;

import com.app.hazelcast.config.HazelcastConfig.CacheConstant;
import com.app.hazelcast.entity.UserEntity;
import com.app.hazelcast.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepo userRepo;
  private final CacheManager cacheManager;

  @Override
  public UserEntity saveUser(UserEntity user) {
    UserEntity save = userRepo.save(user);
    saveAndCached(save);
    return save;
  }

  @Override
  @Transactional
  public UserEntity updateUser(UserEntity user) {
    return Optional.ofNullable(userRepo.findByUserId(user.getUserId()))
        .map(
            byUserId -> {
              byUserId.setUserName(user.getUserName());
              byUserId.setContact(user.getContact());
              UserEntity entity = userRepo.save(byUserId);
              return saveAndCached(entity);
            })
        .orElseGet(
            () -> {
              UserEntity entity = userRepo.save(user);
              return saveAndCached(entity);
            });
  }

  @Override
  public UserEntity findUser(Integer userId) {
    return Optional.ofNullable(cacheManager.getCache(CacheConstant.CACHE_NAME))
        .map(
            cache ->
                Optional.ofNullable(cache.get(userId))
                    .map(user -> (UserEntity) user.get())
                    .orElseGet(() -> getAndCached(userId, cache)))
        .orElseGet(() -> getAndCached(userId, null));
  }

  UserEntity getAndCached(Integer userId, Cache cache) {
    UserEntity byUserId = userRepo.findByUserId(userId);
    Optional.ofNullable(cache).ifPresent(obj -> obj.put(userId, byUserId));
    return byUserId;
  }

  UserEntity saveAndCached(UserEntity userEntity) {
    Optional.ofNullable(cacheManager.getCache(CacheConstant.CACHE_NAME))
        .ifPresentOrElse(
            cache -> cache.put(userEntity.getUserId(), userEntity),
            () -> log.error("Cache not Present"));

    return userEntity;
  }
}
