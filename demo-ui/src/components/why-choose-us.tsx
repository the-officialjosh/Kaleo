import {Clock, LayoutDashboard, Shield, Zap} from 'lucide-react';

const features = [
  {
    icon: Zap,
    title: "Simple registration",
    description: "Set up church events and programs in minutes. No technical skills required."
  },
  {
    icon: Shield,
    title: "Secure check-in",
    description: "QR validation ensures each member is checked in once. No duplicates."
  },
  {
    icon: Clock,
    title: "Save time",
    description: "Automate registrations and spend more time on ministry, not admin."
  },
  {
    icon: LayoutDashboard,
    title: "One dashboard",
    description: "Manage all your church programs, ministries, and events from one place."
  }
];

export default function WhyChooseUs() {
  return (
    <section className="why-choose-section">
      <div className="why-choose-container">
        <div className="why-choose-header">
          <h2 className="why-choose-title">Built for Churches</h2>
          <p className="why-choose-subtitle">Everything you need to manage your ministry</p>
        </div>

        <div className="why-choose-grid">
          {features.map((feature, index) => (
            <div key={index} className="why-choose-card">
              <div className="why-choose-icon">
                <feature.icon size={28} />
              </div>
              <h3 className="why-choose-card-title">{feature.title}</h3>
              <p className="why-choose-card-desc">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
