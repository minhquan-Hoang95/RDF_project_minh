import axiosInstance from "../scripts/axiosInterceptor";

export const signup = (email, password) => {
  return axiosInstance.post("/auth", {
    email: email,
    password: password,
    firstName: "TODO",
    lastName: "TODO",
  });
};

export const login = (email, password) => {
  return axiosInstance.get("/auth", {
    params: { email, password },
  });
};

export const requestPasswordReset = (email) => {
  return axiosInstance.patch("/auth", null, {
    params: { email },
  });
};

export const setNewPassword = (email, code, password) => {
  return axiosInstance.put("/auth", null, {
    params: { email, code, password },
  });
};
