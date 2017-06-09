import React from 'react';
let gapi;
class Login extends React.Component {

  render() {
    return (
      <div className="row margin-top">
        <form method="post" className="col s4 offset-4" action="api/user/login">
          <div className="row">
            <div className="input-field col s12">
              <input type="email" name="email" id="email" className="validate border-bottom" required min="4"
                     max="30"/>
              <label data-error="wrong" data-success="right" htmlFor="email">Email</label>
            </div>
          </div>
          <div className="row">
            <div className="input-field col s12">
              <input type="password" min="4" max="30" name="password" id="password"
                     className="validate border-bottom" required/>
              <label htmlFor="password">Password</label>
            </div>
          </div>
          <div className="col s4 offset-s2">
            <button className="waves-effect waves-light btn #d7ccc8 brown lighten-4" id="loginBtn" type="button">Log
              in
            </button>
          </div>
          <div className="g-signin2" id="signInButton"/>
        </form>
      </div>
    );
  }
}

export default Login;
