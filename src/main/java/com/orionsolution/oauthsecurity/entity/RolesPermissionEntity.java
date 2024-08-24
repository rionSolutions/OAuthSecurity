package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_PERMISSION_ROLES", schema = "OAUTH")
public class RolesPermissionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_permissions_roles_seq")
  @SequenceGenerator(name = "tb_permissions_roles_seq", allocationSize = 1, schema = "oauth")
  private Long id;
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private RolesEntity role;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private PermissionsEntity permission;

}
