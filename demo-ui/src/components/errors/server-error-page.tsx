import {useNavigate} from "react-router";
import {Home, Mail, RefreshCw, ServerCrash} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/landing/background-3d";

export default function ServerErrorPage() {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  const handleTryAgain = () => {
    window.location.reload();
  };

  const handleContactSupport = () => {
    window.location.href = "mailto:support@kaleo.dev?subject=Server%20Error%20Report";
  };

  return (
    <div className="error-page">
      <div className="background-3d-fixed">
        <Background3D />
      </div>

      <div className="error-page-content">
        <div className="error-card error-card-large">
          <div className="error-code-500">
            <span className="digit">5</span>
            <div className="server-container">
              <ServerCrash className="server-icon" />
            </div>
            <span className="digit">0</span>
          </div>

          <h1 className="error-title">Server Error</h1>

          <p className="error-description">
            Oops! Something went wrong on our end. Our team has been notified
            and is working to fix the issue. Please try again in a few moments.
          </p>

          <div className="error-actions">
            <Button
              className="error-btn-primary error-btn-server"
              onClick={handleTryAgain}
            >
              <RefreshCw className="w-4 h-4" />
              Try Again
            </Button>
            <Button
              variant="outline"
              className="error-btn-secondary"
              onClick={handleGoHome}
            >
              <Home className="w-4 h-4" />
              Go Home
            </Button>
            <Button
              variant="outline"
              className="error-btn-secondary"
              onClick={handleContactSupport}
            >
              <Mail className="w-4 h-4" />
              Report Issue
            </Button>
          </div>

          <div className="error-help-text">
            <p>If the problem persists, please contact our support team.</p>
          </div>
        </div>
      </div>
    </div>
  );
}

