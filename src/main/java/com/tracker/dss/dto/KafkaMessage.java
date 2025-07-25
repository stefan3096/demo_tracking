
package com.tracker.dss.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class KafkaMessage {
    private String clientCode;
    private String method;
    private String messageId;
    private Long messageCount;
    private Object data;

    public KafkaMessage(String clientCode, String method, String messageId, Object data){
        this.clientCode = clientCode;
        this.method = method;
        this.messageId = messageId;
        this.data = data;
    }
}
