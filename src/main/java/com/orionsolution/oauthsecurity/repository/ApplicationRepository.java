package com.orionsolution.oauthsecurity.repository;

import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {
    Optional<ApplicationEntity> findByApplicationId(String hashId);
}
