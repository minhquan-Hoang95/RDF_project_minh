import { useState, useEffect } from "react";
import Authentification from "./components/Authentification";
import CampaignOptions from "./components/CampaignOptions";
import ContentAnnotator from "./components/ContentAnnotator";
import { setLogoutHandler } from "./scripts/axiosInterceptor";

function App() {
  const [username, setUsername] = useState(null);
  const [isConfigured, setIsConfigured] = useState(false);
  const [activeCampaign, setActiveCampaign] = useState(null);
  const [currentView, setCurrentView] = useState("campaignOptions");

  useEffect(() => {
    setLogoutHandler(() => {
      handleLogout();
    });

    const storedUsername = localStorage.getItem("username");
    const storedCampaign = localStorage.getItem("activeCampaign");

    if (storedUsername) {
      setUsername(storedUsername);
      setIsConfigured(true);
    }

    if (storedCampaign && storedCampaign !== "undefined") {
      try {
        const parsedCampaign = JSON.parse(storedCampaign);
        setActiveCampaign(parsedCampaign);
        setCurrentView("contentAnnotator");
      } catch (e) {
        console.error("Error parsing stored campaign:", e);
        setCurrentView("campaignOptions");
      }
    }
  }, []);

  const handleUsernameSubmit = (username) => {
    localStorage.setItem("username", username);
    setUsername(username);
    setIsConfigured(true);
  };

  const handleCampaignSelect = (campaign) => {
    if (campaign) {
      localStorage.setItem("activeCampaign", JSON.stringify(campaign));
      setActiveCampaign(campaign);
      setCurrentView("contentAnnotator");
    } else {
      console.error("Attempted to select an undefined campaign");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("username");
    localStorage.removeItem("activeCampaign");
    setUsername(null);
    setIsConfigured(false);
    setActiveCampaign(null);
    setCurrentView("campaignOptions");
  };

  return (
    <div>
      {!isConfigured ? (
        <Authentification onUsernameSubmit={handleUsernameSubmit} />
      ) : currentView === "contentAnnotator" ? (
        <ContentAnnotator
          campaign={activeCampaign}
          onReturnToCampaignOptions={() => {
            localStorage.removeItem("activeCampaign");
            setActiveCampaign(null);
            setCurrentView("campaignOptions");
          }}
        />
      ) : (
        <CampaignOptions
          setActiveCampaign={setActiveCampaign}
          onCampaignSelect={handleCampaignSelect}
          username={username}
          onLogout={handleLogout}
        />
      )}
    </div>
  );
}

export default App;
