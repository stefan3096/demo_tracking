package com.tracker.dss.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dss.model.ESLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {

    public void log(ESLog esLog) {
        try {
            log.info(new ObjectMapper().writeValueAsString(esLog));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
