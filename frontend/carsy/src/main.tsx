import React from "react";
import ReactDOM from "react-dom/client";
import Navbar from "./navbar.tsx";
import AboutUs from "./about-us.tsx";
import "./index.css";

const App = () => (
   <React.StrictMode>
      <div className="flex flex-wrap h-screen">
         <Navbar />
         <AboutUs />
      </div>
   </React.StrictMode>
);

ReactDOM.createRoot(document.getElementById("root")!).render(<App />);
