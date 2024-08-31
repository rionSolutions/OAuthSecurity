package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
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
