import './App.css';
import axios from 'axios';
import React, { useState } from 'react';

/**
 * Renders form for users to enter their credentials and login
 *  
 * @returns form for users to login
 */
function UserLogin() {

  //states to be kept throughout the program 
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const onSubmit = async (user)=>{
   user.preventDefault();
    try{
      const payload = {
        "username": username,
        "password": password
      }

      //sending login information to backend
      const loggedIn = await axios.post('http://localhost:8080/api/auth/signin',payload);
      const userToken = {
        "id":1,
        "token":loggedIn.data.token};
      
      const storeToken = await axios.patch('http://localhost:3500/bearer-tokens/1', userToken)
      console.log('Login successful!');
      alert("Login successful!");

    }catch (error){
      console.error('Login unsucssesful');
      alert('Wrong username or password')
    };
  }
  return (
    <div className="UserLogin" style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
        <h1>Log into your account</h1>
        <form onSubmit={onSubmit}>
          <label htmlFor="userName">Enter your username:</label>
          <p></p>
          <input type="text" id="userName"  value={username} onChange={event => setUsername(event.target.value)}required/>
          <p></p>
          <label htmlFor="passWord">Enter your password:</label>
          <p></p>
          <input type="password" id="passWord" value={password} onChange={event=> setPassword(event.target.value)}required/>
          <p></p>
          <button>Login</button>
        </form>
    </div>
  );
}

export default UserLogin;
