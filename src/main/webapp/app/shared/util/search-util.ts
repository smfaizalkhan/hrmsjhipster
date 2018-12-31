import { HttpParams } from '@angular/common/http';

export const createSearchRequest = (req: any[] , pageable: any): HttpParams  => {
    let  searchoptions = new HttpParams();   
    let prop: string = req[0];  
    const operand: string  = req[1];
    const value: string  = req[2];   
    switch (operand) {     
    case '=':
        prop = prop.concat('.equals');  
    break;
    case '>':
        prop = prop.concat('.greaterOrEqualThan');  
        break;
    case '<':
        prop = prop.concat('.lessThan');  
        break;
    case '%LIKE%':
        prop =  prop.concat('.equals');  
        break;
    }
    searchoptions =  searchoptions.set(prop, value);
     Object.keys(pageable).forEach(key => {
            if (key !== 'sort') {                
                searchoptions = searchoptions.append(key, pageable[key]);
            }
        });
        if (pageable.sort) {
            pageable.sort.forEach(val => {
                searchoptions = searchoptions.append('sort', val);
            });
        }
    return searchoptions;
};
