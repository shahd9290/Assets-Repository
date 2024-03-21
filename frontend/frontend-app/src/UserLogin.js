import './App.css';

function UserLogin() {
  return (
<<<<<<< HEAD
    <div className="UserLogin" style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
      <head>
        <title>User Login</title>
      </head>
=======
    <div className="UserLogin">
      <head>
        <title>User Login</title>
      </head>
      <body>
>>>>>>> main
        <h1>Log into your account</h1>
        <form>
          <label for="userName">Enter your username:</label>
          <p></p>
          <input type="text" id="userName" name="user" required/>
          <p></p>
          <label for="passWord">Enter your password:</label>
          <p></p>
          <input type="password" id="passWord" name="pass" required/>
          <p></p>
<<<<<<< HEAD
          <button type="submit">Register User</button>
        </form>
=======
          <button type="submit" onclick="handleButtonClick(id)" name="button">Register User</button>
        </form>
      </body>
>>>>>>> main
    </div>
  );
}

export default UserLogin;
