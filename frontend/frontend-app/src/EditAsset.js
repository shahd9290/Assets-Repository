import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import bearerToken from './components/tokens/token.json'

const EditAsset = () => {
  const tokens = JSON.stringify(bearerToken['bearer-tokens']);
  const token = tokens.slice(20,tokens.length-3);
  const [assets, setAssets] = useState([]);
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
    fetchAssets();
    const data = assets.filter((a)=>{return (a.id===assetId)? a : null})
      setAsset({
        name: data.name,
        creatorName: data.creatorName,
        link: data.link,
        description: data.description,
        type: data.type,
        // Map other fields as necessary
      });


  }, [assetId]);

  function fetchAssets(){fetch('http://localhost:8080/search',
  {method:'POST',
  headers: { 'Content-Type': 'application/json',
              'Authorization': 'Bearer '+ token
  },
  body:JSON.stringify({})})
  .then((res) => {
      return res.json();
  })
  .then((data) => {
      if (data==null) {return"" }
      console.log(data)
      setAssets(data);
  })
  .catch((error)=>{
      alert('Login to view assets ')
      
  })
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAsset(prevState => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await fetch(`http://localhost:8080/edit-asset/${assetId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer '+ token
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
    <div style={{ marginLeft: '0%', padding: '1px 16px', height: '1000px' }}>
      <h2>Edit Asset</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={asset.name}
            onChange={handleChange}
            style={{border: '1px solid #ccc'}}
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
            style={{border: '1px solid #ccc'}}
          />
        </div>
        <div>
          <label>Description:</label>
          <textarea
            name="description"
            value={asset.description}
            onChange={handleChange}
            style={{border: '1px solid #ccc'}}
          />
        </div>
        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
};

export default EditAsset;
