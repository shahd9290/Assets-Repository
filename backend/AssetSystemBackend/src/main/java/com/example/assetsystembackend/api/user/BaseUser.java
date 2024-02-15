package com.example.assetsystembackend.api.user;

import org.springframework.jdbc.core.JdbcTemplate;

public class BaseUser {

    private final JdbcTemplate template;

    public BaseUser(JdbcTemplate template){
        this.template = template;
    }

    public void newUser(String username, String password){
        String query = "CREATE USER " + username +
                " WITH\nLOGIN\nSUPERUSER\nINHERIT\nCREATEDB\nCREATEROLE\nREPLICATION\n ENCRYPTED PASSWORD '"
                + password + "';\nGRANT pg_signal_backend TO " + username + "WITH ADMIN OPTION;";
        template.execute(query);
    }
}
