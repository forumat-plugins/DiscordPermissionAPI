package me.forumat.permission.api;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.SneakyThrows;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLLite {

    private Connection connection;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public SQLLite() {
        try {

            SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
            sqLiteDataSource.setUrl("jdbc:sqlite:dataBase.db");

            connection = sqLiteDataSource.getConnection();

        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        try {

            return this.connection.prepareStatement(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateValue(PreparedStatement preparedStatement, Runnable whenDone) {
        executorService.execute(() -> {
            try {
                preparedStatement.executeUpdate();
                whenDone.run();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<ResultSet> getResultAsync(PreparedStatement preparedStatement) {
        CompletableFuture<ResultSet> future = new CompletableFuture<>();

        executorService.execute(() -> {
            try {
                future.complete(preparedStatement.executeQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    @SneakyThrows
    public ResultSet getResult(PreparedStatement preparedStatement) {
        return preparedStatement.executeQuery();
    }

}
