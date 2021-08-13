package me.forumat.permission.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.impl.model.PermissionEntity;
import me.forumat.permission.api.shared.IPermissionEntityService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PermissionEntityService implements IPermissionEntityService {

    @Override
    public CompletableFuture<PermissionEntity> getPermissionEntity(Member member) {
        return getPermissionEntity(member.getId(), member.getGuild());
    }

    @SneakyThrows
    @Override
    public CompletableFuture<PermissionEntity> getPermissionEntity(String memberID, Guild guild) {

        CompletableFuture<PermissionEntity> future = new CompletableFuture<>();
        guild.retrieveMemberById(memberID).queue(member -> {

            PermissionEntity permissionEntity = new PermissionEntity(memberID, guild.getId());
            permissionEntity.setPermissionRoles(PermissionAPI.getAPI().getPermissionRoleService().getRoles(member));

            future.complete(permissionEntity);

        });

        return future;
    }

}
