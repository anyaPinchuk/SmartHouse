import React from 'react';
import Route from 'react-router-dom/es/Route'
import Login from './components/common/login';
import Home from './components/common/home';
import Houses from './components/common/houses';
import AddHome from './components/common/add_home';
import Switch from 'react-router-dom/es/Switch';

export const routes = (
  <Switch>
    <Route path='/login' component={Login}/>
    <Route path='/house' component={Houses}/>
    <Route path='/add' component={AddHome}/>
    <Route path='/' component={Home}/>
  </Switch>
);
