package org.example.social_network.repository.db;

import org.example.social_network.domain.Cerere;
import org.example.social_network.domain.Tuple;
import org.example.social_network.domain.validators.Validator;

import java.sql.*;

public class CerereDbRepository extends AbstractDbRepository<Tuple<Long, Long>, Cerere>{
    public CerereDbRepository(Validator<Cerere> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    public  PreparedStatement getSelectOneStatement(Connection c, Tuple<Long, Long> id) throws SQLException {
        PreparedStatement statement = c.prepareStatement("SELECT * FROM Requests WHERE (id_u1 = ? AND id_u2 = ?) OR (id_u1 = ? AND id_u2 = ?)");
        statement.setLong(1, id.getLeft());
        statement.setLong(2, id.getRight());
        statement.setLong(3, id.getRight());
        statement.setLong(4, id.getLeft());
        return statement;
    }
    public  PreparedStatement getSelectAllStatement(Connection c)  throws SQLException {
        return c.prepareStatement("SELECT * FROM Requests");
    }
    public  PreparedStatement getInsertStatement(Connection c, Cerere entity) throws SQLException{
        PreparedStatement statement = c.prepareStatement("INSERT INTO Requests(id_u1, id_u2, dt, status) VALUES (?,?,?,?)");
        statement.setLong(1, entity.getId().getLeft());
        statement.setLong(2, entity.getId().getRight());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getTimeSent()));
        statement.setString(4, entity.getStatus());
        return statement;
    }
    public  PreparedStatement getDeleteStatement(Connection c, Tuple<Long, Long> id)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("DELETE FROM Requests WHERE (id_u1 = ? AND id_u2 = ?) OR (id_u1 = ? AND id_u2 = ?)");
        statement.setLong(1, id.getLeft());
        statement.setLong(2, id.getRight());
        statement.setLong(3, id.getRight());
        statement.setLong(4, id.getLeft());
        return statement;
    }
    public  PreparedStatement getUpdateStatement(Connection c, Cerere entity)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("UPDATE Requests SET dt = ?, status = ? WHERE (id_u1 = ? AND id_u2 = ?) OR (id_u1 = ? AND id_u2 = ?)");
        statement.setTimestamp(1, Timestamp.valueOf(entity.getTimeSent()));
        statement.setString(2, entity.getStatus());
        statement.setLong(3, entity.getId().getLeft());
        statement.setLong(4, entity.getId().getRight());
        statement.setLong(5, entity.getId().getRight());
        statement.setLong(6, entity.getId().getLeft());
        return statement;
    }

    public Cerere createEntity(ResultSet resultSet) throws SQLException{
        Cerere u = new Cerere(resultSet.getTimestamp("dt").toLocalDateTime(), resultSet.getString("status"));
        u.setId(new Tuple<Long, Long>(resultSet.getLong("id_u1"), resultSet.getLong("id_u2")));
        return u;
    }
}
