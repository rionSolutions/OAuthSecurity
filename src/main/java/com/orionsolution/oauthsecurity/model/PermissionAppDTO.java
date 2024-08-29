package com.orionsolution.oauthsecurity.model;


import lombok.Data;

@Data
public class PermissionAppDTO
{
    private String applicationName;
    private Long roleCode;
    private String roleName;
    private String permissionName;
    private Long permissionId;

    public PermissionAppDTO( String applicationName, Long roleCode, String roleName, String permissionName , Long permissionId) {
        this.applicationName = applicationName;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.permissionName = permissionName;
        this.permissionId = permissionId;
    }

    public static String getUniqueKey(PermissionAppDTO permissionAppDTO) {
        return permissionAppDTO.getPermissionId().toString() + permissionAppDTO.getRoleCode().toString();
    }
}
