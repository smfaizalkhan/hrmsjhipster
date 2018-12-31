import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { IEmployee } from 'app/shared/model/employee.model';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { EmployeeService } from './employee.service';

@Component({
    selector: 'jhi-employee',
    templateUrl: './employee.component.html'
   
})
export class EmployeeComponent  implements OnInit, OnDestroy  {
    currentAccount: any;
    employees: IEmployee[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any; 
    searchvalue: string;
    operation: string;
    propkey: string;
    entity: any;
    searchparams: any[] = [];
  
    constructor(            
        private employeeService: EmployeeService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {              
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.employeeService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IEmployee[]>) => this.paginateEmployees(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );         
    }
 
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/employee'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/employee',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInEmployees();   
        
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEmployee) {
        return item.id;
    }

    registerChangeInEmployees() {
        this.eventSubscriber = this.eventManager.subscribe('employeeListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }        
        return result;
    }   
  
     search() {        
         this.searchparams.push(this.propkey);
         this.searchparams.push(this.operation);
         this.searchparams.push(this.searchvalue);
         this.employeeService.search(this.searchparams, {
             page: this.page - 1,
             size: this.itemsPerPage,
             sort: this.sort()
         }).subscribe((res: HttpResponse<IEmployee[]>)  => {
             console.log('red' , res);
             this.searchparams = [];
             this.paginateEmployees(res.body, res.headers),
             (res: HttpErrorResponse) => this.onError(res.message);
         });
     }
    private paginateEmployees(data: IEmployee[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.employees = data;      
        this.entity = Object.keys(this.employees[0]);       
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
