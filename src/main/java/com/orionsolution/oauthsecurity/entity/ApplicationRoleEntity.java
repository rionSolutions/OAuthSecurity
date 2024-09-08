package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "TB_APP_ROLE", schema = "OAUTH")
public class ApplicationRoleEntity {

    /**
     * Application entity
     */
    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "APPLICATION_ID")
    private ApplicationEntity applicationEntity;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "roleCode")
    private RolesEntity roleCode;

    @Column(name = "DT_INCLUDE_REGT")
    private LocalDateTime dtInclusion;

}
