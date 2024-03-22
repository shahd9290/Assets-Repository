import './App.css';
import axios from 'axios';
import React, { useState } from 'react';

function UserLogin() {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  async function onSubmit(){
    const payload = {
      "username": username,
      "password": password
    }
    try{
      const loggedIn = await axios.post('http://localhost:8080/api/auth/signin',payload);
      console.log("log in successful");
      alert("Login successful!");
    }catch (error){
      const errorMessage = error.response ? error.response.data : error.message;
      console.error(errorMessage);
      alert(errorMessage)
    }
  }
  return (
    <div className="UserLogin" style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
      <head>
        <title>User Login</title>
      </head>
        <h1>Log into your account</h1>
        <form>
          <label for="userName">Enter your username:</label>
          <p></p>
          <input type="text" id="userName" name="user" value={username} onChange={event => setUsername(event.target.value)}required/>
          <p></p>
          <label for="passWord">Enter your password:</label>
          <p></p>
          <input type="password" id="passWord" name="pass" value={password} onChange={event=> setPassword(event.target.value)}required/>
          <p></p>
          <button type="submit" onSubmit={onSubmit}>Login</button>
        </form>
    </div>
  );
}

export default UserLogin;
