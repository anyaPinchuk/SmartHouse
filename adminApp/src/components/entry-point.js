import React from 'react';
import * as ReactDOM from 'react-dom';
import Provider from 'react-redux/es/components/Provider';
import {createStore, applyMiddleware, compose} from 'redux';
import thunk from 'redux-thunk';
import {routes} from '../routes';
import rootReducer from '../reducers/index';
import BrowserRouter from 'react-router-dom/es/BrowserRouter';
import {setAuthenticated} from '../actions/UserAction'

require('./style.css');
require('mdi/css/materialdesignicons.css');
require('materialize-css/dist/css/materialize.css');
require('jquery/dist/jquery.min.js');
require('materialize-css/dist/js/materialize.min.js');
// require('../assets/js/script.js');

const store = createStore(rootReducer,
  compose(applyMiddleware(thunk)));

store.dispatch(setAuthenticated(false));

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter >
      {routes}
    </BrowserRouter>
  </Provider>,
  document.getElementById('app')
);



