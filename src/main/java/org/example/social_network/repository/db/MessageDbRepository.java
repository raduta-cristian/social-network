package org.example.social_network.repository.db;

import org.example.social_network.domain.Message;
import org.example.social_network.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDbRepository extends  AbstractDbRepository<Long, Message> {
    public MessageDbRepository(Validator<Message> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    public PreparedStatement getSelectOneStatement(Connection c, Long id) throws SQLException {
        PreparedStatement statement = c.prepareStatement("SELECT * FROM Messages WHERE id_m = ?");
        statement.setLong(1, id);
        return statement;
    }
    public  PreparedStatement getSelectAllStatement(Connection c)  throws SQLException {
        return c.prepareStatement("SELECT * FROM Messages");
    }
    public  PreparedStatement getInsertStatement(Connection c, Message entity) throws SQLException{
        PreparedStatement statement = c.prepareStatement("INSERT INTO Messages(id_from, text, dt, reply_to) VALUES(?, ?, ?, ?) RETURNING id_m;");
        statement.setLong(1, entity.getFromId());
        statement.setString(2, entity.getText());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDateTime()));
        if(entity.getReplyTo() != null)
            statement.setLong(4, entity.getReplyTo());
        else
            statement.setNull(4, Types.NULL);
        return statement;
    }
    public  PreparedStatement getDeleteStatement(Connection c, Long id)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("DELETE FROM Users WHERE id_m = ?");
        statement.setLong(1, id);
        return statement;
    }
    public  PreparedStatement getUpdateStatement(Connection c, Message entity)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("UPDATE Users SET id_from = ?, text = ?, dt = ?, reply_to WHERE id_m = ?");
        statement.setLong(1, entity.getFromId());
        statement.setString(2, entity.getText());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDateTime()));
        statement.setLong(4, entity.getId());
        return statement;
    }

    public  Message createEntity(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id_m");
        Long fromId = resultSet.getLong("id_from");
        String text = resultSet.getString("text");
        LocalDateTime dateTime = resultSet.getTimestamp("dt").toLocalDateTime();
        Long replyTo = resultSet.getLong("reply_to");

        List<Long> toIdList = getToIdList(id);

        Message m = new Message(fromId, toIdList, text, dateTime, replyTo);
        m.setId(id);
        return m;
    }

    private List<Long> getToIdList(Long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id_to FROM MessageToUser WHERE id_m = ?");
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        List<Long> toIdList = new ArrayList<>();
        while (resultSet.next()) {
            toIdList.add(resultSet.getLong("id_to"));
        }
        return toIdList;
    }

    private void setToIdList(List<Long> toIdList, Long id) throws SQLException {
        PreparedStatement statement1 = connection.prepareStatement("DELETE FROM MessageToUser WHERE id_m = ?");
        statement1.setLong(1, id);
        statement1.executeUpdate();
        toIdList.forEach(toId -> {
            PreparedStatement statement2 = null;
            try {
                statement2 = connection.prepareStatement("INSERT INTO MessageToUser(id_m, id_to) VALUES (?, ?)");
                statement2.setLong(1, id);
                statement2.setLong(2, toId);
                statement2.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Optional<Message> save(Message entity) {
        if(entity==null)
            throw new IllegalArgumentException("ENTITY CANNOT BE NULL");

        validator.validate(entity);

        try{
            PreparedStatement statement = getInsertStatement(connection, entity);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            Long id = resultSet.getLong("id_m");
            setToIdList(entity.getToIdList(), id);
            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        try{
            Optional<Message> obj = findOne(entity.getId());
            if(obj.isPresent())
                validator.validate(entity);

            PreparedStatement statement = getUpdateStatement(connection, entity);
            statement.executeUpdate();

            setToIdList(entity.getToIdList(), entity.getId());

            return Optional.empty();
        }
        catch (SQLException e){
            return Optional.of(entity);
        }
    }
}