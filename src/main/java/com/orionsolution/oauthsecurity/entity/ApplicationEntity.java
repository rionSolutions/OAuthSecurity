package com.orionsolution.oauthsecurity.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TB_APPLICATION", schema = "OAUTH")
public class ApplicationEntity {
    @Id
    private String hashId;

    @Column(name = "APPLICATION_NAME")
    private String applicationName;

}
