import {async, ComponentFixture, inject, TestBed} from '@angular/core/testing';
import {DeviceComponent} from './device.component';
import {SharedService} from '../shared/shared.service';
import {TranslateLoader, TranslateModule, TranslateStaticLoader} from 'ng2-translate';
import {Http} from '@angular/http';
import {Router} from '@angular/router';
import {DeviceService} from '../shared/device.service';
import {User} from '../shared/user';

class RouterStub {
  navigateByUrl(url: string) {
    return url;
  }
}

describe('DeviceComponent', () => {
  let component: DeviceComponent;
  let fixture: ComponentFixture<DeviceComponent>;
  let deviceService: DeviceService;
  let spy;
  let spyComponent;
  let spyRouter;


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DeviceComponent],
      imports: [TranslateModule.forRoot({
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [Http]
      })],
      providers: [SharedService, DeviceService, User, {provide: Router, useClass: RouterStub}]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeviceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    deviceService = fixture.debugElement.injector.get(DeviceService);
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should inject device service and call getDevices method', () => {
    spy = spyOn(deviceService, 'getDevices').and.returnValue(Promise.resolve('getDevices method'));
    deviceService.getDevices();
    fixture.detectChanges();
    expect(spy.calls.any()).toBe(true, 'getDevices was called');
  });

  it('should call turn action method', () => {
    spyComponent = spyOn(component, 'turnAction').and.returnValue(Promise.resolve(true));
    component.turnAction(null, null, null);
    expect(spyComponent.calls.any()).toBe(true, 'turn action was called');
  });

  it('should not call navigateByUrl method', () => {
    inject([Router], (router: Router) => {
      spyRouter = spyOn(router, 'navigateByUrl');
      expect(spyRouter.calls.any()).toBe(false, 'navigate by url method was not called');
    });
  });
});
export function createTranslateLoader(http: Http) {
  return new TranslateStaticLoader(http, './assets/i18n', '.json');
}
