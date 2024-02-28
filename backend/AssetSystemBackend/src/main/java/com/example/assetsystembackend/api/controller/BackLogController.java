package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.service.BackLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class BackLogController {

    private final BackLogService backLogService;

    @Autowired
    public BackLogController(BackLogService backLogService){
        this.backLogService = backLogService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getAllLogs() {
        List<String> logs = backLogService.getBackLog();
        return ResponseEntity.ok(logs);
    }

    //ID OF THE ASSET
    @GetMapping("/log{id}")
    public ResponseEntity<List<String>> getLogById(@PathVariable("id") long id) {
        List<String> logs = backLogService.getBackLogByAsset(id)
                .stream()
                .map(BackLog::getMessage).
                toList();
        return ResponseEntity.ok(logs);
    }


}
