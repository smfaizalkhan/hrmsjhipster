package org.khfz.hrms.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.khfz.hrms.domain.Employee;
import org.khfz.hrms.domain.*; // for static metamodels
import org.khfz.hrms.repository.EmployeeRepository;
import org.khfz.hrms.service.dto.EmployeeCriteria;
import org.khfz.hrms.service.dto.EmployeeDTO;
import org.khfz.hrms.service.mapper.EmployeeMapper;

/**
 * Service for executing complex queries for Employee entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeDTO} or a {@link Page} of {@link EmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeQueryService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Return a {@link List} of {@link EmployeeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findByCriteria(EmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        employeeRepository.findAll(specification).forEach(emp -> log.debug("emp" , emp.getFirstname()));
        return employeeMapper.toDto(employeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);      
        return employeeRepository.findAll(specification, page)
            .map(employeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert EmployeeCriteria to a {@link Specification}
     */
    private Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Employee_.id));
            }
            if (criteria.getDoj() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDoj(), Employee_.doj));
            }
            if (criteria.getReportingto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportingto(), Employee_.reportingto));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), Employee_.login));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Employee_.password));
            }
            if (criteria.getFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstname(), Employee_.firstname));
            }
            if (criteria.getLastname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastname(), Employee_.lastname));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Employee_.email));
            }
            if (criteria.getReportess() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportess(), Employee_.reportess));
            }
            if (criteria.getContactnumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactnumber(), Employee_.contactnumber));
            }
            if (criteria.getAuthority() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthority(), Employee_.authority));
            }
        }
        return specification;
    }
}
