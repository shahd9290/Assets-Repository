import React from 'react'
import './AuditTrail.css'

function AuditTrail(props){
    return (props.trigger) ?(
        <div className='audittrail'>
            <div className='audittrail-inner'>
                <button className='close-btn' onClick={()=>props.setTrigger(false)}>close</button>
                {props.children}
            </div>
        </div>
    ): "";
}

export default AuditTrail;