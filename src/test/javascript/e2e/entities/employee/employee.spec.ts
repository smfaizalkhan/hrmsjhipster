/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EmployeeComponentsPage, EmployeeDeleteDialog, EmployeeUpdatePage } from './employee.page-object';

const expect = chai.expect;

describe('Employee e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let employeeUpdatePage: EmployeeUpdatePage;
    let employeeComponentsPage: EmployeeComponentsPage;
    let employeeDeleteDialog: EmployeeDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Employees', async () => {
        await navBarPage.goToEntity('employee');
        employeeComponentsPage = new EmployeeComponentsPage();
        expect(await employeeComponentsPage.getTitle()).to.eq('hrmsApp.employee.home.title');
    });

    it('should load create Employee page', async () => {
        await employeeComponentsPage.clickOnCreateButton();
        employeeUpdatePage = new EmployeeUpdatePage();
        expect(await employeeUpdatePage.getPageTitle()).to.eq('hrmsApp.employee.home.createOrEditLabel');
        await employeeUpdatePage.cancel();
    });

    it('should create and save Employees', async () => {
        const nbButtonsBeforeCreate = await employeeComponentsPage.countDeleteButtons();

        await employeeComponentsPage.clickOnCreateButton();
        await promise.all([
            employeeUpdatePage.setDojInput('2000-12-31'),
            employeeUpdatePage.setReportingtoInput('reportingto'),
            employeeUpdatePage.setLoginInput('login'),
            employeeUpdatePage.setPasswordInput('password'),
            employeeUpdatePage.setFirstnameInput('firstname'),
            employeeUpdatePage.setLastnameInput('lastname'),
            employeeUpdatePage.setEmailInput('email'),
            employeeUpdatePage.setReportessInput('reportess'),
            employeeUpdatePage.setContactnumberInput('contactnumber'),
            employeeUpdatePage.setAuthorityInput('authority')
        ]);
        expect(await employeeUpdatePage.getDojInput()).to.eq('2000-12-31');
        expect(await employeeUpdatePage.getReportingtoInput()).to.eq('reportingto');
        expect(await employeeUpdatePage.getLoginInput()).to.eq('login');
        expect(await employeeUpdatePage.getPasswordInput()).to.eq('password');
        expect(await employeeUpdatePage.getFirstnameInput()).to.eq('firstname');
        expect(await employeeUpdatePage.getLastnameInput()).to.eq('lastname');
        expect(await employeeUpdatePage.getEmailInput()).to.eq('email');
        expect(await employeeUpdatePage.getReportessInput()).to.eq('reportess');
        expect(await employeeUpdatePage.getContactnumberInput()).to.eq('contactnumber');
        expect(await employeeUpdatePage.getAuthorityInput()).to.eq('authority');
        await employeeUpdatePage.save();
        expect(await employeeUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await employeeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Employee', async () => {
        const nbButtonsBeforeDelete = await employeeComponentsPage.countDeleteButtons();
        await employeeComponentsPage.clickOnLastDeleteButton();

        employeeDeleteDialog = new EmployeeDeleteDialog();
        expect(await employeeDeleteDialog.getDialogTitle()).to.eq('hrmsApp.employee.delete.question');
        await employeeDeleteDialog.clickOnConfirmButton();

        expect(await employeeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
