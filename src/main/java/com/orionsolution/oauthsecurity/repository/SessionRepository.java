package com.orionsolution.oauthsecurity.repository;

import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import com.orionsolution.oauthsecurity.entity.SessionEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, String> {

    @Query("SELECT SSE FROM SessionEntity SSE " +
            "JOIN SSE.applicationRole.applicationEntity APP " +
            "WHERE SSE.credentialId = :credentialId")
    List<SessionEntity> getByCredentialSession(String credentialId, Pageable pageable);

    default List<SessionEntity> getByCredentialSessionWithLimit(String credentialId) {
        return getByCredentialSession(credentialId, PageRequest.of(0, 1));
    }
}
