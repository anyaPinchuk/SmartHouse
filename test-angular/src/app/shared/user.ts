import { Injectable } from '@angular/core';
import {Http} from '@angular/http';

@Injectable()
export class User {
   email: string;
   role: string;
   sessionID: string;

  getRole(): string {
    return this.role;
  }

  getEmail(): string {
    return this.email;
  }
}
