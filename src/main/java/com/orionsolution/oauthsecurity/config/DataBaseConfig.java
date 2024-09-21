package com.orionsolution.oauthsecurity.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;

@Configuration
@Slf4j
public class DataBaseConfig {

    private static final String ERROR = "Fail on connect to database!";

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverName;

    @Bean
    public DataSource dataSource() throws Exception {
        try {

            log.warn("folders : [{}] , [{}] ", password, username);

            String user = null;
            String pass = null;

            File userFile = new File(username.concat(".txt"));
            File passFile = new File(password.concat(".txt"));

            if (userFile.exists() && passFile.exists()) {
                log.warn("CREDENTIALS FOUND");
                user = Files.readString(userFile.toPath());
                pass = Files.readString(passFile.toPath());
                log.info("User : [{}] , Pass : [{}]", user, pass);
            }

            if (user != null && pass != null) {
                DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.driverClassName(driverName);
                dataSourceBuilder.url(url);
                dataSourceBuilder.username(user.trim());
                dataSourceBuilder.password(pass.trim());
                return dataSourceBuilder.build();
            }
            throw new Exception(ERROR + " NULL CREDENTIALS");
        } catch (Exception ex) {
            log.error(ERROR);
            throw new Exception(ERROR, ex);
        }

    }

}
