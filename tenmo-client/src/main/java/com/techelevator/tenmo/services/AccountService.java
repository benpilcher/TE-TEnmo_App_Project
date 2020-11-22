package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;

public class AccountService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();

	public AccountService(String url) {
		BASE_URL = url;
	}	
	
	public BigDecimal getBalance(AuthenticatedUser currentUser)  {

		BigDecimal balance = null;
		AUTH_TOKEN = currentUser.getToken();
		try {
			balance = restTemplate.exchange(BASE_URL + "/users/accounts/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}
		return balance;
	}
	
	public Integer getAccountId(AuthenticatedUser currentUser) {
		Integer accountId = null;
		try {
			accountId = restTemplate.exchange(BASE_URL + "/users/accounts/ids" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Integer.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}
		return accountId;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}
