//import React,{useState,useEffect} from "react";
import Dropdown from 'react-dropdown';
import './AssetSearchBar.css';
import 'react-dropdown/style.css';
const SearchBar = ()=>{
    const options=['type','date before','date after','asset name']
    return(
        <div className='searchbar-main-container'>
            <div className='sub-container'>
                <Dropdown options={options} id="dd" placeholder='Select an option'/>
            </div>
            <div className="sub-container">
                <p><input className='search-input' placeholder="Type to search"/></p>
            </div>
        </div>
        
    );
}
export default SearchBar;