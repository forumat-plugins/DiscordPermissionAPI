package me.forumat.permission.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.shared.IPermissionRoleService;
import me.forumat.permission.api.shared.model.IPermissionRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PermissionRoleService implements IPermissionRoleService {

    private List<IPermissionRole> cachedRoles = null;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public CompletableFuture<List<IPermissionRole>> loadRoles() {
        CompletableFuture<List<IPermissionRole>> future = new CompletableFuture<>();

        if (cachedRoles != null) {
            future.complete(cachedRoles);
        } else {
            PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("SELECT * FROM ranks");

            PermissionAPI.getAPI().getSqlLite().getResult(preparedStatement).thenAccept(result -> {
                List<IPermissionRole> roles = new ArrayList<>();

                try {

                    while (result.next()) {
                        roles.add(gson.fromJson(result.getString("permissionRole"), IPermissionRole.class));
                    }

                    cachedRoles = roles;
                    future.complete(roles);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }


        return future;
    }

    @Override
    public IPermissionRole getRole(Member member) {

        for (Role role : member.getRoles()) {
            if (getRole(role.getId()) != null) {
                return getRole(role.getId());
            }
        }

        return null;

    }

    @Override
    public IPermissionRole getRole(String roleID) {
        if(cachedRoles == null) return null;

        return cachedRoles.stream().filter(permissionRole -> permissionRole.getRoleId().equals(roleID)).findFirst().orElse(null);
    }

    @SneakyThrows
    @Override
    public void saveRole(IPermissionRole role) {

        this.cachedRoles = null;

        PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("UPDATE ranks SET permissionRole=? WHERE roleID=?");
        preparedStatement.setString(1, gson.toJson(role));
        preparedStatement.setString(2, role.getRoleId());

        PermissionAPI.getAPI().getSqlLite().updateValue(preparedStatement, () -> { });

        loadRoles();
    }

    @SneakyThrows
    @Override
    public void createRole(IPermissionRole role) {

        PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("INSERT INTO ranks (permissionRole, roleID) VALUES (?, ?)");
        preparedStatement.setString(1, gson.toJson(role));
        preparedStatement.setString(2, role.getRoleId());

        PermissionAPI.getAPI().getSqlLite().updateValue(preparedStatement, () -> {});

    }
}
