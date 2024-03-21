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
const initialEdges = [{ id: 'e1-2', source: '1', target: '2', label: 'describes', type: 'step'}];
 
export default function App(props) {

  //initialising states that need to be kept by the component
  const [asset1,setAsset1] = useState('Asset 1');  
  const [asset2,setAsset2] = useState('Asset 2');  
  const [assets, setNodes] = useNodesState(initialAssets);
  const [edges, setEdges] = useEdgesState(initialEdges);
 
  useEffect(() => {
    setNodes((assets) =>
      assets.map((node) => {
        if (node.id === '1') {
          // initialising the child node with a name from the AssetTable.js 
          node.data = {
            ...node.data,
            label: asset1,
          };
        }
        if (node.id === '2') {
        // initialising the child node with a name from the AssetTable.js
          node.data = {
            ...node.data,
            label: asset2,
          };
        }
        return node;
      })
    );
  }, [asset1,asset2, setNodes]);
  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );
 
  return ((props.trigger)?(

    <div className='asset-relationships' onMouseEnter={()=>setAsset1(props.childName)} >
      <div className="inner-ar" onMouseEnter={()=>setAsset2(props.parentName)}>
        <button className='close-btn' onClick={()=>props.setTrigger(false)}><CgClose /></button> 
        <ReactFlow 
        nodes={assets}
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