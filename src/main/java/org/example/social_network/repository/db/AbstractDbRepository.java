package org.example.social_network.repository.db;

import org.example.social_network.domain.Entity;
import org.example.social_network.domain.validators.Validator;
import org.example.social_network.repository.Repository;
import org.example.social_network.repository.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected final Validator<E> validator;

    private final String url;
    private final String username;
    private final String password;

    Connection connection;

    public AbstractDbRepository(Validator<E> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
    public abstract PreparedStatement getSelectOneStatement(Connection c,ID id) throws SQLException;
    public abstract PreparedStatement getSelectAllStatement(Connection c) throws SQLException;
    public abstract PreparedStatement getInsertStatement(Connection c, E entity) throws SQLException;
    public abstract PreparedStatement getDeleteStatement(Connection c, ID id) throws SQLException;
    public abstract PreparedStatement getUpdateStatement(Connection c, E entity) throws SQLException;

    public abstract E createEntity(ResultSet resultSet) throws SQLException;

    @Override
    public Optional<E> findOne(ID id) {
        try{
            PreparedStatement statement = getSelectOneStatement(connection, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.of(createEntity(resultSet));
        }
        catch (SQLException e){
            return Optional.empty();
        }
    }

    @Override
    public Iterable<E> findAll() {
        List<E> result = new ArrayList<E>();
        try{
            PreparedStatement statement = getSelectAllStatement(connection);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                result.add(createEntity(resultSet));
            }

            return result;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RepositoryException("db connection error");
        }
    }

    @Override
    public Optional<E> save(E entity) {
        if(entity==null)
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");
        validator.validate(entity);

        try{
            PreparedStatement statement = getInsertStatement(connection, entity);
            statement.executeUpdate();

            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID invalid");
        }

        try{
            Optional<E> obj = findOne(id);
            if(obj.isPresent()){
                PreparedStatement statement = getDeleteStatement(connection, id);
                statement.executeUpdate();
            }
            return obj;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> update(E entity) {
        try{
            Optional<E> obj = findOne(entity.getId());
            if(obj.isPresent())
                validator.validate(entity);

            PreparedStatement statement = getUpdateStatement(connection, entity);
            statement.executeUpdate();
            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.of(entity);
        }
    }
}
