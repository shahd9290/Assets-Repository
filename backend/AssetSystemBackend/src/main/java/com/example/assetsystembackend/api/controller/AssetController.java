package com.example.assetsystembackend.api.controller;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.service.AssetService;
import com.example.assetsystembackend.api.service.BackLogService;
import com.example.assetsystembackend.api.service.DynamicService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

/**
 * Controller used to manage assets, including addition, removal and retrieval.
 */
@RestController
public class AssetController {


    private final AssetService assetService;
    private final DynamicService dynamicService;
    private final BackLogService backLogService;

    /**
     * Invalid ID!
     */
    public static final String INVALID_ID_MSG = "Invalid ID!";
    /**
     * Insertion successful!
     */
    public static final String SUCCESS_MSG = "Insertion successful!";
    /**
     * Removal successful!
     */
    public static final String REMOVAL_MSG = "Removal successful!";
    /**
     * Missing data!
     */
    public static final String MISSING_DATA_MSG = "Missing data!";
    /**
     * Invalid Type!
     */
    public static final String INVALID_TYPE_MSG = "Invalid Type!";
    /**
     * Asset has dependencies! Remove them First!
     */
    public static final String DEPENDENCY_MSG = "Asset has dependencies! Remove them First!";
    /**
     * Please specify the relation between the parent and child assets!
     */
    public static final String RELATION_MSG = "Please specify the relation between the parent and child assets!";


    /**
     * Initializes services required for the asset controller to function.
     * @param assetService Works with the assets table in the database.
     * @param dynamicService Works with the specified type table in the database.
     * @param backLogService Works with backlogs to store changes
     */
    @Autowired
    public AssetController(AssetService assetService, DynamicService dynamicService, BackLogService backLogService) {
        this.assetService = assetService;
        this.dynamicService = dynamicService;
        this.backLogService = backLogService;
    }


    /*
    {
        "asset": { asset fields}
        "type" : {type fields}
     */
    /**
     * Creates an asset to be inserted into the asset management system.<br>
     * This will take various attributes from a provided payload, and uses them to insert them into their respected fields in the database.<br>
     * Contents under the "asset" key will be inserted automatically via Spring Boot. Contents under the "type" key will be inserted manually
     * via a pre-written function.
     *
     * @param payload The data provided via a POST request to be inserted into the table.
     * @return A confirmation message informing the user if insertion was successful.
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/add-new-asset")
    public ResponseEntity<String> addAsset(@RequestBody Map<String, Object> payload) {
        //check data is compatible
        if (!payload.containsKey("asset") || !payload.containsKey("type"))
            return ResponseEntity.badRequest().body(MISSING_DATA_MSG);

        //break asset data from type data
        Map<String, String> assetData = (Map<String, String>) payload.get("asset");
        Map<String, Object> typeData = (Map<String, Object>) payload.get("type");

        // Parent ID must belong to an asset in the table.
        String parentIDString = String.valueOf(assetData.getOrDefault("parent_id", null));
        long parent_id = !parentIDString.equals("null") ? Long.parseLong(parentIDString) : 0;

        String relationType = assetData.getOrDefault("relation_type", null);

        if (parent_id != 0 && !assetService.exists(parent_id))
            return ResponseEntity.badRequest().body(INVALID_ID_MSG);
        // Either parent_id without type or type without parent_id. Needs both to work.
        else if ((parent_id != 0 && relationType == null )|| (relationType != null && parent_id == 0))
            return ResponseEntity.badRequest().body(RELATION_MSG);

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
            for (int i = 1; i < columns.size(); i++) {
                if (!typeData.containsKey(columns.get(i))) {
                    return ResponseEntity.badRequest().body(INVALID_TYPE_MSG + "\nEnsure the Type contains the specified columns.");
                }
            }
        }

        String description = assetData.getOrDefault("description", null);
        String link = assetData.getOrDefault("link", null);

        Asset newAsset = new Asset(assetData.get("name"), assetData.get("creatorname"), date, description, type, link, parent_id == 0 ? null : parent_id, relationType);
        long tempID = assetService.saveNewAsset(newAsset);
        typeData.put("id", tempID);
        dynamicService.insertData(type, typeData);
        backLogService.addAssetCreation(newAsset);

        return ResponseEntity.ok(SUCCESS_MSG);
    }

    /**
     * Deletes an asset from the asset management system.<br>
     * This takes an id from the provided payload, and will attempt to delete an asset from both the asset and "type" tables where data is held.
     * @param payload The id provided via a POST request
     * @return A confirmation message informing the user if removal was successful
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/delete-asset")
    public ResponseEntity<String> deleteAsset(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id"))
            return ResponseEntity.badRequest().body(MISSING_DATA_MSG + "(Missing Asset ID)");

        long assetID = ((Integer) payload.get("id")).longValue();

        Optional<Asset> returnedAsset = assetService.findByID(assetID);
        if (returnedAsset.isEmpty())
            return ResponseEntity.badRequest().body(INVALID_ID_MSG);

        if (hasChildren(assetID))
            return ResponseEntity.badRequest().body(DEPENDENCY_MSG);

        String typeName = returnedAsset.get().getType();

        try {
            boolean assetDeletion = assetService.deleteAsset(assetID);
            boolean typeDeletion = dynamicService.deleteData(typeName, assetID);
            backLogService.addAssetDeletion(returnedAsset.get());

            if (!assetDeletion && !typeDeletion) {
                return ResponseEntity.badRequest().body(INVALID_ID_MSG);
            }

            return ResponseEntity.ok(REMOVAL_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Server issue while deleting data");
        }
    }

    /**
     * Returns all the assets in the database.
     * @return A list of assets taken from the database.
     */
    private List<Map<String, Object>> getAssets() {
        List<Asset> assetsInfo = assetService.getAllAssets();
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
            assetData.put("parent_id", asset.getParent_id());
            assetData.put("relation_type", asset.getRelationType());

            // Prevents requesting for certain types repeatedly
            if (!typeColumns.containsKey(type) || !typeDataMap.containsKey(type)) {
                typeColumns.put(type, dynamicService.getTableColumns(type));
                typeDataMap.put(type, dynamicService.retrieveData(type));
            }

            List<Object[]> entries = typeDataMap.get(type); // all entries under one type
            List<String> columns = typeColumns.get(type);


            for (Object[] entry : entries) {
                for (int i = 1; i < columns.size(); i++) {
                    Long id = Long.parseLong(String.valueOf(entry[0]));
                    if (id == asset.getId()) {
                        assetData.put(columns.get(i), entry[i]);
                    }
                }
            }
            output.add(assetData);
        }

        return output;
    }

    /**
     * Returns assets from the database table which meet a given set of criteria
     * <p>
     *     Assets can be filtered by a dedicated search term, date, type or creator. These filters will be provided in the payload from the POST request.
     * </p>
     * @param payload The criteria for filtering through the assets
     * @return An updated assets listing based on the filters provided.
     */
    @PreAuthorize("hasAnyRole('ROLE_VIEWER', 'ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/search")
    public List<Map<String, Object>> search(@RequestBody Map<String, Object> payload) {
        List<Map<String, Object>> assetList = getAssets();
        List<Map<String, Object>> output = new ArrayList<>();

        // no filter, return all assets
        if (payload.isEmpty())
            return assetList;

        /* Filters:
         * type
         * date_before
         * date_after
         * user
         */

        String type = (String) payload.getOrDefault("type", null);
        Date date_before = (payload.containsKey("date_before") ? Date.valueOf((String) payload.get("date_before")) : null);
        Date date_after = (payload.containsKey("date_after") ? Date.valueOf((String) payload.get("date_after")) : null);
        String user = (String) payload.getOrDefault("user", null);
        String search_term = (String) payload.getOrDefault("search_term", null);
        Long parent_id = ((Integer) payload.getOrDefault("parent_id", 0)).longValue();


        // something in the payload that isn't any of the above filters.
        if (type == null && date_before == null && date_after == null && user == null && search_term == null && parent_id == 0)
            return assetList;

        // Check condition. If condition is false restart loop and don't add to output.
        for (Map<String, Object> asset : assetList) {
            if (search_term != null && !((String) asset.get("name")).contains(search_term))
                continue;
            if (type != null && !asset.get("type").equals(type))
                continue;
            if (user != null && !asset.get("creator_name").equals(user))
                continue;
            if (date_before != null && !date_before.after((Date) asset.get("creation_date")))
                continue;
            if (date_after != null && !date_after.before((Date) asset.get("creation_date")))
                continue;
            if (parent_id != 0 && asset.get("parent_id") != parent_id)
                continue;
            output.add(asset);
        }

        return output;
    }

    private boolean hasChildren(Long parent_id) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("parent_id", parent_id.intValue());
        return !search(payload).isEmpty();
    }
}
