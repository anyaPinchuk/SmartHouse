import React from 'react';
import browserHistory from 'react-router-dom'
import ComponentsApp from './layout/components-app';
import * as ReactDOM from 'react-dom';
import BrowserRouter from 'react-router-dom/es/BrowserRouter';
import $ from 'jquery';

require('./style.css');
require('bootstrap/dist/css/bootstrap.min.css');
require('materialize-css/dist/css/materialize.css');
require('jquery/dist/jquery.min.js');
require('bootstrap/dist/js/bootstrap.min.js');
require('materialize-css/dist/js/materialize.min.js');

ReactDOM.render(
  <BrowserRouter history={browserHistory}>
    <ComponentsApp />
  </BrowserRouter>,
  document.getElementById('app')
);


document.onload = function(){
  $('.slider').slider();
};


