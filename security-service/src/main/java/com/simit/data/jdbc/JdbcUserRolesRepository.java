package com.simit.data.jdbc;

import com.simit.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcUserRolesRepository implements UserRolesRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcUserRolesRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }



    @Override
    public Iterable<UserRole> findByUserId(Long userId) {
//        return jdbc.query("select * from user_roles where user_id = " + userId, this::mapRowToUserRoles);
        return null;
    }


//    private UserRole mapRowToUserRoles(ResultSet rs, int rowNum) throws SQLException {
//        return new UserRole(0L,
//                rs.getLong("user_id"),
//                rs.getLong("roles_id")
//                );
//    }
}
