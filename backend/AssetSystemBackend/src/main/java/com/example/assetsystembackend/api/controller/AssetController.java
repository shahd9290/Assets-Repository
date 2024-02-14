package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final Random random;

    @Autowired
    public AssetController(AssetService assetService){
        this.assetService = assetService;
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
        Map<String, String> typeData = (Map<String, String>) payload.get("type");

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        Date date = Date.valueOf(currentDate);


        Asset newAsset = new Asset(random.nextLong(), assetData.get("name"), assetData.get("creatorname"), date, null);
        assetService.saveNewAsset(newAsset);


        return ResponseEntity.ok("Added successfully");
    }


}
