import React from 'react';
import zxcvbn from 'zxcvbn';

const PasswordStrengthMeter = ({ password }) => {
  const testResult = zxcvbn(password);
  // Der zxcvbn-Wert liegt zwischen 0 (am schlechtesten) und 4 (am besten).
  const score = testResult.score;

  const getBarColor = () => {
    switch (score) {
      case 0: return '#e74c3c'; // rot
      case 1: return '#e67e22'; // Orange
      case 2: return '#f1c40f'; // Gelb
      case 3: return '#2ecc71'; // Grün
      case 4: return '#27ae60'; // Dunkel Grün
      default: return 'grey';
    }
  };

  const getStrengthLabel = () => {
    switch(score) {
        case 0: return 'Sehr schwach';
        case 1: return 'Schwach';
        case 2: return 'Mittel';
        case 3: return 'Stark';
        case 4: return 'Sehr stark';
        default: return '';
    }
  }

  const barWidth = password ? `${(score + 1) * 20}%` : '0%';

  return (
    <div className="strength-meter-container">
      <div className="strength-meter-bar" style={{ width: barWidth, backgroundColor: getBarColor() }}></div>
      <p className="strength-meter-label" style={{ color: getBarColor() }}>
        {password && getStrengthLabel()}
      </p>
    </div>
  );
};

export default PasswordStrengthMeter;
