package me.forumat.permission.api;

import lombok.Getter;
import lombok.SneakyThrows;
import me.forumat.permission.api.impl.PermissionEntityService;
import me.forumat.permission.api.impl.PermissionRoleService;
import me.forumat.permission.api.shared.IPermissionEntityService;
import me.forumat.permission.api.shared.IPermissionRoleService;

import java.sql.PreparedStatement;

@Getter
public class PermissionAPI {

    private static PermissionAPI API = null;
    private SQLLite sqlLite;

    private IPermissionRoleService permissionRoleService;
    private IPermissionEntityService permissionEntityService;

    public static PermissionAPI getAPI() {
        if (API == null) {
            API = new PermissionAPI();
            loadAPI();
        }
        return API;
    }

    private static void loadAPI() {
        API.sqlLite = new SQLLite();


        PreparedStatement preparedStatement = API.sqlLite.prepareStatement(
                "CREATE TABLE IF NOT EXISTS ranks (roleID TEXT, permissionRole TEXT)"
        );
        API.sqlLite.updateValue(preparedStatement, () -> {
        });

        API.permissionRoleService = new PermissionRoleService();
        API.permissionEntityService = new PermissionEntityService();
    }
}
