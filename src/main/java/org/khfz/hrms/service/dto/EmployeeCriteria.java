package org.khfz.hrms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Employee entity. This class is used in EmployeeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /employees?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter doj;

    private StringFilter reportingto;

    private StringFilter login;

    private StringFilter password;

    private StringFilter firstname;

    private StringFilter lastname;

    private StringFilter email;

    private StringFilter reportess;

    private StringFilter contactnumber;

    private StringFilter authority;

    public EmployeeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDoj() {
        return doj;
    }

    public void setDoj(LocalDateFilter doj) {
        this.doj = doj;
    }

    public StringFilter getReportingto() {
        return reportingto;
    }

    public void setReportingto(StringFilter reportingto) {
        this.reportingto = reportingto;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getReportess() {
        return reportess;
    }

    public void setReportess(StringFilter reportess) {
        this.reportess = reportess;
    }

    public StringFilter getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(StringFilter contactnumber) {
        this.contactnumber = contactnumber;
    }

    public StringFilter getAuthority() {
        return authority;
    }

    public void setAuthority(StringFilter authority) {
        this.authority = authority;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(doj, that.doj) &&
            Objects.equals(reportingto, that.reportingto) &&
            Objects.equals(login, that.login) &&
            Objects.equals(password, that.password) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(reportess, that.reportess) &&
            Objects.equals(contactnumber, that.contactnumber) &&
            Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        doj,
        reportingto,
        login,
        password,
        firstname,
        lastname,
        email,
        reportess,
        contactnumber,
        authority
        );
    }

    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (doj != null ? "doj=" + doj + ", " : "") +
                (reportingto != null ? "reportingto=" + reportingto + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (firstname != null ? "firstname=" + firstname + ", " : "") +
                (lastname != null ? "lastname=" + lastname + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (reportess != null ? "reportess=" + reportess + ", " : "") +
                (contactnumber != null ? "contactnumber=" + contactnumber + ", " : "") +
                (authority != null ? "authority=" + authority + ", " : "") +
            "}";
    }

}
