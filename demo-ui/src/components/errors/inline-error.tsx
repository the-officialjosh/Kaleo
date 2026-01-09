import {AlertTriangle, Home, RefreshCw, X} from "lucide-react";
import {Button} from "../ui/button";

interface InlineErrorProps {
  message: string;
  onDismiss?: () => void;
  onRetry?: () => void;
  showHomeButton?: boolean;
}

export default function InlineError({
  message,
  onDismiss,
  onRetry,
  showHomeButton = true,
}: InlineErrorProps) {
  const handleGoHome = () => {
    window.location.href = "/";
  };

  return (
    <div className="inline-error">
      <div className="inline-error-card">
        {onDismiss && (
          <button className="inline-error-dismiss" onClick={onDismiss}>
            <X className="w-4 h-4" />
          </button>
        )}
        
        <div className="inline-error-icon-container">
          <AlertTriangle className="inline-error-icon" />
        </div>

        <h3 className="inline-error-title">Something went wrong</h3>
        
        <p className="inline-error-message">{message}</p>

        <div className="inline-error-actions">
          {onRetry && (
            <Button
              className="error-btn-primary"
              onClick={onRetry}
            >
              <RefreshCw className="w-4 h-4" />
              Try Again
            </Button>
          )}
          {showHomeButton && (
            <Button
              variant="outline"
              className="error-btn-secondary"
              onClick={handleGoHome}
            >
              <Home className="w-4 h-4" />
              Go Home
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
