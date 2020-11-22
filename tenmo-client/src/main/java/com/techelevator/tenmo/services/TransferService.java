package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class TransferService {
	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();
	private AccountService accountService = new AccountService("http://localhost:8080/");

	public TransferService(String url) {
		BASE_URL = url;
	}

//	public Double sendAmount(AuthenticatedUser sender, User receiver, Double transferAmount) {
	public BigDecimal sendAmount(AuthenticatedUser sender, User receiver, BigDecimal transferAmount) {
		AUTH_TOKEN = sender.getToken();

//		Double amountSent = null;
		BigDecimal amountSent = null;
		Transfer transfer = new Transfer(sender.getUser().getId(), receiver.getId(), transferAmount);
		
//		if(transferAmount > accountService.getBalance(sender)) {
		if(transferAmount.compareTo(accountService.getBalance(sender)) == 1) {
			return amountSent;
		}
		
		try {
//			amountSent = restTemplate.postForObject(BASE_URL + "/transfers", makeTransferEntity(transfer), Double.class);
			amountSent = restTemplate.postForObject(BASE_URL + "/transfers", makeTransferEntity(transfer), BigDecimal.class);

		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}

		return amountSent;
	}

	public List<Transfer> listTransfers(AuthenticatedUser currentUser) {

		List<Transfer> allUserTransfersAsList = new ArrayList<>();
		Transfer[] allUserTransfers = null;
		AUTH_TOKEN = currentUser.getToken();
		try {
			allUserTransfers = restTemplate.exchange(BASE_URL + "/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
			allUserTransfersAsList = Arrays.asList(allUserTransfers);

		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}
		
		return allUserTransfersAsList;
	}		
		
	public Transfer getTransferDetails(Long userId, int transferId) {
		
		Transfer selectedTransfer = null;
		try {
			selectedTransfer = restTemplate.exchange(BASE_URL + userId + "/transfers/details/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}
		return selectedTransfer;
		
	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}
