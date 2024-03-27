import React, { useState, useEffect } from 'react';
import ReactFlow, {
  useNodesState,
  useEdgesState
} from 'reactflow';
import { CgClose } from "react-icons/cg"; 
import 'reactflow/dist/style.css';
import './AssetRelationships.css'
import bearerToken from './tokens/token.json'

const initialAssets = [
  { id: '1', position: { x: 60, y: 0 }, data: { label: '-' } },
  { id: '2', position: { x: 60, y: 100 }, data: { label: '-' } },
];


/**
 * Graph that shows relationship between assets
 * @param {*} props child,parent and relation variables from AssetTable.js
 * @returns html code for a popup that renders a graph of the relationship between assets
 */ 
export default function App(props) {

  //initialising states that need to be kept by the component
  const tokens = JSON.stringify(bearerToken['bearer-tokens']);
  const token = tokens.slice(20,tokens.length-3);
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

  /**
   * Initialises the state of the variables used to render the graph
   * @param {*} childID ID of child asset
   * @param {*} parent name of parent asset
   * @param {*} edgeR relation between assets
   */
  function makeGraph(childID,parent,edgeR){
    fetch('http://localhost:8080/search',
    {method:'POST',
    headers: { 'Content-Type': 'application/json',
    'Authorization': 'Bearer '+ token 
  },
    body:JSON.stringify({"parent_id":childID})})
    .then((res) => {
        return res.json();
        })
        .then((data) => {
            if (data==='') {
             alert("no relationships found")          
            }
            setChild(data);
        });
    setParent(parent);
    setRelation(edgeR)
  }
 
  return ((props.trigger)?(

    <div className='asset-relationships' onMouseEnter={()=> makeGraph(props.childID,props.parentName,props.relation)}>
      <button className='close-btn' onClick={()=>props.setTrigger(false)}><CgClose /></button> 
      { child && child.map( (c)=>(
        <div className="inner-ar" key={c.id} onMouseEnter={()=>setChildName(c.name)}>
        <p> {parent} {relation} {childName}</p>
        <ReactFlow nodes={assets} edges={edges}>
        </ReactFlow>
        {props.children}
      </div>))}
      {childName===null ?(<h3 className='no-relation'>No related assets</h3>):(<></>)}
  </div>
  ):""

  );
}