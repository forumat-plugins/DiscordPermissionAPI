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

    @Getter private static PermissionAPI API = null;
    private SQLLite sqlLite;

    private IPermissionRoleService permissionRoleService;
    private IPermissionEntityService permissionEntityService;

    @SneakyThrows
    public static void main(String[] args) {

        API = new PermissionAPI();

        API.sqlLite = new SQLLite();

        //ranks table
        {
            PreparedStatement preparedStatement = API.sqlLite.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS ranks (roleID TEXT, permissionRole TEXT)"
            );
            API.sqlLite.updateValue(preparedStatement, () -> {
            });
        }

        //users table
        {
            PreparedStatement preparedStatement = API.sqlLite.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS rankEntities (memberID TEXT, permissionEntity TEXT)"
            );
            API.sqlLite.updateValue(preparedStatement, () -> {
            });
        }

        API.permissionRoleService = new PermissionRoleService();
        API.permissionEntityService = new PermissionEntityService();
    }

    public PermissionAPI() throws Exception {
        if (API != null) throw new Exception("Cannot instanciate permission-api");
        API = this;
    }

}
