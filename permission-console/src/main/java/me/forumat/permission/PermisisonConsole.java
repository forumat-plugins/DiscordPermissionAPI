package me.forumat.permission;

import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.impl.model.PermissionNode;
import me.forumat.permission.api.impl.model.PermissionRole;
import me.forumat.permission.api.shared.model.IPermissionRole;
import me.forumat.permission.command.SimpleCommandManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class PermisisonConsole {

    public static void main(String[] arguments) {
        PermissionAPI permissionAPI = PermissionAPI.getAPI();

        SimpleCommandManager commandManager = new SimpleCommandManager();

        commandManager.getCommands().put("createrank", args -> {
            //createrank <name> <parents...>
            if (args.length < 2) {
                System.out.println("createrank <roleID> <parentgroups...>");
                return;
            }
            String[] parentGroupIDs = Arrays.copyOfRange(args, 2, args.length);
            IPermissionRole permissionRole = new PermissionRole(
                    args[0],
                    Arrays.stream(parentGroupIDs).map(groupID -> permissionAPI.getPermissionRoleService().getRole(groupID)).collect(Collectors.toList()),
                    new ArrayList<>());
            permissionAPI.getPermissionRoleService().createRole(permissionRole);
            System.out.println("created rank for roleID: " + args[0] + " with parents: " + Arrays.toString(parentGroupIDs));
        });

        commandManager.getCommands().put("addpermission", args -> {

            if(args.length != 3) {
                System.out.println("addpermission <roleID> <permission> <negated>");
                return;
            }

            IPermissionRole permissionRole = permissionAPI.getPermissionRoleService().getRole(args[0]);
            permissionRole.addPermissions(new PermissionNode(args[1], Boolean.getBoolean(args[2])));
            permissionAPI.getPermissionRoleService().saveRole(permissionRole);
            System.out.println("Added permission " + args[1] + " to role " + args[0]);

        });

        new Thread(() -> {

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {

                String command = scanner.next();
                if (commandManager.getCommands().containsKey(command.split(" ")[0])) {
                    commandManager.getCommands().get(command.split(" ")[0]).accept(Arrays.copyOfRange(command.split(" "), 1, command.split(" ").length));
                }

            }

        }, "command-listener").start();

    }

}
