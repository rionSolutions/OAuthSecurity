package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_PERMISSIONS", schema = "OAUTH")
public class PermissionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_permissions_seq")
    @SequenceGenerator(name = "tb_permissions_seq", allocationSize = 1, schema = "oauth")
    private Long id;

    @Column(name = "PERM_NAME")
    private String permissionName;

}
