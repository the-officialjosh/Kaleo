import {
    Activity,
    BarChart3,
    Calendar,
    CreditCard,
    Mail,
    QrCode,
    Smartphone,
    Ticket,
    Users,
    Wallet,
    Zap
} from 'lucide-react';

const organizerFeatures = [
  {
    category: "Event Management",
    items: [
      { icon: Calendar, text: "Event creation and publishing" },
      { icon: Ticket, text: "Ticket types and capacity control" },
      { icon: QrCode, text: "QR-coded passes" }
    ]
  },
  {
    category: "Check-in & Analytics",
    items: [
      { icon: Activity, text: "Real-time check-in" },
      { icon: BarChart3, text: "Analytics and attendance tracking" },
      { icon: Users, text: "Team roles (staff, organizers)" }
    ]
  }
];

const attendeeFeatures = [
  { icon: CreditCard, text: "Simple ticket purchase" },
  { icon: Smartphone, text: "Mobile-friendly passes" },
  { icon: Zap, text: "Fast check-in" },
  { icon: Mail, text: "Email and wallet delivery" },
  { icon: Wallet, text: "No account friction" }
];

export function OrganizerFeatures() {
  return (
    <section className="organizer-features-section">
      <div className="features-container">
        <div className="features-header">
          <h2 className="features-title">Built for Organizers</h2>
          <p className="features-subtitle">Powerful tools without the complexity</p>
        </div>

        <div className="features-grid">
          {organizerFeatures.map((group, groupIndex) => (
            <div key={groupIndex} className="feature-group">
              <h3 className="feature-group-title">{group.category}</h3>
              <div className="feature-items">
                {group.items.map((item, itemIndex) => (
                  <div key={itemIndex} className="feature-item">
                    <div className="feature-item-icon">
                      <item.icon size={20} />
                    </div>
                    <span>{item.text}</span>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export function AttendeeFeatures() {
  return (
    <section className="attendee-features-section">
      <div className="features-container">
        <div className="features-header">
          <h2 className="features-title">Loved by Attendees</h2>
          <p className="features-subtitle">A seamless experience from purchase to entry</p>
        </div>

        <div className="attendee-features-list">
          {attendeeFeatures.map((feature, index) => (
            <div key={index} className="attendee-feature-item">
              <div className="attendee-feature-icon">
                <feature.icon size={24} />
              </div>
              <span>{feature.text}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
