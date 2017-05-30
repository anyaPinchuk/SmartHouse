import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Http, HttpModule, JsonpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {routing} from './app.routes';
import {RouterModule, RouterOutlet, RouterLink, Router} from '@angular/router';
import {ReactiveFormsModule} from '@angular/forms';
import {HomeComponent} from './home/home.component';
import {DeviceComponent} from './device/device.component';
import {DeviceService} from './shared/device.service';
import {SharedService} from './shared/shared.service';
import {User} from './shared/user';
import {DeviceAddComponent} from './device-add/device-add.component';
import {HouseComponent} from './house/house.component';
import {HouseAddComponent} from './house-add/house-add.component';
import {HouseService} from './shared/house.service';
import {NewAccountComponent} from './new-account/new-account.component';
import {DeviceManageComponent} from './device-manage/device-manage.component';
import { ChartComponent } from './chart/chart.component';
import {CustomErrorHandler} from './shared/error-handler';
import {TranslateLoader, TranslateModule, TranslateStaticLoader} from 'ng2-translate';
import {LocaleService} from './shared/locale.service';


export const ROUTER_DIRECTIVES = [RouterOutlet, RouterLink];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    DeviceComponent,
    DeviceAddComponent,
    HouseComponent,
    HouseAddComponent,
    NewAccountComponent,
    DeviceManageComponent,
    ChartComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpModule,
    TranslateModule.forRoot({
      provide: TranslateLoader,
      useFactory: (createTranslateLoader),
      deps: [Http]
    }),
    RouterModule,
    JsonpModule,
    routing
  ],
  providers: [DeviceService, LocaleService, SharedService, HouseService, User, { provide: ErrorHandler, useClass: CustomErrorHandler } ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
export function createTranslateLoader(http: Http) {
  return new TranslateStaticLoader(http, './assets/i18n', '.json');
}
