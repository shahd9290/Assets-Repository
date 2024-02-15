import React, { useState, useEffect } from "react";
import Assets from './assets.json'
import './AssetTable.css'

function AssetTable(){

    const [assets, setAssets] = useState([]);
        useEffect(() => {
            setAssets(Assets);
    }, []);

    const deleteBtn = (id) => {
        alert("Are you sure you want to delete this?")
      };
   return (
   <div>
        <h1> Assets </h1>
        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Link</th>
                    <th>Creator</th>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {
                Assets && Assets.map(asset =>(
                    <tr key={asset.AssetId}>
                        <td>{asset.Title}</td>
                        <td>{asset.Link}</td>
                        <td>{asset.Creator}</td>
                        <td>{asset.Date}</td>
                        <td>{asset.Type}</td>
                        <td>
                            <button className = "btn btn-danger" 
                            style = {{marginLeft:"10px"}} onClick={()=>deleteBtn(asset.AssetId)}> Delete</button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
   </div>

  );
}
  export default AssetTable;