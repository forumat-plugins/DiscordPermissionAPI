package me.forumat.permission.api.shared;

import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.impl.model.PermissionEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.concurrent.CompletableFuture;

public interface IPermissionEntityService {

    CompletableFuture<PermissionEntity> getPermissionEntity(Member member);

    CompletableFuture<PermissionEntity> getPermissionEntity(String memberID, Guild guild);

}
