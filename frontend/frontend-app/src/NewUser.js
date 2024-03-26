import './App.css';
import axios from 'axios';
import React, { useState } from 'react';

/**
 * renders a form for users to be created on.
 * 
 * @returns form for creating users in the system
 */
function NewUser() {

  //states to be kept throughout the process
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [pwd, setPwd] = useState("");
  const [role, setRole] = useState("");

  const onSubmit = async (user)=>{
    user.preventDefault();
     try{
       const payload = {
         "username": username,
         "password": pwd,
         "email":email,
         "role":[role]
       }

       //send user data to the backend
       const registerUser = await axios.post('http://localhost:8080/api/auth/signup',payload);
 
       
       const token = {"token":JSON.stringify(registerUser.data)};
       console.log(token);
       alert("user registration successful!");
 
     }catch (error){
       console.error('N/a');
       alert('no bueno')
     };
   }
  return (
    <div className="NewUser"  style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
        <h1>Registering a New User</h1>
        <form onSubmit={onSubmit}>
          <label htmlFor="email">Enter your email address:</label>
          <p></p>
          <input type="text" id="email" className="email" value={email} onChange={(e)=>{setEmail(e.target.value)}}required/>
          <p></p>
          <label htmlFor="userName">Enter a new username:</label>
          <p></p>
          <input type="text" id="userName" className="user" value={username} onChange={(e)=>{setUsername(e.target.value)}} required/>
          <p></p>
          <label htmlFor="passWord">Enter your password:</label>
          <p></p>
          <input type="password" id="passWord" className="pass" value={pwd} onChange={(e)=>{setPwd(e.target.value)}} required/>
          <p></p>
          <label htmlFor="user-type">User Type:</label>
          <p></p>
          <select id="user-type" className="type" required value ={role} onChange={(e)=>{setRole(e.target.value)}}>
            <option value='ROLE_USER'>User</option>
            <option value='ROLE_ADMIN'>Admin</option>
            <option value='ROLE_VIEWER'>Viewer</option>
          </select>
          <p></p>
          <p></p>
          <p></p>
          <button name="button">Register User</button>
        </form>
    </div>
  );
}

export default NewUser;
