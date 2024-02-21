import { useState, useEffect } from 'react';
import "./AssetTable.css"
const Fetch = () => {
  const [assets, setAssets] = useState([]);
  useEffect(() => {
    fetch('http://localhost:8080/get-assets')
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);
        setAssets(data);
      });
  }, []);
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
                 assets.map((asset) =>(
                     <tr key={asset.asset_id}>
                         <td>{asset.title}</td>
                         <td>{asset.link}</td>
                         <td>{asset.creator}</td>
                         <td>{asset.creation_date}</td>
                         <td>{asset.type}</td>
                         <td>
                             <button className = "btn btn-danger" 
                             style = {{marginLeft:"10px"}}> Delete</button>
                         </td>
                     </tr>
                 ))}
             </tbody>
         </table>
    </div>
 
   );
};
export default Fetch;