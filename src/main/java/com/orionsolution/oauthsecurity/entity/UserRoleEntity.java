package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_USER_ROLE", schema = "OAUTH")
public class UserRoleEntity {

  @Id
  @Column(name = "CREDENTIALS")
  private String credentials;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "roleCode")
  private RolesEntity roleCode;

  @Column(name = "DT_INCLUDE_REGT")
  private LocalDateTime dtInclusion;

}
