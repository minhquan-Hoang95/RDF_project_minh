import React, { useState, useEffect } from "react";
import { Menu, X, Bookmark } from "lucide-react";

const Header: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 10) {
        setIsScrolled(true);
      } else {
        setIsScrolled(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <header
      className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ${
        isScrolled ? "bg-white shadow-md py-3" : "bg-transparent py-5"
      }`}
    >
      <div className="container mx-auto px-4 md:px-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <Bookmark className="h-8 w-8 text-indigo-600 mr-2" />
            <span className="text-xl font-bold text-indigo-600">
              WebAnnotate
            </span>
          </div>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex space-x-8">
            <a
              href="#features"
              className="text-slate-700 hover:text-indigo-600 transition-colors"
            >
              Features
            </a>
            <a
              href="#installation"
              className="text-slate-700 hover:text-indigo-600 transition-colors"
            >
              Installation
            </a>
            <a
              href="#getting-started"
              className="text-slate-700 hover:text-indigo-600 transition-colors"
            >
              Getting Started
            </a>
            <a
              href="#download"
              className="text-slate-700 hover:text-indigo-600 transition-colors"
            >
              Download
            </a>
          </nav>

          {/* Mobile menu button */}
          <button
            className="md:hidden text-slate-700 hover:text-indigo-600 focus:outline-none"
            onClick={() => setIsOpen(!isOpen)}
          >
            {isOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>

        {/* Mobile Navigation */}
        {isOpen && (
          <div className="md:hidden mt-4 pb-4">
            <div className="flex flex-col space-y-4">
              <a
                href="#features"
                className="text-slate-700 hover:text-indigo-600 transition-colors"
                onClick={() => setIsOpen(false)}
              >
                Features
              </a>
              <a
                href="#installation"
                className="text-slate-700 hover:text-indigo-600 transition-colors"
                onClick={() => setIsOpen(false)}
              >
                Installation
              </a>
              <a
                href="#getting-started"
                className="text-slate-700 hover:text-indigo-600 transition-colors"
                onClick={() => setIsOpen(false)}
              >
                Getting Started
              </a>
              <a
                href="#download"
                className="text-slate-700 hover:text-indigo-600 transition-colors"
                onClick={() => setIsOpen(false)}
              >
                Download
              </a>
            </div>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;
