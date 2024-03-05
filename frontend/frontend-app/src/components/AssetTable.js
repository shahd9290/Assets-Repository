import { useState, useEffect } from 'react';
import "./AssetTable.css"
import AuditTrail from './AuditTrail';

const Fetch = () => {

  const [assets, setAssets] = useState([]);
  const [btn_aT,setBtn_AT] = useState(false);
  const [btn_gAT, setBtn_gAT] = useState(false);
  const [gAT,setGAT]=useState([]);
  const [aT,setAT]= useState([]);
  useEffect(() => {
    getAssets();
  }, []);


  function getAssetTrail(id){
    setBtn_AT(true)
    fetch(`http://localhost:8080/audit/log${id}`)
    .then((trail)=>{
        return trail.json();
    })
    .then((record)=>{
        if (record==null) {return }
        console.log(record);
        setAT(record);
    })
  }

  function getGTA(){
    setBtn_gAT(true)
    fetch('http://localhost:8080/audit/logs')
    .then((res) => {
        return res.json();
        })
        .then((gRecord) => {
            if (gRecord==null) {return }
            console.log(gRecord);
            setGAT(gRecord);
        });
  }
  function getAssets(){
    fetch('http://localhost:8080/get-assets')
    .then((res) => {
        return res.json();
        })
        .then((data) => {
            if (data==null) {return }
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
    <div>
         <h1> Assets </h1>
         <button className='gAT' onClick={()=>getGTA()}>General Audit Trail</button>
         <AuditTrail trigger={btn_gAT} setTrigger={setBtn_gAT}>
         <ul>
            {gAT.map((gLog)=>(
                <li key={gLog.id}>{gLog.entry}</li>
            ))}
        </ul>
         </AuditTrail>
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
                             <button className = "delete" 
                             style = {{marginLeft:"5px"}} onClick={()=>deleteBtn(asset.id)} > Delete</button>
                             <button className = "auditTrail" onClick={()=>getAssetTrail(asset.id)}>Show Audit Trail</button>
                             <AuditTrail trigger={btn_aT} setTrigger={setBtn_AT}>
                                <ul>
                                    {aT.map((log)=>(
                                        <li key={log.id}>{log.entry}</li>
                                    ))}
                                </ul>
                             </AuditTrail>
                         </td>
                     </tr>
                 ))}
             </tbody>
         </table>
    </div>
 
   );
};
export default Fetch;