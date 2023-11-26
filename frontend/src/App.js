import React, { useState } from "react";
import "./App.css";

const App = () => {
   const [showNavbar, setShowNavbar] = useState(true);

   const toggleNavbar = () => {
      setShowNavbar(!showNavbar);
   };

   return (
      <div className="flex h-screen flex-wrap">
         <div className="w-full bg-nav-bg text-white justify-center">
            <img
               src="./carsy-logo.webp"
               alt="carsy logo"
               className="w-1/5 p-8 m-0 mx-auto"
            />
            <nav
               className={`text-lg
            ${showNavbar ? "block" : "hidden"}`}
            >
               <ul className="space-y-6">
                  <li>
                     <a
                        href="/"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        Główna strona
                     </a>
                  </li>
                  <li>
                     <a
                        href="/about"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        O nas
                     </a>
                  </li>
                  <li>
                     <a
                        href="/services"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        Serwis
                     </a>
                  </li>
                  <li>
                     <a
                        href="/opening-times"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        Godziny otwarcia
                     </a>
                  </li>
                  <li>
                     <a
                        href="/reservation"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        Rezerwacja
                     </a>
                  </li>
                  <li>
                     <a
                        href="/pricing"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        Cennik
                     </a>
                  </li>
                  <li>
                     <a
                        href="/contact"
                        className="hover:text-gray-400 transition duration-300"
                     >
                        Kontakt
                     </a>
                  </li>
               </ul>
            </nav>
         </div>

         {/* Mobile navbar toggle button */}
         <button
            className="md:hidden bg-nav-bg text-white p-2 fixed top-0 right-0 m-4"
            onClick={toggleNavbar}
         >
            <svg
               xmlns="http://www.w3.org/2000/svg"
               className="h-6 w-6"
               fill="none"
               viewBox="0 0 24 24"
               stroke="currentColor"
            >
               <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M4 6h16M4 12h16M4 18h16"
               />
            </svg>
         </button>

         {/* Main content */}
         <div className="w-full md:w-4/5 bg-gray-100 p-8">
            <div className="mb-6 text-center">
               <h2 className="text-5xl font-bold text-gray-800 mb-3">O nas</h2>
               <p className="text-gray-700 text-lg">
                  Witamy w Carsy, zaufanej firmie zajmującej się naprawą
                  samochodów! Naszym celem jest świadczenie najwyższej jakości
                  usług wszystkim naszym klientom, zapewniając sprawną i
                  bezpieczne działanie Twojego pojazdu na drodze. Nasz zespół
                  wysoko wykwalifikowanych techników ma wieloletnie
                  doświadczenie w branży motoryzacyjnej i jest wyposażony w
                  najnowocześniejsze narzędzia i technologie, aby wykonać każdą
                  naprawę lub konserwację. Rozumiemy znaczenie Twojego samochodu
                  i niedogodności, jakie może powodować, gdy nie działa
                  prawidłowo.
               </p>
               <a
                  href="/about"
                  className="text-blue-600 hover:text-blue-800 transition duration-300"
               >
                  więcej
               </a>
            </div>
            {/* Additional content can be added here */}
         </div>
      </div>
   );
};

export default App;
