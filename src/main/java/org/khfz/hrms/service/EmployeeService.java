package org.khfz.hrms.service;

import org.khfz.hrms.domain.Employee;
import org.khfz.hrms.domain.User;
import org.khfz.hrms.repository.EmployeeRepository;
import org.khfz.hrms.repository.UserRepository;
import org.khfz.hrms.security.SecurityUtils;
import org.khfz.hrms.service.dto.EmployeeDTO;
import org.khfz.hrms.service.dto.UserDTO;
import org.khfz.hrms.service.mapper.EmployeeMapper;
import org.khfz.hrms.service.mapper.UserMapper;
import org.khfz.hrms.web.rest.errors.EmailAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Employee.
 */
@Service
@Transactional
public class EmployeeService {

	private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

	private final EmployeeRepository employeeRepository;

	private final EmployeeMapper employeeMapper;

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, UserMapper userMapper,
			PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	/**
	 * Save a employee.
	 *
	 * @param employeeDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public EmployeeDTO save(EmployeeDTO employeeDTO) {
		log.debug("Request to save Employee : {}", employeeDTO);

		if (userRepository.findOneByEmailIgnoreCase(employeeDTO.getEmail()).isPresent())
			throw new EmailAlreadyUsedException();

		Employee employee = employeeMapper.toEntity(employeeDTO);

		
		UserDTO userDto = new UserDTO();
		
		userDto.setLogin(employeeDTO.getLogin());
		userDto.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
		userDto.setFirstName(employeeDTO.getFirstname());
		userDto.setLastName(employeeDTO.getLastname());
		userDto.setEmail(employeeDTO.getEmail());
		userDto.setCreatedBy(SecurityUtils.getCurrentUserLogin().toString());
		Set<String> authSet = new HashSet<String>();
		authSet.add(employeeDTO.getAuthority());
		userDto.setAuthorities(authSet);
		User user = userMapper.userDTOToUser(userDto);
		employee = employeeRepository.save(employee);
		user = userRepository.save(user);
		return employeeMapper.toDto(employee);
	}

	/**
	 * Get all the employees.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<EmployeeDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Employees");
		return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
	}

	/**
	 * Get one employee by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Optional<EmployeeDTO> findOne(Long id) {
		log.debug("Request to get Employee : {}", id);
		return employeeRepository.findById(id).map(employeeMapper::toDto);
	}

	/**
	 * Delete the employee by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Employee : {}", id);
		employeeRepository.deleteById(id);
	}
}
