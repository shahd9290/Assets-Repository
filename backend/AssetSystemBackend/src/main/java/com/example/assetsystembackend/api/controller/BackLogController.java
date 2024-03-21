package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.service.BackLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class to handle audit logs.
 */
@RestController
@RequestMapping("/audit")
public class BackLogController {

    private final BackLogService backLogService;

    @Autowired
    public BackLogController(BackLogService backLogService){
        this.backLogService = backLogService;
    }

    /**
     * Get all audit logs.
     *
     * @return ResponseEntity containing a list of maps representing audit log entries.
     */
    @GetMapping("/logs")
    public ResponseEntity<List<Map<String,String>>> getAllLogs() {
        List<BackLog> logs = backLogService.getBackLogs();
        List<Map<String, String>> output = new ArrayList<>();
        for (BackLog backLog : logs) {
            Map<String, String > map = new HashMap<>();
            map.put("id", backLog.getId().toString());
            map.put("entry", backLog.getMessage());
            output.add(map);
        }
        return ResponseEntity.ok(output);
    }

    /**
     * Get audit logs by asset ID.
     *
     * @param id The ID of the asset.
     * @return List of maps representing audit log entries.
     */
    @GetMapping("/log/{id}")
    public List<Map<String, String>> getLogById(@PathVariable("id") long id) {
        List<BackLog> list = backLogService.getBackLogByAsset(id);
        List<Map<String, String>> output = new ArrayList<>();
        for (BackLog entry : list) {
            Map<String, String > map = new HashMap<>();
            map.put("id", entry.getId().toString());
            map.put("entry", entry.getMessage());
            output.add(map);
        }
        return output;
    }
}
