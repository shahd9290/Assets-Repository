import './App.css';


function Types() {
  return (
    <div className="App" style={{ marginLeft: '15%', padding: '1px 16px', height: '1000px' }}>
      <head>
        <h1>Type Management System</h1>
      </head>
        <h1>Create New Type</h1>
        <form>
          <label for="type-title">Title:</label>
          <p></p>
          <input type="text" id="type-title" name="title" required/>
          <p></p>
          <label for="type-description">Description:</label>
          <p></p>
          <textarea id="type-description" name="description" required></textarea>
          <p></p>
          <button id="createButton" onclick="handleButtonClick(id)" type="submit">Create Type</button>
        </form>
        <button id="deleteButton" onclick="handleButtonClick(id)">Delete Type</button>
    </div>
  );
}


export default Types;
