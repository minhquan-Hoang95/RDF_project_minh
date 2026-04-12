import Header from "./components/Header";
import Hero from "./components/Hero";
import Features from "./components/Features";
import Installation from "./components/Installation";
import GettingStarted from "./components/GettingStarted";
import Download from "./components/Download";
import Footer from "./components/Footer";

function App() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-50 to-white text-slate-800">
      <Header />
      <main>
        <Hero />
        <Features />
        <Installation />
        <GettingStarted />
        <Download />
      </main>
      <Footer />
    </div>
  );
}

export default App;
