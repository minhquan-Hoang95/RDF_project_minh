import axios from "axios";

const interceptRequest = async (request) => {
  const token = localStorage.getItem("token");

  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }

  return request;
};

const interceptSuccessResponse = (response) => {
  return response;
};

let logoutHandler = null;

export const setLogoutHandler = (handler) => {
  logoutHandler = handler;
};

const interceptorErrorResponse = (error) => {
  if (
    error.code === "ERR_NETWORK" ||
    error.response?.data === "Invalid bearer token."
  ) {
    if (logoutHandler) {
      alert("Your session has expired. Please log in again.");
      logoutHandler();
      return;
    }
  }

  return Promise.reject(error);
};

const axiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_URL,
  headers: {
    "Content-type": "application/json",
  },
});

axiosInstance.interceptors.request.use(interceptRequest);
axiosInstance.interceptors.response.use(
  interceptSuccessResponse,
  interceptorErrorResponse
);

export default axiosInstance;
