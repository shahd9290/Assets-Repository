package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.repository.BackLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackLogService {

    //Dummy User
    private final String USER = "James";

    private final BackLogRepository backLogRepository;

    //Creation message
    private final String CREATION_STR = USER + " created %s on %s";


    @Autowired
    public BackLogService(BackLogRepository logRepo) {
        backLogRepository = logRepo;
    }
    
    public boolean addAssetCreation(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset);

        //Generate message
        newEntry.setMessage(String.format(CREATION_STR, asset.getName(), asset.getCreationDate().toString()));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }




}
