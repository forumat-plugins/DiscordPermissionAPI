package me.forumat.permission.api.shared;

import me.forumat.permission.api.shared.model.IPermissionEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.concurrent.CompletableFuture;

public interface IPermissionEntityService {

    CompletableFuture<IPermissionEntity> getPermissionEntity(Member member);

    CompletableFuture<IPermissionEntity> getPermissionEntity(String memberID, Guild guild);

    void saveUser(IPermissionEntity entity);

}
