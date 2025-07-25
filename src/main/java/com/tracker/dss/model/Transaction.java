package com.tracker.dss.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Setter
@Getter
public class Transaction implements Persistable<String> {

	@Id
    private String id;
	private String username;
	private String customerId;
	private String transactionId;
	private String event;
	private String statusCode;
	private String message;
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime createdDate;

	@Override
	public boolean isNew() {
		return true;
	}
	
}
