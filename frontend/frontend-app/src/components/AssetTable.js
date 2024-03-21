import { useState, useEffect } from 'react';
import "./AssetTable.css"
import AuditTrail from './AuditTrail';
import SearchBar from './AssetSearchBar';
import AssetRelationships from './AssetRelationships';
import { AiOutlineAudit } from "react-icons/ai";
import { MdOutlineDeleteForever } from "react-icons/md";
import { TbCirclesRelation } from "react-icons/tb";
import { FiEdit } from "react-icons/fi";
import ATSB from './ATSearchBar';

/**
 *  This is a function that fetches and creates a table from the backend along with initialising actions
 * to be done on the rows of the table
 * @param {*} param0 used to edit the rows of the table
 * @returns html code that creates the table
 */
const Fetch = ({ onEdit }) => {

  //creating the states that need to be kept track off as the website deals with requests
  const [assets, setAssets] = useState([]);
  const [btn_aT,setBtn_AT] = useState(false);
  const [btn_gAT, setBtn_gAT] = useState(false);
  const [gAT,setGAT]=useState([]);
  const [aT,setAT]= useState([]);
  const[sTerm,setSTerm] = useState(null);
  const[sType,setSType] = useState(null);
  const[sUser,setSUser] = useState(null);
  const [btn_RA,setBtn_RA] = useState(false);
  const [child, setChild]  = useState(0);
  const [parent, setParent] = useState("name");
  const [relationship, setRelationship] = useState();
  const [at_search, setAT_Search]= useState(null)

  useEffect(() => {
    retrieveAssets();
  }, );

  /**
   * Retrieves the log of an asset
   * @param {*} id id of asset whose logs to retrive
   */
  function getAssetTrail(id){
    setBtn_AT(true)
     //retrieving the audit trail for a specific asset from the backend
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

 /**
  * Retrieves the logs of all assets
  */
  function getGTA(){
    setBtn_gAT(true)
    //retrieving the audit trail for all assets
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

  /**
   * retrieving assets from database through the backend
   */
  function retrieveAssets(){
    if(sTerm===""){setSTerm(null)}
    if(sType===""){setSType(null)}
    if(sUser===""){setSUser(null)}
    //retriving all assets from the backend 
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

  /**
   * Deletes asset
   * @param {*} id asset to be deleted
   */
  function deleteBtn(id){
    if(window.confirm("Are you sure you want to delete this asset?")){
        //using the delete endpoint to delete assets from both backend and frontend
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

  /**
   * triggers the popup that displays relationships
   */
  function getRA(){
    setBtn_RA(true)
  }

  /**
   * Initialises the props to be sent to the AssetRelationships.js
   * @param {*} c child of the asset 
   * @param {*} p the asset itself
   * @param {*} r relationship between the assets 
   */
  function setGraphV(c,p,r){
    setChild(c);
    setParent(p);
    setRelationship(r);
  }
  /**
   * Filters the assets whose logs will be retrieved
   */
  const filteredGAT= gAT.filter((gatR)=>{
    if(at_search== null)
        return gatR;
    else if(gatR.entry.includes(at_search))
        return gatR;
  })
  return (
    <div className='main_container'>
         <h1> Assets </h1>
         <div className='searchbar-wrapper'>
            <SearchBar sn={setSTerm} st={setSType} su={setSUser} />
        </div>
         <button className='gAT' onClick={()=>getGTA()}><AiOutlineAudit /></button>
         <AuditTrail trigger={btn_gAT} setTrigger={setBtn_gAT}>
            <ATSB sn={setAT_Search}/>
         <ul>
            {filteredGAT.map((gLog)=>(
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
                         <td>{asset.name}</td>
                         <td>{asset.link}</td>
                         <td>{asset.creator_name}</td>
                         <td>{asset.creation_date}</td>
                         <td>{asset.type}</td>
                         <td>{asset.description}</td>
                         <td>
                            <td className='d-row'>
                                <button className = "delete" 
                                style = {{marginLeft:"5px"}} onClick={()=>deleteBtn(asset.id)}><MdOutlineDeleteForever /></button>
                            </td>
                            <td className='e-row'>
                              <button className="btn" ><FiEdit /></button>
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
                             <td onMouseEnter={()=>setGraphV(asset.id,asset.name,asset.relation_type)}>
                                <AssetRelationships trigger={btn_RA} setTrigger={setBtn_RA} childID={child} parentName={parent} relation={relationship}/>
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
