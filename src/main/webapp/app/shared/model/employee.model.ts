import { Moment } from 'moment';

export interface IEmployee {
    id?: number;
    doj?: Moment;
    reportingto?: string;
    login?: string;
    password?: string;
    firstname?: string;
    lastname?: string;
    email?: string;
    reportess?: string;
    contactnumber?: string;
    authority?: string;
}

export class Employee implements IEmployee {
    
    constructor(
        public id?: number,
        public doj?: Moment,
        public reportingto?: string,
        public login?: string,
        public password?: string,
        public firstname?: string,
        public lastname?: string,
        public email?: string,
        public reportess?: string,
        public contactnumber?: string,
        public authority?: string
    ) {}
}
