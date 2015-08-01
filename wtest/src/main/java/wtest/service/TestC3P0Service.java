package wtest.service;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestC3P0Service {
    
    private JdbcTemplate jdbcTemplate;
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void save () {
        System.out.println("===========");
        StringBuffer sql = new StringBuffer();
        sql.append("insert into t1 (t) value (1)");
        jdbcTemplate.execute(sql.toString());
    }
    
    public void saveSlave() {
        System.out.println("-----------");
        StringBuffer sql = new StringBuffer();
        sql.append("insert into j1 (j) value (1)");
        jdbcTemplate.execute(sql.toString());
    }
    
}
