package com.orionsolution.oauthsecurity.repository;

import com.orionsolution.oauthsecurity.entity.PermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionsRepository extends JpaRepository<PermissionsEntity, String> {

    @Query("SELECT PE FROM RolesPermissionEntity RE JOIN PermissionsEntity PE " +
            " ON PE.id = RE.permission.id" +
            " WHERE RE.role.id = :roleId" +
            " ORDER BY PE.id DESC ")
    List<PermissionsEntity> getPermissionsEntityByRoleId(Long roleId);


}
