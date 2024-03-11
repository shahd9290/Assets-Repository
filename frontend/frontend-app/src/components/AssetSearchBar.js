import React,{useState} from "react";
import './AssetSearchBar.css';
import 'react-dropdown/style.css';

function SearchBar(props){
    const[sTerm,setSTerm] = useState('');
    const[sType,setSType] = useState('');
    const[sUser,setSUser] = useState('');
    const[sDa,setSDa]= useState('');
    const[sDb,setSDb]= useState('');

    return(
        <div className='searchbar-main-container'>
            <div className='sub-container'>
                <p>Asset Name: </p>
                <input className='search-by-name' placeholder="Type to search in order asset name,type" value={sTerm}/>
            </div>
            <div className="sub-container">
                <p>Type: </p>
                <input className='search-by type' placeholder="Type to search in order asset name,type" value={sType}/>
            </div>
            <div className="sub-container">
                <p>User: </p>
                <input className='search-by-user' placeholder="Type to search in order asset name,type" value={sUser}/>
            </div>
            <div className="sub-container">
                <p>date after: </p>
                <input className='search-by-da' placeholder="Type to search in order asset name,type" value={sDa}/>
            </div>
            <div className="sub-container">
                <p>date before: </p>
                <input className='search-by-db' placeholder="Type to search in order asset name,type" value={sDb}/>
            </div>
            {props.children}
        </div>
        
    );
}
export default SearchBar;