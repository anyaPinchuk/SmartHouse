import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';

import {HouseComponent} from './house.component';
import {SharedService} from '../shared/shared.service';
import {TranslateLoader, TranslateModule, TranslateStaticLoader} from 'ng2-translate';
import {Http, HttpModule} from '@angular/http';
import {HouseService} from '../shared/house.service';
import {Router} from '@angular/router';

class RouterStub {
  navigateByUrl(url: string) {
    return url;
  }
}

describe('HouseComponent', () => {
  let component: HouseComponent;
  let fixture: ComponentFixture<HouseComponent>;
  let houseService: HouseService;
  let spy;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HouseComponent],
      imports: [TranslateModule.forRoot({
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [Http]
      })],
      providers: [SharedService, HouseService, {provide: Router, useClass: RouterStub}]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HouseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    houseService = fixture.debugElement.injector.get(HouseService);
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should inject house service and call getHouses method', () => {
    spy = spyOn(houseService, 'getHouses').and.returnValue(Promise.resolve('ggg'));
    houseService.getHouses();
    fixture.detectChanges();
    expect(spy.calls.any()).toBe(true, 'getHouses was called');
  });

  it('should inject router', () => {
    inject([Router], (router: Router) => {
      const spyRouter = spyOn(router, 'navigateByUrl');
      expect(router).toBeTruthy();
    });
  });
});
export function createTranslateLoader(http: Http) {
  return new TranslateStaticLoader(http, './assets/i18n', '.json');
}
