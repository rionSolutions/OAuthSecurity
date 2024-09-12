package com.orionsolution.oauthsecurity.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "TB_SESSION", schema = "OAUTH")
public class SessionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_user_session_seq")
    @SequenceGenerator(name = "tb_user_session_seq", allocationSize = 1, schema = "oauth")
    private Long id = 1L;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "APPLICATION_ID")
    private ApplicationRoleEntity applicationRole;

    @Column(name = "DT_INCLUDE_REGT")
    private LocalDateTime dtInclusion;

    @Column(name = "DT_EXPIRATION_REGT")
    private LocalDateTime dtExpiration;

    @Column(name = "ACTIVE")
    private Boolean active;

    public static SessionEntity getSessionEntity(String client_secret,
                                                 Boolean active,
                                                 Long id,
                                                 Long duration) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(id);
        sessionEntity.setActive(active);
        sessionEntity.setDtInclusion(LocalDateTime.now());
        sessionEntity.setDtExpiration(LocalDateTime.now().plusMinutes(duration));
        ApplicationRoleEntity applicationRole = new ApplicationRoleEntity();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationRole.setApplicationEntity(applicationEntity);
        sessionEntity.setApplicationRole(applicationRole);
        sessionEntity.getApplicationRole().getApplicationEntity().setApplicationId(client_secret);
        return sessionEntity;
    }

}
