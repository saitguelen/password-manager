import React, { useState, useEffect } from 'react';
import axios from 'axios';
import toast from 'react-hot-toast';
import PasswordStrengthMeter from './PasswordStrengthMeter';

function Dashboard({ token }) {
  // Zustände für die Liste, Laden und Fehler
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);

  // Zustände für das Formular
  const [website, setWebsite] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  // Zustand, um zu wissen, ob wir einen Eintrag bearbeiten oder erstellen
  const [editingId, setEditingId] = useState(null);

  // Holt die Passwörter vom Server, wenn die Komponente geladen wird.
  useEffect(() => {
    const fetchEntries = async () => {
        if (!token) { setLoading(false); return; }
        try {
            setLoading(true);
            const response = await axios.get('http://localhost:8080/api/passwords', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            setEntries(response.data);
        } catch (err) {
            toast.error('Passwörter konnten nicht geladen werden.');
        } finally {
            setLoading(false);
        }
    };
    fetchEntries();
  }, [token]); // Dieser Effekt wird nur ausgeführt, wenn sich das Token ändert.

  // Hauptfunktion, die beim Absenden des Formulars aufgerufen wird.
  const handleFormSubmit = async (event) => {
    event.preventDefault();

    // Wir entscheiden, ob wir die Erstellungs- oder die Update-Funktion aufrufen.
    const promise = editingId
        ? handleUpdateEntry()
        : handleCreateEntry();

    // toast.promise kümmert sich um die Benachrichtigungen für alle Zustände der Promise.
    toast.promise(promise, {
        loading: 'Speichern...',
        success: editingId ? 'Eintrag erfolgreich aktualisiert!' : 'Eintrag erfolgreich erstellt!',
        error: editingId ? 'Fehler beim Aktualisieren.' : 'Fehler beim Erstellen.',
    });
  };

  // Erstellt einen neuen Passworteintrag.
  const handleCreateEntry = async () => {
    const response = await axios.post(
      'http://localhost:8080/api/passwords',
      { website, username, encryptedPassword: password },
      { headers: { 'Authorization': `Bearer ${token}` } }
    );
    setEntries([...entries, response.data]);
    resetForm();
  };

  // Aktualisiert einen vorhandenen Passworteintrag.
  const handleUpdateEntry = async () => {
    const response = await axios.put(
        `http://localhost:8080/api/passwords/${editingId}`,
        { website, username, encryptedPassword: password },
        { headers: { 'Authorization': `Bearer ${token}` } }
    );
    // Aktualisiert die Liste, indem der alte Eintrag durch den neuen ersetzt wird.
    setEntries(entries.map(entry => entry.id === editingId ? response.data : entry));
    resetForm();
  };

  // Löscht einen Passworteintrag.
  const handleDeleteEntry = (id) => {
    // Bestätigungsdialog anzeigen, bevor gelöscht wird.
    if (window.confirm('Diesen Eintrag wirklich löschen?')) {
        const promise = axios.delete(`http://localhost:8080/api/passwords/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        }).then(() => {
            // Aktualisiert die Liste, indem der gelöschte Eintrag entfernt wird.
            setEntries(entries.filter(entry => entry.id !== id));
        });

        toast.promise(promise, {
            loading: 'Löschen...',
            success: 'Eintrag erfolgreich gelöscht!',
            error: 'Fehler beim Löschen.',
        });
    }
  };

  // Füllt das Formular aus, wenn auf "Bearbeiten" geklickt wird.
  const handleEditClick = (entry) => {
    setEditingId(entry.id);
    setWebsite(entry.website);
    setUsername(entry.username);
    setPassword(entry.encryptedPassword);
    window.scrollTo(0, 0); // Scrollt die Seite nach oben zum Formular.
  };

  // Kopiert das Passwort in die Zwischenablage.
  const handleCopyPassword = (passwordToCopy) => {
      navigator.clipboard.writeText(passwordToCopy)
        .then(() => toast.success('Passwort in die Zwischenablage kopiert!'))
        .catch(() => toast.error('Kopieren fehlgeschlagen.'));
  };

  // Setzt das Formular und den Bearbeitungsmodus zurück.
  const resetForm = () => {
    setWebsite(''); setUsername(''); setPassword(''); setEditingId(null);
  };

  return (
    <div className="dashboard">
      <h2>Ihr Passwort-Dashboard</h2>

      <div className="form-container">
        <h3>{editingId ? 'Eintrag Bearbeiten' : 'Neuen Eintrag hinzufügen'}</h3>
        <form onSubmit={handleFormSubmit}>
          <input type="text" placeholder="Webseite" value={website} onChange={(e) => setWebsite(e.target.value)} required />
          <input type="text" placeholder="Benutzername" value={username} onChange={(e) => setUsername(e.target.value)} required />
          <input type="password" placeholder="Passwort" value={password} onChange={(e) => setPassword(e.target.value)} required />
          <PasswordStrengthMeter password={password} />
          <button type="submit">{editingId ? 'Aktualisieren' : 'Erstellen'}</button>
          {editingId && <button type="button" onClick={resetForm} style={{marginLeft: '10px'}}>Abbrechen</button>}
        </form>
      </div>

      <h3>Ihre Gespeicherten Passwörter</h3>
      {loading ? (<p>Lade Passwörter...</p>) : (
        <>
          {entries.length === 0 ? (<p>Noch keine Einträge vorhanden.</p>) : (
            <table>
              <thead>
                <tr>
                  <th>Webseite</th>
                  <th>Benutzername</th>
                  <th>Passwort</th>
                  <th>Aktionen</th>
                </tr>
              </thead>
              <tbody>
                {entries.map(entry => (
                  <tr key={entry.id}>
                    <td>{entry.website}</td>
                    <td>{entry.username}</td>
                    <td>{entry.encryptedPassword}</td>
                    <td>
                      <button onClick={() => handleEditClick(entry)}>Bearbeiten</button>
                      <button onClick={() => handleCopyPassword(entry.encryptedPassword)} style={{marginLeft: '5px'}}>Kopieren</button>
                      <button onClick={() => handleDeleteEntry(entry.id)} style={{marginLeft: '5px'}}>Löschen</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}
    </div>
  );
}

export default Dashboard;
