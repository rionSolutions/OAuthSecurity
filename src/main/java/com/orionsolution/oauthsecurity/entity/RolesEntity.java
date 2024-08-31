package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "TB_ROLES", schema = "OAUTH")
public class RolesEntity {

    @Id
    private Long id;

    @Column(name = "ROLE_NAME")
    private String roleName;

}
