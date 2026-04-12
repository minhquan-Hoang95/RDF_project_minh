import { useState } from "react";
import "../styles/Authentification.css";
import {
  signup,
  login,
  requestPasswordReset,
  setNewPassword,
} from "../services/authServices";

function Authentification({ onUsernameSubmit }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [code, setCode] = useState("");
  // 0 login, 1 signup, 2 new password
  const [state, setState] = useState(0);
  const [error, setError] = useState("");

  const handleLogin = async () => {
    await login(username, password)
      .then((response) => {
        localStorage.setItem("token", response.data.token);
        onUsernameSubmit(username);
      })
      .catch((error) => {
        setError(error.response.data);
      });
  };

  const handleSignup = async () => {
    await signup(username, password)
      .then(() => {
        alert("Activate your account via the link sent to your email.");
        setError("");
        setState(0);
      })
      .catch((error) => {
        setError(error.response.data);
      });
  };

  const handlePasswordReset = async () => {
    if (!username) {
      setError("Please enter your email address to reset your password.");
      return;
    }

    await requestPasswordReset(username)
      .then(() => {
        alert("Password reset email sent. Please check your inbox.");
        setError("");
      })
      .catch((error) => {
        setError(error.response.data);
      });
  };

  const handleNewPassword = async () => {
    if (!username || !code || !password) {
      setError("Please provide all required fields.");
      return;
    }

    await setNewPassword(username, code, password)
      .then(() => {
        alert("Your password has been successfully updated.");
        setError("");
        setState(0);
      })
      .catch((error) => {
        setError(error.response.data);
      });
  };

  return (
    <div className="configuration-container">
      <form className="configuration-form">
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        {state === 2 && (
          <div className="form-group">
            <label>Code:</label>
            <input
              type="text"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              required
            />
          </div>
        )}
        {error && <p className="error-message">{error}</p>}

        {state === 0 && (
          <button type="button" onClick={handleLogin} className="auth-button">
            Login
          </button>
        )}

        {state === 1 && (
          <button type="button" onClick={handleSignup} className="auth-button">
            Sign Up
          </button>
        )}

        {(state === 0 || state === 1) && (
          <button
            type="button"
            onClick={() => setState(state === 0 ? 1 : 0)}
            className="toggle-button"
          >
            Switch to {state === 0 ? "Sign Up" : "Login"}
          </button>
        )}
        {(state === 0 || state === 1) && (
          <button
            type="button"
            onClick={() => setState(2)}
            className="toggle-button"
          >
            New Password
          </button>
        )}

        {state === 2 && (
          <button
            type="button"
            onClick={handleNewPassword}
            className="auth-button"
          >
            New Password
          </button>
        )}
        {state === 2 && (
          <button
            type="button"
            onClick={handlePasswordReset}
            className="reset-button"
          >
            Forgot Password?
          </button>
        )}
        {state === 2 && (
          <button
            type="button"
            onClick={() => setState(0)}
            className="toggle-button"
          >
            Switch to Login
          </button>
        )}
      </form>
    </div>
  );
}

export default Authentification;
