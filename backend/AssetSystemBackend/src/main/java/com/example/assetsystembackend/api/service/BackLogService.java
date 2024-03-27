package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.repository.BackLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BackLogService {

    //Dummy User
    private final String USER = "James";

    private final BackLogRepository backLogRepository;

    //Creation message
    private final String CREATION_STR = "%s created %s on %s";

    private final String EDIT_ASSET_STR = "%s edited %s on %s";


    @Autowired
    public BackLogService(BackLogRepository logRepo) {
        backLogRepository = logRepo;
    }


    public List<BackLog> getBackLogs() {
        return backLogRepository.findAll();
    }

    public boolean addAssetCreation(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        //Generate message
        newEntry.setMessage(String.format(CREATION_STR, asset.getCreatorName(), asset.getName(), asset.getCreationDate().toString()));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }

    public List<BackLog> getBackLogByAsset(Long id) {
        return backLogRepository.findByAssetId(id);

    }

    public boolean addAssetEdit(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        // Generate message with today's date
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // You can adjust the date format as needed
        newEntry.setMessage(String.format(EDIT_ASSET_STR, asset.getCreatorName(), asset.getName(), formattedDate));

        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }



}
