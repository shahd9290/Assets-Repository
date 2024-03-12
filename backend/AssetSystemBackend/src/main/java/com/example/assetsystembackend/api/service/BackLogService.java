package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.repository.BackLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class BackLogService {

    //Dummy User

    private final BackLogRepository backLogRepository;

    //Creation message
    private final String CREATION_STR = "%s created %s on %s";

    //Deletion message
    private final String DELETION_STR = "%s deleted %s on %s";


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
        newEntry.setMessage(String.format(CREATION_STR,asset. getCreatorName(), asset.getName(), asset.getCreationDate().toString()));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }

    public boolean addAssetDeletion(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        // Get current date
        LocalDate currentDate = LocalDate.now();
        Date date = Date.valueOf(currentDate);

        //Generate message
        newEntry.setMessage(String.format(DELETION_STR,asset. getCreatorName(), asset.getName(), date));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }


    public List<BackLog> getBackLogByAsset(Long id) {
        return backLogRepository.findByAssetId(id);

    }



}
