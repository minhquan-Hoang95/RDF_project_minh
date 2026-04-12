import React, { useState } from "react";
import { Chrome, Siren as Firefox, Monitor, Play } from "lucide-react";

type BrowserType = "chrome" | "firefox" | "edge";

const Installation: React.FC = () => {
  const [selectedBrowser, setSelectedBrowser] = useState<BrowserType>("chrome");

  const instructions = {
    chrome: [
      "Download the ZIP file from the link below.",
      "Unzip the file on your computer.",
      "Open Google Chrome.",
      "Navigate to chrome://extensions/",
      "Enable Developer mode (top right corner).",
      'Click on "Load unpacked".',
      "Select the folder you unzipped.",
    ],
    firefox: [
      "Download the ZIP file from the link below.",
      "Unzip the file on your computer.",
      "Open Mozilla Firefox.",
      "Navigate to about:addons",
      'Click the gear icon and select "Debug Add-ons".',
      'Click "Load Temporary Add-on".',
      "Select any file in the unzipped folder.",
    ],
    edge: [
      "Download the ZIP file from the link below.",
      "Unzip the file on your computer.",
      "Open Microsoft Edge.",
      "Navigate to edge://extensions/",
      "Enable Developer mode (bottom left).",
      'Click on "Load unpacked".',
      "Select the folder you unzipped.",
    ],
  };

  return (
    <section id="installation" className="py-20">
      <div className="container mx-auto px-4 md:px-6">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4">
            Easy Installation
          </h2>
          <p className="text-xl text-slate-600 max-w-3xl mx-auto">
            Follow these simple steps to install the extension on your browser.
          </p>
        </div>

        <div className="max-w-4xl mx-auto">
          <div className="flex flex-wrap justify-center mb-8 gap-4">
            <button
              onClick={() => setSelectedBrowser("chrome")}
              className={`flex items-center px-5 py-3 rounded-lg transition-colors ${
                selectedBrowser === "chrome"
                  ? "bg-indigo-600 text-white"
                  : "bg-slate-100 text-slate-700 hover:bg-slate-200"
              }`}
            >
              <Chrome className="w-5 h-5 mr-2" />
              Chrome
            </button>
            <button
              onClick={() => setSelectedBrowser("firefox")}
              className={`flex items-center px-5 py-3 rounded-lg transition-colors ${
                selectedBrowser === "firefox"
                  ? "bg-indigo-600 text-white"
                  : "bg-slate-100 text-slate-700 hover:bg-slate-200"
              }`}
            >
              <Firefox className="w-5 h-5 mr-2" />
              Firefox
            </button>
            <button
              onClick={() => setSelectedBrowser("edge")}
              className={`flex items-center px-5 py-3 rounded-lg transition-colors ${
                selectedBrowser === "edge"
                  ? "bg-indigo-600 text-white"
                  : "bg-slate-100 text-slate-700 hover:bg-slate-200"
              }`}
            >
              <Monitor className="w-5 h-5 mr-2" />
              Edge
            </button>
          </div>

          <div className="bg-white p-8 rounded-xl shadow-sm border border-slate-200">
            <h3 className="text-xl font-semibold text-slate-900 mb-6 flex items-center">
              <span>
                Installation Steps for{" "}
                {selectedBrowser.charAt(0).toUpperCase() +
                  selectedBrowser.slice(1)}
              </span>
            </h3>

            <ol className="space-y-4">
              {instructions[selectedBrowser].map((step, index) => (
                <li key={index} className="flex items-start">
                  <span className="flex items-center justify-center w-6 h-6 rounded-full bg-indigo-100 text-indigo-600 font-medium text-sm mr-3 mt-0.5">
                    {index + 1}
                  </span>
                  <span className="text-slate-700">{step}</span>
                </li>
              ))}
            </ol>

            <div className="mt-8 pt-6 border-t border-slate-200">
              <div className="flex items-center">
                <Play className="w-6 h-6 text-indigo-600 mr-3" />
                <p className="text-slate-700">
                  <span className="font-medium">
                    Watch the installation demo:{" "}
                  </span>
                  <a
                    href="https://www.youtube.com/watch?v=xjFTTA6vjnU"
                    className="text-indigo-600 hover:underline"
                  >
                    Installation Video
                  </a>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Installation;
