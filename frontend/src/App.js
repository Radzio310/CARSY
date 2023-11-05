import React from "react";
import "./App.css"; // Make sure to link the stylesheet

const App = () => {
   return (
      <div className="app">
         <div className="header">
            <button className="btn">LOGIN</button>
            <button className="btn">REGISTER</button>
         </div>
         <div className="main-content">
            <div className="car-silhouette"></div>
            <div className="logo">CARSY</div>
            <div className="menu">
               <button className="btn">PRICE LIST</button>
               <button className="btn">CONTACT</button>
               <button className="btn">OPINIONS</button>
            </div>
         </div>
      </div>
   );
};

export default App;
