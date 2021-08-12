package me.forumat.permission.api.shared.model;

import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public interface IPermissionRole {

    String getRoleId();

    List<IPermissionRole> getParents();

    List<IPermissionNode> getPermissions();

    void addPermissions(IPermissionNode... permissionNode);

    void removePermissions(IPermissionNode... permissionNode);

}
