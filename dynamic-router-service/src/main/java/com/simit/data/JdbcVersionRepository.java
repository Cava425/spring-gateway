package com.simit.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simit.entity.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

@Repository
public class JdbcVersionRepository implements VersionRepository {

    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insert;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcVersionRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc)
                .withTableName("Version")
                .usingGeneratedKeyColumns("id");
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public Iterable<Version> findAll() {
        return jdbc.query("select id, name, comment, create_at from Version", this::mapRowToVersion);
    }

    @Override
    public Version findLatest() {
        return jdbc.queryForObject("select id, name, comment, create_at from Version order by create_at desc limit 1",
                this::mapRowToVersion);
    }

    @Override
    public Version save(Version version) {
        version.setCreateAt(new Date());

        Map<String, Object> values =
                objectMapper.convertValue(version, Map.class);
        values.put("createAt", version.getCreateAt());
        long versionId = insert.executeAndReturnKey(values)
                        .longValue();

        version.setId(versionId);
        return version;
    }

    private Version mapRowToVersion(ResultSet rs, int rowNum) throws SQLException {
        return new Version(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("comment"),
                rs.getTimestamp("create_at")
        );
    }


}
