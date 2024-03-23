import React from 'react';
import './Navbar.css';
import axios from 'axios';

const Navbar = () => {
  const handleClick = async () => {
    try{
      const payload = {
        "id":1,
        "token": "revoked"
      }
      const token = await axios.patch('http://localhost:3500/bearer-tokens/1',payload);
      console.log(token);
    }catch(err){
      
    }
  };
  return (
    <div className='navbar-container'>
      {/* menu */}
      <ul>
       <li><a href='/assets'>Home</a></li>
       <li><a href='/register-user'>Register User</a></li>
       <li><a href='/user-login'>User Login</a></li>
       <li><a href='/create-assets'>Create Assets </a></li>
       <li><a href='/create-types'>Create Types</a></li>
       <li><a href='/logout' onClick={handleClick}>Logout</a></li>
     </ul>
    </div>
  );
};

export default Navbar;
