package com.tracker.dss.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserInfo {
    @Id
    private String id;
    private Integer level;
    private Long NIK;
    private String username;
    private String password;
    private String tagId;
    private String nickname;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;
    private String countryCode;
    private String destinationCountryCode;
}
