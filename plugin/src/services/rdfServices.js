import axiosInstance from "../scripts/axiosInterceptor";

export const createCampaign = (data) => {
  return axiosInstance.post("/campaign", data);
};

export const getCampaignById = (id) => {
  return axiosInstance.get("/campaign", {
    params: { id },
  });
};

export const getAllCampaigns = () => {
  return axiosInstance.get("/campaign/all");
};

export const inviteParticipant = (campaignId, participantEmail) => {
  return axiosInstance.post("/campaign/invite", null, {
    params: { campaignId, participantEmail },
  });
};

export const exportCampaign = (id) => {
  return axiosInstance.get("/campaign/export", {
    params: { id },
    responseType: "blob",
  });
};

export const createAnnotation = (annotation) => {
  return axiosInstance.post("/annotation", annotation, {});
};

export const updateAnnotation = (id, name) => {
  return axiosInstance.patch("/annotation", null, {
    params: { id, name },
  });
};
