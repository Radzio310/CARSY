function AboutUs() {
   const bodyContent =
      "Witamy w Carsy, zaufanej firmie zajmującej się naprawą samochodów! Naszym celem jest świadczenie najwyższej jakości usług wszystkim naszym klientom, zapewniając sprawne i bezpieczne działanie Twojego pojazdu na drodze. Nasz zespół wysoko wykwalifikowanych techników ma wieloletnie doświadczenie w branży motoryzacyjnej i jest wyposażony w najnowocześniejsze narzędzia i technologię, aby wykonać każdą naprawę lub konserwację. Rozumiemy znaczenie Twojego samochodu i niedogodności, jakie może powodować, gdy nie działa prawidłowo.";

   return (
      <div className="flex items-center p-12 bg-black text-yellow-500 h-5/6">
         <div
            className="flex-1 h-full bg-cover"
            style={{ backgroundImage: "url('/about-us.webp')" }}
         ></div>
         <div className="flex-1 mt-12 ml-12">
            <h2 className="text-4xl font-bold mb-6 text-yellow-400">O nas</h2>
            <p className="text-lg leading-loose text-gray-300">{bodyContent}</p>
            <button className="mt-8 py-3 px-6 bg-yellow-600 hover:bg-yellow-700 text-black font-semibold rounded-lg shadow-lg focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:ring-opacity-50 transition ease-in-out duration-300">
               Zarezerwuj usługę
            </button>
         </div>
      </div>
   );
}

export default AboutUs;
