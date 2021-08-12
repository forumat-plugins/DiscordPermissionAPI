package me.forumat.permission.api.shared;

import me.forumat.permission.api.shared.model.IPermissionRole;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IPermissionRoleService {

    CompletableFuture<List<IPermissionRole>> loadRoles();

    IPermissionRole getRole(Member member);

    IPermissionRole getRole(String roleID);

    void saveRole(IPermissionRole role);

    void createRole(IPermissionRole role);

}
