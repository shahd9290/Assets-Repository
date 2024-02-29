import React, { useState } from 'react';
import axios from 'axios';
import './App.css';


function Types() {
  const [formData, setFormData] = useState({
    type: '',
    description: '',
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
        type: formData.type,
        description: formData.description
      };
      const response = await axios.post(`http://localhost:8080/type/add-type`, payload);
      console.log(response.data);
      alert("Type created successfully!");
      setFormData({
        type: '',
        description: '',
      });


    } catch (error) {
      // Error handling
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Error submitting form:', errorMessage);
      alert("Failed to create type. Error: " + (typeof errorMessage === 'string' ? errorMessage : JSON.stringify(errorMessage, null, 2)));
    }
  };


  return (
    <div className="App">
      <h1>Create New Type</h1>
      <form onSubmit={handleSubmit}> {/* Ensure handleSubmit is defined */}
        <label htmlFor="type">Type:</label>
        <input
          type="text"
          id="type"
          name="type"
          required
          value={formData.title}
          onChange={handleChange}
        />
        <label htmlFor="type-description">Description:</label>
        <textarea
          id="type-description"
          name="description"
          required
          value={formData.description}
          onChange={handleChange}
        />
        <button type="submit">Create type</button>
      </form>
    </div>
  );
}



export default Types;
