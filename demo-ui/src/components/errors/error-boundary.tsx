import React, {Component, ErrorInfo, ReactNode} from "react";
import {AlertTriangle, Home, RefreshCw} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/background-3d";

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
  errorInfo: ErrorInfo | null;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null,
    errorInfo: null,
  };

  public static getDerivedStateFromError(error: Error): Partial<State> {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error("ErrorBoundary caught an error:", error, errorInfo);
    this.setState({ error, errorInfo });
  }

  private handleGoHome = () => {
    window.location.href = "/";
  };

  private handleTryAgain = () => {
    this.setState({ hasError: false, error: null, errorInfo: null });
    window.location.reload();
  };

  public render() {
    if (this.state.hasError) {
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

              <div className="error-code">Oops!</div>

              <h1 className="error-title">Something went wrong</h1>

              {this.state.error && (
                <p className="error-details">{this.state.error.message}</p>
              )}

              <p className="error-description">
                Don't worry, these things happen. Let's get you back on track.
              </p>

              <div className="error-actions">
                <Button
                  className="error-btn-primary"
                  onClick={this.handleGoHome}
                >
                  <Home className="w-4 h-4" />
                  Go Home
                </Button>
                <Button
                  variant="outline"
                  className="error-btn-secondary"
                  onClick={this.handleTryAgain}
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

    return this.props.children;
  }
}

export default ErrorBoundary;
