import React, {PropTypes} from 'react';
import PageContent from '../layout/page-content';
import {connect} from 'react-redux';
import {checkRights} from '../../actions/UserAction';
import $ from 'jquery';

class Houses extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      houses: []
    };
  }

  componentDidMount() {
    this.props.checkRights().then(res => {
      if (res.email === '') {
        this.props.history.push('/login')
      } else {
        $.get('api/house/all').then((res) => {
          this.setState({houses: res});
        }, () => {
          Materialize.toast('Data is not available', 4000);
        });
      }
    });

  }

  render() {
    return (
      <PageContent>
        <div className="row margin-top">
          <div className="collection col s10 offset-s1">
            {this.state.houses.map(house => (
                <a className="collection-item brown-text">
                  <div className="row">
                    <div className="col s9 offset-s1">
                      <h4>Address: {house.country}, {house.city}, {house.street}, {house.house} {house.flat}</h4>
                      <p>Owner: {house.ownerLogin}</p>
                      <a href={'api/user/sendConfirm?email=' + house.ownerLogin}
                         className="waves-effect waves-light btn #bcaaa4 brown lighten-2">Send confirmation
                      </a>
                    </div>
                  </div>
                </a>
              )
            )}
          </div>
        </div>
      </PageContent>
    );
  }
}
Houses.propTypes = {
  history: PropTypes.object.isRequired,
  checkRights: React.PropTypes.func.isRequired
};
export default connect(null, {checkRights})(Houses)

