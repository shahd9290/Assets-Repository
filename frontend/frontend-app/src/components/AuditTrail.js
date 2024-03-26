import React from 'react';
import './AuditTrail.css';
import { CgClose } from "react-icons/cg";

/**
 * 
 * @param {*} props triggers the popup and  function to intialise from AssetTable.js
 * @returns popup with close button
 */
function AuditTrail(props){
    return (props.trigger) ?(
        <div className='audittrail'>
            <button className='close-btn' onClick={()=>props.setTrigger(false)}><CgClose /></button>
            <div className='audittrail-inner'>
                {props.children}
            </div>
        </div>
    ): "";
}

export default AuditTrail;
