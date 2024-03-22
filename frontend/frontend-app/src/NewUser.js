import './App.css';

function NewUser() {
  return (
    <div className="NewUser"  style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
        <title>User Registration</title>
        <h1>Registering a New User</h1>
        <form>
          <label for="email">Enter your email address:</label>
          <p></p>
          <input type="text" id="email" className="email" required/>
          <p></p>
          <label className="userName">Enter a new username:</label>
          <p></p>
          <input type="text" id="userName" className="user" required/>
          <p></p>
          <label className="passWord">Enter your password:</label>
          <p></p>
          <input type="password" id="passWord" className="pass" required/>
          <p></p>
          <label className="user-type">User Type:</label>
          <p></p>
          <select id="user-type" className="type" required value = " --Any--">
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
