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
    public void newAdmin(String username, String password){
        String query = "CREATE USER " + username +
                " WITH\nSUPERUSER\nINHERIT\nCREATEDB\nCREATEROLE\nREPLICATION\n ENCRYPTED PASSWORD '"
                + password + "';\nGRANT ALL TO " + username + " WITH ADMIN OPTION;";
        template.execute(query);
    }

    public void newGeneral(String username, String password){
        String query = "CREATE USER " + username +
                " WITH ENCRYPTED PASSWORD '" + password + "';";
        template.execute(query);
    }

    public void newViewer(String username, String password){
        String query = "CREATE USER " + username + " WITH ENCYPTED PASSWORD '" + password + "';"
    }
}
