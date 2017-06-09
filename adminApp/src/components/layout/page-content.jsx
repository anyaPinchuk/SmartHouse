import React from 'react';
import Link from 'react-router-dom/es/Link';
import Main from './main'

class PageContent extends React.Component {

  constructor(props) {
    super(props);
    this.state = this.getCurrentState();
  }

  getCurrentState() {
    let items = [];
    items.push({
      id: 1,
      caption: 'Webpack',
      usage: 'Использовался в качестве сборщика модулей',
      readMoreLink: 'https://blog.risingstack.com/using-react-with-webpack-tutorial/'
    });
    items.push({
      id: 2,
      caption: 'Twitter Bootstrap',
      usage: 'Использовался для оформления примера',
      readMoreLink: 'http://getbootstrap.com/css/'
    });
    items.push({
      id: 3,
      caption: 'ESLint',
      usage: 'Использовался для статического анализа кода',
      readMoreLink: 'http://eslint.org/docs/about/'
    });
    items.push({
      id: 4,
      caption: 'JSX',
      usage: 'Использовался для генерации разметки',
      readMoreLink: 'https://facebook.github.io/react/docs/jsx-in-depth.html'
    });
    items.push({
      id: 5,
      caption: 'Babel',
      usage: 'Использовался для компиляции EcmaScript5 компилируется в JavaScript',
      readMoreLink: 'https://babeljs.io/docs/learn-es2015/'
    });
    return {
      technologies: items
    }
  }

  render() {
    return (
      <div>
        <nav>
          <div className="nav-wrapper #d7ccc8 brown lighten-4">
            <a className="brand-logo"><img src="../assets/img/logo.png"/></a>
            <ul id="nav-mobile" className="right hide-on-med-and-down">
              <li><Link to='/login'>Log in</Link></li>
              <li><a >Log out</a></li>
            </ul>
          </div>
        </nav>
        <div className="container">
          <Main />
          <div className="fixed-action-btn horizontal click-to-toggle">
            <a className="btn-floating btn-large #bcaaa4 brown lighten-3">
              <i className="material-icons">menu</i>
            </a>
            <ul>
              <li ><a className="btn-floating #bcaaa4 brown lighten-2"><i
                className="material-icons">add</i></a>
              </li>
              <li ><a className="btn-floating #bcaaa4 brown lighten-2"><i
                className="material-icons">home</i></a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    );
  }
}

export default PageContent;
