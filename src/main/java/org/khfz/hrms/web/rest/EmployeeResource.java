package org.khfz.hrms.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.khfz.hrms.service.EmployeeQueryService;
import org.khfz.hrms.service.EmployeeService;
import org.khfz.hrms.service.dto.EmployeeCriteria;
import org.khfz.hrms.service.dto.EmployeeDTO;
import org.khfz.hrms.web.rest.errors.BadRequestAlertException;
import org.khfz.hrms.web.rest.util.HeaderUtil;
import org.khfz.hrms.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    private final EmployeeService employeeService;

    private final EmployeeQueryService employeeQueryService;

    public EmployeeResource(EmployeeService employeeService, EmployeeQueryService employeeQueryService) {
        this.employeeService = employeeService;
        this.employeeQueryService = employeeQueryService;
    }

    /**
     * POST  /employees : Create a new employee.
     *
     * @param employeeDTO the employeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeDTO, or with status 400 (Bad Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employees")
    @Timed
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees : Updates an existing employee.
     *
     * @param employeeDTO the employeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeDTO,
     * or with status 400 (Bad Request) if the employeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the employeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employees")
    @Timed
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees : get all the employees.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     */
    @GetMapping("/employees")
    @Timed
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(EmployeeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Employees by criteria: {}", criteria);
        Page<EmployeeDTO> page = employeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employees");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /employees/count : count all the employees.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/employees/count")
    @Timed
    public ResponseEntity<Long> countEmployees(EmployeeCriteria criteria) {
        log.debug("REST request to count Employees by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /employees/:id : get the "id" employee.
     *
     * @param id the id of the employeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/employees/{id}")
    @Timed
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeDTO);
    }

    /**
     * DELETE  /employees/:id : delete the "id" employee.
     *
     * @param id the id of the employeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employees/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
