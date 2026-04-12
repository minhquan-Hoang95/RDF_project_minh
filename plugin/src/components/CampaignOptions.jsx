import { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faSignOutAlt,
  faPlusCircle,
  faGear,
  faDownload,
} from "@fortawesome/free-solid-svg-icons";
import thesaurusData from "../data/thesaurus.json";
import Modal from "./Modal";
import "../styles/CampaignOptions.css";
import {
  createCampaign,
  getAllCampaigns,
  inviteParticipant,
  exportCampaign,
} from "../services/rdfServices";

const CampaignOptions = ({
  setActiveCampaign,
  onCampaignSelect,
  username,
  onLogout,
}) => {
  const [view, setView] = useState("options");
  const [selectedThesauri, setSelectedThesauri] = useState([]);
  const [campaignName, setCampaignName] = useState("");

  const [userCampaigns, setUserCampaigns] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedCampaign, setSelectedCampaign] = useState(null);

  const [emailToAdd, setEmailToAdd] = useState("");
  const [modalStyle, setModalStyle] = useState({});

  useEffect(() => {
    if (username) {
      fetchCampaigns();
    }
  }, [username]);

  const fetchCampaigns = async () => {
    await getAllCampaigns()
      .then((response) => {
        setUserCampaigns(response.data);
      })
      .catch((error) => {
        console.error("Error fetching campaigns:", error);
      });
  };

  const renderCampaignsList = () => (
    <div className="campaigns-list">
      {" "}
      {userCampaigns.map((campaign) => (
        <div
          key={campaign.id}
          className="campaign"
          onClick={() => handleCampaignClick(campaign)}
        >
          {" "}
          <span>{campaign.name}</span>
          <div>
            <button
              onClick={(e) => {
                e.stopPropagation();
                downloadTurtle(campaign, e);
              }}
              className="gear-icon-button"
            >
              <FontAwesomeIcon icon={faDownload} />
            </button>
            {campaign.creator.email === username && (
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  openModal(campaign, e);
                }}
                className="gear-icon-button"
              >
                <FontAwesomeIcon icon={faGear} />
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  );

  const handleCampaignClick = (campaign) => {
    if (!campaign) {
      console.error("Attempted to select an undefined campaign");
      return;
    }
    setActiveCampaign(campaign);
    onCampaignSelect(campaign);
  };

  const handleThesaurusChange = (thesaurusName) => {
    setSelectedThesauri((prevSelected) => {
      if (prevSelected.includes(thesaurusName)) {
        return prevSelected.filter((name) => name !== thesaurusName);
      } else {
        return [...prevSelected, thesaurusName];
      }
    });
  };

  const renderThesaurusOptions = () => (
    <div className="thesaurus-dropdown">
      {thesaurusData.map((thesaurus, index) => (
        <label key={index}>
          <input
            type="checkbox"
            checked={selectedThesauri.includes(thesaurus.name)}
            onChange={() => handleThesaurusChange(thesaurus.name)}
          />
          {thesaurus.name}
        </label>
      ))}
    </div>
  );

  const renderOptionsView = () => (
    <div className="campaign-options">
      <button onClick={() => setView("create")} className="campaign-button">
        <FontAwesomeIcon icon={faPlusCircle} /> Create Campaign
      </button>
      {renderCampaignsList()}
    </div>
  );

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!campaignName || selectedThesauri.length === 0) {
      alert("Please fill in all fields and select at least one thesaurus.");
      return;
    }

    const campaignData = {
      name: campaignName,
      selectedThesauri: selectedThesauri,
    };

    await createCampaign(campaignData)
      .then((response) => {
        setUserCampaigns([...userCampaigns, response.data]);
        alert("Campaign created successfully!");

        setCampaignName("");
        setSelectedThesauri([]);
        setView("options");
      })
      .catch((error) => {
        console.error("Error adding campaign:", error);
        alert(error.response.data);
      });
  };

  const renderCreateView = () => (
    <div className="create-campaign">
      <div className="back-arrow" onClick={() => setView("options")}>
        ←
      </div>
      <form onSubmit={handleSubmit}>
        <label className="campaign-name-label">Campaign Name:</label>
        <input
          type="text"
          className="campaign-input"
          placeholder="Campaign Name"
          value={campaignName}
          onChange={(e) => setCampaignName(e.target.value)}
        />
        <br />
        <label className="thesaurus-label">Thesaurus:</label>
        {renderThesaurusOptions()}
        <button type="submit" className="campaign-button">
          Create Campaign
        </button>
      </form>
    </div>
  );

  const handleLogout = () => {
    onLogout();
  };

  const downloadTurtle = async (campaign, e) => {
    e.stopPropagation();

    await exportCampaign(campaign.id)
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `campaign-${campaign.name}.ttl`);
        document.body.appendChild(link);
        link.click();
        link.remove();
      })
      .catch((error) => {
        console.error("Error exporting campaign:", error);
        alert(error.response.data);
      });
  };

  const openModal = (campaign, e) => {
    if (showModal && selectedCampaign?.id === campaign.id) {
      closeModal();
    } else {
      const modalPosition = { top: e.clientY, left: e.clientX };
      setModalStyle(modalPosition);
      setSelectedCampaign(campaign);
      setShowModal(true);
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedCampaign(null);
  };

  const handleAddUserSubmit = async (e) => {
    e.preventDefault();

    await inviteParticipant(selectedCampaign.id, emailToAdd)
      .then(() => {
        alert(`${emailToAdd} added and invitation sent successfully.`);
        closeModal();
        fetchCampaigns();
      })
      .catch((error) => {
        console.error("Error adding user or sending invitation:", error);
        alert(error.response.data);
      });
  };

  const handleEmailChange = (e) => {
    setEmailToAdd(e.target.value);
  };

  return (
    <div className="campaign-container">
      {" "}
      <div style={{ position: "absolute", top: 0, left: 0 }}>
        <button onClick={handleLogout} className="logout-button">
          <FontAwesomeIcon icon={faSignOutAlt} /> Logout
        </button>
      </div>
      {view === "options" && renderOptionsView()}
      {view === "create" && renderCreateView()}
      {showModal && (
        <Modal onClose={closeModal} style={modalStyle}>
          <h4>Campaign name: {selectedCampaign?.name}</h4>
          <form onSubmit={handleAddUserSubmit}>
            <label>
              Email to add:
              <input
                type="email"
                className="campaign-input"
                value={emailToAdd}
                onChange={handleEmailChange}
                required
              />
            </label>
            <button type="submit" className="campaign-button">
              Add User
            </button>
          </form>
        </Modal>
      )}
    </div>
  );
};

export default CampaignOptions;
