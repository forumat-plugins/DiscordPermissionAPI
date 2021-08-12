package me.forumat.permission.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import me.forumat.permission.api.PermissionAPI;
import me.forumat.permission.api.impl.model.PermissionEntity;
import me.forumat.permission.api.impl.model.PermissionRole;
import me.forumat.permission.api.shared.IPermissionEntityService;
import me.forumat.permission.api.shared.model.IPermissionEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import sun.dc.pr.PRError;

import java.lang.ref.PhantomReference;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PermissionEntityService implements IPermissionEntityService {

    private final List<IPermissionEntity> cachedPermissionEntities = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public CompletableFuture<IPermissionEntity> getPermissionEntity(Member member) {
        return getPermissionEntity(member.getId(), member.getGuild());
    }

    @SneakyThrows
    @Override
    public CompletableFuture<IPermissionEntity> getPermissionEntity(String memberID, Guild guild) {
        CompletableFuture<IPermissionEntity> future = new CompletableFuture<>();

        Optional<IPermissionEntity> cachedEntity = cachedPermissionEntities.stream().filter(entity -> entity.getId().equals(memberID)).findAny();
        if (cachedEntity.isPresent()) {
            future.complete(cachedEntity.get());
        } else {
            PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("SELECT * FROM rankEntities WHERE memberID=?");
            preparedStatement.setString(1, memberID);

            PermissionAPI.getAPI().getSqlLite().getResult(preparedStatement).thenAccept(result -> {
                try {

                    if (!result.next()) {
                        PreparedStatement insertUser = PermissionAPI.getAPI().getSqlLite().prepareStatement("INSERT INTO rankEntities (memberID, permissionEntity) VALUES (?, ?)");
                        guild.retrieveMemberById(memberID).queue(member -> {
                            IPermissionEntity permissionEntity = new PermissionEntity(member, memberID, new ArrayList<>());
                            try {

                                insertUser.setString(1, memberID);
                                insertUser.setString(2, gson.toJson(permissionEntity));
                                future.complete(permissionEntity);

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        IPermissionEntity entity = gson.fromJson(result.getString("permissionEntity"), IPermissionEntity.class);
                        cachedPermissionEntities.add(entity);
                        future.complete(entity);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        return future;
    }

    @SneakyThrows
    @Override
    public void saveUser(IPermissionEntity entity) {

        PreparedStatement preparedStatement = PermissionAPI.getAPI().getSqlLite().prepareStatement("UPDATE rankEntities SET permissionEntity=? WHERE memberID=?");
        preparedStatement.setString(1, gson.toJson(entity));
        preparedStatement.setString(2, entity.getId());

        PermissionAPI.getAPI().getSqlLite().updateValue(preparedStatement, () -> { });

    }
}
