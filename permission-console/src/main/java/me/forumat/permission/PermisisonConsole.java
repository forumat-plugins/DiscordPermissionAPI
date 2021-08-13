package me.forumat.permission;

import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.impl.model.PermissionNode;
import me.forumat.permission.api.impl.model.PermissionRole;
import me.forumat.permission.command.SimpleCommandManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class PermisisonConsole {

    public static void main(String[] arguments) {
        PermissionAPI permissionAPI = PermissionAPI.getAPI();
        permissionAPI.getPermissionRoleService().loadRoles();

        SimpleCommandManager commandManager = new SimpleCommandManager();

        commandManager.getCommands().put("createrank", args -> {
            //createrank <name> <parents...>
            System.out.println(Arrays.toString(args));
            if (args.length < 1) {
                System.out.println("createrank <roleID> <parentgroups...>");
                return;
            }
            String[] parentGroupIDs = args.length == 1 ? new String[]{} : Arrays.copyOfRange(args, 1, args.length);
            PermissionRole permissionRole = new PermissionRole(
                    args[0],
                    Arrays.asList(parentGroupIDs),
                    new ArrayList<>());
            permissionAPI.getPermissionRoleService().createRole(permissionRole);
            System.out.println("created rank for roleID: " + args[0] + " with parents: " + Arrays.toString(parentGroupIDs));
        });

        commandManager.getCommands().put("addpermission", args -> {

            if (args.length != 3) {
                System.out.println("addpermission <roleID> <permission> <power>");
                return;
            }

            PermissionRole permissionRole = permissionAPI.getPermissionRoleService().getRole(args[0]);
            permissionRole.addPermissions(new PermissionNode(args[1], Integer.parseInt(args[2])));
            permissionAPI.getPermissionRoleService().saveRole(permissionRole);
            System.out.println("Added permission " + args[1] + " to role " + args[0]);

        });

        commandManager.getCommands().put("removepermission", args -> {
            if (args.length != 3) {
                System.out.println("addpermission <roleID> <permission> <power>");
                return;
            }

            PermissionRole permissionRole = permissionAPI.getPermissionRoleService().getRole(args[0]);
            permissionRole.removePermissions(new PermissionNode(args[1], Integer.parseInt(args[2])));
            permissionAPI.getPermissionRoleService().saveRole(permissionRole);
            System.out.println("Removed permission " + args[1] + " from role " + args[0]);
        });

        commandManager.getCommands().put("permissions", args -> {

            if (args.length != 1) {
                System.out.println("permissions <roleID>");
                return;
            }

            PermissionRole rank = permissionAPI.getPermissionRoleService().getRole(args[0]);
            if (rank == null) {
                System.out.println("rank not found");
                return;
            }
            for (PermissionNode permissionNode : rank.getPermissions()) {
                System.out.println("» " + permissionNode.getPermissionString() + " | " + permissionNode.getPower());
            }

        });

        new Thread(() -> {

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {

                String command = scanner.nextLine();
                String[] args = command.split(" ");
                if (commandManager.getCommands().containsKey(args[0])) {
                    commandManager.getCommands().get(args[0]).accept(Arrays.copyOfRange(args, 1, args.length));
                }

            }

        }, "command-listener").start();

    }

}
