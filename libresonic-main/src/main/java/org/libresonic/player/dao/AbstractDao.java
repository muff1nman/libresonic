/*
 This file is part of Libresonic.

 Libresonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Libresonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Libresonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2016 (C) Libresonic Authors
 Based upon Subsonic, Copyright 2009 (C) Sindre Mehus
 */
package org.libresonic.player.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.libresonic.player.Logger;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

/**
 * Abstract superclass for all DAO's.
 *
 * @author Sindre Mehus
 */
public class AbstractDao {
    private static final Logger LOG = Logger.getLogger(AbstractDao.class);
    
    private DaoHelper daoHelper;

    /**
     * Returns a JDBC template for performing database operations.
     * @return A JDBC template.
     */
    public JdbcTemplate getJdbcTemplate() {
        return daoHelper.getJdbcTemplate();
    }

    /**
     * Similar to {@link #getJdbcTemplate()}, but with named parameters.
     */
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return daoHelper.getNamedParameterJdbcTemplate();
    }

    protected String questionMarks(String columns) {
        int count = columns.split(", ").length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append('?');
            if (i < count - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    protected String prefix(String columns, String prefix) {
        StringBuilder builder = new StringBuilder();
        for (String s : columns.split(", ")) {
            builder.append(prefix).append(".").append(s).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    protected int update(String sql, Object... args) {
        long t = System.nanoTime();
        int result = getJdbcTemplate().update(sql, args);
        log(sql, t);
        return result;
    }

    protected int updateAndReturnKey(
            String sql,
            Object... args
    ) {
        long t = System.nanoTime();
        PreparedStatementCreator psc = new AutoIncrementIdPreparedStatementCreator(sql);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(args);
        KeyHolder holder = new GeneratedKeyHolder();
        PreparedStatementCallback<Integer> callback =
                new AutoIncrementAndArgSetterPreparedStatementCallback(pss, holder);
        getJdbcTemplate().execute(psc, callback);
        log(sql, t);
        return holder.getKey().intValue();
    }

    private void log(String sql, long startTimeNano) {
        long millis = (System.nanoTime() - startTimeNano) / 1000000L;

        // Log queries that take more than 2 seconds.
        if (millis > TimeUnit.SECONDS.toMillis(2L)) {
            LOG.debug(millis + " ms:  " + sql);
        }
    }

    protected <T> List<T> query(String sql, RowMapper rowMapper, Object... args) {
        long t = System.nanoTime();
        List<T> result = getJdbcTemplate().query(sql, args, rowMapper);
        log(sql, t);
        return result;
    }

    protected <T> List<T> namedQuery(String sql, RowMapper rowMapper, Map<String, Object> args) {
        long t = System.nanoTime();
        List<T> result = getNamedParameterJdbcTemplate().query(sql, args, rowMapper);
        log(sql, t);
        return result;
    }

    protected List<String> queryForStrings(String sql, Object... args) {
        long t = System.nanoTime();
        List<String> result = getJdbcTemplate().queryForList(sql, args, String.class);
        log(sql, t);
        return result;
    }

    protected List<Integer> queryForInts(String sql, Object... args) {
        long t = System.nanoTime();
        List<Integer> result = getJdbcTemplate().queryForList(sql, args, Integer.class);
        log(sql, t);
        return result;
    }

    protected List<String> namedQueryForStrings(String sql, Map<String, Object> args) {
        long t = System.nanoTime();
        List<String> result = getNamedParameterJdbcTemplate().queryForList(sql, args, String.class);
        log(sql, t);
        return result;
    }

    protected Integer queryForInt(String sql, Integer defaultValue, Object... args) {
        long t = System.nanoTime();
        List<Integer> list = getJdbcTemplate().queryForList(sql, args, Integer.class);
        Integer result = list.isEmpty() ? defaultValue : list.get(0) == null ? defaultValue : list.get(0);
        log(sql, t);
        return result;
    }

    protected Integer namedQueryForInt(String sql, Integer defaultValue, Map<String, Object> args) {
        long t = System.nanoTime();
        List<Integer> list = getNamedParameterJdbcTemplate().queryForList(sql, args, Integer.class);
        Integer result = list.isEmpty() ? defaultValue : list.get(0) == null ? defaultValue : list.get(0);
        log(sql, t);
        return result;
    }

    protected Date queryForDate(String sql, Date defaultValue, Object... args) {
        long t = System.nanoTime();
        List<Date> list = getJdbcTemplate().queryForList(sql, args, Date.class);
        Date result = list.isEmpty() ? defaultValue : list.get(0) == null ? defaultValue : list.get(0);
        log(sql, t);
        return result;
    }

    protected Long queryForLong(String sql, Long defaultValue, Object... args) {
        long t = System.nanoTime();
        List<Long> list = getJdbcTemplate().queryForList(sql, args, Long.class);
        Long result = list.isEmpty() ? defaultValue : list.get(0) == null ? defaultValue : list.get(0);
        log(sql, t);
        return result;
    }

    protected <T> T queryOne(String sql, RowMapper rowMapper, Object... args) {
        List<T> list = query(sql, rowMapper, args);
        return list.isEmpty() ? null : list.get(0);
    }

    protected <T> T namedQueryOne(String sql, RowMapper rowMapper, Map<String, Object> args) {
        List<T> list = namedQuery(sql, rowMapper, args);
        return list.isEmpty() ? null : list.get(0);
    }

    public void setDaoHelper(DaoHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    private static class AutoIncrementIdPreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

        private final String sql;

        public AutoIncrementIdPreparedStatementCreator(String sql) {
            Assert.notNull(sql, "SQL must not be null");
            this.sql = sql;
        }

        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            return con.prepareStatement(this.sql, new String[] { "ID" });
        }

        public String getSql() {
            return this.sql;
        }
    }

    private static class AutoIncrementAndArgSetterPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        private final PreparedStatementSetter pss;
        private final KeyHolder holder;

        public AutoIncrementAndArgSetterPreparedStatementCallback(PreparedStatementSetter pss, KeyHolder holder) {
            this.pss = pss;
            this.holder = holder;
        }

        @Override
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            try {
                pss.setValues(ps);
                int rows = ps.executeUpdate();
                List<Map<String, Object>> generatedKeys = holder.getKeyList();
                generatedKeys.clear();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys != null) {
                    try {
                        RowMapperResultSetExtractor<Map<String, Object>> rse =
                                new RowMapperResultSetExtractor<>(new ColumnMapRowMapper(), 1);
                        generatedKeys.addAll(rse.extractData(keys));
                    }
                    finally {
                        JdbcUtils.closeResultSet(keys);
                    }
                }
                LOG.debug("SQL update affected " + rows + " rows and returned " + generatedKeys.size() + " keys");
                return rows;
            }
            finally {
                if (pss instanceof ParameterDisposer) {
                    ((ParameterDisposer) pss).cleanupParameters();
                }
            }
        }
    }
}
