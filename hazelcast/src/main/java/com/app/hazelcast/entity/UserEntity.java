package com.app.hazelcast.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER")
@Data
public class UserEntity {
  @Id @GeneratedValue private Integer userId;
  private String userName;
  private String contact;
}
