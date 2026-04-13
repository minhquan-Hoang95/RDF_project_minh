import { useState } from "react";
import "../styles/Authentification.css";
import {
  signup,
  login,
  requestPasswordReset,
  setNewPassword,
} from "../services/authServices";

function Authentification({ onUsernameSubmit }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [code, setCode] = useState("");
  // 0: login, 1: signup, 2: reset password
  const [state, setState] = useState(0);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  };

  const handleLogin = async () => {
    setError("");

    // Validation
    if (!email || !password) {
      setError("Email and password are required");
      return;
    }

    if (!validateEmail(email)) {
      setError("Invalid email format");
      return;
    }

    if (password.length < 8) {
      setError("Password must be at least 8 characters");
      return;
    }

    setLoading(true);
    try {
      const response = await login(email, password);
      localStorage.setItem("token", response.data.token);
      setError("");
      onUsernameSubmit(email);
    } catch (error) {
      console.error("Login error:", error);
      setError(
        error.response?.data?.message ||
          error.message ||
          "Login failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleSignup = async () => {
    setError("");

    // Validation
    if (!email || !password || !firstName || !lastName) {
      setError("All fields are required");
      return;
    }

    if (!validateEmail(email)) {
      setError("Invalid email format");
      return;
    }

    if (firstName.length < 2) {
      setError("First name must be at least 2 characters");
      return;
    }

    if (lastName.length < 2) {
      setError("Last name must be at least 2 characters");
      return;
    }

    if (password.length < 8) {
      setError("Password must be at least 8 characters");
      return;
    }

    setLoading(true);
    try {
      await signup(email, password, firstName, lastName);
      alert("✅ Account created! Check your email for activation link.");
      setError("");
      setState(0);
      setEmail("");
      setPassword("");
      setFirstName("");
      setLastName("");
    } catch (error) {
      console.error("Signup error:", error);
      setError(
        error.response?.data?.message ||
          error.message ||
          "Signup failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  const handlePasswordReset = async () => {
    setError("");

    if (!email) {
      setError("Email is required");
      return;
    }

    if (!validateEmail(email)) {
      setError("Invalid email format");
      return;
    }

    setLoading(true);
    try {
      await requestPasswordReset(email);
      alert("✅ Check your email for password reset instructions");
      setError("");
      setState(0);
    } catch (error) {
      console.error("Password reset error:", error);
      setError(
        error.response?.data || error.message || "Failed to send reset email"
      );
    } finally {
      setLoading(false);
    }
  };

  const handleNewPassword = async () => {
    setError("");

    if (!email || !code || !password) {
      setError("All fields are required");
      return;
    }

    if (password.length < 8) {
      setError("Password must be at least 8 characters");
      return;
    }

    setLoading(true);
    try {
      await setNewPassword(email, code, password);
      alert("✅ Password updated successfully!");
      setError("");
      setState(0);
      setCode("");
    } catch (error) {
      console.error("Password update error:", error);
      setError(
        error.response?.data || error.message || "Failed to update password"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="configuration-container">
      <form className="configuration-form">
        <h2>{state === 0 ? "🔐 Login" : state === 1 ? "✨ Sign Up" : "🔑 Reset Password"}</h2>

        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            disabled={loading}
            placeholder="your@email.com"
          />
        </div>

        {state === 1 && (
          <>
            <div className="form-group">
              <label>First Name:</label>
              <input
                type="text"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                required
                disabled={loading}
                placeholder="John"
              />
            </div>

            <div className="form-group">
              <label>Last Name:</label>
              <input
                type="text"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                required
                disabled={loading}
                placeholder="Doe"
              />
            </div>
          </>
        )}

        {state !== 2 && (
          <div className="form-group">
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required={state !== 2}
              disabled={loading}
              placeholder="Minimum 8 characters"
            />
          </div>
        )}

        {state === 2 && (
          <div className="form-group">
            <label>Verification Code:</label>
            <input
              type="text"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              required
              disabled={loading}
              placeholder="Code from email"
            />
          </div>
        )}

        {error && <p className="error-message">❌ {error}</p>}

        {state === 0 && (
          <button
            type="button"
            onClick={handleLogin}
            className="auth-button"
            disabled={loading}
          >
            {loading ? "⏳ Logging in..." : "🔐 Login"}
          </button>
        )}

        {state === 1 && (
          <button
            type="button"
            onClick={handleSignup}
            className="auth-button"
            disabled={loading}
          >
            {loading ? "⏳ Creating account..." : "✨ Sign Up"}
          </button>
        )}

        {state === 2 && (
          <>
            <button
              type="button"
              onClick={handleNewPassword}
              className="auth-button"
              disabled={loading}
            >
              {loading ? "⏳ Updating..." : "🔑 Update Password"}
            </button>

            <button
              type="button"
              onClick={handlePasswordReset}
              className="reset-button"
              disabled={loading}
            >
              {loading ? "⏳ Sending..." : "📧 Resend Reset Link"}
            </button>
          </>
        )}

        {state !== 2 && (
          <>
            <button
              type="button"
              onClick={() => {
                setState(state === 0 ? 1 : 0);
                setError("");
              }}
              className="toggle-button"
              disabled={loading}
            >
              {state === 0 ? "Need an account? Sign Up" : "Have an account? Login"}
            </button>

            <button
              type="button"
              onClick={() => {
                setState(2);
                setError("");
              }}
              className="toggle-button"
              disabled={loading}
            >
              Forgot password?
            </button>
          </>
        )}

        {state === 2 && (
          <button
            type="button"
            onClick={() => {
              setState(0);
              setCode("");
              setError("");
            }}
            className="toggle-button"
            disabled={loading}
          >
            Back to Login
          </button>
        )}
      </form>
    </div>
  );
}

export default Authentification;
