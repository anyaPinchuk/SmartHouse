import React, {PropTypes} from 'react';
import Link from 'react-router-dom/es/Link';
import {connect} from 'react-redux';
import {checkRights, logout} from '../../actions/UserAction';

class PageContent extends React.Component {
  logout(e) {
    e.preventDefault();
    this.props.logout();
  }

  render() {
    const {isAuthenticated} = this.props.auth;
    const logout = (
      <li><a onClick={this.logout.bind(this)}>Log out</a></li>
    );
    const login = (
      <li><Link to='/login'>Log in</Link></li>
    );
    const adminLinks = (
      <div className="fixed-action-btn horizontal click-to-toggle">
        <a className="btn-floating btn-large #bcaaa4 brown lighten-3">
          <i className="material-icons">menu</i>
        </a>
        <ul>
          <li><Link to='/add'>
            <button className="btn-floating #bcaaa4 brown lighten-2">
              <i className="material-icons">add</i></button>
          </Link>
          </li>
          <li><Link to='/house'><a className="btn-floating #bcaaa4 brown lighten-2">
            <i className="material-icons">home</i></a></Link>
          </li>
        </ul>
      </div>
    );

    return (
      <div>
        <nav>
          <div className="nav-wrapper #d7ccc8 brown lighten-4">
            <a className="brand-logo"><Link to='/'><img src="../assets/img/logo.png"/></Link></a>
            <ul id="nav-mobile" className="right hide-on-med-and-down">
              { isAuthenticated ? logout : login}
            </ul>
          </div>
        </nav>
        <div className="container">
          {this.props.children}
          { isAuthenticated ? adminLinks : ''}
        </div>
      </div>
    );
  }

  componentDidMount() {
    this.props.checkRights();
  }
}
PageContent.propTypes = {
  auth: PropTypes.object.isRequired,
  logout: React.PropTypes.func.isRequired,
  checkRights: React.PropTypes.func.isRequired
};

function mapStateToProps(state) {
  return {
    auth: state.auth
  }
}

export default connect(mapStateToProps, {logout, checkRights})(PageContent);
