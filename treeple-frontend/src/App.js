import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import { withRouter } from 'react-router';
import './App.css';
import HomeContainer from './components/HomeContainer';
import UploadFileContainer from './components/UploadFileContainer';
import ListTreesContainer from './components/ListTreesContainer';
import UpdateTreeDataContainer from './components/UpdateTreeDataContainer';
import ForecastingContainer from './components/ForecastingContainer';
// import MapContainer from './components/MapContainer';

class App extends Component {
  render() {
    return (
      <div>
          <div className="navigationBar">Welcome to TreePLE</div>
          <ul>
              <li><a onClick={() => this.props.history.push('/')}>Home</a></li>
              <li><a onClick={() => this.props.history.push('/listAllTrees')}>Trees</a></li>
              <li><a onClick={() => this.props.history.push('/uploadFile')}>Upload Trees</a></li>
              <li><a onClick={() => this.props.history.push('/updateTreeData')}>Update Trees</a></li>
              <li><a onClick={() => this.props.history.push('/forecasting')}>Forecasting</a></li>
              {/*<li><a onClick={() => this.props.history.push('/map')}>Map</a></li>*/}
          </ul>

          <Route exact path="/" component={HomeContainer} />
          <Route exact path="/uploadFile" component={UploadFileContainer} />
          <Route exact path="/listAllTrees" component={ListTreesContainer} />
          <Route exact path="/updateTreeData" component={UpdateTreeDataContainer} />
          <Route exact path="/forecasting" component={ForecastingContainer} />
          {/*<Route exact path="/map" component={MapContainer} />*/}

      </div>
    );
  }
}

export default withRouter(App);
