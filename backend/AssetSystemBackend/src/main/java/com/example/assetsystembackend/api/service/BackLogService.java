package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.repository.BackLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class BackLogService {

    private final BackLogRepository backLogRepository;

    @Autowired
    public BackLogService(BackLogRepository logRepo) {
        backLogRepository = logRepo;
    }

}
