//comments for team memebers to understand
import React, { useEffect, useState } from 'react';
import './App.css';
import bearerToken from './components/tokens/token.json'

function App() {
  const tokens = JSON.stringify(bearerToken['bearer-tokens']);
  const token = tokens.slice(20,tokens.length-3);
  const usernames = JSON.stringify(bearerToken['users']);
  const user = usernames.slice(19,usernames.length-3);
  const [types, setTypes] = useState([]);
  const [assetName, setAssetName] = useState('');
  const [assetType, setAssetType] = useState('');
  const [desc,setDesc] = useState('');
  const [link, setLink] = useState('');
  const [pid,setPID] = useState(0);
  const [columns,setColumns] = useState([]);
  const [type, setType] = useState({});

  useEffect(()=>{
    fetch('http://localhost:8080/type/get-types',
    {method:'GET',
    headers: { 'Content-Type': 'application/json',
                'Authorization': 'Bearer '+ token
    }})
    .then((res) => {
        return res.json();
        })
        .then((t) => {
            if (t==null) {return }
            setTypes(t);
        });
    
  });

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setType(values => ({...values, [name]: value}))
  }
  function selectType(type){
    setAssetType(type);
    fetch(`http://localhost:8080/type/get-columns/${type}`,
    {method:'GET',
    headers: { 'Content-Type': 'application/json',
                'Authorization': 'Bearer '+ token
    }})
    .then((res) => {
        return res.json();
        })
        .then((col) => {
            if (col==null) {return }
            setColumns(col);
        })
  }
  const handleSubmit = async (event) => {
    event.preventDefault();


      // POST request to the backend endpoint
      fetch('http://localhost:8080/add-new-asset',
      {method:'POST',
      headers: { 'Content-Type': 'application/json',
                  'Authorization': 'Bearer '+ token
      },
      body : JSON.stringify({asset: {
        "name": assetName,
        "creatorname": user,
        "type": assetType,
       "description": desc,
        "link":link,
        "parent_id":pid
      },
      type})}
      )
      .then((res)=>{
       res.text()
       .then((response)=>{
        console.log(response)
        alert(response)
       })
      })
      .catch((err)=>{
        console.log(err)
      })
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
          value={assetName}
          onChange={(e)=>{setAssetName(e.target.value)}}
        />
        <br></br>
        <label htmlFor="asset-link">Link:</label>
        <input
          type="text"
          id="asset-link"
          name="link"
          required
          value={link}
          onChange={(e)=>{setLink(e.target.value)}}
        />
        <br></br>
        <label htmlFor="asset-description">Description:</label>
        <textarea
          id="asset-description"
          name="description"
          required
          value={desc}
          onChange={(e)=>{setDesc(e.target.value)}}
        />
        <br></br>
        <label htmlFor="asset-type">Type:</label>
        <select
              id="asset-type"
              name="type"
              required
              value={assetType}
              onChange={(e)=>{selectType(e.target.value)}}
            >
        {
          types.map((t)=>(
              <option key={t.type}>{t.type}</option>
          ))
}
          </select>

        
        <label htmlFor="asset-parent">Parent ID:</label>
        <input
          type="text"
          id="asset-parent"
          name="parent"
          required
          value={pid}
          onChange={(e)=>{setPID(e.target.value)}}
        />

        <div>
          {columns.map((addRec,index)=>(
            <div>
              <br></br>
              <label key={index}>{addRec}</label><input
              type="text"
              id="asset-parent"
              name={addRec}
              required
              value={type.name}
              onChange={handleChange} />
              </div>
          ))}
        </div>
        <p></p>
        <button type="submit">Create Asset</button>
      </form>
    </div>
  );
}
export default App;
