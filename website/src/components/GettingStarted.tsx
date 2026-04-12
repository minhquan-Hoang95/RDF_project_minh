import React from "react";
import {
  MousePointer,
  UserPlus,
  FolderPlus,
  FileText,
  Link2,
  Save,
} from "lucide-react";

const steps = [
  {
    icon: (
      <MousePointer className="w-8 h-8 text-indigo-600 group-hover:scale-110 transition-transform duration-300" />
    ),
    title: "Click the extension icon",
    description:
      "Find the WebAnnotate icon in your browser toolbar and click it to open.",
    gradient: "from-indigo-500 to-blue-500",
  },
  {
    icon: (
      <UserPlus className="w-8 h-8 text-purple-600 group-hover:scale-110 transition-transform duration-300" />
    ),
    title: "Create your account",
    description:
      "Sign up with your email and create a password to get started.",
    gradient: "from-purple-500 to-pink-500",
  },
  {
    icon: (
      <FolderPlus className="w-8 h-8 text-emerald-600 group-hover:scale-110 transition-transform duration-300" />
    ),
    title: "Start or join a campaign",
    description:
      "Create a new annotation campaign or join an existing one using a campaign ID.",
    gradient: "from-emerald-500 to-teal-500",
  },
  {
    icon: (
      <FileText className="w-8 h-8 text-amber-600 group-hover:scale-110 transition-transform duration-300" />
    ),
    title: "Select website content",
    description:
      "Highlight text or images on any website you want to annotate.",
    gradient: "from-amber-500 to-orange-500",
  },
  {
    icon: (
      <Link2 className="w-8 h-8 text-rose-600 group-hover:scale-110 transition-transform duration-300" />
    ),
    title: "Link to semantic entities",
    description:
      "Connect your annotations to knowledge graphs like DBpedia or GeoNames.",
    gradient: "from-rose-500 to-red-500",
  },
  {
    icon: (
      <Save className="w-8 h-8 text-cyan-600 group-hover:scale-110 transition-transform duration-300" />
    ),
    title: "Auto-save and sync",
    description:
      "Your annotations are automatically saved to the cloud and synced across devices.",
    gradient: "from-cyan-500 to-blue-500",
  },
];

const GettingStarted: React.FC = () => {
  return (
    <section
      id="getting-started"
      className="py-20 bg-gradient-to-b from-white to-slate-50"
    >
      <div className="container mx-auto px-4 md:px-6">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4 bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600">
            Getting Started
          </h2>
          <p className="text-xl text-slate-600 max-w-3xl mx-auto">
            Follow these steps to begin annotating web content with our plugin.
          </p>
        </div>

        <div className="max-w-5xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {steps.map((step, index) => (
              <div key={index} className="group">
                <div className="flex items-center mb-4">
                  <div className="flex items-center justify-center w-12 h-12 rounded-xl bg-gradient-to-br opacity-10 mr-4">
                    {step.icon}
                  </div>
                  <span className="text-sm font-semibold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600">
                    Step {index + 1}
                  </span>
                </div>
                <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1">
                  <h3 className="text-lg font-semibold text-slate-900 mb-2">
                    {step.title}
                  </h3>
                  <p className="text-slate-600">{step.description}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
};

export default GettingStarted;
