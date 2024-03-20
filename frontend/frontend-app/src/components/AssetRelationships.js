import React, { useCallback, useState, useEffect } from 'react';
import ReactFlow, {
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';
import { CgClose } from "react-icons/cg"; 
import 'reactflow/dist/style.css';
import './AssetRelationships.css'



const initialAssets = [
  { id: '1', position: { x: 60, y: 0 }, data: { label: '-' } },
  { id: '2', position: { x: 60, y: 100 }, data: { label: '-' } },
];


 
export default function App(props) {

  //initialising states that need to be kept by the component
  const [child,setChild] = useState([]);  
  const [parent,setParent] = useState('Asset 2');  
  const [relation, setRelation] = useState(props.relation);

  const initialEdges = [{ id: 'e1-2', source: '1', target: '2', label:'-',type: 'step'}];


  const [assets, setNodes] = useNodesState(initialAssets);
  const [edges, setEdges] = useEdgesState(initialEdges);
  const [childName, setChildName] = useState("asset");

  useEffect(() => {
    setNodes((assets) =>
      assets.map((node) => {
        if (node.id === '1') {
          // initialising the Parent node with a name from the AssetTable.js 
          node.data = {
            ...node.data,
            label:parent,
          };
        }
        if (node.id === '2') {
        // initialising the child node with a name from the AssetTable.js
          node.data = {
            ...node.data,
            label: childName,
          };
        }
        return node;
      })
    );
    setEdges((eds) =>
    eds.map((edge) => {
      if (edge.id === 'e1-2') {
        edge.data = {
          ...edge.data,
          label: relation,
        }
      }
      return edge;
    })
  );
  }, [childName,parent, relation, setEdges,setNodes]);


  function makeGraph(childID,parent,edgeR){
    fetch('http://localhost:8080/search',
    {method:'POST',
    headers: { 'Content-Type': 'application/json' },
    body:JSON.stringify({"parent_id":childID})})
    .then((res) => {
        return res.json();
        })
        .then((data) => {
            if (data===null) {
              return console.log("no assets returned");            
            }
            console.log(data);
            setChild(data);
        });
    setParent(parent);
    setRelation(edgeR)
  }
 
  return ((props.trigger)?(

    <div className='asset-relationships' onMouseEnter={()=> makeGraph(props.childID,props.parentName,props.relation)} >
      
      { child.map( (c)=>(
        <div className="inner-ar" key={c.id} onMouseEnter={()=>setChildName(c.name)}>
           <p> {parent} {relation} {childName}</p>
        <button className='close-btn' onClick={()=>props.setTrigger(false)}><CgClose /></button> 
       
        <ReactFlow nodes={assets} edges={edges}>
          
        </ReactFlow>
        {props.children}
      </div>))}
  </div>
  ):""

  );
}