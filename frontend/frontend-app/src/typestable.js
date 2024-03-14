import { useState, useEffect } from 'react';
import "./AssetTable.css"
const Fetch = () => {
    const [types, setTypes] = useState([]);
    useEffect(() => {
        fetch('http://localhost:8080/get-type')
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                console.log(data);
                setTypes(data);
            });
    }, []);
    function deleteBtn(id) {
        fetch('http://localhost:8080/delete-type', {
            method: 'DELETE',
            body: JSON.stringify({ "id": id })
        })
            .then((item) => {
                item.json().then((response) => { console.warn(response) })
            }

            )
    }
    return (
        <div>
            <h1> Type </h1>
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Columns</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        types.map((types) => (
                            <tr key={types.type_id}>
                                <td>{types.title}</td>
                                <td>{types.link}</td>
                                <td>
                                    <button className="btn btn-danger"
                                        style={{ marginLeft: "10px" }} onClick={() => deleteBtn(types.types_id)} > Delete</button>
                                </td>
                            </tr>
                        ))}
                </tbody>
            </table>
        </div>

    );
};
export default Fetch;