package me.forumat.permission.api.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.forumat.permission.api.PermissionAPI;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PermissionRole {

    @Getter private final String roleId;
    @Getter private List<String> parents = new ArrayList<>();
    private List<PermissionNode> permissions = new ArrayList<>();

    public void addPermissions(PermissionNode... permissionNodes) {
        this.permissions.addAll(Arrays.asList(permissionNodes));
    }

    public void removePermissions(PermissionNode... permissionNodes) {
        this.permissions.removeAll(Arrays.asList(permissionNodes));
    }

    public List<PermissionNode> getPermissions() {
        List<PermissionNode> permissionNodes = new ArrayList<>(permissions);

        for (String parentRoleID : parents) {
            for (PermissionNode node : PermissionAPI.getAPI().getPermissionRoleService().getRole(parentRoleID).getPermissions()) {
                if (this.permissions.stream().noneMatch(permissionNode -> permissionNode.getPermissionString().equals(node.getPermissionString()))) {
                    permissionNodes.add(node);
                }
            }
        }

        return permissionNodes;
    }
}
