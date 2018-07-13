import React from 'react';
import { post } from 'axios';

const apiURL = process.env.NODE_ENV === 'production' ? 'http://ecse321-5.ece.mcgill.ca:8080' : 'http://localhost:8088'

export default class UploadFileContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state ={
      file:null
    }
    this.onFormSubmit = this.onFormSubmit.bind(this)
    this.onChange = this.onChange.bind(this)
    this.fileUpload = this.fileUpload.bind(this)
  }
  onFormSubmit(e){
    e.preventDefault() // Stop form submit
    this.fileUpload(this.state.file).then((response)=>{
      alert(response.data);
      console.log(response.data);
    })
  }
  onChange(e) {
    this.setState({file:e.target.files[0]})
  }
  fileUpload(file){
    const url = apiURL+'/uploadFile';
    const formData = new FormData();
    formData.append('file',file)
    const config = {
        headers: {
            'content-type': 'multipart/form-data'
        }
    }
    return  post(url, formData,config)
  }

    render() {
        return (
            <div className="body">
                <h2>Uploading a File:</h2>
                <p>You can upload you csv file here:</p>
                <form onSubmit={this.onFormSubmit}>
                  <input type="file" onChange={this.onChange} />
                  <button type="submit">Upload</button>
                </form>
            </div>
        );
    }
}
