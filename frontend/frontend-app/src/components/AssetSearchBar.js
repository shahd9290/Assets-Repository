

import './AssetSearchBar.css';

/**
 * 
 * @param {*} props functions to be intialised from AssetTable.js
 * @returns searchbar for assets
 */
function SearchBar(props){
    return(
        <div className='searchbar-main-container'>
            <div className='sub-container'>
                <p>Asset Name: </p>
                <input className='search-by-name' placeholder="input asset name to search" 
                 onChange={event=>{props.sn(event.target.value)}}/> 
            </div>
            <div className="sub-container">
                <p>Type: </p>
                <input className='search-by type' placeholder="input type of asset to search" 
                 onChange={event=>{props.st(event.target.value)}}/>
            </div>
            <div className="sub-container">
                <p>User: </p>
                <input className='search-by-user' placeholder="input name of user to search" 
                onChange={event=>{props.su(event.target.value)}}/>
            </div>
            {props.children}
        </div>
        
    );
}
export default SearchBar;