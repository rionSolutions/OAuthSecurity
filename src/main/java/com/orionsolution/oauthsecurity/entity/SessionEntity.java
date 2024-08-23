package com.orionsolution.oauthsecurity.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_USER_SESSION", schema = "OAUTH")
public class SessionEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Long id;

  @Column(name = "ENCR_CREDENTIALS")
  private String encryptedCredentials;

  @ManyToOne
  @Column(name = "USER_ROLE", precision = 2)
  @JoinColumn(name = "ROLE_CODE")
  private RolesEntity userRoleCode;

  @Column(name = "DT_INCLUDE_REGT")
  private LocalDateTime dtInclusion;

  @Column(name = "DT_EXPIRATION_REGT")
  private LocalDateTime dtExpiration;

}
