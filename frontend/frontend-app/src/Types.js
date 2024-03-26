import './App.css';

import React, { useState } from 'react';
import axios from 'axios';
import './App.css';


function Types() {
  const [formData, setFormData] = useState({
    table_name: '',
    columns: [],
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
        table_name: formData.table_name,
        columns: formData.columns.split(",")
      };
      const response = await axios.post(`http://localhost:8080/type/add-type`, payload);
      console.log(response.data);
      alert("Type created successfully!");
      setFormData({
        table_name: '',
        columns: []
      });


    } catch (error) {
      // Error handling
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Error submitting form:', errorMessage);
      alert("Failed to create type. Error: " + (typeof errorMessage === 'string' ? errorMessage : JSON.stringify(errorMessage, null, 2)));
    }
  };


  return (
    <div className="App" style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
      <h1>Create New Type</h1>
      <form onSubmit={handleSubmit}> {/* Ensure handleSubmit is defined */}
        <label htmlFor="table_name">Type:</label>
        <textarea
          id="table_name"
          name="table_name"
          required
          value={formData.table_name}
          onChange={handleChange}
        />
        <label htmlFor="type-columns">Column (Seperated by commas):</label>
        <textarea
          id="type-columns"
          name="columns"
          required
          value={formData.columns}
          onChange={handleChange}
        />
        <button type="submit">Create type</button>
      </form>
    </div>
  );
}



export default Types;
