import React from 'react';
import './Navbar.css';
const Navbar = () => {

  return (
    <div className='navbar-container'>
      {/* menu */}
      <ul>
       <li><a href='/assets'>Home</a></li>
       <li><a href='/register-user'>Register User</a></li>
       <li><a href='/user-login'>User Login</a></li>
       <li><a href='/create-assets'>Create Assets </a></li>
       <li><a href='/create-types'>Create Types</a></li>
       <li><a>Logout</a></li>
     </ul>
    </div>
  );
};

export default Navbar;
