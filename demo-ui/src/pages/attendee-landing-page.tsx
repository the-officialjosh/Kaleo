import {useAuth} from "react-oidc-context";
import {Button} from "../components/ui/button";
import {useNavigate} from "react-router";
import {AlertCircle, Search, Sparkles, X} from "lucide-react";
import {useEffect, useRef, useState} from "react";
import {PublishedProgramSummary, SpringBootPagination} from "@/domain/domain";
import {listPublishedPrograms, searchPublishedPrograms} from "@/lib/api";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import PublishedProgramCard from "@/components/published-program-card";
import {SimplePagination} from "@/components/simple-pagination";

const AttendeeLandingPage: React.FC = () => {
  const { isAuthenticated, isLoading, signinRedirect, signoutRedirect } =
    useAuth();

  const navigate = useNavigate();

  const [page, setPage] = useState(0);
  const [publishedPrograms, setPublishedPrograms] = useState<
    SpringBootPagination<PublishedProgramSummary> | undefined
  >();
  const [error, setError] = useState<string | undefined>();
  const [query, setQuery] = useState<string | undefined>();
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const [isSearchActive, setIsSearchActive] = useState(false);
  const [isSearching, setIsSearching] = useState(false);
  const resultsRef = useRef<HTMLElement>(null);

  useEffect(() => {
    if (query && query.length > 0) {
      queryPublishedPrograms();
    } else {
      refreshPublishedPrograms();
    }
  }, [page]);

  const refreshPublishedPrograms = async () => {
    try {
      setPublishedPrograms(await listPublishedPrograms(page));
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

  const queryPublishedPrograms = async () => {
    if (!query) {
      await refreshPublishedPrograms();
      return;
    }

    setIsSearching(true);
    setIsSearchActive(true);

    try {
      setPublishedPrograms(await searchPublishedPrograms(query, page));
      // Smooth scroll to results after a brief delay for the animation
      setTimeout(() => {
        resultsRef.current?.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }, 300);
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      } else if (typeof err === "string") {
        setError(err);
      } else {
        setError("An unknown error has occurred");
      }
    } finally {
      setIsSearching(false);
    }
  };

  const clearSearch = () => {
    setQuery("");
    setIsSearchActive(false);
    refreshPublishedPrograms();
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      queryPublishedPrograms();
    }
  };

  if (error) {
    return (
      <div className="min-h-screen bg-black text-white">
        <Alert variant="destructive" className="bg-gray-900 border-red-700">
          <AlertCircle className="h-4 w-4" />
          <AlertTitle>Error</AlertTitle>
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="min-h-screen bg-black flex items-center justify-center">
        <div className="animate-pulse text-white text-xl">Loading...</div>
      </div>
    );
  }

  return (
    <div className={`landing-page ${isSearchActive ? 'search-mode' : ''}`}>
      {/* Hero Section */}
      <section className={`hero-section ${isSearchActive ? 'collapsed' : ''}`}>
        {/* Background */}
        <div className="hero-background" />
        <div className="hero-overlay" />
        
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

        {/* Hero Content */}
        <div className="hero-content">
          <h1 className="hero-title">
            Discover Your Next
            <span className="hero-title-accent"> Experience</span>
          </h1>
          <p className="hero-subtitle">
            Find and book passes to amazing programs, events, and gatherings in your community
          </p>

          {/* Search Bar */}
          <div className={`search-container ${isSearchFocused ? 'focused' : ''} ${isSearchActive ? 'active' : ''}`}>
            <div className="search-icon">
              <Search className="w-5 h-5" />
            </div>
            <input
              type="text"
              placeholder="Search for programs, events, or venues..."
              className="search-input"
              value={query || ""}
              onChange={(e) => setQuery(e.target.value)}
              onFocus={() => setIsSearchFocused(true)}
              onBlur={() => setIsSearchFocused(false)}
              onKeyDown={handleKeyDown}
            />
            {isSearchActive && (
              <button className="search-clear-btn" onClick={clearSearch} aria-label="Clear search">
                <X className="w-4 h-4" />
              </button>
            )}
            <button 
              className={`search-button ${isSearching ? 'loading' : ''}`} 
              onClick={queryPublishedPrograms}
              disabled={isSearching}
            >
              {isSearching ? (
                <span className="search-spinner" />
              ) : (
                'Search'
              )}
            </button>
          </div>
        </div>

        {/* Scroll indicator */}
        <div className="scroll-indicator">
          <div className="scroll-arrow" />
        </div>
      </section>

      {/* Programs Section */}
      <section className={`programs-section ${isSearchActive ? 'search-results-mode' : ''}`} ref={resultsRef}>
        <div className="programs-container">
          <div className="programs-header">
            <h2 className="programs-title">
              {isSearchActive ? (
                <>
                  <span className="results-label">Results for</span>
                  <span className="results-query">"{query}"</span>
                </>
              ) : (
                'Upcoming Programs'
              )}
            </h2>
            <p className="programs-subtitle">
              {isSearchActive 
                ? `Found ${publishedPrograms?.totalElements || 0} program${(publishedPrograms?.totalElements || 0) !== 1 ? 's' : ''}` 
                : 'Explore what\'s happening in your community'
              }
            </p>
          </div>

          {/* Program Cards Grid */}
          <div className="programs-grid">
            {publishedPrograms?.content?.map((publishedProgram) => (
              <PublishedProgramCard
                publishedProgram={publishedProgram}
                key={publishedProgram.id}
              />
            ))}
          </div>

          {/* Empty State */}
          {publishedPrograms?.content?.length === 0 && (
            <div className="empty-state">
              <Sparkles className="w-12 h-12 text-purple-400 mb-4" />
              <h3 className="text-xl font-semibold text-white mb-2">No programs found</h3>
              <p className="text-gray-400">Check back later for upcoming events</p>
            </div>
          )}

          {/* Pagination */}
          {publishedPrograms && publishedPrograms.content.length > 0 && (
            <div className="pagination-container">
              <SimplePagination
                pagination={publishedPrograms}
                onPageChange={setPage}
              />
            </div>
          )}
        </div>
      </section>
    </div>
  );
};

export default AttendeeLandingPage;
