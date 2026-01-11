import {isRouteErrorResponse, useNavigate, useRouteError} from "react-router";
import {AlertTriangle, Home, RefreshCw} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/landing/background-3d";
import UnauthorizedPage from "./unauthorized-page";
import ForbiddenPage from "./forbidden-page";
import ServerErrorPage from "./server-error-page";
import NotFoundPage from "./not-found-page";

export default function ErrorPage() {
  const error = useRouteError();
  const navigate = useNavigate();

  let errorCode = "Error";
  let errorMessage = "Something went wrong";
  let errorDetails = "";
  let statusCode = 0;

  if (isRouteErrorResponse(error)) {
    statusCode = error.status;
    errorCode = String(error.status);
    errorMessage = error.statusText || "An error occurred";
    errorDetails = error.data?.message || "";
  } else if (error instanceof Error) {
    errorMessage = error.message;
    errorDetails = error.stack?.split("\n")[0] || "";

    // Try to extract status code from error message
    const statusMatch = errorMessage.match(/(\d{3})/);
    if (statusMatch) {
      statusCode = parseInt(statusMatch[1], 10);
    }
  }

  // Route to specialized error pages based on status code
  switch (statusCode) {
    case 401:
      return <UnauthorizedPage />;
    case 403:
      return <ForbiddenPage />;
    case 404:
      return <NotFoundPage />;
    case 500:
    case 502:
    case 503:
    case 504:
      return <ServerErrorPage />;
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
