import React, {PropTypes} from 'react';
import {connect} from 'react-redux';
import PageContent from '../layout/page-content';
import {login} from '../../actions/UserAction';

class Login extends React.Component {
  onLogin(e) {
    e.preventDefault();
    this.setState({error: ''});
    this.props.login(this.state).then(
      (res, status, options) => {
        console.log(options.getAllResponseHeaders());
        if (options.getResponseHeader('errortext')) {
          this.setState({error: 'User with such credentials does not exist'});
          this.notifyError();
        } else {
          this.props.history.push('/house')
        }
      },
      () => {
        this.setState({error: 'Something goes wrong'});
        this.notifyError();
      }
    );
  }

  onChange(e) {
    this.setState({[e.target.name]: e.target.value});
  }

  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      error: ''
    };

    this.onLogin = this.onLogin.bind(this);
    this.onChange = this.onChange.bind(this);
  }

  render() {
    const {password, email} = this.state;
    return (
      <PageContent>
        <div className="row margin-top">
          <form method="post" className="col s4 offset-s4" onSubmit={this.onLogin}>
            <div className="row">
              <div className="input-field col s12">
                <input type="email" name="email" id="email" className="validate border-bottom" required min="4"
                       max="30" onChange={this.onChange} value={email}/>
                <label data-error="wrong" data-success="right" htmlFor="email">Email</label>
              </div>
            </div>
            <div className="row">
              <div className="input-field col s12">
                <input type="password" min="4" max="30" name="password" id="password" value={password}
                       className="validate border-bottom" onChange={this.onChange} required/>
                <label htmlFor="password">Password</label>
              </div>
            </div>
            <div className="col s4 offset-s2">
              <button className="waves-effect waves-light btn #d7ccc8 brown lighten-4" id="loginBtn" type="submit">Log
                in
              </button>
            </div>
            <div className="g-signin2" data-onsuccess="onSignIn"/>
            <br/>
          </form>
        </div>

      </PageContent>
    )
  }

  notifyError() {
    Materialize.toast(this.state.error, 4000);
  }
}

Login.propTypes = {
  login: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired
};


export default connect(null, {login})(Login)
