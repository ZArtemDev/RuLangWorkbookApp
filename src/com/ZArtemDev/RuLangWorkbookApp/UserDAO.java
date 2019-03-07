package com.ZArtemDev.RuLangWorkbookApp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    private DBConnector dbConnector = new DBConnector();

    private List<User> users = new ArrayList<>();

    public UserDAO(){
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get((int) id));
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void save(User user) {
        dbConnector.connectDataBase();
        String query = "INSERT INTO users(username, password, type) value('" + user.getName() + "'," + user.getPassword() + "," + user.getType() + ");";
        dbConnector.executeStatement(query);
        users.add(user);
        dbConnector.disconnectDataBase();
    }

    @Override
    public void update(User user, String[] params) {
        user.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        user.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));

        users.add(user);
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }
}
