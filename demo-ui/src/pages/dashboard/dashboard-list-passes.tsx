import NavBar from "@/components/common/nav-bar";
import {SimplePagination} from "@/components/common/simple-pagination";
import {PassSummary, SpringBootPagination} from "@/domain/domain";
import {listPasses} from "@/lib/api";
import {Calendar, Clock, MapPin, Tag, Ticket} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {Link} from "react-router";
import {InlineError} from "@/components/errors";

// Helper to format dates nicely
const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', { 
    month: 'short', 
    day: 'numeric', 
    year: 'numeric' 
  });
};

const formatTime = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleTimeString('en-US', { 
    hour: 'numeric', 
    minute: '2-digit',
    hour12: true 
  });
};

const formatDateRange = (start: string, end: string) => {
  const startDate = new Date(start);
  const endDate = new Date(end);
  
  // Same day event
  if (startDate.toDateString() === endDate.toDateString()) {
    return `${formatDate(start)} • ${formatTime(start)} - ${formatTime(end)}`;
  }
  
  // Multi-day event
  return `${formatDate(start)} - ${formatDate(end)}`;
};

const DashboardListPasses: React.FC = () => {
  const { isLoading, user } = useAuth();

  const [passes, setPasses] = useState<
    SpringBootPagination<PassSummary> | undefined
  >();
  const [error, setError] = useState<string | undefined>();
  const [page, setPage] = useState(0);

  const refreshPasses = async () => {
    if (!user?.access_token) return;
    try {
      setPasses(await listPasses(user.access_token, page));
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      } else if (typeof err === "string") {
        setError(err);
      } else {
        setError("An unknown error occurred");
      }
    }
  };

  useEffect(() => {
    if (isLoading || !user?.access_token) {
      return;
    }
    refreshPasses();
  }, [isLoading, user?.access_token, page]);

  if (error) {
    return (
      <div className="dashboard-page">
        <NavBar />
        <InlineError 
          message={error} 
          onRetry={() => {
            setError(undefined);
            refreshPasses();
          }}
        />
      </div>
    );
  }

  return (
    <div className="dashboard-page">
      <NavBar />

      {/* Hero Header */}
      <div className="dashboard-list-hero">
        <div className="dashboard-list-hero-content">
          <div className="dashboard-list-hero-left">
            <h1 className="dashboard-list-hero-title">Your Passes</h1>
            <p className="dashboard-list-hero-subtitle">
              {passes?.totalElements 
                ? `${passes.totalElements} pass${passes.totalElements !== 1 ? 'es' : ''} in your collection` 
                : 'View and manage your purchased passes'}
            </p>
          </div>
        </div>
      </div>

      {/* Passes Grid */}
      <div className="dashboard-list-container">
        {passes?.content.length === 0 ? (
          <div className="dashboard-list-empty">
            <Ticket className="w-16 h-16 opacity-20" />
            <h3>No passes yet</h3>
            <p>Purchase passes from available programs</p>
          </div>
        ) : (
          <div className="dashboard-passes-grid">
            {passes?.content.map((passItem) => (
              <Link key={passItem.id} to={`/dashboard/passes/${passItem.id}`} className="dashboard-pass-card-link">
                <div className="dashboard-pass-card-new">
                  {/* Status Badge */}
                  <div className="pass-status-badge-wrapper">
                    <span className={`pass-status-badge ${passItem.status.toLowerCase()}`}>
                      {passItem.status}
                    </span>
                  </div>

                  {/* Program Info */}
                  <div className="pass-program-section">
                    <h3 className="pass-program-name">{passItem.programName}</h3>
                    
                    <div className="pass-program-details">
                      <div className="pass-detail-row">
                        <Calendar className="pass-detail-icon" />
                        <span>{formatDateRange(passItem.programStartTime, passItem.programEndTime)}</span>
                      </div>
                      
                      <div className="pass-detail-row">
                        <MapPin className="pass-detail-icon" />
                        <span className="pass-venue">{passItem.programVenue.split(',')[0]}</span>
                      </div>
                    </div>
                  </div>

                  {/* Divider */}
                  <div className="pass-card-divider"></div>

                  {/* Pass Type Info */}
                  <div className="pass-type-section">
                    <div className="pass-type-header">
                      <div className="pass-type-icon">
                        <Ticket className="w-4 h-4" />
                      </div>
                      <span className="pass-type-name">{passItem.passTypeName}</span>
                    </div>
                    
                    <div className="pass-type-price">
                      <span className="currency">$</span>
                      <span className="amount">{passItem.passTypePrice.toFixed(2)}</span>
                    </div>
                  </div>

                  {/* Footer */}
                  <div className="pass-card-footer">
                    <div className="pass-id-row">
                      <Tag className="w-3 h-3" />
                      <span>ID: {passItem.id.slice(0, 8)}...</span>
                    </div>
                    <div className="pass-purchased-row">
                      <Clock className="w-3 h-3" />
                      <span>Purchased {formatDate(passItem.createdAt)}</span>
                    </div>
                  </div>

                  {/* CTA */}
                  <div className="pass-card-cta">
                    View Pass Details →
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}

        {passes && passes.content.length > 0 && (
          <div className="dashboard-list-pagination">
            <SimplePagination pagination={passes} onPageChange={setPage} />
          </div>
        )}
      </div>
    </div>
  );
};

export default DashboardListPasses;
