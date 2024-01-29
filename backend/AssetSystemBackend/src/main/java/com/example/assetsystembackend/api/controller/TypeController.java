package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {
    private final DynamicService service;

    @Autowired
    public TypeController(DynamicService service) {
        this.service = service;
    }

    @GetMapping("/get-{name}")
    public ResponseEntity<List<Object[]>> getType(@PathVariable String name){
        List<Object[]> tableData= service.retrieveData(name);
        if (tableData == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(tableData);
    }

}
