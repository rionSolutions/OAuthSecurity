package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "TB_USER_ROLE", schema = "OAUTH")
public class UserRolesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "ENCR_CREDENTIALS")
  private String encryptedCredentials;

  @ManyToMany
  @Column(name = "USER_ROLE", precision = 2)
  @JoinColumn(name = "ROLE_CODE")
  private List<RolesEntity> userRoleCode;

  @Column(name = "DT_INCLUDE_REGT")
  private LocalDateTime dtInclusion;

}
