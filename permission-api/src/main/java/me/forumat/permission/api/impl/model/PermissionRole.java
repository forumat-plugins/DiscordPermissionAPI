package me.forumat.permission.api.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.forumat.permission.api.shared.model.IPermissionNode;
import me.forumat.permission.api.shared.model.IPermissionRole;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PermissionRole implements IPermissionRole {

    @Getter private final String roleId;
    @Getter private List<IPermissionRole> parents = new ArrayList<>();
    private List<IPermissionNode> permissions = new ArrayList<>();

    @Override
    public void addPermissions(IPermissionNode... permissionNodes) {
        this.permissions.addAll(Arrays.asList(permissionNodes));
    }

    @Override
    public void removePermissions(IPermissionNode... permissionNodes) {
        this.permissions.removeAll(Arrays.asList(permissionNodes));
    }

    public List<IPermissionNode> getPermissions() {
        List<IPermissionNode> permissionNodes = new ArrayList<>(permissions);

        for (IPermissionRole parent : parents) {
            permissionNodes.addAll(parent.getPermissions());
        }

        return permissionNodes;
    }
}
