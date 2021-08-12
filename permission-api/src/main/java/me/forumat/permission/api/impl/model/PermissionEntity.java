package me.forumat.permission.api.impl.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.forumat.permission.api.shared.model.IPermissionEntity;
import me.forumat.permission.api.shared.model.IPermissionRole;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PermissionEntity implements IPermissionEntity {

    private final Member member;
    private final String id;
    private final List<IPermissionRole> permissionRoles;


    @Override
    public boolean checkPermission(String permission) {
        for(IPermissionRole permissionRole : permissionRoles) {
            if(permissionRole.getPermissions().stream().anyMatch(permissionNode -> !permissionNode.isNegated() && permissionNode.getPermissionString().equals(permission))) {
                return true;
            }
        }
        return false;
    }
}
