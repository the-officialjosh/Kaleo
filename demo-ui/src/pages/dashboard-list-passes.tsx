import NavBar from "@/components/nav-bar";
import {SimplePagination} from "@/components/simple-pagination";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {PassSummary, SpringBootPagination} from "@/domain/domain";
import {listPasses} from "@/lib/api";
import {AlertCircle, Tag, Ticket} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {Link} from "react-router";

const DashboardListPasses: React.FC = () => {
  const { isLoading, user } = useAuth();

  const [passes, setPasses] = useState<
    SpringBootPagination<PassSummary> | undefined
  >();
  const [error, setError] = useState<string | undefined>();
  const [page, setPage] = useState(0);

  useEffect(() => {
    if (isLoading || !user?.access_token) {
      return;
    }

    const doUseEffect = async () => {
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

    doUseEffect();
  }, [isLoading, user?.access_token, page]);

  if (error) {
    return (
      <div className="dashboard-page">
        <NavBar />
        <Alert variant="destructive" className="bg-gray-900 border-red-700">
          <AlertCircle className="h-4 w-4" />
          <AlertTitle>Error</AlertTitle>
          <AlertDescription>{error}</AlertDescription>
        </Alert>
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
            <p className="dashboard-list-hero-subtitle">View and manage your purchased passes</p>
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
          <div className="dashboard-list-grid">
            {passes?.content.map((passItem) => (
              <Link key={passItem.id} to={`/dashboard/passes/${passItem.id}`} className="dashboard-pass-card-link">
                <div className="dashboard-pass-card">
                  <div className="dashboard-pass-card-header">
                    <div className="dashboard-pass-icon">
                      <Ticket className="w-5 h-5" />
                    </div>
                    <span className={`dashboard-pass-status ${passItem.status.toLowerCase()}`}>
                      {passItem.status}
                    </span>
                  </div>
                  
                  <h3 className="dashboard-pass-card-title">{passItem.passType.name}</h3>
                  
                  <div className="dashboard-pass-price">
                    <span className="currency">$</span>
                    <span className="amount">{passItem.passType.price}</span>
                  </div>
                  
                  <div className="dashboard-pass-id">
                    <Tag className="w-3 h-3" />
                    <span>{passItem.id.slice(0, 12)}...</span>
                  </div>
                  
                  <div className="dashboard-pass-cta">
                    View Pass →
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
