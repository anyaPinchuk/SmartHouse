import React, {PropTypes} from 'react';
import PageContent from '../layout/page-content';
import {connect} from 'react-redux';
import {addHome} from '../../actions/HomeAction';
import {checkRights} from '../../actions/UserAction';

class AddHome extends React.Component {
  onSubmit(e) {
    e.preventDefault();
    this.setState({errors: {}});
    this.props.addHome(this.state).then(
      (res) => {
        if(res === true){
          this.props.history.push('/house')
        } else {
          Materialize.toast('User with such email already exists', 4000);
        }
      },
      (err) => {
        this.setState({errors: err.response.data.error})
      }
    );
  }

  componentDidMount() {
    this.props.checkRights().then(res => {
      if (res.email === '') {
        this.props.history.push('/login')
      }
    });
  }

  onChange(e) {
    this.setState({[e.target.name]: e.target.value});
  }

  constructor(props) {
    super(props);
    this.state = {
      country: '',
      city: '',
      street: '',
      house: '',
      flat: '',
      errors: {}
    };

    this.onSubmit = this.onSubmit.bind(this);
    this.onChange = this.onChange.bind(this);
  }

  render() {
    return (
      <PageContent>
        <div className="row margin-top">
          <form method="post" className="col s4 offset-s4" onSubmit={this.onSubmit}>
            <div className="row">
              <div className="input-field col s12">
                <input required type="text" name="country" id="country" min="2" max="30" onChange={this.onChange}
                       className="validate border-bottom"/>
                <label htmlFor="country">Country</label>
              </div>
            </div>
            <div className="row">
              <div className="input-field col s12">
                <input required type="text" name="city" id="city" onChange={this.onChange}
                       min="2" max="30" className="validate border-bottom"/>
                <label htmlFor="city">City</label>
              </div>
            </div>
            <div className="row">
              <div className="input-field col s12">
                <input required type="text" name="street" id="street" min="2" max="30" onChange={this.onChange}
                       className="validate border-bottom"/>
                <label htmlFor="street">Street</label>
              </div>
            </div>
            <div className="row">
              <div className="input-field col s12">
                <input required type="text" name="house" onChange={this.onChange}
                       id="house" min="1" max="5" className="validate border-bottom"/>
                <label htmlFor="house">House</label>
              </div>
            </div>
            <div className="row">
              <div className="input-field col s12">
                <input type="number" name="flat" id="flat" onChange={this.onChange} className="validate border-bottom"/>
                <label htmlFor="flat">Flat</label>
              </div>
            </div>
            <div className="row">
              <div className="input-field col s12">
                <input required name="ownerLogin" id="email" min="4" max="30" onChange={this.onChange}
                       className="validate border-bottom"/>
                <label htmlFor="email">Email</label>
              </div>
            </div>
            <div className="center">
              <button className="waves-effect waves-light btn #bcaaa4 brown lighten-2" type="submit">Add house</button>
            </div>
          </form>
        </div>
      </PageContent>
    );
  }
}

AddHome.propTypes = {
  addHome: PropTypes.func.isRequired,
  history: PropTypes.object.isRequired,
  checkRights: React.PropTypes.func.isRequired
};


export default connect(null, {addHome, checkRights})(AddHome)
