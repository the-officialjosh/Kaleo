import {Button} from "@/components/ui/button";
import {useAuth} from "react-oidc-context";
import {useNavigate} from "react-router";
import {ArrowRight, Calendar, QrCode, Sparkles, Users} from "lucide-react";

const OrganizersLandingPage: React.FC = () => {
  const { isAuthenticated, isLoading, signinRedirect, signoutRedirect } =
    useAuth();

  const navigate = useNavigate();

  if (isLoading) {
    return (
      <div className="min-h-screen bg-black flex items-center justify-center">
        <div className="animate-pulse text-white text-xl">Loading...</div>
      </div>
    );
  }

  const features = [
    {
      icon: Calendar,
      title: "Easy Program Creation",
      description: "Set up events, manage schedules, and customize pass types in minutes"
    },
    {
      icon: Users,
      title: "Attendee Management",
      description: "Track registrations, send updates, and manage your community"
    },
    {
      icon: QrCode,
      title: "QR Code Validation",
      description: "Instant check-in with secure QR codes for seamless entry"
    }
  ];

  return (
    <div className="organizers-page">
      {/* Hero Section */}
      <section className="organizers-hero">
        {/* Background */}
        <div className="organizers-hero-bg" />
        <div className="organizers-hero-overlay" />

        {/* Navigation */}
        <nav className="hero-nav">
          <div className="hero-nav-brand">
            <Sparkles className="w-6 h-6" />
            <span>Kaleo</span>
          </div>
          <div className="hero-nav-actions">
            {isAuthenticated ? (
              <>
                <Button
                  variant="ghost"
                  onClick={() => navigate("/dashboard/programs")}
                  className="nav-btn"
                >
                  Dashboard
                </Button>
                <Button
                  variant="ghost"
                  onClick={() => signoutRedirect()}
                  className="nav-btn"
                >
                  Log out
                </Button>
              </>
            ) : (
              <Button onClick={() => signinRedirect()} className="nav-btn-primary">
                Sign In
              </Button>
            )}
          </div>
        </nav>

        {/* Hero Content - Centered */}
        <div className="organizers-hero-content">
          <div className="hero-badge">
            <Sparkles className="w-4 h-4" />
            <span>For Organizers</span>
          </div>
          
          <h1 className="organizers-hero-title">
            Create Memorable
            <span className="organizers-title-accent"> Experiences</span>
          </h1>
          
          <p className="organizers-hero-subtitle">
            The complete platform to create programs, sell passes, and validate 
            attendees with QR codes. Everything you need to run successful events.
          </p>

          <div className="organizers-hero-actions">
            <Button 
              className="cta-primary"
              onClick={() => signinRedirect()}
            >
              Start Creating
              <ArrowRight className="w-5 h-5 ml-2" />
            </Button>
            <Button 
              variant="ghost" 
              className="cta-secondary"
              onClick={() => navigate("/")}
            >
              Browse Programs
            </Button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="features-container">
          <div className="features-header">
            <h2 className="features-title">Everything You Need</h2>
            <p className="features-subtitle">
              Powerful tools to manage your events from start to finish
            </p>
          </div>

          <div className="features-grid">
            {features.map((feature, index) => (
              <div key={index} className="feature-card">
                <div className="feature-icon">
                  <feature.icon className="w-6 h-6" />
                </div>
                <h3 className="feature-title">{feature.title}</h3>
                <p className="feature-description">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="cta-container">
          <h2 className="cta-title">Ready to get started?</h2>
          <p className="cta-subtitle">
            Join thousands of organizers creating amazing experiences
          </p>
          <Button 
            className="cta-primary large"
            onClick={() => signinRedirect()}
          >
            Create Your First Program
            <ArrowRight className="w-5 h-5 ml-2" />
          </Button>
        </div>
      </section>
    </div>
  );
};

export default OrganizersLandingPage;
