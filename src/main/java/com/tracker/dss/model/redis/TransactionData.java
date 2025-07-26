package com.tracker.dss.model.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@RedisHash("Transaction")
public class TransactionData implements Serializable {

    @Id
    private String id;
    private String username;
    private String customerId;
    private String locationCode;
    private String location;
    private String transactionId;
    private String senderName;
    private String receiverName;
    private String message;
    private String event;
    private String statusCode;
    private String workerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    private OffsetDateTime createdDate;
    private BigDecimal weight;

}
