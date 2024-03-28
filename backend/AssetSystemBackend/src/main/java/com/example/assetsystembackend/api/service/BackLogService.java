package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.model.BackLog;
import com.example.assetsystembackend.api.repository.BackLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class to manage backlogs.
 */
@Service
public class BackLogService {

    private final BackLogRepository backLogRepository;

    // Creation message
    private final String CREATION_STR = "%s created %s on %s";

    private final String DATE_FORMAT = "yyyy-MM-dd";

    private final String DELETION_STR=  "%s created %s on %s";


    @Autowired
    public BackLogService(BackLogRepository logRepo) {
        backLogRepository = logRepo;
    }

    /**
     * Get all backlogs.
     *
     * @return List of BackLog objects representing backlogs.
     */
    public List<BackLog> getBackLogs() {
        return backLogRepository.findAll();
    }

    /**
     * Add a new entry for asset creation to the backlog.
     *
     * @param asset The asset that was created.
     * @return {@code true} if the entry was successfully added to the backlog, {@code false} otherwise.
     */
    public boolean addAssetCreation(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        //Generate message
        newEntry.setMessage(String.format(CREATION_STR, asset.getCreatorName(), asset.getName(), asset.getCreationDate().toString()));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }

    /**
     * Add a new entry for asset deletion to the backlog.
     *
     * @param asset The asset that was deleted.
     * @return {@code true} if the entry was successfully added to the backlog, {@code false} otherwise.
     */
    public boolean addAssetDeletion(Asset asset) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Generate message
        newEntry.setMessage(String.format(DELETION_STR, asset.getCreatorName(), asset.getName(), currentDate));
        BackLog returned = backLogRepository.save(newEntry);
        return returned.getId() != null;
    }


    /**
     * Adds an entry to the backlog for the change of asset title.
     * This method creates a new entry in the backlog with information about the change of asset title,
     * including the creator's name, previous title, new title, and the current date.
     *
     * @param asset The asset whose title has been changed.
     * @param prevTitle The previous title of the asset.
     */
    public void addAssetTitleChange(Asset asset, String prevTitle) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        // Today's date
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        newEntry.setMessage(String.format("%s changed title of asset from %s to %s on %s", asset.getCreatorName(), prevTitle, asset.getName(), formattedDate));

        backLogRepository.save(newEntry);
    }

    /**
     * Adds an entry to the backlog for the change of asset link or description.
     * This method creates a new entry in the backlog with information about the change of asset link or description,
     * including the creator's name, the option changed (link or description), the asset name, and the current date.
     *
     * @param asset The asset whose link or description has been changed.
     * @param option The option changed, either "link" or "description".
     */
    public void addAssetLinkOrDescriptionChange(Asset asset, String option) {
        BackLog newEntry = new BackLog();
        newEntry.setAsset(asset.getId());

        // Today's date
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        newEntry.setMessage(String.format("%s changed %s of %s on %s", asset.getCreatorName(), option, asset.getName(), formattedDate));

        backLogRepository.save(newEntry);
    }

    public List<BackLog> getBackLogByAsset(Long id) {
        return backLogRepository.findByAssetId(id);

    }



}
