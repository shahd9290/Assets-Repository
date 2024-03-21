import './App.css';

function NewUser() {
  return (
    <div className="NewUser"  style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
      <head>
        <title>User Registration</title>
      </head>
        <h1>Registering a New User</h1>
        <form>
          <label for="firstName">Enter your first name:</label>
          <p></p>
          <input type="text" id="firstName" name="firstN" required/>
          <p></p>
          <label for="lastName">Enter your family name:</label>
          <p></p>
          <input type="text" id="lastName" name="lastN" required/>
          <p></p>
          <label for="email">Enter your email address:</label>
          <p></p>
          <input type="text" id="email" name="email" required/>
          <p></p>
          <label for="userName">Enter a new username:</label>
          <p></p>
          <input type="text" id="userName" name="user" required/>
          <p></p>
          <label for="passWord">Enter your password:</label>
          <p></p>
          <input type="password" id="passWord" name="pass" required/>
          <p></p>
          <label for="user-type">User Type:</label>
          <p></p>
          <select id="user-type" name="type" required value = " --Any--">
            <option>User</option>
            <option>Admin</option>
          </select>
          <p></p>
          <p></p>
          <p></p>
          <button id="submit" onclick="handleButtonClick(id)" name="button">Register User</button>
        </form>
    </div>
  );
}

export default NewUser;
