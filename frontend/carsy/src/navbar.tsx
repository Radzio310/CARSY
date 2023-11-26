import "./index.css";

function Navbar() {
   const menuItems = [
      { href: "/about", text: "O nas" },
      { href: "/opening-times", text: "Godziny otwarcia" },
      { href: "/reservation", text: "Rezerwacja" },
      { href: "/pricing", text: "Cennik" },
      { href: "/contact", text: "Kontakt" },
   ];

   return (
      <div className="w-full flex flex-col items-center bg-black text-white border-b md:h-1/6">
         <img
            src="/carsy-logo.webp"
            alt="Carsy logo"
            className="my-4 mx-auto"
         />
         <nav>
            <ul className="flex flex-col md:flex-row justify-center items-center gap-4 p-2">
               {menuItems.map((item, index) => (
                  <li key={index} className="md:ml-4">
                     <a
                        href={item.href}
                        className="text-yellow-400 hover:text-yellow-200 transition duration-300"
                     >
                        {item.text}
                     </a>
                  </li>
               ))}
            </ul>
         </nav>
      </div>
   );
}

export default Navbar;
