//comments for team memebers to understand
import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    type: '', // Assuming 'type' is a value you want to capture; adjust as needed.
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
      const tableName = "owen"; // Replace with your actual table name
      const payload = {
        title: formData.title,
        description: formData.description,
        type: formData.type
      };
  
      const response = await axios.post(`http://localhost:8080/type/insert-data/${tableName}`, payload);
      console.log(response.data);
      alert("Asset created successfully!");
      setFormData({
        title: '',
        description: '',
        type: '',
      });
    } catch (error) {
      // Properly accessing the error message
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Error submitting form:', errorMessage);
      // Convert the error object to a string if it's not already a string
      alert("Failed to create asset. Error: " + (typeof errorMessage === 'string' ? errorMessage : JSON.stringify(errorMessage, null, 2)));
    }
  };
  
  

  return (
    <div className="App">
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
          <option value="Software Module">Software Module</option>
          <option value="Documentation">Documentation</option>
        </select>
        <button type="submit">Create Asset</button>
      </form>
    </div>
  );
}

export default App;
