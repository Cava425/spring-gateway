package com.simit.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simit.entity.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcRoutesRepository implements RoutesRepository{

    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insert;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcRoutesRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc)
                .withTableName("Routes")
                .usingGeneratedKeyColumns("id");;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Iterable<Routes> findAll() {
        return jdbc.query("select * from Routes", this::mapRowToRoutes);
    }

    @Override
    public Routes save(Routes routes) {
        routes.setCreateTime(new Date());
        routes.setUpdateTime(new Date());
        routes.setIsDel(false);
        routes.setIsEbl(false);

//        Map<String, Object> values =
//                objectMapper.convertValue(routes, Map.class);
//        values.put("createTime", routes.getCreateTime());
//        values.put("updateTime", routes.getUpdateTime());
//
//        long routesId = insert.executeAndReturnKey(values)
//                .longValue();

        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Routes (route_id, route_uri, route_order, is_ebl, is_del, create_time, update_time, predicates, filters) " +
                                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.TINYINT, Types.TINYINT, Types.TIMESTAMP, Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR
                        );
        // 关键方法
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                routes.getRouteId(),
                                routes.getRouteUri(),
                                routes.getRouteOrder(),
                                routes.getIsEbl(),
                                routes.getIsDel(),
                                routes.getCreateTime(),
                                routes.getUpdateTime(),
                                routes.getPredicates(),
                                routes.getFilters()
                ));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        routes.setId(keyHolder.getKey().longValue());
        return routes;
    }


    private Routes mapRowToRoutes(ResultSet rs, int rowNum) throws SQLException {
        return new Routes(
                rs.getLong("id"),
                rs.getString("route_id"),
                rs.getString("route_uri"),
                rs.getInt("route_order"),
                rs.getBoolean("is_ebl"),
                rs.getBoolean("is_del"),
                rs.getTimestamp("create_time"),
                rs.getTimestamp("update_time"),
                rs.getString("predicates"),
                rs.getString("filters")
        );
    }
}
