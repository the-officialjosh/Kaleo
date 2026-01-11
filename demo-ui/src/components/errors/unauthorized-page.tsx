import {useNavigate} from "react-router";
import {ArrowLeft, Home, Lock, LogIn} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/landing/background-3d";
import {useAuth} from "react-oidc-context";

export default function UnauthorizedPage() {
  const navigate = useNavigate();
  const { signinRedirect } = useAuth();

  const handleSignIn = () => {
    signinRedirect();
  };

  const handleGoHome = () => {
    navigate("/");
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="error-page">
      <div className="background-3d-fixed">
        <Background3D />
      </div>

      <div className="error-page-content">
        <div className="error-card error-card-large">
          <div className="error-code-401">
            <span className="digit">4</span>
            <div className="lock-container">
              <Lock className="lock-icon" />
            </div>
            <span className="digit">1</span>
          </div>

          <h1 className="error-title">Authentication Required</h1>

          <p className="error-description">
            You need to sign in to access this page. Your session may have expired
            or you haven't logged in yet.
          </p>

          <div className="error-actions">
            <Button
              className="error-btn-primary error-btn-signin"
              onClick={handleSignIn}
            >
              <LogIn className="w-4 h-4" />
              Sign In
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
              onClick={handleGoBack}
            >
              <ArrowLeft className="w-4 h-4" />
              Go Back
            </Button>
          </div>

          <div className="error-help-text">
            <p>Having trouble signing in? Make sure your credentials are correct.</p>
          </div>
        </div>
      </div>
    </div>
  );
}

