package com.tracker.dss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransactionRequest {

    private String location;
    private String senderName;
    private String senderEmail;
    private Long nik;
    private String receiverName;
    private String branchCode;
    private String cityCode;
    private String senderAddress;
    private String workerId;
    private String receiverAddress;
    private String phoneNumber;
    private String message;
    private String description;
    private String statusCode;
    @DecimalMin(value = "0.000", message = "Weight cannot be negative")
    @Digits(integer = 10, fraction = 3, message = "Weight must have maximum 3 decimal places")
    @JsonProperty("weight")
    private BigDecimal weight;


}
