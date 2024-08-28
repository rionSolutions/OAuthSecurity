package com.orionsolution.oauthsecurity.repository;

import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {
    ApplicationEntity findByHashId(String hashId);
}
