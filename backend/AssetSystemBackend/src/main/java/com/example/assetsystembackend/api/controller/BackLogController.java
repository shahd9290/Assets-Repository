package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.service.BackLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
