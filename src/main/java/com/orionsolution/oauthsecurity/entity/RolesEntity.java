package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_ROLES", schema = "OAUTH")
public class RolesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "NAME_ROLE")
  private String nameRole;

  @Column(name = "PERMISSIONS")
  private String permission;

  @Column(name = "ROLE_CODE", precision = 2)
  private char roleCode;

}
