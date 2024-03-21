import React from 'react';
import './Navbar.css';
const Navbar = () => {

  return (
    <div className='navbar-container'>
      {/* menu */}
<<<<<<< HEAD
      <ul>
       <li><a href='/assets'>Home</a></li>
       <li><a href='/register-user'>Register User</a></li>
       <li><a href='/user-login'>User Login</a></li>
       <li><a href='/create-assets'>Create Assets </a></li>
       <li><a href='/create-types'>Create Types</a></li>
     </ul>
=======
      <nav className='justify-content-center'>
        <a href='/assets'>Home</a>
        <a href='/register-user'>Register User</a>
        <a href='/user-login'>User Login</a>
        <a href='/create-assets'>Create Assets </a>
        <a href='/create-types'>Create Types</a>
      </nav>
>>>>>>> main
    </div>
  );
};

export default Navbar;
