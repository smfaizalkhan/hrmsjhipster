package org.khfz.hrms.service.mapper;

import org.khfz.hrms.domain.*;
import org.khfz.hrms.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
 
    default Employee fromId(Long id) {
        if (id == null) { 
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
