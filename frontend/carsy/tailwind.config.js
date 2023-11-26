/** @type {import('tailwindcss').Config} */
export default {
   content: ["./src/**/*.{js,jsx,ts,tsx}"],
   theme: {
      extend: {
         colors: {
            background: "#1f1e1e",
         },
         fontFamily: {
            sans: ["Zilla Slab", "sans-serif"],
         },
      },
   },
   plugins: [],
};
