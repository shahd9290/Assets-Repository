
import './AssetSearchBar.css';
import 'react-dropdown/style.css';

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
            <div className="sub-container">
                <p>date after: </p>
                <input className='search-by-da' placeholder="input date asset was created after" 
                 onChange={event=>{props.sda(event.target.value)}}/>
            </div>
            <div className="sub-container">
                <p>date before: </p>
                <input className='search-by-db' placeholder="input date asset was created before" 
                 onChange={event=>{props.sdb(event.target.value)}}/>
            </div>
            {props.children}
        </div>
        
    );
}
export default SearchBar;