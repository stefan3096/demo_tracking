package com.tracker.dss.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dss.dto.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;

//@Document(indexName = "#{@indexName}")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ESLog {
    @Id
    private String id;
    private String method;
    private String message;
    //@Nullable
    private Long triggerTime;
    //@Nullable
    private Long finishTime;
    //@Nullable
    private Long processTime;
    //@Nullable
    private Object payload;
    //@Nullable
    private String strPayload;
    //@Nullable
    private Long dataCount;
    private String logTime = Instant.now().toString();



    public ESLog(String event, Transaction trxHistory){
        this.id = event+" "+trxHistory.getUsername()+" "+logTime;
        this.method = "Transaction "+event;
        this.payload = trxHistory;
        this.message = event +" "+ trxHistory.getUsername() ;
    }

    public ESLog(String event, UserInfo userInfo){
        this.id = event+" "+userInfo.getUsername()+" "+ logTime;
        this.method = "System Log";
        this.payload = userInfo;
        this.message = event+" "+userInfo.getUsername();
    }



    public ESLog(KafkaMessage message) {
        this.id = "Received "+message.getMessageId();
        this.method = message.getMethod();
        this.message = "Received Data from API";
        if (null != message.getData()){
            try {
                this.strPayload = new ObjectMapper().writeValueAsString(message.getData());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ESLog(String messageId, String method, Long dataCount, Long triggerTime, Long finishTime) {
        this.id = "Done "+messageId;
        this.method = method;
        this.dataCount = dataCount;
        this.message = "Done Send Back All Response data to API";
        this.triggerTime = triggerTime;
        this.finishTime = finishTime;
        this.processTime = finishTime - triggerTime;
    }

    public ESLog(String openingSession, String openingSessionTime){
        this.id = "Scheduler "+openingSession+" "+openingSessionTime;
        this.method = "Scheduler";
    }
}
