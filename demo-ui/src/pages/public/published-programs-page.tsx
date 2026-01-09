import RandomProgramImage from "@/components/programs/random-program-image";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {Button} from "@/components/ui/button";
import {PublishedProgramDetails, PublishedProgramPassTypeDetails,} from "@/domain/domain";
import {getPublishedProgram} from "@/lib/api";
import {AlertCircle, ArrowLeft, Calendar, Check, Clock, MapPin, Sparkles, Ticket} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {Link, useNavigate, useParams} from "react-router";

const PublishedProgramsPage: React.FC = () => {
  const { isAuthenticated, isLoading, signinRedirect, signoutRedirect } =
    useAuth();
  const navigate = useNavigate();
  const { id } = useParams();
  const [error, setError] = useState<string | undefined>();
  const [publishedProgram, setPublishedProgram] = useState<
    PublishedProgramDetails | undefined
  >();
  const [selectedPassType, setSelectedPassType] = useState<
    PublishedProgramPassTypeDetails | undefined
  >();

  useEffect(() => {
    if (!id) {
      setError("ID must be provided!");
      return;
    }

    const doUseEffect = async () => {
      try {
        const programData = await getPublishedProgram(id);
        setPublishedProgram(programData);
        if (programData.passTypes.length > 0) {
          setSelectedPassType(programData.passTypes[0]);
        }
      } catch (err) {
        if (err instanceof Error) {
          setError(err.message);
        } else if (typeof err === "string") {
          setError(err);
        } else {
          setError("An unknown error has occurred");
        }
      }
    };
    doUseEffect();
  }, [id]);

  if (error) {
    return (
      <div className="event-page">
        <div className="event-page-container">
          <Alert variant="destructive" className="bg-red-900/30 border-red-700">
            <AlertCircle className="h-4 w-4" />
            <AlertTitle>Error</AlertTitle>
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        </div>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="event-page">
        <div className="event-loading">
          <div className="event-loading-spinner" />
          <p>Loading event details...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="event-page">
      {/* Hero Header with Image */}
      <div className="event-hero">
        <div className="event-hero-image">
          <RandomProgramImage />
          <div className="event-hero-overlay" />
        </div>
        
        {/* Navigation */}
        <nav className="event-nav">
          <button className="event-back-btn" onClick={() => navigate(-1)}>
            <ArrowLeft className="w-5 h-5" />
            <span>Back</span>
          </button>
          <div className="event-nav-actions">
            {isAuthenticated ? (
              <>
                <Button
                  variant="ghost"
                  onClick={() => navigate("/dashboard")}
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

        {/* Event Title Section */}
        <div className="event-hero-content">
          <div className="event-badge">
            <Sparkles className="w-4 h-4" />
            <span>Featured Event</span>
          </div>
          <h1 className="event-title">{publishedProgram?.name}</h1>
          <div className="event-meta">
            <div className="event-meta-item">
              <MapPin className="w-5 h-5" />
              <span>{publishedProgram?.venue}</span>
            </div>
            {publishedProgram?.startTime && (
              <div className="event-meta-item">
                <Calendar className="w-5 h-5" />
                <span>
                  Starts: {new Date(publishedProgram.startTime).toLocaleDateString('en-US', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                  })}
                </span>
              </div>
            )}
            {publishedProgram?.endTime && (
              <div className="event-meta-item">
                <Calendar className="w-5 h-5" />
                <span>
                  Ends: {new Date(publishedProgram.endTime).toLocaleDateString('en-US', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                  })}
                </span>
              </div>
            )}
            {(publishedProgram?.registrationStart || publishedProgram?.registrationEnd) && (
              <div className="event-meta-item">
                <Clock className="w-5 h-5" />
                <span>
                  Registration: {publishedProgram?.registrationStart && new Date(publishedProgram.registrationStart).toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                    year: 'numeric'
                  })} 
                  {publishedProgram?.registrationEnd && ` - ${new Date(publishedProgram.registrationEnd).toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                    year: 'numeric'
                  })}`}
                </span>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Content Section */}
      <div className="event-content">
        <div className="event-content-grid">
          {/* Left Column - Pass Types */}
          <div className="event-passes-section">
            <h2 className="event-section-title">
              <Ticket className="w-5 h-5 text-purple-400" />
              Available Passes
            </h2>
            <div className="event-passes-list">
              {publishedProgram?.passTypes?.map((passType) => (
                <div
                  className={`event-pass-card ${selectedPassType?.id === passType.id ? 'selected' : ''}`}
                  key={passType.id}
                  onClick={() => setSelectedPassType(passType)}
                >
                  <div className="event-pass-radio">
                    {selectedPassType?.id === passType.id && (
                      <Check className="w-4 h-4" />
                    )}
                  </div>
                  <div className="event-pass-info">
                    <h3 className="event-pass-name">{passType.name}</h3>
                    <p className="event-pass-description">{passType.description}</p>
                  </div>
                  <div className="event-pass-price">
                    <span className="event-pass-currency">$</span>
                    <span className="event-pass-amount">{passType.price}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Right Column - Purchase Card */}
          <div className="event-purchase-section">
            <div className="event-purchase-card">
              <div className="event-purchase-header">
                <h3>Selected Pass</h3>
                <span className="event-purchase-type">{selectedPassType?.name}</span>
              </div>
              <div className="event-purchase-price">
                <span className="event-purchase-currency">$</span>
                <span className="event-purchase-amount">{selectedPassType?.price}</span>
              </div>
              <p className="event-purchase-description">
                {selectedPassType?.description}
              </p>
              <div className="event-purchase-divider" />
              <Link
                to={`/programs/${publishedProgram?.id}/purchase/${selectedPassType?.id}`}
              >
                <Button className="event-purchase-btn">
                  <Ticket className="w-5 h-5" />
                  Purchase Pass
                </Button>
              </Link>
              <p className="event-purchase-note">
                Secure checkout • Instant confirmation
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer Gradient */}
      <div className="event-footer-gradient" />
    </div>
  );
};

export default PublishedProgramsPage;
