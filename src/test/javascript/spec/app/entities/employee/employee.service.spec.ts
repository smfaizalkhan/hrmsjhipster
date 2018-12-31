/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { EmployeeService } from 'app/entities/employee/employee.service';
import { IEmployee, Employee } from 'app/shared/model/employee.model';

describe('Service Tests', () => {
    describe('Employee Service', () => {
        let injector: TestBed;
        let service: EmployeeService;
        let httpMock: HttpTestingController;
        let elemDefault: IEmployee;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(EmployeeService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Employee(
                0,
                currentDate,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        doj: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Employee', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        doj: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        doj: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Employee(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Employee', async () => {
                const returnedFromService = Object.assign(
                    {
                        doj: currentDate.format(DATE_FORMAT),
                        reportingto: 'BBBBBB',
                        login: 'BBBBBB',
                        password: 'BBBBBB',
                        firstname: 'BBBBBB',
                        lastname: 'BBBBBB',
                        email: 'BBBBBB',
                        reportess: 'BBBBBB',
                        contactnumber: 'BBBBBB',
                        authority: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        doj: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Employee', async () => {
                const returnedFromService = Object.assign(
                    {
                        doj: currentDate.format(DATE_FORMAT),
                        reportingto: 'BBBBBB',
                        login: 'BBBBBB',
                        password: 'BBBBBB',
                        firstname: 'BBBBBB',
                        lastname: 'BBBBBB',
                        email: 'BBBBBB',
                        reportess: 'BBBBBB',
                        contactnumber: 'BBBBBB',
                        authority: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        doj: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Employee', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
