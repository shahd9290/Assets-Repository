
import './App.css';


function App() {
  return (
    <div className="App">
      <head>
        <title>Asset Management System</title>
      </head>
      <body>
        <h1>Create New Asset</h1>
        <form>
          <label for="asset-title">Title:</label>
          <p></p>
          <input type="text" id="asset-title" name="title" required/>
          <p></p>
          <label for="asset-description">Description:</label>
          <p></p>
          <textarea id="asset-description" name="description" required></textarea>
          <p></p>
          <label for="asset-type">Type:</label>
          <p></p>
          <select id="asset-type" name="type" required value = " --Any--">
            <option>Software Module</option>
            <option>Documentation</option>
          </select>
          <p></p>
          <button id="createButton" onclick="handleButtonClick(id)" type="submit">Create Asset</button>
        </form>
        <button id="deleteButton" onclick="handleButtonClick(id)">Delete Asset</button>
      </body>
    </div>
  );
}
export default App;
