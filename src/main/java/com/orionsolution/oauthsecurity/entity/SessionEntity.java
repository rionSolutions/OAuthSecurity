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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "APPLICATION_ID")
    private ApplicationRoleEntity applicationRole;

    @Column(name = "CREDENTIAL_ID")
    private String credentialId;

    @Column(name = "DT_INCLUDE_REGT")
    private LocalDateTime dtInclusion;

    @Column(name = "DT_EXPIRATION_REGT")
    private LocalDateTime dtExpiration;

    @Column(name = "ACTIVE")
    private Boolean active;

}
