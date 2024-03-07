import { useState } from 'react';

function EditAssetForm({ asset, onSave }) {
  const [formData, setFormData] = useState({ ...asset });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onSave(formData);
  };

  return (
    <div>
      <h1>Edit Asset</h1>
      <form onSubmit={handleSubmit}>
        <label htmlFor="title">Title:</label>
        <input
          type="text"
          id="title"
          name="title"
          value={formData.title}
          onChange={handleChange}
        />
        <label htmlFor="description">Description:</label>
        <textarea
          id="description"
          name="description"
          value={formData.description}
          onChange={handleChange}
        />
        {/* Add inputs for other asset fields as needed */}
        <button type="submit">Save Asset</button>
      </form>
    </div>
  );
}

export default EditAssetForm;
