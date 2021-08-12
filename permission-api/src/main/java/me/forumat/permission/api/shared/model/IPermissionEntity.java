package me.forumat.permission.api.shared.model;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public interface IPermissionEntity {

    Member getMember();

    String getId();

    List<IPermissionRole> getPermissionRoles();

    boolean checkPermission(String permission);

}
