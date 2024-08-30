package com.app.hazelcast.controller;

import com.app.hazelcast.entity.UserEntity;
import com.app.hazelcast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping("/save")
  public ResponseEntity<UserEntity> saveUser(@RequestBody UserEntity user) {
    return ResponseEntity.ok().body(userService.saveUser(user));
  }

  @GetMapping("/get/{userId}")
  public ResponseEntity<UserEntity> findUser(@PathVariable Integer userId) {
    UserEntity user = userService.findUser(userId);
    return ResponseEntity.ok().body(user);
  }

  @PutMapping("/update")
  public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user) {
    UserEntity userUpdated = userService.updateUser(user);
    return ResponseEntity.ok().body(userUpdated);
  }

  @GetMapping("/status")
  public String getStatus() {
    return "Working";
  }
}
