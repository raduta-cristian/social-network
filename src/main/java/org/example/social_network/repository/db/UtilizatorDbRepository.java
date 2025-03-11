package org.example.social_network.repository.db;

import org.example.social_network.domain.Utilizator;
import org.example.social_network.domain.validators.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilizatorDbRepository extends AbstractDbRepository<Long, Utilizator>{
    public UtilizatorDbRepository(Validator<Utilizator> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    public  PreparedStatement getSelectOneStatement(Connection c, Long id) throws SQLException {
        PreparedStatement statement = c.prepareStatement("SELECT * FROM Users WHERE id_u = ?");
        statement.setLong(1, id);
        return statement;
    }
    public  PreparedStatement getSelectAllStatement(Connection c)  throws SQLException {
        return c.prepareStatement("SELECT * FROM Users");
    }
    public  PreparedStatement getInsertStatement(Connection c, Utilizator entity) throws SQLException{
        PreparedStatement statement = c.prepareStatement("INSERT INTO Users(fname, lname, email, pass) VALUES (?,?,?,?);");
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getEmail());
        statement.setString(4, entity.getPassword());
        return statement;
    }
    public  PreparedStatement getDeleteStatement(Connection c, Long id)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("DELETE FROM Users WHERE id_u = ?");
        statement.setLong(1, id);
        return statement;
    }
    public  PreparedStatement getUpdateStatement(Connection c, Utilizator entity)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("UPDATE Users SET fname = ?, lname = ?, email = ?, pass = ? WHERE id_u = ?");
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getEmail());
        statement.setString(4, entity.getPassword());
        statement.setLong(5, entity.getId());
        return statement;
    }

    public  Utilizator createEntity(ResultSet resultSet) throws SQLException{
            Utilizator u = new Utilizator(resultSet.getString("fname"), resultSet.getString("lname"), resultSet.getString("email"), resultSet.getString( "pass"));
            u.setId(resultSet.getLong("id_u"));
            return u;
    }
}
