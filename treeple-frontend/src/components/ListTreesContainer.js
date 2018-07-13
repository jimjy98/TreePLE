import React from 'react';
import axios from 'axios';

const apiURL = process.env.NODE_ENV === 'production' ? 'http://ecse321-5.ece.mcgill.ca:8080' : 'http://localhost:8088'

export default class ListTreesContainer extends React.Component {

    constructor() {
        super();

        this.state = {
            treeList: [],
            municipalityList: [],
            selectedMunicipality: "",
            biodiversityIndex: "",
            carbonSequestration: "",
        };
    };

    componentDidMount() {
      axios.get(apiURL+'/trees').then(response => {
          var unique = [...new Set(response.data.treeList.map(item => item.municipality.name))];
          this.setState({
              municipalityList: unique,
              treeList: response.data.treeList,
              biodiversityIndex: response.data.attributes.biodiversityIndex,
              carbonSequestration: response.data.attributes.carbonSequestration,
          });
      });
    };

    listTrees = data => {
        return data.map(row => (

            <tr  style={{ textAlign: 'center'}}>
                <td>{row.municipality.name}</td>
                <td>{row.landType}</td>
                <td>{row.x}</td>
                <td>{row.y}</td>
                <td>{row.species.nameEnglish}</td>
                <td>{row.diameter}</td>
                <td>{row.height}</td>
                <td>{row.status}</td>
                <td>{row.reportDate}</td>
            </tr>

        ));
    };

    handleSelectedMunicipality = e => {
      axios.get(apiURL+'/trees?municipality='+e.target.value).then(response => {
          this.setState({
              treeList: response.data.treeList,
              biodiversityIndex: response.data.attributes.biodiversityIndex,
              carbonSequestration: response.data.attributes.carbonSequestration,
          });
      });
    };

    rendermunicipalityList = data => {
        return data.map(option => (
            <option value={option}>{option}</option>
        ));
    };


    render() {
        return (
            <div>
            <select onClick={this.handleSelectedMunicipality}>
                {this.rendermunicipalityList(this.state.municipalityList)}
            </select>
                <div className="table-responsive">
                <table className="table" style={{width: '100%', textAlign: 'center'}}>
                    <thead>
                    <tr style={{ fontSize: 23 }}>
                        <th>Municipality</th>
                        <th>Land Type</th>
                        <th>X Coordinate</th>
                        <th>Y Coordinate</th>
                        <th>Name in English</th>
                        <th>Diameter</th>
                        <th>Height</th>
                        <th>Status</th>
                        <th>Record Date</th>
                    </tr>
                    </thead>

                    {this.listTrees(this.state.treeList)}

                </table>

                </div>

                <p>Biodiversity Index: {this.state.biodiversityIndex}</p>
                <p>Carbon Sequestration: {this.state.carbonSequestration}</p>
            </div>
        );
    }
}
