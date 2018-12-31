import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { createSearchRequest } from 'app/shared/util/search-util.ts';
import { IEmployee } from 'app/shared/model/employee.model';

type EntityResponseType = HttpResponse<IEmployee>;
type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeService {
    public resourceUrl = SERVER_API_URL + 'api/employees';

    constructor(private http: HttpClient) {}

    create(employee: IEmployee): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(employee);        
        return this.http
            .post<IEmployee>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(employee: IEmployee): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(employee);
        return this.http
            .put<IEmployee>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {       
        return this.http
            .get<IEmployee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => {             
               return  this.convertDateFromServer(res);
            }));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);         
        return this.http
            .get<IEmployee[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => 
                 this.convertDateArrayFromServer(res)           
           
            ));        
    }
    
    search(req: any[], pageable: any): Observable<EntityArrayResponseType> {      
        const options = createSearchRequest(req , pageable); 
        return this.http.get<IEmployee[]>(this.resourceUrl,
                {params: options , observe: 'response'})
                 .pipe(map((res: EntityArrayResponseType) => 
                     {                       
                        return  this.convertDateArrayFromServer(res);
                     }    
                 ));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
    
    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }

    protected convertDateFromClient(employee: IEmployee): IEmployee {
        const copy: IEmployee = Object.assign({}, employee, {
            doj: employee.doj != null && employee.doj.isValid() ? employee.doj.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.doj = res.body.doj != null ? moment(res.body.doj) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {          
            res.body.forEach((employee: IEmployee) => {
                employee.doj = employee.doj != null ? moment(employee.doj) : null;
            });
        }
        return res;
    }
}
