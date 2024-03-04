package com.example.assetsystembackend.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BaseUser {

    private final JdbcTemplate template;

    @Autowired
    public BaseUser(JdbcTemplate template){
        this.template = template;
    }

    //admin user
    public void newUser(String username, String password){
        String query = "CREATE USER " + username +
                " WITH\nLOGIN\nSUPERUSER\nINHERIT\nCREATEDB\nCREATEROLE\nREPLICATION\n ENCRYPTED PASSWORD '"
                + password + "';\nGRANT pg_signal_backend TO " + username + " WITH ADMIN OPTION;";
        template.execute(query);
    }
}
