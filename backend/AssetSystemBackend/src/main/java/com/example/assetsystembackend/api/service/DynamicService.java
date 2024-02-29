package com.example.assetsystembackend.api.service;


import com.example.assetsystembackend.api.repository.DynamicTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Service class for handling dynamic table data retrieval. Or when unsure if said table exists in the database,
 *
 */
@Service
public class DynamicService {

    private final DynamicTableRepository dynamicTableRepository;
    private final JdbcTemplate template;

    @Autowired
    public DynamicService(DynamicTableRepository dynamicTableRepository, JdbcTemplate template) {
        this.dynamicTableRepository = dynamicTableRepository;
        this.template = template;
    }

    /**
     * Retrieves the column names of a specified table.
     *
     * @param tableName The name of the table for which to retrieve column names.
     * @return A list of column names for the specified table.
     */
    public List<String> getTableColumns(String tableName) {
        // "WHERE false" so it returns only the columns but no data
        String query = "SELECT * FROM " + tableName + " WHERE false";

        return template.query(query, rs -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<String> columns = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columns.add(columnName);
            }

            return columns;
        });
    }

    /**
     * Looks through all the tables under the public schema, and returns all tables other than the assets table.
     * These tables should refer to the individual type tables holding their respected attributes.
     *
     * @return A list of unique type tables
     */
    public List<String> getTypeTableNames() {
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';";

        return template.query(query, rs -> {

            List<String> columns = new ArrayList<>();

            while (rs.next()) {
                String table = rs.getString("table_name");
                if (!table.equals("assets"))
                    columns.add(table);
            }

            return columns;
        });
    }

    /**
     * Retrieve data from a dynamic table.
     *
     * @param tableName The name of the dynamic table.
     * @return A List of Object arrays representing the data from the table.
     *         Each Object array corresponds to a row in the table.
     *         Returns null if the table does not exist.
     */
    public List<Object[]> retrieveData(String tableName) {

        if (dynamicTableRepository.doesTableExist(tableName)) {
            return dynamicTableRepository.retrieveDataFromTable(tableName);
        }
        return null;
    }

    /**
     * Creates a new table in the database with the specified tableName and columns.
     *
     * @param tableName The name of the table to be created.
     * @param columns   A list of column names to be added to the table.
     *                 Each column is a VARCHAR(100) data type by default.
     * @return {@code true} if the table creation is successful, {@code false} otherwise.
     */
    public boolean createTable(String tableName, List<String> columns) {
        try {
            StringBuilder query = new StringBuilder("CREATE TABLE " + tableName + " (" +
                    "id SERIAL PRIMARY KEY,");

            for (String item : columns) {
                //Everything default varchar100
                query.append(item).append(" VARCHAR(100)").append(",");
            }
            query.deleteCharAt(query.length() - 1); //delete last trailing comma
            query.append(");");

            template.execute(query.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean deleteTable(String tableName) {
        try {
            StringBuilder query = new StringBuilder("DROP TABLE " + tableName + ";");

            template.execute(query.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Inserts data into the specified table.
     *
     * @param tableName The name of the table where the data will be inserted
     * @param data A Map representing the data to be inserted, where keys are column names and values are the corresponding values
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
    public boolean insertData(String tableName, Map<String, Object> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        data.forEach((key, element)->{
            columns.append(key).append(",");
            values.append("'").append(element).append("'").append((","));
        });

        //Delete trailing commas
        columns.deleteCharAt(columns.length()-1);
        values.deleteCharAt(values.length()-1);


        String query = String.format("INSERT INTO %s (%s) VALUES (%s);",tableName, columns, values);
        try {
            return template.update(query) == 1;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteData(String tableName, long id) {

        String query = String.format("DELETE FROM %s WHERE id = %d", tableName, id);

        try {
            return template.update(query) == 1;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}