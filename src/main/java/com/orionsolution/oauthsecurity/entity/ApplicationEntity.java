package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "TB_APPLICATION", schema = "OAUTH")
public class ApplicationEntity {

    @Id
    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "APPLICATION_NAME")
    private String applicationName;

}
