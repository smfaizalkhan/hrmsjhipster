package org.khfz.hrms.cucumber.stepdefs;

import static org.assertj.core.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

import org.khfz.hrms.domain.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class UserStepDefs extends StepDefs {

	private ResponseEntity<List> responseEntityOfAuthorities;
	private ResponseEntity<String> responseEntity;

	@When("i login as {string} with password {string}")
	public void i_login_as_with_password(String userId, String password) {
		// Write code here that turns the phrase above into concrete actions
		this.setXsrfToken(userId, password);
	}

	@And("I'm able to access getAuthorities method")
	public void i_m_able_to_access_getAuthorities_method() {
	
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.COOKIE, jsessionId);
		httpHeaders.add(HttpHeaders.COOKIE, xsrfToken);

		responseEntityOfAuthorities = testRestTemplate.exchange("/api/users/authorities", HttpMethod.GET,
				new HttpEntity<>(httpHeaders), List.class);
	
		assertThat(responseEntityOfAuthorities.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Then("I get List of Authorities")
	public void i_get_List_of_Authorities(DataTable dataTable) {

		List<String> roles = dataTable.asList();
		assertThat(responseEntityOfAuthorities.getBody()).isEqualTo(roles);

	}

	@When("i access getAuthorities method")
	public void i_access_getAuthorities_method() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.COOKIE, jsessionId);
		httpHeaders.add(HttpHeaders.COOKIE, xsrfToken);
		 responseEntity = testRestTemplate.exchange("/api/users/authorities", HttpMethod.GET,
				new HttpEntity<>(httpHeaders), String.class);
		
	}

	@Then("i get {int} status code")
	public void i_get_status_code(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	 assertThat(HttpStatus.FORBIDDEN).isEqualTo(responseEntity.getStatusCode());
	}
}
