import React from "react";
import { ChevronRight, Bookmark } from "lucide-react";

const Hero: React.FC = () => {
  return (
    <section className="pt-32 pb-20 md:pt-40 md:pb-24">
      <div className="container mx-auto px-4 md:px-6">
        <div className="flex flex-col md:flex-row items-center">
          <div className="md:w-1/2 mb-10 md:mb-0">
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight text-slate-900 mb-6">
              Collaborative Web
              <span className="text-indigo-600"> Annotation</span> Plugin
            </h1>
            <p className="text-lg md:text-xl text-slate-600 mb-8 max-w-lg">
              Create collaborative annotation campaigns on any website. Select
              text or images and link them to RDF knowledge graphs.
            </p>
            <div className="flex flex-col sm:flex-row gap-4">
              <a
                href="#download"
                className="px-6 py-3 bg-indigo-600 text-white font-medium rounded-lg hover:bg-indigo-700 transition-colors duration-300 flex items-center justify-center"
              >
                Download Now
                <ChevronRight className="ml-2 h-5 w-5" />
              </a>
              <a
                href="#features"
                className="px-6 py-3 bg-white text-indigo-600 font-medium rounded-lg border border-indigo-600 hover:bg-indigo-50 transition-colors duration-300 flex items-center justify-center"
              >
                Explore Features
              </a>
            </div>
          </div>
          <div className="md:w-1/2 relative">
            <div className="relative rounded-lg shadow-xl overflow-hidden border border-slate-200">
              <div className="absolute top-0 left-0 right-0 h-10 bg-slate-100 flex items-center px-4">
                <div className="flex space-x-2">
                  <div className="w-3 h-3 rounded-full bg-red-500"></div>
                  <div className="w-3 h-3 rounded-full bg-yellow-500"></div>
                  <div className="w-3 h-3 rounded-full bg-green-500"></div>
                </div>
                <div className="flex-1 text-center text-xs text-slate-500">
                  example.com
                </div>
              </div>
              <div className="pt-10 pb-6 px-6">
                <div className="bg-white rounded-lg p-4 shadow-sm mb-4 border border-slate-200">
                  <p className="text-slate-800">
                    <span className="bg-yellow-100 px-1 py-0.5 rounded">
                      Annotation example
                    </span>{" "}
                    This text is highlighted by the WebAnnotate plugin,
                    connecting to knowledge graphs.
                  </p>
                </div>
                <div className="flex items-start gap-3 bg-slate-50 p-3 rounded-lg border border-slate-200">
                  <Bookmark className="h-5 w-5 text-indigo-600 mt-0.5" />
                  <div>
                    <h4 className="font-semibold text-slate-800">
                      Entity: DBpedia
                    </h4>
                    <p className="text-sm text-slate-600">
                      Linked to: Knowledge Management
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div className="absolute -bottom-4 -right-4 w-24 h-24 bg-emerald-100 rounded-full -z-10"></div>
            <div className="absolute -top-4 -left-4 w-16 h-16 bg-amber-100 rounded-full -z-10"></div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Hero;
