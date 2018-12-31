package org.khfz.hrms.cucumber.stepdefs;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.khfz.hrms.HrmsApp;
import org.khfz.hrms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HrmsApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class StepDefs {

	@Autowired
	protected TestRestTemplate testRestTemplate;

	protected String xsrfToken;
	
	protected String jsessionId;
	
	protected String userId;

	protected void setXsrfToken(String userId , String password) {
		String uuid = UUID.randomUUID().toString(); // "61288dd-4ca5-4e60-8d8a-57ebce8e7999"
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.COOKIE, "XSRF-TOKEN=" + uuid);
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add("X-XSRF-TOKEN", uuid);

		MultiValueMap<String, String> formValues = new LinkedMultiValueMap<>();

		formValues.add("j_username", userId);
		formValues.add("j_password", password);
		formValues.add("submit", "Login");
		this.userId = userId;
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(formValues,
				httpHeaders);
		ResponseEntity<String> cookieValue = testRestTemplate.exchange("/api/authentication", HttpMethod.POST,
				httpEntity, String.class);
		if (cookieValue.getStatusCode().equals(HttpStatus.OK)) {
			for(String cookieVal : cookieValue.getHeaders().get(HttpHeaders.SET_COOKIE)) {
				if(cookieVal.startsWith("JSESSIONID"))
					jsessionId = cookieVal;
				else
					xsrfToken  = cookieVal;
			}
			
			
		}

	}

}