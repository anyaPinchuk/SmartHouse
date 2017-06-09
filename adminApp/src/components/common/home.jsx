import React from 'react';
/*
 This component is a view for specific feature - List of Technologies
 */
class Home extends React.Component {

  /*
   Use arrow functions as much as possible. They don't change this pointer.
   (a) = > { return 5*a} is approx. the same as function(a) {return 5*a}
   */
  render() {
    return (
        <div className="slider">
          <ul className="slides">
            <li>
              <img src="assets/img/smart_house.jpg"/>
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
    );
  }
}

export default Home;


