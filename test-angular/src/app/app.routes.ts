import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {HomeComponent} from './home/home.component';
import {DeviceComponent} from './device/device.component';
import {DeviceAddComponent} from './device-add/device-add.component';
import {HouseComponent} from "./house/house.component";
import {HouseAddComponent} from "./house-add/house-add.component";

// Route Configuration
export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'reg',
    component: RegisterComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'device/all',
    component: DeviceComponent
  },
  {
    path: 'device/add',
    component: DeviceAddComponent
  },
  {
    path: 'house/all',
    component: HouseComponent
  },
  {
    path: 'house/add',
    component: HouseAddComponent
  }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);

