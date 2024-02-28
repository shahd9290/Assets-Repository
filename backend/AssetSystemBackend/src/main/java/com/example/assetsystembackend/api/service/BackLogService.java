package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.repository.BackLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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


    public List<String> getBackLog() {
        List<BackLog> all = backLogRepository.findAll();
        return all.stream()
                .map(BackLog::getMessage)
                .collect(Collectors.toList());
    }

    public boolean addAssetCreation(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset);

        //Generate message
        newEntry.setMessage(String.format(CREATION_STR, asset.getName(), asset.getCreationDate().toString()));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }

    public List<BackLog> getBackLogByAsset(Long id) {
        return backLogRepository.findByAssetId(id);
        // return backLogRepository.findAll().stream().filter(e -> Objects.equals(e.getId(), id)).toList();
    }



}
