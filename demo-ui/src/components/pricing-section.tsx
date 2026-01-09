import {Check, Sparkles} from 'lucide-react';
import {Button} from './ui/button';

export default function PricingSection() {
  return (
    <section className="pricing-section">
      <div className="pricing-container">
        <div className="pricing-header">
          <h2 className="pricing-title">Free for Churches</h2>
          <p className="pricing-subtitle">No hidden fees. Built to serve your ministry.</p>
        </div>

        <div className="pricing-cards">
          <div className="pricing-card pricing-card-free">
            <div className="pricing-card-header">
              <h3 className="pricing-plan-name">Ministry Starter</h3>
              <div className="pricing-amount">
                <span className="price">Free</span>
                <span className="period">forever</span>
              </div>
            </div>
            <ul className="pricing-features">
              <li><Check size={16} /> Unlimited programs</li>
              <li><Check size={16} /> QR check-in included</li>
              <li><Check size={16} /> Member management</li>
              <li><Check size={16} /> Email notifications</li>
            </ul>
            <Button className="pricing-cta">Get Started</Button>
          </div>

          <div className="pricing-card pricing-card-pro">
            <div className="pricing-badge">
              <Sparkles size={14} />
              <span>For Growing Churches</span>
            </div>
            <div className="pricing-card-header">
              <h3 className="pricing-plan-name">Ministry Pro</h3>
              <div className="pricing-amount">
                <span className="price">$29</span>
                <span className="period">/month</span>
              </div>
            </div>
            <ul className="pricing-features">
              <li><Check size={16} /> Everything in Free</li>
              <li><Check size={16} /> Advanced analytics</li>
              <li><Check size={16} /> Team management</li>
              <li><Check size={16} /> Custom branding</li>
              <li><Check size={16} /> Priority support</li>
            </ul>
            <Button className="pricing-cta pricing-cta-primary">Start Free Trial</Button>
          </div>
        </div>

        <p className="pricing-note">
          Free for most churches. No credit card required.
        </p>
      </div>
    </section>
  );
}
