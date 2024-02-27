import React from 'react';
import './Navbar.css';
const Navbar = () => {

  return (
    <div>
      {/* menu */}
      <nav className='justify-content-center'>
        <a href='/assets'>Home</a>
        <a href='/register-user'>Register User</a>
        <a href='/user-login'>User Login</a>
        <a href='/create-assets'>Create Assets </a>
        <a href='/create-types'>Create Types</a>
      </nav>
    </div>
  );
};

export default Navbar;
