import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const EditAsset = () => {
  const { assetId } = useParams();
  const navigate = useNavigate();
  const [asset, setAsset] = useState({
    name: '',
    creatorName: '',
    link: '',
    description: '',
    type: '',
    // Include other fields as necessary
  });

  useEffect(() => {
    // Fetch the asset data when the component mounts
    const fetchAsset = async () => {
      const response = await fetch(`http://localhost:8080/assets/${assetId}`);
      const data = await response.json();
      setAsset({
        name: data.name,
        creatorName: data.creatorName,
        link: data.link,
        description: data.description,
        type: data.type,
        // Map other fields as necessary
      });
    };

    fetchAsset();
  }, [assetId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAsset(prevState => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await fetch(`http://localhost:8080/assets/update-asset/${assetId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ asset }),
    });

    if (response.ok) {
      alert('Asset updated successfully!');
      navigate('/assets'); // Adjust the navigation path as needed
    } else {
      alert('Failed to update the asset.');
    }
  };

  return (
    <div>
      <h2>Edit Asset</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={asset.name}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>Creator Name:</label>
          <input
            type="text"
            name="creatorName"
            value={asset.creatorName}
            onChange={handleChange}
          />
        </div>
        {/* Include other fields in the same way */}
        <div>
          <label>Link:</label>
          <input
            type="text"
            name="link"
            value={asset.link}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>Description:</label>
          <textarea
            name="description"
            value={asset.description}
            onChange={handleChange}
          />
        </div>
        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
};

export default EditAsset;
