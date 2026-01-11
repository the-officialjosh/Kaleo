import {useNavigate} from "react-router";
import {ArrowLeft, Home, Mail, ShieldX} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/landing/background-3d";

export default function ForbiddenPage() {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleContactSupport = () => {
    window.location.href = "mailto:support@kaleo.dev?subject=Access%20Request";
  };

  return (
    <div className="error-page">
      <div className="background-3d-fixed">
        <Background3D />
      </div>

      <div className="error-page-content">
        <div className="error-card error-card-large">
          <div className="error-code-403">
            <span className="digit">4</span>
            <div className="shield-container">
              <ShieldX className="shield-icon" />
            </div>
            <span className="digit">3</span>
          </div>

          <h1 className="error-title">Access Forbidden</h1>

          <p className="error-description">
            You don't have permission to access this resource.
            This might be restricted to certain user roles or accounts.
          </p>

          <div className="error-actions">
            <Button
              className="error-btn-primary error-btn-forbidden"
              onClick={handleGoHome}
            >
              <Home className="w-4 h-4" />
              Go Home
            </Button>
            <Button
              variant="outline"
              className="error-btn-secondary"
              onClick={handleGoBack}
            >
              <ArrowLeft className="w-4 h-4" />
              Go Back
            </Button>
            <Button
              variant="outline"
              className="error-btn-secondary"
              onClick={handleContactSupport}
            >
              <Mail className="w-4 h-4" />
              Contact Support
            </Button>
          </div>

          <div className="error-help-text">
            <p>If you believe you should have access, please contact your administrator.</p>
          </div>
        </div>
      </div>
    </div>
  );
}

