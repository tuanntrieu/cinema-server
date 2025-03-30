package com.doan.cinemaserver.constant;

public enum RoleConstant {
    ROLE_ADMIN("Admin"),
    ROLE_USER("User");

    private String roleName;

    RoleConstant(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return this.roleName;
    }
}
