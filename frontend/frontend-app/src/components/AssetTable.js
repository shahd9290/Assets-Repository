import { useState, useEffect } from 'react';
import "./AssetTable.css"
import AuditTrail from './AuditTrail';
import SearchBar from './AssetSearchBar';
import AssetRelationships from './AssetRelationships';
import { AiOutlineAudit } from "react-icons/ai";
import { MdOutlineDeleteForever } from "react-icons/md";
import { TbCirclesRelation } from "react-icons/tb";


const Fetch = () => {

  const [assets, setAssets] = useState([]);
  const [btn_aT,setBtn_AT] = useState(false);
  const [btn_gAT, setBtn_gAT] = useState(false);
  const [gAT,setGAT]=useState([]);
  const [aT,setAT]= useState([]);
  const[sTerm,setSTerm] = useState(null);
  const[sType,setSType] = useState(null);
  const[sUser,setSUser] = useState(null);
  const [btn_RA,setBtn_RA] = useState(false);
  const [asset1, setAsset1]  = useState('Node 1');

  useEffect(() => {
    retrieveAssets();
  }, );


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
  function retrieveAssets(){
    if(sTerm===""){setSTerm(null)}
    if(sType===""){setSType(null)}
    if(sUser===""){setSUser(null)}
    fetch('http://localhost:8080/search',
    {method:'POST',
    headers: { 'Content-Type': 'application/json' },
    body:JSON.stringify({"search_term":sTerm,
                         "type":sType,
                         "user":sUser})})
    .then((res) => {
        return res.json();
        })
        .then((data) => {
            if (data==null) {return"" }
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
                retrieveAssets()
            })
        }
    )}
  }

  function getRA(){
    setBtn_RA(true)
  }

  return (
    <div className='main_container'>
         <h1> Assets </h1>
         <div className='searchbar-wrapper'>
            <SearchBar sn={setSTerm} st={setSType} su={setSUser} />
        </div>
         <button className='gAT' onClick={()=>getGTA()}><AiOutlineAudit /></button>
         <AuditTrail trigger={btn_gAT} setTrigger={setBtn_gAT}>
         <ul>
            {gAT.map((gLog)=>(
                <li key={gLog.id}>{gLog.entry}</li>
            ))}
        </ul>
         </AuditTrail>
         <div className='table-container'>
         <table>
             <thead>
                 <tr>
                     <th>Title</th>
                     <th>Link</th>
                     <th>Creator</th>
                     <th>Date</th>
                     <th>Type</th>
                     <th>Description</th>
                     <th>Actions</th>
                 </tr>
             </thead>
             <tbody>
                 {
                    //intialising and filling the table from the database
                 assets && assets.map((asset) =>(
                     <tr key={asset.id}>
                         <td>{asset.title}</td>
                         <td>{asset.link}</td>
                         <td>{asset.creator}</td>
                         <td>{asset.creation_date}</td>
                         <td>{asset.type}</td>
                         <td>{asset.description}</td>
                         <td>
                            <td className='d-row'>
                                <button className = "delete" 
                                style = {{marginLeft:"5px"}} onClick={()=>deleteBtn(asset.id)}><MdOutlineDeleteForever /></button>
                            </td>

                             <td className='at-row'>
                                <button className = "auditTrail" onClick={()=>getAssetTrail(asset.id)} ><AiOutlineAudit /></button>
                                <AuditTrail trigger={btn_aT} setTrigger={setBtn_AT}>
                                    <ul>
                                        {aT.map((log)=>(
                                            <li key={log.id}>{log.entry}</li>
                                        ))}
                                    </ul>
                                </AuditTrail>
                             </td>
                             <td onMouseEnter={()=>setAsset1(asset.title)}>
                                <AssetRelationships trigger={btn_RA} setTrigger={setBtn_RA} childName={asset1} parentName='test_1.mp3'/>
                                <button className='ra-row' onClick={()=>getRA()}><TbCirclesRelation /></button>
                             </td>
                             
                         </td>
                     </tr>
                 ))}
             </tbody>
         </table>
         </div>

    </div>
 
   );
};
export default Fetch;