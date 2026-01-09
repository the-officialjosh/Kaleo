import {isRouteErrorResponse, useNavigate, useRouteError} from "react-router";
import {AlertTriangle, Home, RefreshCw} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/landing/background-3d";

export default function ErrorPage() {
  const error = useRouteError();
  const navigate = useNavigate();

  let errorCode = "Error";
  let errorMessage = "Something went wrong";
  let errorDetails = "";

  if (isRouteErrorResponse(error)) {
    errorCode = String(error.status);
    errorMessage = error.statusText || "An error occurred";
    errorDetails = error.data?.message || "";
  } else if (error instanceof Error) {
    errorMessage = error.message;
    errorDetails = error.stack?.split("\n")[0] || "";
  }

  const handleGoHome = () => {
    navigate("/");
  };

  const handleTryAgain = () => {
    window.location.reload();
  };

  return (
    <div className="error-page">
      <div className="background-3d-fixed">
        <Background3D />
      </div>

      <div className="error-page-content">
        <div className="error-card">
          <div className="error-icon-container">
            <AlertTriangle className="error-icon" />
          </div>

          <div className="error-code">{errorCode}</div>

          <h1 className="error-title">{errorMessage}</h1>

          {errorDetails && (
            <p className="error-details">{errorDetails}</p>
          )}

          <p className="error-description">
            Don't worry, these things happen. Let's get you back on track.
          </p>

          <div className="error-actions">
            <Button
              className="error-btn-primary"
              onClick={handleGoHome}
            >
              <Home className="w-4 h-4" />
              Go Home
            </Button>
            <Button
              variant="outline"
              className="error-btn-secondary"
              onClick={handleTryAgain}
            >
              <RefreshCw className="w-4 h-4" />
              Try Again
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
