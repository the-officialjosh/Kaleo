import {Calendar, ScanLine, Share2, Ticket} from 'lucide-react';

const steps = [
  {
    number: 1,
    icon: Calendar,
    title: "Create your event",
    description: "Set up your event details, venue, and schedule in minutes."
  },
  {
    number: 2,
    icon: Ticket,
    title: "Publish tickets",
    description: "Define ticket types, pricing, and capacity limits."
  },
  {
    number: 3,
    icon: Share2,
    title: "Share your link",
    description: "Get a unique event page to share with your audience."
  },
  {
    number: 4,
    icon: ScanLine,
    title: "Scan and manage",
    description: "Check in attendees with QR codes and track in real-time."
  }
];

export default function HowItWorks() {
  return (
    <section className="how-it-works-section">
      <div className="how-it-works-container">
        <div className="how-it-works-header">
          <h2 className="how-it-works-title">How It Works</h2>
          <p className="how-it-works-subtitle">Four simple steps to event success</p>
        </div>

        <div className="how-it-works-steps">
          {steps.map((step, index) => (
            <div key={index} className="how-it-works-step">
              <div className="step-number">{step.number}</div>
              <div className="step-icon">
                <step.icon size={32} />
              </div>
              <h3 className="step-title">{step.title}</h3>
              <p className="step-desc">{step.description}</p>
              {index < steps.length - 1 && <div className="step-connector" />}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
