package assetsystem.backend.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing tables and data contained within them in the database.
 */
@Repository
public class DynamicTableRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DynamicTableRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Method which checks if the table is present in the database
     * @param tableName name of the table
     * @return True if found, else False
     */
    public boolean doesTableExist(String tableName) {
        try {
            String query = "SELECT 1 FROM " + tableName + " LIMIT 1";
            jdbcTemplate.queryForObject(query, Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the data from a table given its name.
     * @param tableName Name of the target table
     * @return a list (rows of table) of Object lists(data within the row)
     */
    public List<Object[]> retrieveDataFromTable(String tableName) {
        String query = "SELECT * FROM " + tableName;
        //return jdbcTemplate.query(query, new DynamicResultSetExtractor());
        return jdbcTemplate.query(query, new DynamicResultSetExtractor());

    }

    /**
     * Class to parse the return of the table into a list of data
     */
    private static class DynamicResultSetExtractor implements ResultSetExtractor<List<Object[]>> {
        @Override
        public List<Object[]> extractData(ResultSet resultSet) throws SQLException {
            List<Object[]> result = new ArrayList<>();
            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                result.add(rowData);
            }

            return result;
        }
    }
}

