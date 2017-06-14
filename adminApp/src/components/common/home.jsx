import React from 'react';
import PageContent from '../layout/page-content';

class Home extends React.Component {

  render() {
    return (
      <PageContent>
        <div className="slider">
          <ul className="slides">
            <li>
              <img src="../assets/img/smart_house.jpg"/>
              <div className="caption right-align">
                <h3>Your dreams will come true</h3>
                <h5 className="light grey-text text-lighten-3">Just try.</h5>
              </div>
            </li>
            <li>
              <img src="assets/img/house.jpg"/>
              <div className="caption right-align">
                <h3>One click - hundred of possibilities</h3>
              </div>
            </li>
            <li>
              <img src="assets/img/smart.jpg"/>
              <div className="caption left-align">
                <h3>Manage your desires</h3>
                <h5 className="light grey-text text-lighten-3">It is simple</h5>
              </div>
            </li>
          </ul>
        </div>
      </PageContent>
    );
  }
}

export default Home;


