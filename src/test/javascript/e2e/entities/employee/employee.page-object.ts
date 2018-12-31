import { element, by, ElementFinder } from 'protractor';

export class EmployeeComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-employee div table .btn-danger'));
    title = element.all(by.css('jhi-employee div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmployeeUpdatePage {
    pageTitle = element(by.id('jhi-employee-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    dojInput = element(by.id('field_doj'));
    reportingtoInput = element(by.id('field_reportingto'));
    loginInput = element(by.id('field_login'));
    passwordInput = element(by.id('field_password'));
    firstnameInput = element(by.id('field_firstname'));
    lastnameInput = element(by.id('field_lastname'));
    emailInput = element(by.id('field_email'));
    reportessInput = element(by.id('field_reportess'));
    contactnumberInput = element(by.id('field_contactnumber'));
    authorityInput = element(by.id('field_authority'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setDojInput(doj) {
        await this.dojInput.sendKeys(doj);
    }

    async getDojInput() {
        return this.dojInput.getAttribute('value');
    }

    async setReportingtoInput(reportingto) {
        await this.reportingtoInput.sendKeys(reportingto);
    }

    async getReportingtoInput() {
        return this.reportingtoInput.getAttribute('value');
    }

    async setLoginInput(login) {
        await this.loginInput.sendKeys(login);
    }

    async getLoginInput() {
        return this.loginInput.getAttribute('value');
    }

    async setPasswordInput(password) {
        await this.passwordInput.sendKeys(password);
    }

    async getPasswordInput() {
        return this.passwordInput.getAttribute('value');
    }

    async setFirstnameInput(firstname) {
        await this.firstnameInput.sendKeys(firstname);
    }

    async getFirstnameInput() {
        return this.firstnameInput.getAttribute('value');
    }

    async setLastnameInput(lastname) {
        await this.lastnameInput.sendKeys(lastname);
    }

    async getLastnameInput() {
        return this.lastnameInput.getAttribute('value');
    }

    async setEmailInput(email) {
        await this.emailInput.sendKeys(email);
    }

    async getEmailInput() {
        return this.emailInput.getAttribute('value');
    }

    async setReportessInput(reportess) {
        await this.reportessInput.sendKeys(reportess);
    }

    async getReportessInput() {
        return this.reportessInput.getAttribute('value');
    }

    async setContactnumberInput(contactnumber) {
        await this.contactnumberInput.sendKeys(contactnumber);
    }

    async getContactnumberInput() {
        return this.contactnumberInput.getAttribute('value');
    }

    async setAuthorityInput(authority) {
        await this.authorityInput.sendKeys(authority);
    }

    async getAuthorityInput() {
        return this.authorityInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class EmployeeDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-employee-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-employee'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
