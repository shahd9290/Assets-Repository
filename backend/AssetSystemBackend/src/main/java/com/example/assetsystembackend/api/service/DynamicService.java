package com.example.assetsystembackend.api.service;


import com.example.assetsystembackend.api.repository.DynamicTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for handling dynamic table data retrieval. Or when unsure if said table exists in the database,
 *
 */
@Service
public class DynamicService {

    private final DynamicTableRepository dynamicTableRepository;

    @Autowired
    public DynamicService(DynamicTableRepository dynamicTableRepository) {
        this.dynamicTableRepository = dynamicTableRepository;
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
}