import React from 'react';
import axios from 'axios';

const apiURL = process.env.NODE_ENV === 'production' ? 'http://ecse321-5.ece.mcgill.ca:8080' : 'http://localhost:8088'

var styles = {
  color:'white',
  backgroundColor:'green',
  fontWeight:'bold'
};

export default class ForecastingContainer extends React.Component {
    constructor() {
        super();

        this.state = {
            treeList: [],
            municipalityList: [],
            selectedMunicipality: "",
            biodiversityIndex: "",
            species: [],

            landType: "",
            forecatingMunicipality: "",
            currentBiodiversityIndex: "",
            currentCarbonIndex: "",
            predictedBiodiversityIndex: "",
            predictedCarbonIndex: ""
        };
    };

    componentDidMount() {
        axios.get(apiURL+'/trees').then(response =>{
            const unique = [...new Set(response.data.treeList.map(item => item.municipality.name))];
            this.setState({
                treeList: response.data.treeList,
                biodiversityIndex: response.data.attributes.biodiversityIndex,
                municipalityList: unique
            });
        });
    };

    renderMunicipalityList = data => {
        return data.map(option => (
            <option value={option}>{option}</option>
        ));
    };

    handleSelectMunicipality = e => {
        console.log(e.target.value);
        axios.get(apiURL+'/forecasting/biodiversity?municipality=' + e.target.value).then(response => {
            this.setState({
                treeList: response.data.treeList,
                biodiversityIndex: response.data.attributes.biodiversityIndex,
                species: response.data.species
            });
        });
    };

    renderSpeciesList = data => {
        return data.map((row, i) => (
            <tr key={i} style={{textAlign: 'center'}}>
                <td>{row}</td>
            </tr>
        ))
    };

    handleCurrentAttributes = e => {
        console.log(e.target.value);
        this.setState({
            forecatingMunicipality: e.target.value
        });
        axios.get(apiURL+'/trees?municipality='+e.target.value).then(response => {
            this.setState({
                currentBiodiversityIndex: response.data.attributes.biodiversityIndex,
                currentCarbonIndex: response.data.attributes.carbonSequestration
            });
        });
    };

    handlerRemoveLandType = e => {
        console.log(e.target.value);
        this.setState({landType: e.target.value});

        axios.get(apiURL+'/forecasting/remove?municipality='+this.state.forecatingMunicipality+'&landType='+e.target.value).then(response => {
            this.setState({
                predictedBiodiversityIndex: response.data.attributes.biodiversityIndex,
                predictedCarbonIndex: response.data.attributes.carbonSequestration
            });
        });
    };

    render() {
        return (
            <div className="body">
                <p style={styles}>Area species predictor</p>
                <select onClick={this.handleSelectMunicipality}>
                    {this.renderMunicipalityList(this.state.municipalityList)}
                </select>
                <table style={{width: '30%'}}>
                    <tr>
                        <th>Species</th>
                    </tr>
                    <tbody>{this.renderSpeciesList(this.state.species)}</tbody>
                </table>
                <p>Biodiversity Index: {this.state.biodiversityIndex}</p>
                <p>In order to increase biodiversity, we encourage you to plant trees with different species than that present in the table above</p>

                <p style={styles}>Remove Trees Predictor</p>
                <p>Select an area to view current sustainability attributes:
                <select onClick={this.handleCurrentAttributes}>
                    {this.renderMunicipalityList(this.state.municipalityList)}
                </select >
                </p>
                <table>
                    <tr>
                        <td>Current Biodiversity Index</td>
                        <td>{this.state.currentBiodiversityIndex}</td>
                    </tr>
                    <tr>
                        <td>Current Carbon Sequestration</td>
                        <td>{this.state.currentCarbonIndex}</td>
                    </tr>
                </table>

                <p>Select a landType to remove:
                <select onClick={this.handlerRemoveLandType}>
                    <option>Residential</option>
                    <option>Institutional</option>
                    <option>Park</option>
                    <option>Municipal</option>
                </select>
                </p>
                <table>
                    <tr>
                        <td>New Biodiversity Index</td>
                        <td>{this.state.predictedBiodiversityIndex}</td>
                    </tr>
                    <tr>
                        <td>New Carbon Sequestration</td>
                        <td>{this.state.predictedCarbonIndex}</td>
                    </tr>
                </table>
            </div>
        );
    };
}
