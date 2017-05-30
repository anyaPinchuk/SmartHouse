import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';
import {NO_ERRORS_SCHEMA} from '@angular/core';

import {TranslateLoader, TranslateModule, TranslateService, TranslateStaticLoader} from 'ng2-translate';
import {Http} from '@angular/http';
import {Router} from '@angular/router';
import {AppComponent} from './app.component';
import {DeviceService} from './shared/device.service';
import {SharedService} from './shared/shared.service';
import {User} from './shared/user';
import {RouterLinkStubDirective} from './stub/router-stubs';


class RouterStub {
  navigateByUrl(url: string) {
    return url;
  }
}


describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let translateService: TranslateService;
  let spy;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent, RouterLinkStubDirective],
      schemas: [NO_ERRORS_SCHEMA],
      imports: [TranslateModule.forRoot({
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [Http]
      })],
      providers: [SharedService, DeviceService, User, TranslateService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    translateService = fixture.debugElement.injector.get(TranslateService);
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should inject Router', () => {
    inject([Router], (router: Router) => {
      const spyRouter = spyOn(router, 'navigateByUrl');
      expect(router).toBeTruthy();
    });
  });


  it('should inject translate service', () => {
    expect(translateService).toBeTruthy();
  });

  it('should return translation of LANG.EN like English', () => {
    translateService.get('LANG.EN').subscribe(res => {
      expect(res).toBe('English');
    });
  });

});
export function createTranslateLoader(http: Http) {
  return new TranslateStaticLoader(http, './assets/i18n', '.json');
}

