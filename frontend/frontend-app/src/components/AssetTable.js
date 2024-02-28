import { useState, useEffect } from 'react';
import "./AssetTable.css"
const Fetch = () => {
  const [assets, setAssets] = useState([]);
  useEffect(() => {
    getAssets();
  }, []);

  function getAssets(){
    fetch('http://localhost:8080/get-assets')
    .then((res) => {
        return res.json();
        })
        .then((data) => {
            console.log(data);
            setAssets(data);
        });
  }
  function deleteBtn(id){
    if(window.confirm("Are you sure you want to delete this asset?")){
        fetch('http://localhost:8080/delete-asset',
        {method :'DELETE', 
        headers : {"Content-Type": "application/json"},
        body:JSON.stringify({"id":id})})
        .then( (item)=>{
            item.text()
            .then((response)=>{
                console.warn(response);
                alert(response);
                getAssets()
            })
        }
    )}
  }
  return (
    <div style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
         <h1> Assets </h1>
         <table>
             <thead>
                 <tr>
                    <th>ID </th>
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
                     <tr key={asset.id}>
                        <td>{asset.id}</td>
                         <td>{asset.title}</td>
                         <td>{asset.link}</td>
                         <td>{asset.creator}</td>
                         <td>{asset.creation_date}</td>
                         <td>{asset.type}</td>
                         <td>
                             <button className = "btn btn-danger"
                             style = {{marginLeft:"10px"}} onClick={()=>deleteBtn(asset.id)} > Delete</button>
                         </td>
                     </tr>
                 ))}
             </tbody>
         </table>
    </div>

   );
};
export default Fetch;
