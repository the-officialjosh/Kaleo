import {useNavigate} from "react-router";
import {ArrowLeft, Home, Search} from "lucide-react";
import {Button} from "../ui/button";
import Background3D from "@/components/background-3d";
import {useState} from "react";

export default function NotFoundPage() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");

  const handleGoHome = () => {
    navigate("/");
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleSearch = () => {
    if (searchQuery.trim()) {
      navigate(`/?search=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleSearch();
    }
  };

  return (
    <div className="error-page">
      <div className="background-3d-fixed">
        <Background3D />
      </div>

      <div className="error-page-content">
        <div className="error-card error-card-large">
          <div className="error-code-404">
            <span className="digit">4</span>
            <span className="digit-zero">0</span>
            <span className="digit">4</span>
          </div>

          <h1 className="error-title">Page Not Found</h1>

          <p className="error-description">
            Oops! The page you're looking for seems to have wandered off. 
            Let's help you find what you need.
          </p>

          <div className="error-search-container">
            <div className="error-search-input-wrapper">
              <Search className="error-search-icon" />
              <input
                type="text"
                placeholder="Search for programs..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onKeyDown={handleKeyDown}
                className="error-search-input"
              />
            </div>
            <Button
              className="error-search-btn"
              onClick={handleSearch}
              disabled={!searchQuery.trim()}
            >
              Search
            </Button>
          </div>

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
              onClick={handleGoBack}
            >
              <ArrowLeft className="w-4 h-4" />
              Go Back
            </Button>
          </div>

          <div className="error-links">
            <p className="error-links-label">Quick links:</p>
            <div className="error-quick-links">
              <button onClick={() => navigate("/programs")} className="error-link">
                Browse Programs
              </button>
              <button onClick={() => navigate("/organizers")} className="error-link">
                For Organizers
              </button>
              <button onClick={() => navigate("/dashboard")} className="error-link">
                Dashboard
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
