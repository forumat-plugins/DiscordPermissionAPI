package me.forumat.permission.api.impl.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class PermissionEntity {

    private final String id, guildID;
    @Setter private List<PermissionRole> permissionRoles = new ArrayList<>();

    public boolean checkPermission(String permission) {
        for(PermissionRole permissionRole : permissionRoles) {
            if(permissionRole.getPermissions().stream().anyMatch(permissionNode -> permissionNode.getPower() >= 0 && permissionNode.getPermissionString().equals(permission))) {
                return true;
            }
        }
        return false;
    }
}
