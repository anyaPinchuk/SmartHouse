import Route from 'react-router-dom/es/Route'
import Login from '../common/login';
import Home from '../common/home'
import Switch from 'react-router-dom/es/Switch';
import * as React from 'react';


const Main = () => (
  <div >
    <Switch>
      <Route exact path='/' component={Home}/>
      <Route path='/login' component={Login}/>
    </Switch>
  </div>
);
export default Main;
