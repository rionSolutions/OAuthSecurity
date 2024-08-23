package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "TB_ROLES", schema = "OAUTH")
public class RolesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "tb_roles_seq")
  @SequenceGenerator(name = "tb_roles_seq", allocationSize = 1, schema = "oauth")
  private Long id;

  @Column(name = "ROLE_NAME")
  private String roleName;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private PermissionsEntity permissionId;

}
