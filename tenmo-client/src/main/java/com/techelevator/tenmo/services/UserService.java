package com.techelevator.tenmo.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;

public class UserService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();

	public UserService(String url) {
		BASE_URL = url;
	}

	public List<User> findAll() {
		User[] allUsers = null;

		try {
			allUsers = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}
		List<User> allUsersAsList = Arrays.asList(allUsers);
		
		return allUsersAsList;
	}

	public User findByUserId(Long userId) {
		User selectedUser = null;
		try {
			selectedUser = restTemplate.exchange(BASE_URL + "/users/" + userId, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode());
		}
		return selectedUser;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
}
