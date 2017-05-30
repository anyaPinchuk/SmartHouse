import {Injectable} from '@angular/core';
import {Http} from '@angular/http';

@Injectable()
export class LocaleService {
  constructor(private http: Http) {
  }

  getLang() {
    return this.http.get('/api/lang/get');
  }

  setLang(lang: string) {
    return this.http.get('/api/lang/set?lang=' + lang);
  }
}
