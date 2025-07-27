package com.tracker.dss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dss.dto.KafkaMessage;
import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.kafka.producer.KafkaSenderTemplate;
import com.tracker.dss.model.UserInfo;
import com.tracker.dss.repository.DetailBranchRepository;
import com.tracker.dss.repository.StatusDetailRepository;
import com.tracker.dss.repository.TransactionRepository;
import com.tracker.dss.repository.UserInfoRepository;
import com.tracker.dss.service.impl.GenerateTransactionServiceImpl;
import com.tracker.dss.service.impl.TransactionRedisServiceimpl;
import com.tracker.dss.util.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DssApplicationTests {

	@Mock
	private UserInfoRepository userInfoRepository;

	@Mock
	private StatusDetailRepository statusDetailRepository;

	@Mock
	private DetailBranchRepository detailBranchRepository;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TransactionRedisServiceimpl transactionRedisService;

	@Mock
	private Weight util;

	@Mock
	private KafkaSenderTemplate kafkaSenderTemplate;

	@InjectMocks
	private GenerateTransactionServiceImpl generateTransactionService;

	private TransactionRequest transactionRequest;
	private UserInfo mockUserInfo;

	@BeforeEach
	void setUp() {
		// Setup test data
		transactionRequest = new TransactionRequest();
		transactionRequest.setNik(1234567890L);
		transactionRequest.setSenderEmail("test@example.com");
		transactionRequest.setSenderName("testuser");
		transactionRequest.setWorkerId("worker123");
		transactionRequest.setStatusCode("PENDING");
		transactionRequest.setBranchCode("BR001");
		transactionRequest.setWeight(BigDecimal.valueOf(1.234));
		transactionRequest.setReceiverName("receiver");

		mockUserInfo = new UserInfo();
		mockUserInfo.setId("user123");
		mockUserInfo.setUsername("testuser");
		mockUserInfo.setNik(1234567890L);
		mockUserInfo.setEmail("test@example.com");
		mockUserInfo.setWorkerId("worker123");
	}


	@Test
	void publishTransaction_PositiveCase_UserFound_ShouldReturnSuccess() {
		when(userInfoRepository.findByNik(anyLong())).thenReturn(Mono.just(mockUserInfo));
		when(userInfoRepository.findByUsername(anyString())).thenReturn(Mono.empty());
		when(userInfoRepository.findByEmail(anyString())).thenReturn(Mono.empty());
		StepVerifier.create(generateTransactionService.publishTransaction(transactionRequest))
				.assertNext(response -> {
					assert "success".equals(response.getBody());
				})
				.verifyComplete();

		verify(userInfoRepository).findByNik(transactionRequest.getNik());
		verify(kafkaSenderTemplate).sendMessage(any(KafkaMessage.class));
	}

	@Test
	void publishTransaction_NegativeCase_NoUserFound_ShouldReturnBadRequest() {
		when(userInfoRepository.findByNik(anyLong())).thenReturn(Mono.empty());
		when(userInfoRepository.findByEmail(anyString())).thenReturn(Mono.empty());
		when(userInfoRepository.findByUsername(anyString())).thenReturn(Mono.empty());

		StepVerifier.create(generateTransactionService.publishTransaction(transactionRequest))
				.assertNext(response -> {
					assert response.getStatusCode().is4xxClientError();
				})
				.verifyComplete();

		verify(userInfoRepository).findByNik(transactionRequest.getNik());
		verify(userInfoRepository).findByEmail(transactionRequest.getSenderEmail());
		verify(userInfoRepository).findByUsername(transactionRequest.getSenderName());
	}

	@Test
	void publishTransaction_NegativeCase_UserFoundByEmail_ShouldReturnSuccess() {
		when(userInfoRepository.findByNik(anyLong())).thenReturn(Mono.empty());
		when(userInfoRepository.findByEmail(anyString())).thenReturn(Mono.just(mockUserInfo));
		when(userInfoRepository.findByUsername(anyString())).thenReturn(Mono.empty());

		StepVerifier.create(generateTransactionService.publishTransaction(transactionRequest))
				.assertNext(response -> {
					assert "success".equals(response.getBody());
				})
				.verifyComplete();

		verify(userInfoRepository).findByNik(transactionRequest.getNik());
		verify(userInfoRepository).findByEmail(transactionRequest.getSenderEmail());
		verify(kafkaSenderTemplate).sendMessage(any(KafkaMessage.class));
	}

	@Test
	void publishTransaction_NegativeCase_UserFoundByUsername_ShouldReturnBadRequest() {
		when(userInfoRepository.findByNik(anyLong())).thenReturn(Mono.empty());
		when(userInfoRepository.findByEmail(anyString())).thenReturn(Mono.empty());
		when(userInfoRepository.findByUsername(anyString())).thenReturn(Mono.just(mockUserInfo));

		StepVerifier.create(generateTransactionService.publishTransaction(transactionRequest))
				.assertNext(response -> {
					assert response.getStatusCode().is2xxSuccessful();
					assert "success".equals(response.getBody());
				})
				.verifyComplete();

		verify(userInfoRepository).findByNik(transactionRequest.getNik());
		verify(userInfoRepository).findByEmail(transactionRequest.getSenderEmail());
		verify(userInfoRepository).findByUsername(transactionRequest.getSenderName());
		verify(kafkaSenderTemplate).sendMessage(any(KafkaMessage.class));
	}

}
