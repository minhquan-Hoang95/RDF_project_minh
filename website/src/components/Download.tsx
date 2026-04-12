import React from "react";
import axios from "axios";
import {
  Download as DownloadIcon,
  ChevronRight,
  Chrome,
  Siren as Firefox,
  Monitor,
} from "lucide-react";

const Download: React.FC = () => {
  const handleDownload = async () => {
    const baseUrl = import.meta.env.VITE_APP_API_BASE_URL;

    await axios
      .get(`${baseUrl}/plugin`, {
        responseType: "blob",
      })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "annotation-plugin.zip");
        document.body.appendChild(link);
        link.click();
        link.remove();
      })
      .catch((error) => {
        console.error("Download error:", error);
      });
  };

  return (
    <section id="download" className="py-20 bg-indigo-600 text-white">
      <div className="container mx-auto px-4 md:px-6">
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Download the Extension
          </h2>
          <p className="text-xl text-indigo-100 max-w-3xl mx-auto">
            Get started with WebAnnotate today and transform how you annotate
            web content.
          </p>
        </div>

        <div className="max-w-2xl mx-auto bg-white text-slate-800 rounded-xl overflow-hidden shadow-lg">
          <div className="p-8">
            <div className="flex items-center mb-6">
              <DownloadIcon className="w-8 h-8 text-indigo-600 mr-3" />
              <h3 className="text-2xl font-semibold">WebAnnotate v1.0</h3>
            </div>

            <p className="mb-6 text-slate-600">
              The latest version supports Chrome, Firefox, and Edge browsers.
              Download once and install on your preferred browser.
            </p>

            <div className="flex flex-wrap gap-3 mb-8">
              <div className="flex items-center px-3 py-1 bg-slate-100 rounded-full text-slate-600 text-sm">
                <Chrome className="w-4 h-4 mr-1" />
                <span>Chrome</span>
              </div>
              <div className="flex items-center px-3 py-1 bg-slate-100 rounded-full text-slate-600 text-sm">
                <Firefox className="w-4 h-4 mr-1" />
                <span>Firefox</span>
              </div>
              <div className="flex items-center px-3 py-1 bg-slate-100 rounded-full text-slate-600 text-sm">
                <Monitor className="w-4 h-4 mr-1" />
                <span>Edge</span>
              </div>
            </div>

            <button
              onClick={handleDownload}
              className="block w-full py-4 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg transition-colors duration-300 text-center"
            >
              Download ZIP File
            </button>

            <p className="mt-4 text-sm text-slate-500 text-center">
              By downloading, you agree to our terms of service and privacy
              policy.
            </p>
          </div>

          <div className="px-8 py-6 bg-slate-50 border-t border-slate-200">
            <h4 className="font-semibold mb-3">After downloading:</h4>
            <p className="text-slate-600 mb-4">
              Follow the installation instructions for your browser to complete
              setup.
            </p>
            <a
              href="#installation"
              className="text-indigo-600 hover:text-indigo-800 font-medium flex items-center"
            >
              View Installation Guide
              <ChevronRight className="w-4 h-4 ml-1" />
            </a>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Download;
