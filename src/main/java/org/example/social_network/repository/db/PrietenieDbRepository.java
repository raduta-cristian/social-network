package org.example.social_network.repository.db;

import org.example.social_network.domain.dto.FilterDTO;
import org.example.social_network.domain.Prietenie;
import org.example.social_network.domain.dto.PrietenieFilterDTO;
import org.example.social_network.domain.Tuple;
import org.example.social_network.domain.validators.Validator;
import org.example.social_network.repository.PagingRepository;
import org.example.social_network.utils.Pair;
import org.example.social_network.utils.paging.Page;
import org.example.social_network.utils.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrietenieDbRepository extends AbstractDbRepository<Tuple<Long, Long>, Prietenie> implements PagingRepository<Tuple<Long, Long>, Prietenie> {
    public PrietenieDbRepository(Validator<Prietenie> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    public  PreparedStatement getSelectOneStatement(Connection c, Tuple<Long, Long> id) throws SQLException {
        PreparedStatement statement = c.prepareStatement("SELECT * FROM Friendships WHERE (id_u1 = ? AND id_u2 = ?) OR (id_u1 = ? AND id_u2 = ?)");
        statement.setLong(1, id.getLeft());
        statement.setLong(2, id.getRight());
        statement.setLong(3, id.getRight());
        statement.setLong(4, id.getLeft());
        return statement;
    }
    public  PreparedStatement getSelectAllStatement(Connection c)  throws SQLException {
        return c.prepareStatement("SELECT * FROM Friendships");
    }
    public  PreparedStatement getInsertStatement(Connection c, Prietenie entity) throws SQLException{
        PreparedStatement statement = c.prepareStatement("INSERT INTO Friendships(id_u1, id_u2, dt) VALUES (?,?,?)");
        statement.setLong(1, entity.getId().getLeft());
        statement.setLong(2, entity.getId().getRight());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
        return statement;
    }
    public  PreparedStatement getDeleteStatement(Connection c, Tuple<Long, Long> id)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("DELETE FROM Friendships WHERE (id_u1 = ? AND id_u2 = ?) OR (id_u1 = ? AND id_u2 = ?)");
        statement.setLong(1, id.getLeft());
        statement.setLong(2, id.getRight());
        statement.setLong(3, id.getRight());
        statement.setLong(4, id.getLeft());
        return statement;
    }
    public  PreparedStatement getUpdateStatement(Connection c, Prietenie entity)  throws SQLException {
        PreparedStatement statement = c.prepareStatement("UPDATE Friendships SET dt = ?  WHERE (id_u1 = ? AND id_u2 = ?) OR (id_u1 = ? AND id_u2 = ?)");
        statement.setTimestamp(1, Timestamp.valueOf(entity.getFriendsFrom()));
        statement.setLong(2, entity.getId().getLeft());
        statement.setLong(3, entity.getId().getRight());
        statement.setLong(4, entity.getId().getRight());
        statement.setLong(5, entity.getId().getLeft());
        return statement;
    }

    public  Prietenie createEntity(ResultSet resultSet) throws SQLException{
        Prietenie u = new Prietenie(resultSet.getTimestamp("dt").toLocalDateTime());
        u.setId(new Tuple<Long, Long>(resultSet.getLong("id_u1"), resultSet.getLong("id_u2")));
        return u;
    }

    private Pair<String, List<Object>> toSql(PrietenieFilterDTO filter) {
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        filter.getId().ifPresent(id -> {
            conditions.add("(id_u1 = ? OR id_u2 = ?)");
            params.add(id);
            params.add(id);
        });
        String sql = String.join(" and ", conditions);
        return new Pair<>(sql, params);
    }

    private int count(PrietenieFilterDTO filter) throws SQLException {
        String sql = "select count(*) as count from Friendships";
        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getFirst().isEmpty()) {
            sql += " where " + sqlFilter.getFirst();
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfFriendships = 0;
                if (result.next()) {
                    totalNumberOfFriendships = result.getInt("count");
                }
                return totalNumberOfFriendships;
            }
        }
    }

    @Override
    public Page<Prietenie> findAllOnPage(Pageable pageable, FilterDTO filter) {
        List<Prietenie> friendships = new ArrayList<>();

        PrietenieFilterDTO prietenieFilterDTO = (PrietenieFilterDTO) filter;



        try {
            Pair<String, List<Object>> sqlFilter = toSql(prietenieFilterDTO);
            String sql = "select * from Friendships ";
            if(!sqlFilter.getFirst().isEmpty())
                sql += " where " + sqlFilter.getFirst();
            sql += " LIMIT ? OFFSET ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            int index = 0;
            for(Object param : sqlFilter.getSecond())
                statement.setObject(++index, param);
            statement.setInt(++index, pageable.getPageSize());
            statement.setInt(++index, pageable.getPageSize() * (pageable.getPageNumber() - 1));


            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                friendships.add(createEntity(resultSet));
            }
            return new Page<>(friendships, count(prietenieFilterDTO));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
