package com.tracker.dss.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class StatusDetail {
    @Id
    private String id;
    private String code;
    private String description;
    private String status;
}
