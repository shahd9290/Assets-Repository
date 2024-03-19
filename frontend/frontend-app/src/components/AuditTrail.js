import React from 'react';
import './AuditTrail.css';
import { CgClose } from "react-icons/cg";

function AuditTrail(props){
    return (props.trigger) ?(
        <div className='audittrail'>
            <div className='audittrail-inner'>
                <button className='close-btn' onClick={()=>props.setTrigger(false)}><CgClose /></button>
                {props.children}
            </div>
        </div>
    ): "";
}

export default AuditTrail;