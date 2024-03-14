import React, { useCallback, useState } from 'react';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';
import { CgClose } from "react-icons/cg"; 
import 'reactflow/dist/style.css';
import './AssetRelationships.css'


const initialNodes = [
  { id: '1', position: { x: 60, y: 0 }, data: { label: 'test 1' } },
  { id: '2', position: { x: 60, y: 100 }, data: { label: 'test 2' } },
];
const initialEdges = [{ id: 'e1-2', source: '1', target: '2', data:{label: 'test'}}];
 
export default function App(props) {

  //const [asset,setAsset] = useState([]);  
  const [nodes, setNodes] = useNodesState(initialNodes);
  const [edges, setEdges] = useEdgesState(initialEdges);
 
  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );
 
  return ((props.trigger)?(
    <div className='asset-relationships'>
      <div className="inner-ar">
        <button className='close-btn' onClick={()=>props.setTrigger(false)}><CgClose /></button> 
        <ReactFlow 
        nodes={nodes}
        edges={edges}
        onConnect={onConnect}
        >
        </ReactFlow>
        {props.children}
      </div>
  </div>
  ):""

  );
}