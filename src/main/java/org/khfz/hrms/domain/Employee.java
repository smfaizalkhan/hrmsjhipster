package org.khfz.hrms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "doj", nullable = false)
    private LocalDate doj;

    @NotNull
    @Column(name = "reportingto", nullable = false)
    private String reportingto;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "jhi_password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "reportess")
    private String reportess;

    @NotNull
    @Column(name = "contactnumber", nullable = false)
    private String contactnumber;

    @NotNull
    @Column(name = "authority", nullable = false)
    private String authority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public Employee doj(LocalDate doj) {
        this.doj = doj;
        return this;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public String getReportingto() {
        return reportingto;
    }

    public Employee reportingto(String reportingto) {
        this.reportingto = reportingto;
        return this;
    }

    public void setReportingto(String reportingto) {
        this.reportingto = reportingto;
    }

    public String getLogin() {
        return login;
    }

    public Employee login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public Employee password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public Employee firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Employee lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReportess() {
        return reportess;
    }

    public Employee reportess(String reportess) {
        this.reportess = reportess;
        return this;
    }

    public void setReportess(String reportess) {
        this.reportess = reportess;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public Employee contactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
        return this;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getAuthority() {
        return authority;
    }

    public Employee authority(String authority) {
        this.authority = authority;
        return this;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if (employee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", doj='" + getDoj() + "'" +
            ", reportingto='" + getReportingto() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", reportess='" + getReportess() + "'" +
            ", contactnumber='" + getContactnumber() + "'" +
            ", authority='" + getAuthority() + "'" +
            "}";
    }
}
