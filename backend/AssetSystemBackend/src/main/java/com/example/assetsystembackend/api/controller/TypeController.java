package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/type")
public class TypeController {
    private final DynamicService service;

    @Autowired
    public TypeController(DynamicService service) {
        this.service = service;
    }

    /**
     * Get all types data by name.
     *
     * @param name The name of the type.
     * @return ResponseEntity containing a list of Object arrays representing table data.
     */
    @GetMapping("/get-type-data/{name}")
    public ResponseEntity<List<Object[]>> getAllTypes(@PathVariable String name){
        List<Object[]> tableData= service.retrieveData(name);
        if (tableData.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tableData);
    }

    /**
     * Get columns for a given table.
     *
     * @param tableName The name of the table.
     * @return ResponseEntity containing a list of strings representing table columns.
     */
    @GetMapping("/get-columns/{tableName}")
    public ResponseEntity<List<String>> getTypeAttributes(@PathVariable String tableName){
        try{
            List<String> columns = service.getTableColumns(tableName);
            return ResponseEntity.ok(columns);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Get types available in the system.
     *
     * @return ResponseEntity containing an Object representing available types.
     */

    @GetMapping("/get-types")
    public ResponseEntity<Object> getType() {
        List<Object> tables = service.getTypes();
        if (tables.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tables);
    }

    /**
     * Add a new type.
     *
     * @param payload The payload containing table name and columns.
     * @return ResponseEntity containing a status message.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-type")
    public ResponseEntity<Object> addType(@RequestBody Map<String, Object> payload) {
        if (!checkDataValid(payload))
            return ResponseEntity.badRequest().body("Invalid data provided!");

        String tableName = (String) payload.get("table_name");

        Object columnsObject = payload.get("columns");
        if (!(columnsObject instanceof List)) {
            return ResponseEntity.badRequest().body("Invalid 'columns' format. Expected a List<String>.");
        }

        List<String> columns = (List<String>) columnsObject;

        if (!service.createTable(tableName, columns))
            return ResponseEntity.badRequest().body("Table '" + tableName + "' already exists.");

        return ResponseEntity.status(HttpStatus.CREATED).body("Data added successfully");
    }

    /**
     * Delete a type.
     *
     * @param payload The payload containing table name.
     * @return ResponseEntity containing a status message.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-type")
    public ResponseEntity<Object> deleteType(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("table_name"))
            return ResponseEntity.badRequest().body("Invalid data provided!");

        String tableName = (String) payload.get("table_name");

        if (!service.deleteTable(tableName))
            return ResponseEntity.badRequest().body("Table '" + tableName + "' doesn't exist.");

        return ResponseEntity.ok().body("Data removed successfully");
    }

    /**
     * Insert data into a table.
     *
     * @param tableName The name of the table.
     * @param data The data to be inserted.
     * @return ResponseEntity containing a status message.
     */
    @PostMapping("/insert-data/{tableName}")
    public ResponseEntity<Object> insertData(@PathVariable String tableName, @RequestBody Map<String, Object> data) {
        if (!isValidInsertData(tableName, data))
            return ResponseEntity.badRequest().body("Invalid data provided!");

        if (!service.insertData(tableName, data))
            return ResponseEntity.badRequest().body("Database Error! \nInvalid data provided!");

        return ResponseEntity.status(HttpStatus.CREATED).body("Data inserted successfully");
    }

    /**
     * Delete data from a table.
     *
     * @param tableName The name of the table.
     * @param data The data to be deleted.
     * @return ResponseEntity containing a status message.
     */

    @PostMapping("/remove-data/{tableName}")
    public ResponseEntity<Object> deleteData(@PathVariable String tableName, @RequestBody Map<String, Object> data) {
        if (!isValidInsertData(tableName, data))
            return ResponseEntity.badRequest().body("Invalid data provided!");

        if (!service.deleteData(tableName, (Long) data.get("id")))
            return ResponseEntity.badRequest().body("Database Error! \nInvalid data provided!");

        return ResponseEntity.status(HttpStatus.CREATED).body("Data removed successfully");
    }

    /**
     * Method to verify the structure of the data received matches the expected one
     * from the API.
     * More checks can be added, now contains only the minimum the requirements.
     *
     * @param payload The data received
     * @return {@code true} if the data matches the requirements, else {@code false}
     */
    private boolean checkDataValid(Map<String, Object> payload) {
        if (!payload.containsKey("table_name"))
            return false;

        if (!payload.containsKey("columns"))
            return false;

        return true;
    }

    /**
     * Validate if the insert data is valid for the given table.
     *
     * @param tableName The name of the table.
     * @param data The data to be inserted.
     * @return {@code true} if the insert data is valid, else {@code false}
     */
    private boolean isValidInsertData(String tableName, Map<String, Object> data) {
        // Get the expected columns for the given table
        List<String> expectedColumns = service.getTableColumns(tableName);

        // Check if the data keys match the expected columns
        return expectedColumns.containsAll(data.keySet());
    }
}
