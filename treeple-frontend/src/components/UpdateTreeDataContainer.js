import React from 'react';
import axios from 'axios';

const apiURL = process.env.NODE_ENV === 'production' ? 'http://ecse321-5.ece.mcgill.ca:8080' : 'http://localhost:8088'

export default class UpdateTreeDataContainer extends React.Component {

    constructor() {
        super();

        this.state = {
            treeList: [],
            selectedId: "1",
            x: "",
            y: "",
            height: "",
            diameter: "",
            landType: "",
            municipality: "",
            species_ac: "",
            species_la: "",
            species_en: "",
            species_fr: "",
        };
    };

    componentDidMount() {
        axios.get(apiURL+'/trees').then(response => {
            this.setState({
               treeList: response.data.treeList
            });
        });

    };

    renderTreeOptions = data => {
        return data.map(option => (
            <option value={option.id}>{option.id}</option>
        ));
    };

    handleChangeSelectedId = e => {
      this.setState({selectedId: e.target.value});
    };

    handleChangeX = e => {
        this.setState({x: e.target.value});
    };

    handleChangeY = e => {
        this.setState({y: e.target.value});
    };

    handleChangeHeight = e => {
        this.setState({height: e.target.value});
    };

    handleChangeDiameter= e => {
        this.setState({diameter: e.target.value});
    };

    handleChangeLandType = e => {
        this.setState({landType: e.target.value});
    };

    handleChangeMunicipality = e => {
        this.setState({municipality: e.target.value});
    };

    handleChangeSpeciesAc = e => {
        this.setState({species_ac: e.target.value});
    };

    handleChangeSpeciesLa = e => {
        this.setState({species_la: e.target.value});
    };

    handleChangespeciesEn = e => {
        this.setState({species_en: e.target.value});
    };

    handleChangeSpeciesFr = e => {
        this.setState({species_fr: e.target.value});
    };

    handleSubmit = () => {
      if(this.state.x.trim().length === 0 && this.state.y.trim().length === 0 &&
        this.state.height.trim().length === 0 && this.state.diameter.trim().length === 0 &&
        this.state.landType.trim().length === 0 && this.state.municipality.trim().length === 0 &&
        this.state.species_ac.trim().length === 0 && this.state.species_la.trim().length === 0&&
      this.state.species_en.trim().length === 0 && this.state.species_fr.trim().length === 0){
        alert("Empty input value, at least one update area need to be set or selected");
        return;
      }

      if(isNaN(this.state.x)){
        alert("Input error: x value should be a number");
        return;
      }
      if(isNaN(this.state.y)){
        alert("Input error: y value should be a number");
        return;
      }
      if(isNaN(this.state.height)){
        alert("Input error: height value should be a number");
        return;
      }
      if(isNaN(this.state.diameter)){
        alert("Input error: diameter value should be a number");
        return;
      }
      var postData = {
          "x": this.state.x,
          "y": this.state.y,
          "height": this.state.height,
          "diameter": this.state.diameter,
          "landType": this.state.landType,
          "municipality": this.state.municipality,
          "species_ac": this.state.species_ac,
          "species_la": this.state.species_la,
          "species_en": this.state.species_en,
          "species_fr": this.state.species_fr
      };
      var url = apiURL+'/tree/'+this.state.selectedId;
      const config = {
          headers: {
              'content-type': 'application/json'
          }
      }
      axios.post(url, postData, config).then((response) => {
          console.log('axios log: ', response)
          alert("Update success!");
        })
        .catch(error => Promise.reject(error))
      };

    render(){
        return(
            <div className="body">
                <select value={this.state.selectedId} onChange={this.handleChangeSelectedId}>
                    {this.renderTreeOptions(this.state.treeList)}
                </select>
                <input type="text" name="X Coordinate" value={this.state.x} onChange={this.handleChangeX} placeholder="X Coordinate"></input>
                <input type="text" name="Y Coordinate" value={this.state.y} onChange={this.handleChangeY} placeholder="Y Coordinate"></input>
                <input type="text" name="Height" value={this.state.height} onChange={this.handleChangeHeight} placeholder="Height"></input>
                <input type="text" name="Diameter" value={this.state.diameter} onChange={this.handleChangeDiameter} placeholder="Diameter"></input>
                <select name="Land Type" value={this.state.landType} onChange={this.handleChangeLandType} placeholder="Land type">
                    <option>Residential</option>
                    <option>Institutional</option>
                    <option>Park</option>
                    <option>Municipal</option>
                </select>
                <input type="text" name="Municipality" value={this.state.municipality} onChange={this.handleChangeMunicipality} placeholder="Municipality"></input>
                <input type="text" name="Species in Acronym" value={this.state.species_ac} onChange={this.handleChangeSpeciesAc} placeholder="Species in Acronym"></input>
                <input type="text" name="Species in Latin" value={this.state.species_la} onChange={this.handleChangeSpeciesLa} placeholder="Species in Latin"></input>
                <input type="text" name="Species in English" value={this.state.species_en} onChange={this.handleChangespeciesEn} placeholder="Species in English"></input>
                <input type="text" name="Species in French" value={this.state.species_fr} onChange={this.handleChangeSpeciesFr} placeholder="Species in French"></input>
                <button onClick={this.handleSubmit}>Update</button>
            </div>
        );
    };
}
