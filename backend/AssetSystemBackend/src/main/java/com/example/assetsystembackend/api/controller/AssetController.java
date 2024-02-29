package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.service.AssetService;
import com.example.assetsystembackend.api.service.BackLogService;
import com.example.assetsystembackend.api.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

@RestController
public class AssetController {

    private final AssetService assetService;
    private final DynamicService dynamicService;
    private final BackLogService backLogService;

    public static final String INVALID_ID_MSG = "Invalid ID!";
    public static final String SUCCESS_MSG = "Insertion successful!";
    public static final String REMOVAL_MSG = "Removal successful!";
    public static final String MISSING_DATA_MSG = "Missing data!";
    public static final String INVALID_TYPE_MSG = "Invalid Type!";


    @Autowired
    public AssetController(AssetService assetService, DynamicService dynamicService, BackLogService backLogService){
        this.assetService = assetService;
        this.dynamicService = dynamicService;
        this.backLogService = backLogService;
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
            return ResponseEntity.badRequest().body(MISSING_DATA_MSG);

        //break asset data from type data
        Map<String, String> assetData = (Map<String, String>) payload.get("asset");
        Map<String, Object> typeData = (Map<String, Object>) payload.get("type");

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        Date date = Date.valueOf(currentDate);

        String type = assetData.get("type");

        // Check if type table exists
        if (!dynamicService.getTypeTableNames().contains(type)) {
            return ResponseEntity.badRequest().body(INVALID_TYPE_MSG + "\nEnsure the Type exists.");
        }
        // Check if columns keys are actual columns in the table
        else {
            List<String> columns = dynamicService.getTableColumns(type);
            for (int i = 1; i<columns.size(); i++) {
                if (!typeData.containsKey(columns.get(i))) {
                    return ResponseEntity.badRequest().body(INVALID_TYPE_MSG +"\nEnsure the Type contains the specified columns.");
                }
            }
        }

        String description = assetData.getOrDefault("description", null);
        String link = assetData.getOrDefault("link", null);

        Asset newAsset = new Asset(assetData.get("name"), assetData.get("creatorname"), date, description, type, link);
        long tempID = assetService.saveNewAsset(newAsset);
        typeData.put("id", tempID);
        dynamicService.insertData(type, typeData);
        backLogService.addAssetCreation(newAsset);

        return ResponseEntity.ok(SUCCESS_MSG);
    }

    @DeleteMapping("/delete-asset")
    public ResponseEntity<String> deleteAsset(@RequestBody  Map<String, Object> payload) {
        if (!payload.containsKey("id"))
            return ResponseEntity.badRequest().body(MISSING_DATA_MSG + "(Missing Asset ID)");

        long assetID = ((Integer) payload.get("id")).longValue();

        Optional<Asset> returnedAsset = assetService.findByID(assetID);
        if (returnedAsset.isEmpty())
            return ResponseEntity.badRequest().body(INVALID_ID_MSG);


        String typeName = returnedAsset.get().getType();

        try {
            if (!dynamicService.deleteData(typeName, assetID) && assetService.deleteAsset(assetID)){
                return ResponseEntity.badRequest().body(INVALID_ID_MSG);
            }

            return ResponseEntity.ok(REMOVAL_MSG);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Server issue while deleting data");
        }
    }

    @GetMapping("/get-assets")
    public List<Map<String, Object>> getAssets() {
        List<Asset> assetsInfo =  assetService.getAllAssets();
        ListIterator<Asset> assetIterator = assetsInfo.listIterator();

        Map<String, List<String>> typeColumns = new HashMap<>();
        Map<String, List<Object[]>> typeDataMap = new HashMap<>();

        List<Map<String, Object>> output = new ArrayList<>();


        while (assetIterator.hasNext()) {
            Asset asset = assetIterator.next();
            String type = asset.getType();
            Map<String, Object> assetData = new HashMap<>();

            assetData.put("id", asset.getId());
            assetData.put("name", asset.getName());
            assetData.put("creator_name", asset.getCreatorName());
            assetData.put("creation_date", asset.getCreationDate());
            assetData.put("description", asset.getDescription());
            assetData.put("type", type);
            assetData.put("link", asset.getLink());

            // Prevents requesting for certain types repeatedly
            if (!typeColumns.containsKey(type) || !typeDataMap.containsKey(type)) {
                typeColumns.put(type, dynamicService.getTableColumns(type));
                typeDataMap.put(type, dynamicService.retrieveData(type));
            }

            List<Object[]> entries = typeDataMap.get(type); // all entries under one type
            List<String> columns = typeColumns.get(type);


            for (Object[] entry : entries) {
                for (int i = 1; i < columns.size(); i++) {
                    if (entry[0] == asset.getId()) {
                        assetData.put(columns.get(i), entry[i]);
                    }
                }
            }
            output.add(assetData);
        }

        return output;
    }

}
