import {Lock, QrCode, Shield, UserCog} from 'lucide-react';

const securityFeatures = [
  {
    icon: Shield,
    title: "Secure Payments",
    description: "PCI-compliant payment processing for safe transactions."
  },
  {
    icon: QrCode,
    title: "Anti-Duplicate QR",
    description: "Each ticket can only be scanned once. No duplicates, no fraud."
  },
  {
    icon: UserCog,
    title: "Role-Based Access",
    description: "Staff vs organizer permissions for controlled access."
  },
  {
    icon: Lock,
    title: "Data Protection",
    description: "Your data is encrypted and never shared with third parties."
  }
];

export default function SecuritySection() {
  return (
    <section className="security-section">
      <div className="security-container">
        <div className="security-header">
          <h2 className="security-title">Security & Reliability</h2>
          <p className="security-subtitle">Trust is our foundation</p>
        </div>

        <div className="security-grid">
          {securityFeatures.map((feature, index) => (
            <div key={index} className="security-card">
              <div className="security-icon">
                <feature.icon size={24} />
              </div>
              <h3 className="security-card-title">{feature.title}</h3>
              <p className="security-card-desc">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
