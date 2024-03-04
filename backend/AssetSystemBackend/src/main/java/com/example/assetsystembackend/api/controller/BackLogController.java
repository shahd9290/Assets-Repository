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

@RestController
@RequestMapping("/audit")
public class BackLogController {

    private final BackLogService backLogService;

    @Autowired
    public BackLogController(BackLogService backLogService){
        this.backLogService = backLogService;
    }

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

    //ID OF THE ASSET
    @GetMapping("/log{id}")
    public List<Map<String, String>> getLogById(@PathVariable("id") long id) {
        List<BackLog> a = backLogService.getBackLogByAsset(id);
        List<Map<String, String>> output = new ArrayList<>();
        for (BackLog b : a) {

            Map<String, String > map = new HashMap<>();
            map.put("id", b.getId().toString());
            map.put("entry", b.getMessage());
            output.add(map);
        }

        return output;
    }


}
