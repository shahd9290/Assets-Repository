package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.service.AssetService;
import com.example.assetsystembackend.api.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Map;
import java.util.Random;

@RestController
public class AssetController {

    private final AssetService assetService;
    private final DynamicService dynamicService;
    private final Random random;

    @Autowired
    public AssetController(AssetService assetService, DynamicService dynamicService){
        this.assetService = assetService;
        this.dynamicService = dynamicService;
        random = new Random();
    }


    /*
    {
        "asset": { asset fields}
        "type" : {type fields}
     */
    @PostMapping("/add-new-asset")
    public ResponseEntity<String> addAsset(@RequestBody  Map<String, Object> payload) {
        //check data is compatible
        if (!payload.containsKey("asset") || !payload.containsKey("type"))
            return ResponseEntity.badRequest().body("Missing data");



        //break asset data from type data
        Map<String, String> assetData = (Map<String, String>) payload.get("asset");
        Map<String, Object> typeData = (Map<String, Object>) payload.get("type");

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        Date date = Date.valueOf(currentDate);

        long id = random.nextLong();
        String type = assetData.get("type");
        typeData.put("id", (Object) id);


        if (!dynamicService.getTypeTableNames().contains(type)) {
            return ResponseEntity.badRequest().body("Invalid Type!\nEnsure the Type exists.");
        }

        Asset newAsset = new Asset(id, assetData.get("name"), assetData.get("creatorname"), date, null, type);
        assetService.saveNewAsset(newAsset);
        dynamicService.insertData(type, typeData);


        return ResponseEntity.ok("Added successfully");
    }


}
