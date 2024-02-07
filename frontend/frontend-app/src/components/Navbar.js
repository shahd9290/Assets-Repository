import React from 'react';
import './Navbar.css';
const Navbar = () => {

  return (
    <div>
      {/* menu */}
      <nav className='justify-content-center'>
        <a href='/create-assets'>Create Assets </a>
        <a href='/create-types'>Create Types</a>
      </nav>
    </div>
  );
};

export default Navbar;