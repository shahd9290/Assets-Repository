import './App.css';

function UserLogin() {
  return (
    <div className="UserLogin">
      <head>
        <title>User Login</title>
      </head>
      <body>
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
          <button type="submit" onclick="handleButtonClick(id)" name="button">Register User</button>
        </form>
      </body>
    </div>
  );
}

export default UserLogin;
