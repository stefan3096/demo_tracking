package com.tracker.dss.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "StatusDetail")
public class StatusDetail {
    @Id
    private String id;
    private String code;
    private String description;
    private String status;
}
