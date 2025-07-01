import React, { useState } from 'react';
import axios from 'axios';
import './App.css';
import Dashboard from './Dashboard';
import logo from './schlusselperson.png';
import { Toaster } from 'react-hot-toast';

// Login
function LoginForm({ onLoginSuccess, onSwitchToRegister }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password
      });
      onLoginSuccess(response.data.token);
    } catch (err) {
      setError('Anmeldung fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.');
    }
  };

  return (
    <div className="form-container">
      <h2>Anmelden</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" placeholder="Benutzername" value={username} onChange={(e) => setUsername(e.target.value)} required />
        <input type="password" placeholder="Passwort" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <button type="submit">Anmelden</button>
        {error && <p className="error-message">{error}</p>}
      </form>
      <button onClick={onSwitchToRegister} className="link-button">
        Noch kein Konto?
        Jetzt registrieren.
      </button>
    </div>
  );
}

// RegisterForm
function RegisterForm({ onSwitchToLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    setMessage('');
    try {
      await axios.post('http://localhost:8080/api/auth/register', {
        username,
        password
      });
      setMessage('Registrierung erfolgreich! Sie können sich jetzt anmelden.');
    } catch (err) {
      setError('Registrierung fehlgeschlagen. Benutzername eventuell vergeben.');
    }
  };

  return (
    <div className="form-container">
      <h2>Registrieren</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" placeholder="Benutzername" value={username} onChange={(e) => setUsername(e.target.value)} required />
        <input type="password" placeholder="Neues Passwort" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <button type="submit">Konto Erstellen</button>
        {error && <p className="error-message">{error}</p>}
        {message && <p className="success-message">{message}</p>}
      </form>
      <button onClick={onSwitchToLogin} className="link-button">
        Bereits ein Konto? Hier anmelden.
      </button>
    </div>
  );
}


// main APP
function App() {
  const [token, setToken] = useState(null);
  const [view, setView] = useState('login'); // 'login' oder 'register'

  const handleLogout = () => {
    setToken(null);
    setView('login'); // zurück main Fenster
  };

  // Show Dashboard, wenn token er hat
  if (token) {
    return (
        <div className="App">
            <Toaster position="top-right" />

            <header className="App-header">
            <img src={logo} className="auth-logo" alt="Passwort-Manager Logo" />
                <h1><strong>Passwort-Manager</strong></h1>
                <Dashboard token={token} />
                <button onClick={handleLogout} className="logout-button">Ausloggen</button>
            </header>
        </div>
    );
  }

  //wenn er nicht token hat,, zeigt Login oder Register form
  return (
    <div className="App">
      <header className="App-header">
      <img src={logo} className="auth-logo" alt="Passwort-Manager Logo" />
        <h1><strong>Passwort-Manager</strong></h1>
        {view === 'login' ? (
          <LoginForm
            onLoginSuccess={setToken}
            onSwitchToRegister={() => setView('register')}
          />
        ) : (
          <RegisterForm
            onSwitchToLogin={() => setView('login')}
          />
        )}
      </header>
    </div>
  );
}

export default App;


