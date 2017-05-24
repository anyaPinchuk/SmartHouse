import { Injectable } from '@angular/core';
import {Http} from '@angular/http';

@Injectable()
export class User {
   email: string;
   name: string;
   role: string;
   sessionID: string;
}
