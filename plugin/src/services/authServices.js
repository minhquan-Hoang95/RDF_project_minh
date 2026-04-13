import axiosInstance from "../scripts/axiosInterceptor";

/**
 * Login with email and password (POST to /auth/login)
 * @param {string} email - User email
 * @param {string} password - User password
 * @returns {Promise<AxiosResponse>}
 */
export const login = (email, password) => {
  // Client-side validation
  if (!email || !password) {
    return Promise.reject(new Error("Email and password are required"));
  }

  if (!email.includes("@")) {
    return Promise.reject(new Error("Invalid email format"));
  }

  if (password.length < 8) {
    return Promise.reject(new Error("Password must be at least 8 characters"));
  }

  // Send as POST with body (not query params)
  return axiosInstance.post("/auth/login", {
    email: email,
    password: password,
  });
};

/**
 * Signup new user
 * @param {string} email - User email
 * @param {string} password - User password
 * @param {string} firstName - First name
 * @param {string} lastName - Last name
 * @returns {Promise<AxiosResponse>}
 */
export const signup = (email, password, firstName, lastName) => {
  // Client-side validation
  if (!email || !password || !firstName || !lastName) {
    return Promise.reject(new Error("All fields are required"));
  }

  if (!email.includes("@")) {
    return Promise.reject(new Error("Invalid email format"));
  }

  if (firstName.length < 2) {
    return Promise.reject(new Error("First name must be at least 2 characters"));
  }

  if (lastName.length < 2) {
    return Promise.reject(new Error("Last name must be at least 2 characters"));
  }

  if (password.length < 8) {
    return Promise.reject(new Error("Password must be at least 8 characters"));
  }

  return axiosInstance.post("/auth", {
    email: email,
    password: password,
    firstName: firstName,
    lastName: lastName,
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
