package com.orionsolution.oauthsecurity.repository;


import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import com.orionsolution.oauthsecurity.entity.ApplicationRoleEntity;
import com.orionsolution.oauthsecurity.model.PermissionAppDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRoleRepository extends JpaRepository<ApplicationEntity, String> {


    @Query("SELECT new com.orionsolution.oauthsecurity.model.PermissionAppDTO( app.applicationName , approle.roleCode.id , roles.roleName ,pe.permissionName , pe.id) " +
            "FROM  ApplicationRoleEntity approle, ApplicationEntity app  , RolesPermissionEntity perm , RolesEntity roles , PermissionsEntity pe" +
            " WHERE app.applicationId =  approle.applicationEntity.applicationId" +
            " AND perm.role.id = approle.roleCode.id " +
            " AND perm.role.id = roles.id " +
            " AND pe.id = perm.permission.id" +
            " AND app.applicationId = :applicationId  " +
            " ORDER BY pe.id DESC " )
    List<PermissionAppDTO> findRoleByApplicationId(String applicationId);
}
