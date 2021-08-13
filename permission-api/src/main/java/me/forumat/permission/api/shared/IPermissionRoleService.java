package me.forumat.permission.api.shared;

import me.forumat.permission.api.impl.model.PermissionRole;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public interface IPermissionRoleService {

    List<PermissionRole> loadRoles();

    List<PermissionRole> getRoles(Member member);

    PermissionRole getRole(String roleID);

    void saveRole(PermissionRole role);

    void createRole(PermissionRole role);

}
