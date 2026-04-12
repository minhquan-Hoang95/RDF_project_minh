import React from "react";
import { Highlighter, Brain, Users, Lock, Save, Layers } from "lucide-react";

const features = [
  {
    icon: (
      <Highlighter className="w-12 h-12 text-indigo-600 mb-4 transform transition-transform group-hover:scale-110 duration-300" />
    ),
    title: "Web Content Annotation",
    description:
      "Highlight text or images directly on any web page with precision and ease.",
    gradient: "from-indigo-500 to-blue-500",
  },
  {
    icon: (
      <Brain className="w-12 h-12 text-purple-600 mb-4 transform transition-transform group-hover:scale-110 duration-300" />
    ),
    title: "Semantic Enrichment",
    description:
      "Link annotations to knowledge graphs like DBpedia, GeoNames, and more for richer context.",
    gradient: "from-purple-500 to-pink-500",
  },
  {
    icon: (
      <Users className="w-12 h-12 text-emerald-600 mb-4 transform transition-transform group-hover:scale-110 duration-300" />
    ),
    title: "Collaborative Campaigns",
    description:
      "Create annotation campaigns and invite participants by email to contribute.",
    gradient: "from-emerald-500 to-teal-500",
  },
  {
    icon: (
      <Lock className="w-12 h-12 text-amber-600 mb-4 transform transition-transform group-hover:scale-110 duration-300" />
    ),
    title: "User Accounts",
    description:
      "Register, manage, and join annotation campaigns with personal accounts.",
    gradient: "from-amber-500 to-orange-500",
  },
  {
    icon: (
      <Save className="w-12 h-12 text-rose-600 mb-4 transform transition-transform group-hover:scale-110 duration-300" />
    ),
    title: "Cloud Storage",
    description:
      "Annotations are automatically saved and synced to the cloud for seamless collaboration.",
    gradient: "from-rose-500 to-red-500",
  },
  {
    icon: (
      <Layers className="w-12 h-12 text-cyan-600 mb-4 transform transition-transform group-hover:scale-110 duration-300" />
    ),
    title: "Cross-Browser Support",
    description: "Works seamlessly across Chrome, Firefox, and Edge browsers.",
    gradient: "from-cyan-500 to-blue-500",
  },
];

const Features: React.FC = () => {
  return (
    <section id="features" className="py-20 bg-slate-50">
      <div className="container mx-auto px-4 md:px-6">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-slate-900 mb-4 bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600">
            Powerful Features
          </h2>
          <p className="text-xl text-slate-600 max-w-3xl mx-auto">
            Everything you need to create, manage, and collaborate on web
            annotations.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, index) => (
            <div
              key={index}
              className="group bg-white p-8 rounded-xl shadow-sm border border-slate-100 hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 relative overflow-hidden"
            >
              <div className="absolute inset-0 bg-gradient-to-br opacity-0 group-hover:opacity-5 transition-opacity duration-300" />
              <div className="relative z-10">
                {feature.icon}
                <h3 className="text-xl font-semibold text-slate-900 mb-3">
                  {feature.title}
                </h3>
                <p className="text-slate-600">{feature.description}</p>
              </div>
              <div className="absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Features;
