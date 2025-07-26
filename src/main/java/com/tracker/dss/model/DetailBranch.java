package com.tracker.dss.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "DetailBranch")
public class DetailBranch {

    @Id
    private String _id;
    private String code;
    private String name;
    private String branchCode;
    private String description;
    private String address;
    private String countryCode;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String latitude;
    private String longitude;
    private String phone;
    private String phoneType;
    private String phoneNumber;
}
