package com.tracker.dss.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionRequest {

    private String location;
    private String senderName;
    private String senderEmail;
    private Long nik;
    private String receiverName;
    private String senderAddress;
    private String receiverAddress;
    private String phoneNumber;
    private String message;
    private String description;

}
