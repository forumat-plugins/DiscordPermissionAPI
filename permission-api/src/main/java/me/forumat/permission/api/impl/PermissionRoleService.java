package me.forumat.permission.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.impl.model.PermissionRole;
import me.forumat.permission.api.shared.IPermissionRoleService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionRoleService implements IPermissionRoleService {

    private List<PermissionRole> cachedRoles = null;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SneakyThrows
    @Override
    public List<PermissionRole> loadRoles() {

        if (cachedRoles != null) {
            return cachedRoles;
        } else {
            PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("SELECT * FROM ranks");

            ResultSet result = PermissionAPI.getAPI().getSqlLite().getResult(preparedStatement);
            List<PermissionRole> roles = new ArrayList<>();

            while (result.next()) {
                roles.add(gson.fromJson(result.getString("permissionRole"), PermissionRole.class));
            }

            cachedRoles = roles;
            return roles;
        }
    }

    @Override
    public List<PermissionRole> getRoles(Member member) {

        List<PermissionRole> roles = new ArrayList<>();
        for (Role role : member.getRoles()) {
            if (getRole(role.getId()) != null) {
                roles.add(getRole(role.getId()));
            }
        }

        return roles;

    }

    @Override
    public PermissionRole getRole(String roleID) {
        if (cachedRoles == null) {
            loadRoles();
            return getRole(roleID);
        }

        return cachedRoles.stream().filter(permissionRole -> permissionRole.getRoleId().equals(roleID)).findFirst().orElse(null);
    }

    @SneakyThrows
    @Override
    public void saveRole(PermissionRole role) {

        this.cachedRoles = null;

        PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("UPDATE ranks SET permissionRole=? WHERE roleID=?");
        preparedStatement.setString(1, gson.toJson(role));
        preparedStatement.setString(2, role.getRoleId());

        PermissionAPI.getAPI().getSqlLite().updateValue(preparedStatement, () -> {
        });

        loadRoles();
    }

    @SneakyThrows
    @Override
    public void createRole(PermissionRole role) {

        PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("INSERT INTO ranks (permissionRole, roleID) VALUES (?, ?)");
        preparedStatement.setString(1, gson.toJson(role));
        preparedStatement.setString(2, role.getRoleId());

        PermissionAPI.getAPI().getSqlLite().updateValue(preparedStatement, () -> {});

        cachedRoles = null;
        loadRoles();

    }
}
