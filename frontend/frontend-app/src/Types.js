import './App.css';


function Types() {
  return (
    <div className="App">
      <head>
        <title>Type Management System</title>
      </head>
      <body>
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
      </body>
    </div>
  );
}


export default Types;
