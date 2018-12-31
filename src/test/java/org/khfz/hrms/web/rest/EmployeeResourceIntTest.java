package org.khfz.hrms.web.rest;

import org.khfz.hrms.HrmsApp;

import org.khfz.hrms.domain.Employee;
import org.khfz.hrms.domain.User;
import org.khfz.hrms.repository.AuthorityRepository;
import org.khfz.hrms.repository.EmployeeRepository;
import org.khfz.hrms.repository.UserRepository;
import org.khfz.hrms.service.EmployeeService;
import org.khfz.hrms.service.dto.EmployeeDTO;
import org.khfz.hrms.service.mapper.EmployeeMapper;
import org.khfz.hrms.web.rest.errors.ExceptionTranslator;
import org.khfz.hrms.service.dto.EmployeeCriteria;
import org.khfz.hrms.service.EmployeeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.khfz.hrms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EmployeeResource REST controller.
 *
 * @see EmployeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HrmsApp.class)
public class EmployeeResourceIntTest {

	private static final LocalDate DEFAULT_DOJ = LocalDate.ofEpochDay(0L);
	private static final LocalDate UPDATED_DOJ = LocalDate.now(ZoneId.systemDefault());

	private static final String DEFAULT_REPORTINGTO = "AAAAAAAAAA";
	private static final String UPDATED_REPORTINGTO = "BBBBBBBBBB";

	private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
	private static final String UPDATED_LOGIN = "BBBBBBBBBB";

	private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
	private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

	private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
	private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

	private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
	private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

	private static final String DEFAULT_EMAIL = "f@f.com";
	private static final String UPDATED_EMAIL = "k@k.com";

	private static final String DEFAULT_REPORTESS = "AAAAAAAAAA";
	private static final String UPDATED_REPORTESS = "BBBBBBBBBB";

	private static final String DEFAULT_CONTACTNUMBER = "AAAAAAAAAA";
	private static final String UPDATED_CONTACTNUMBER = "BBBBBBBBBB";

	private static final String DEFAULT_AUTHORITY = "ROLE_USER";
	private static final String UPDATED_AUTHORITY = "ROLE_HR";

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeQueryService employeeQueryService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restEmployeeMockMvc;

	private Employee employee;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final EmployeeResource employeeResource = new EmployeeResource(employeeService, employeeQueryService);
		this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setConversionService(createFormattingConversionService()).setMessageConverters(jacksonMessageConverter)
				.build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Employee createEntity(EntityManager em) {
		Employee employee = new Employee().doj(DEFAULT_DOJ).reportingto(DEFAULT_REPORTINGTO).login(DEFAULT_LOGIN)
				.password(DEFAULT_PASSWORD).firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME).email(DEFAULT_EMAIL)
				.reportess(DEFAULT_REPORTESS).contactnumber(DEFAULT_CONTACTNUMBER).authority(DEFAULT_AUTHORITY);
		return employee;
	}

	@Before
	public void initTest() {
		employee = createEntity(em);
	}

	@Test
	@Transactional
	public void createEmployee() throws Exception {
		int databaseSizeBeforeCreate = employeeRepository.findAll().size();
		int userTableSizeBeforeCreate = userRepository.findAll().size();
		// Create the Employee
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isCreated());

		List<User> userList = userRepository.findAll();

		assertThat(userList).hasSize(userTableSizeBeforeCreate + 1);
		// Validate the Employee in the database
		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
		Employee testEmployee = employeeList.get(employeeList.size() - 1);
		assertThat(testEmployee.getDoj()).isEqualTo(DEFAULT_DOJ);
		assertThat(testEmployee.getReportingto()).isEqualTo(DEFAULT_REPORTINGTO);
		assertThat(testEmployee.getLogin()).isEqualTo(DEFAULT_LOGIN);
		assertThat(testEmployee.getPassword()).isEqualTo(DEFAULT_PASSWORD);
		assertThat(testEmployee.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
		assertThat(testEmployee.getLastname()).isEqualTo(DEFAULT_LASTNAME);
		assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(testEmployee.getReportess()).isEqualTo(DEFAULT_REPORTESS);
		assertThat(testEmployee.getContactnumber()).isEqualTo(DEFAULT_CONTACTNUMBER);
		assertThat(testEmployee.getAuthority()).isEqualTo(DEFAULT_AUTHORITY);
	}

	@Test
	@Transactional
	public void createEmployeeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = employeeRepository.findAll().size();

		// Create the Employee with an existing ID
		employee.setId(1L);
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		// An entity with an existing ID cannot be created, so this API call must fail
		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		// Validate the Employee in the database
		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkDojIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setDoj(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkReportingtoIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setReportingto(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLoginIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setLogin(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkPasswordIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setPassword(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkFirstnameIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setFirstname(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkEmailIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setEmail(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkContactnumberIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setContactnumber(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkAuthorityIsRequired() throws Exception {
		int databaseSizeBeforeTest = employeeRepository.findAll().size();
		// set the field null
		employee.setAuthority(null);

		// Create the Employee, which fails.
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		restEmployeeMockMvc.perform(post("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllEmployees() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList
		restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
				.andExpect(jsonPath("$.[*].doj").value(hasItem(DEFAULT_DOJ.toString())))
				.andExpect(jsonPath("$.[*].reportingto").value(hasItem(DEFAULT_REPORTINGTO.toString())))
				.andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
				.andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
				.andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
				.andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
				.andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
				.andExpect(jsonPath("$.[*].reportess").value(hasItem(DEFAULT_REPORTESS.toString())))
				.andExpect(jsonPath("$.[*].contactnumber").value(hasItem(DEFAULT_CONTACTNUMBER.toString())))
				.andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));
	}

	@Test
	@Transactional
	public void getEmployee() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get the employee
		restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(employee.getId().intValue()))
				.andExpect(jsonPath("$.doj").value(DEFAULT_DOJ.toString()))
				.andExpect(jsonPath("$.reportingto").value(DEFAULT_REPORTINGTO.toString()))
				.andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
				.andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
				.andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
				.andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
				.andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
				.andExpect(jsonPath("$.reportess").value(DEFAULT_REPORTESS.toString()))
				.andExpect(jsonPath("$.contactnumber").value(DEFAULT_CONTACTNUMBER.toString()))
				.andExpect(jsonPath("$.authority").value(DEFAULT_AUTHORITY.toString()));
	}

	@Test
	@Transactional
	public void getAllEmployeesByDojIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where doj equals to DEFAULT_DOJ
		defaultEmployeeShouldBeFound("doj.equals=" + DEFAULT_DOJ);

		// Get all the employeeList where doj equals to UPDATED_DOJ
		defaultEmployeeShouldNotBeFound("doj.equals=" + UPDATED_DOJ);
	}

	@Test
	@Transactional
	public void getAllEmployeesByDojIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where doj in DEFAULT_DOJ or UPDATED_DOJ
		defaultEmployeeShouldBeFound("doj.in=" + DEFAULT_DOJ + "," + UPDATED_DOJ);

		// Get all the employeeList where doj equals to UPDATED_DOJ
		defaultEmployeeShouldNotBeFound("doj.in=" + UPDATED_DOJ);
	}

	@Test
	@Transactional
	public void getAllEmployeesByDojIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where doj is not null
		defaultEmployeeShouldBeFound("doj.specified=true");

		// Get all the employeeList where doj is null
		defaultEmployeeShouldNotBeFound("doj.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByDojIsGreaterThanOrEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where doj greater than or equals to DEFAULT_DOJ
		defaultEmployeeShouldBeFound("doj.greaterOrEqualThan=" + DEFAULT_DOJ);

		// Get all the employeeList where doj greater than or equals to UPDATED_DOJ
		defaultEmployeeShouldNotBeFound("doj.greaterOrEqualThan=" + UPDATED_DOJ);
	}

	@Test
	@Transactional
	public void getAllEmployeesByDojIsLessThanSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where doj less than or equals to DEFAULT_DOJ
		defaultEmployeeShouldNotBeFound("doj.lessThan=" + DEFAULT_DOJ);

		// Get all the employeeList where doj less than or equals to UPDATED_DOJ
		defaultEmployeeShouldBeFound("doj.lessThan=" + UPDATED_DOJ);
	}

	@Test
	@Transactional
	public void getAllEmployeesByReportingtoIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where reportingto equals to DEFAULT_REPORTINGTO
		defaultEmployeeShouldBeFound("reportingto.equals=" + DEFAULT_REPORTINGTO);

		// Get all the employeeList where reportingto equals to UPDATED_REPORTINGTO
		defaultEmployeeShouldNotBeFound("reportingto.equals=" + UPDATED_REPORTINGTO);
	}

	@Test
	@Transactional
	public void getAllEmployeesByReportingtoIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where reportingto in DEFAULT_REPORTINGTO or
		// UPDATED_REPORTINGTO
		defaultEmployeeShouldBeFound("reportingto.in=" + DEFAULT_REPORTINGTO + "," + UPDATED_REPORTINGTO);

		// Get all the employeeList where reportingto equals to UPDATED_REPORTINGTO
		defaultEmployeeShouldNotBeFound("reportingto.in=" + UPDATED_REPORTINGTO);
	}

	@Test
	@Transactional
	public void getAllEmployeesByReportingtoIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where reportingto is not null
		defaultEmployeeShouldBeFound("reportingto.specified=true");

		// Get all the employeeList where reportingto is null
		defaultEmployeeShouldNotBeFound("reportingto.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByLoginIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where login equals to DEFAULT_LOGIN
		defaultEmployeeShouldBeFound("login.equals=" + DEFAULT_LOGIN);

		// Get all the employeeList where login equals to UPDATED_LOGIN
		defaultEmployeeShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
	}

	@Test
	@Transactional
	public void getAllEmployeesByLoginIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where login in DEFAULT_LOGIN or UPDATED_LOGIN
		defaultEmployeeShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

		// Get all the employeeList where login equals to UPDATED_LOGIN
		defaultEmployeeShouldNotBeFound("login.in=" + UPDATED_LOGIN);
	}

	@Test
	@Transactional
	public void getAllEmployeesByLoginIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where login is not null
		defaultEmployeeShouldBeFound("login.specified=true");

		// Get all the employeeList where login is null
		defaultEmployeeShouldNotBeFound("login.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByPasswordIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where password equals to DEFAULT_PASSWORD
		defaultEmployeeShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

		// Get all the employeeList where password equals to UPDATED_PASSWORD
		defaultEmployeeShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
	}

	@Test
	@Transactional
	public void getAllEmployeesByPasswordIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where password in DEFAULT_PASSWORD or
		// UPDATED_PASSWORD
		defaultEmployeeShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

		// Get all the employeeList where password equals to UPDATED_PASSWORD
		defaultEmployeeShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
	}

	@Test
	@Transactional
	public void getAllEmployeesByPasswordIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where password is not null
		defaultEmployeeShouldBeFound("password.specified=true");

		// Get all the employeeList where password is null
		defaultEmployeeShouldNotBeFound("password.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByFirstnameIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where firstname equals to DEFAULT_FIRSTNAME
		defaultEmployeeShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

		// Get all the employeeList where firstname equals to UPDATED_FIRSTNAME
		defaultEmployeeShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
	}

	@Test
	@Transactional
	public void getAllEmployeesByFirstnameIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where firstname in DEFAULT_FIRSTNAME or
		// UPDATED_FIRSTNAME
		defaultEmployeeShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

		// Get all the employeeList where firstname equals to UPDATED_FIRSTNAME
		defaultEmployeeShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
	}

	@Test
	@Transactional
	public void getAllEmployeesByFirstnameIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where firstname is not null
		defaultEmployeeShouldBeFound("firstname.specified=true");

		// Get all the employeeList where firstname is null
		defaultEmployeeShouldNotBeFound("firstname.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByLastnameIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where lastname equals to DEFAULT_LASTNAME
		defaultEmployeeShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

		// Get all the employeeList where lastname equals to UPDATED_LASTNAME
		defaultEmployeeShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
	}

	@Test
	@Transactional
	public void getAllEmployeesByLastnameIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where lastname in DEFAULT_LASTNAME or
		// UPDATED_LASTNAME
		defaultEmployeeShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

		// Get all the employeeList where lastname equals to UPDATED_LASTNAME
		defaultEmployeeShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
	}

	@Test
	@Transactional
	public void getAllEmployeesByLastnameIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where lastname is not null
		defaultEmployeeShouldBeFound("lastname.specified=true");

		// Get all the employeeList where lastname is null
		defaultEmployeeShouldNotBeFound("lastname.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByEmailIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where email equals to DEFAULT_EMAIL
		defaultEmployeeShouldBeFound("email.equals=" + DEFAULT_EMAIL);

		// Get all the employeeList where email equals to UPDATED_EMAIL
		defaultEmployeeShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
	}

	@Test
	@Transactional
	public void getAllEmployeesByEmailIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where email in DEFAULT_EMAIL or UPDATED_EMAIL
		defaultEmployeeShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

		// Get all the employeeList where email equals to UPDATED_EMAIL
		defaultEmployeeShouldNotBeFound("email.in=" + UPDATED_EMAIL);
	}

	@Test
	@Transactional
	public void getAllEmployeesByEmailIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where email is not null
		defaultEmployeeShouldBeFound("email.specified=true");

		// Get all the employeeList where email is null
		defaultEmployeeShouldNotBeFound("email.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByReportessIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where reportess equals to DEFAULT_REPORTESS
		defaultEmployeeShouldBeFound("reportess.equals=" + DEFAULT_REPORTESS);

		// Get all the employeeList where reportess equals to UPDATED_REPORTESS
		defaultEmployeeShouldNotBeFound("reportess.equals=" + UPDATED_REPORTESS);
	}

	@Test
	@Transactional
	public void getAllEmployeesByReportessIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where reportess in DEFAULT_REPORTESS or
		// UPDATED_REPORTESS
		defaultEmployeeShouldBeFound("reportess.in=" + DEFAULT_REPORTESS + "," + UPDATED_REPORTESS);

		// Get all the employeeList where reportess equals to UPDATED_REPORTESS
		defaultEmployeeShouldNotBeFound("reportess.in=" + UPDATED_REPORTESS);
	}

	@Test
	@Transactional
	public void getAllEmployeesByReportessIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where reportess is not null
		defaultEmployeeShouldBeFound("reportess.specified=true");

		// Get all the employeeList where reportess is null
		defaultEmployeeShouldNotBeFound("reportess.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByContactnumberIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where contactnumber equals to DEFAULT_CONTACTNUMBER
		defaultEmployeeShouldBeFound("contactnumber.equals=" + DEFAULT_CONTACTNUMBER);

		// Get all the employeeList where contactnumber equals to UPDATED_CONTACTNUMBER
		defaultEmployeeShouldNotBeFound("contactnumber.equals=" + UPDATED_CONTACTNUMBER);
	}

	@Test
	@Transactional
	public void getAllEmployeesByContactnumberIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where contactnumber in DEFAULT_CONTACTNUMBER or
		// UPDATED_CONTACTNUMBER
		defaultEmployeeShouldBeFound("contactnumber.in=" + DEFAULT_CONTACTNUMBER + "," + UPDATED_CONTACTNUMBER);

		// Get all the employeeList where contactnumber equals to UPDATED_CONTACTNUMBER
		defaultEmployeeShouldNotBeFound("contactnumber.in=" + UPDATED_CONTACTNUMBER);
	}

	@Test
	@Transactional
	public void getAllEmployeesByContactnumberIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where contactnumber is not null
		defaultEmployeeShouldBeFound("contactnumber.specified=true");

		// Get all the employeeList where contactnumber is null
		defaultEmployeeShouldNotBeFound("contactnumber.specified=false");
	}

	@Test
	@Transactional
	public void getAllEmployeesByAuthorityIsEqualToSomething() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where authority equals to DEFAULT_AUTHORITY
		defaultEmployeeShouldBeFound("authority.equals=" + DEFAULT_AUTHORITY);

		// Get all the employeeList where authority equals to UPDATED_AUTHORITY
		defaultEmployeeShouldNotBeFound("authority.equals=" + UPDATED_AUTHORITY);
	}

	@Test
	@Transactional
	public void getAllEmployeesByAuthorityIsInShouldWork() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where authority in DEFAULT_AUTHORITY or
		// UPDATED_AUTHORITY
		defaultEmployeeShouldBeFound("authority.in=" + DEFAULT_AUTHORITY + "," + UPDATED_AUTHORITY);

		// Get all the employeeList where authority equals to UPDATED_AUTHORITY
		defaultEmployeeShouldNotBeFound("authority.in=" + UPDATED_AUTHORITY);
	}

	@Test
	@Transactional
	public void getAllEmployeesByAuthorityIsNullOrNotNull() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		// Get all the employeeList where authority is not null
		defaultEmployeeShouldBeFound("authority.specified=true");

		// Get all the employeeList where authority is null
		defaultEmployeeShouldNotBeFound("authority.specified=false");
	}

	/**
	 * Executes the search, and checks that the default entity is returned
	 */
	private void defaultEmployeeShouldBeFound(String filter) throws Exception {
		restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
				.andExpect(jsonPath("$.[*].doj").value(hasItem(DEFAULT_DOJ.toString())))
				.andExpect(jsonPath("$.[*].reportingto").value(hasItem(DEFAULT_REPORTINGTO.toString())))
				.andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
				.andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
				.andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
				.andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
				.andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
				.andExpect(jsonPath("$.[*].reportess").value(hasItem(DEFAULT_REPORTESS.toString())))
				.andExpect(jsonPath("$.[*].contactnumber").value(hasItem(DEFAULT_CONTACTNUMBER.toString())))
				.andExpect(jsonPath("$.[*].authority").value(hasItem(DEFAULT_AUTHORITY.toString())));

		// Check, that the count call also returns 1
		restEmployeeMockMvc.perform(get("/api/employees/count?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().string("1"));
	}

	/**
	 * Executes the search, and checks that the default entity is not returned
	 */
	private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
		restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());

		// Check, that the count call also returns 0
		restEmployeeMockMvc.perform(get("/api/employees/count?sort=id,desc&" + filter)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content().string("0"));
	}

	@Test
	@Transactional
	public void getNonExistingEmployee() throws Exception {
		// Get the employee
		restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateEmployee() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

		// Update the employee
		Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
		// Disconnect from session so that the updates on updatedEmployee are not
		// directly saved in db
		em.detach(updatedEmployee);
		updatedEmployee.doj(UPDATED_DOJ).reportingto(UPDATED_REPORTINGTO).login(UPDATED_LOGIN)
				.password(UPDATED_PASSWORD).firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).email(UPDATED_EMAIL)
				.reportess(UPDATED_REPORTESS).contactnumber(UPDATED_CONTACTNUMBER).authority(UPDATED_AUTHORITY);
		EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

		restEmployeeMockMvc.perform(put("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isOk());
		
		// Validate the Employee in the database
		  List<Employee> employeeList = employeeRepository.findAll();
		
		  assertThat(employeeList).hasSize(databaseSizeBeforeUpdate); Employee
		  testEmployee = employeeList.get(employeeList.size() - 1);
		  assertThat(testEmployee.getDoj()).isEqualTo(UPDATED_DOJ);
		  assertThat(testEmployee.getReportingto()).isEqualTo(UPDATED_REPORTINGTO);
		  assertThat(testEmployee.getLogin()).isEqualTo(UPDATED_LOGIN);
		  assertThat(testEmployee.getPassword()).isEqualTo(UPDATED_PASSWORD);
		  assertThat(testEmployee.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
		  assertThat(testEmployee.getLastname()).isEqualTo(UPDATED_LASTNAME);
		  assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
		  assertThat(testEmployee.getReportess()).isEqualTo(UPDATED_REPORTESS);
		  assertThat(testEmployee.getContactnumber()).isEqualTo(UPDATED_CONTACTNUMBER);
		  assertThat(testEmployee.getAuthority()).isEqualTo(UPDATED_AUTHORITY);
		 
	}

	@Test
	@Transactional
	public void updateNonExistingEmployee() throws Exception {
		int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

		// Create the Employee
		EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restEmployeeMockMvc.perform(put("/api/employees").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());

		// Validate the Employee in the database
		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void deleteEmployee() throws Exception {
		// Initialize the database
		employeeRepository.saveAndFlush(employee);

		int databaseSizeBeforeDelete = employeeRepository.findAll().size();

		// Get the employee
		restEmployeeMockMvc
				.perform(delete("/api/employees/{id}", employee.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Employee> employeeList = employeeRepository.findAll();
		assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Employee.class);
		Employee employee1 = new Employee();
		employee1.setId(1L);
		Employee employee2 = new Employee();
		employee2.setId(employee1.getId());
		assertThat(employee1).isEqualTo(employee2);
		employee2.setId(2L);
		assertThat(employee1).isNotEqualTo(employee2);
		employee1.setId(null);
		assertThat(employee1).isNotEqualTo(employee2);
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(EmployeeDTO.class);
		EmployeeDTO employeeDTO1 = new EmployeeDTO();
		employeeDTO1.setId(1L);
		EmployeeDTO employeeDTO2 = new EmployeeDTO();
		assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
		employeeDTO2.setId(employeeDTO1.getId());
		assertThat(employeeDTO1).isEqualTo(employeeDTO2);
		employeeDTO2.setId(2L);
		assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
		employeeDTO1.setId(null);
		assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
	}

	@Test
	@Transactional
	public void testEntityFromId() {
		assertThat(employeeMapper.fromId(42L).getId()).isEqualTo(42);
		assertThat(employeeMapper.fromId(null)).isNull();
	}
}
