package com.example.assetsystembackend.api.service;


import com.example.assetsystembackend.api.repository.DynamicTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
     *                 Each column is a VARCHAR(20) data type by default.
     * @return {@code true} if the table creation is successful, {@code false} otherwise.
     */
    public boolean createTable(String tableName, List<String> columns) {
        try {
            StringBuilder query = new StringBuilder("CREATE TABLE " + tableName + " (" +
                    "id int PRIMARY KEY,");

            for (String item : columns) {
                //Everything default varchar20
                query.append(item).append(" VARCHAR(20)").append(",");
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
}