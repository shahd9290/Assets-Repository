//comments for team memebers to understand
import React, { useState } from 'react';
import axios from 'axios';
import './App.css';
import bearerToken from './components/tokens/token.json'

function App() {
  const tokens = JSON.stringify(bearerToken['bearer-tokens']);
  const token = tokens.slice(20,tokens.length-3);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    type: '',
  });


  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const payload = {
        'headers':{
          method:'POST',
        headers: { 'Content-Type': 'application/json',
                    'Authorization': 'Bearer '+ token
        }},
       'body' : {asset: {
          name: 'asdasdsetrrhhnn',
          creator: 'sdsf',
          type: 'test',
        },
        type: {
          title: formData.title,
          description: formData.description,
          type: formData.type,
        }}
      };

      // POST request to the backend endpoint
      const response = await axios.post('http://localhost:8080/add-new-asset', payload);
      console.log(response.data);
      alert("Asset created successfully!");

      // Reset the form
      setFormData({
        title: '',
        description: '',
        type: '',
      });
    } catch (error) {
      // Error handling
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Error submitting form:', errorMessage);
      alert("Failed to create asset. Error: " + (typeof errorMessage === 'string' ? errorMessage : JSON.stringify(errorMessage, null, 2)));
    }
  };



  return (
    <div className="App" style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
      <h1>Create New Asset</h1>
      <form onSubmit={handleSubmit}> {/* Ensure handleSubmit is defined */}
        <label htmlFor="asset-title">Title:</label>
        <input
          type="text"
          id="asset-title"
          name="title"
          required
          value={formData.title}
          onChange={handleChange}
        />
        <label htmlFor="asset-description">Description:</label>
        <textarea
          id="asset-description"
          name="description"
          required
          value={formData.description}
          onChange={handleChange}
        />
        <label htmlFor="asset-type">Type:</label>
        <select
          id="asset-type"
          name="type"
          required
          value={formData.type}
          onChange={handleChange}
        >
          <option value="">-- Select a Type --</option>
          <option value="colours">Software Module</option>
          <option value="Documentation">Documentation</option>
        </select>
        <p></p>
        <p></p>
        <button type="submit" >Create Asset</button>
      </form>
    </div>
  );
}
export default App;
