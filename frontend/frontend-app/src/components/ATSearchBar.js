import React from "react";
import './ATSearchBar.css';

/**
 * 
 * @param {*} props function from AssetTable that needs to be intialised
 * @returns searchbar for asset logs
 */
function ATSB(props){
    return(
        <div className='at-searchbar-main-container'>
        <div className='at-sub-container'>
            <p>Asset Name: </p>
            <input className='at-search-by-name' placeholder="input asset name to search" 
             onChange={event=>{props.sn(event.target.value)}}/> 
        </div>
        {props.children}
    </div>
    )
}export default ATSB;