import React from "react";

function Popup(){
    return(
        <div>
            <Popup trigger= {<button>Show audit trail</button>} position="right center">
                <p>This is was last edited by Owen Mashingaidze</p>
            </Popup>
        </div>
    )
}
export default Popup;