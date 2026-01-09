import {PassDetails, PassStatus} from "@/domain/domain";
import {getPass, getPassQr} from "@/lib/api";
import {AlertCircle, ArrowLeft, Calendar, CheckCircle2, Clock, DollarSign, MapPin, Ticket, XCircle} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {useNavigate, useParams} from "react-router";
import NavBar from "@/components/common/nav-bar";
import {InlineError} from "@/components/errors";

// Format date nicely
const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', { 
    weekday: 'short',
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

const DashboardViewPassPage: React.FC = () => {
  const [pass, setPass] = useState<PassDetails | undefined>();
  const [qrCodeUrl, setQrCodeUrl] = useState<string | undefined>();
  const [isQrLoading, setIsQrCodeLoading] = useState(true);
  const [error, setError] = useState<string | undefined>();

  const { id } = useParams();
  const { isLoading, user } = useAuth();
  const navigate = useNavigate();

  const loadPass = async () => {
    if (!user?.access_token || !id) return;
    
    try {
      setIsQrCodeLoading(true);
      setError(undefined);
      setPass(await getPass(user.access_token, id));
      setQrCodeUrl(URL.createObjectURL(await getPassQr(user.access_token, id)));
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      } else if (typeof err === "string") {
        setError(err);
      } else {
        setError("An unknown error has occurred");
      }
    } finally {
      setIsQrCodeLoading(false);
    }
  };

  useEffect(() => {
    if (isLoading || !user?.access_token || !id) return;
    loadPass();

    return () => {
      if (qrCodeUrl) {
        URL.revokeObjectURL(qrCodeUrl);
      }
    };
  }, [user?.access_token, isLoading, id]);

  const getStatusInfo = (status: PassStatus) => {
    switch (status) {
      case PassStatus.ACTIVE:
        return { color: "active", icon: CheckCircle2, label: "Active" };
      case PassStatus.PURCHASED:
        return { color: "purchased", icon: Ticket, label: "Purchased" };
      case PassStatus.USED:
        return { color: "used", icon: CheckCircle2, label: "Used" };
      case PassStatus.CANCELLED:
        return { color: "cancelled", icon: XCircle, label: "Cancelled" };
      default:
        return { color: "default", icon: AlertCircle, label: status };
    }
  };

  if (error) {
    return (
      <div className="dashboard-page">
        <NavBar />
        <InlineError 
          message={error} 
          onRetry={() => {
            setError(undefined);
            loadPass();
          }}
        />
      </div>
    );
  }

  if (!pass || isLoading) {
    return (
      <div className="dashboard-page">
        <NavBar />
        <div className="pass-view-loading">
          <div className="pass-view-loading-spinner"></div>
          <p>Loading pass details...</p>
        </div>
      </div>
    );
  }

  const statusInfo = getStatusInfo(pass.status);
  const StatusIcon = statusInfo.icon;

  return (
    <div className="dashboard-page">
      <NavBar />

      <div className="pass-view-container">
        {/* Back Button */}
        <button className="pass-view-back" onClick={() => navigate('/dashboard/passes')}>
          <ArrowLeft className="w-4 h-4" />
          Back to Passes
        </button>

        <div className="pass-view-layout">
          {/* Pass Card */}
          <div className="pass-view-card">
            {/* Status Badge */}
            <div className={`pass-view-status ${statusInfo.color}`}>
              <StatusIcon className="w-4 h-4" />
              <span>{statusInfo.label}</span>
            </div>

            {/* Program Header */}
            <div className="pass-view-program-header">
              <h1 className="pass-view-program-name">{pass.programName}</h1>
              <div className="pass-view-pass-type">
                <Ticket className="w-4 h-4" />
                <span>{pass.passTypeName}</span>
              </div>
            </div>

            {/* QR Code Section */}
            <div className="pass-view-qr-section">
              <div className="pass-view-qr-wrapper">
                {isQrLoading ? (
                  <div className="pass-view-qr-loading">
                    <div className="pass-view-loading-spinner"></div>
                    <span>Loading QR...</span>
                  </div>
                ) : qrCodeUrl ? (
                  <img
                    src={qrCodeUrl}
                    alt="Pass QR Code"
                    className="pass-view-qr-image"
                  />
                ) : (
                  <div className="pass-view-qr-error">
                    <AlertCircle className="w-8 h-8" />
                    <span>QR unavailable</span>
                  </div>
                )}
              </div>
              <p className="pass-view-qr-hint">
                Show this QR code at the venue for entry
              </p>
            </div>

            {/* Divider */}
            <div className="pass-view-divider"></div>

            {/* Event Details */}
            <div className="pass-view-details">
              <div className="pass-view-detail-row">
                <div className="pass-view-detail-icon">
                  <Calendar className="w-5 h-5" />
                </div>
                <div className="pass-view-detail-content">
                  <span className="pass-view-detail-label">Date</span>
                  <span className="pass-view-detail-value">
                    {formatDate(pass.programStartTime)} — {formatDate(pass.programEndTime)}
                  </span>
                </div>
              </div>

              <div className="pass-view-detail-row">
                <div className="pass-view-detail-icon">
                  <Clock className="w-5 h-5" />
                </div>
                <div className="pass-view-detail-content">
                  <span className="pass-view-detail-label">Time</span>
                  <span className="pass-view-detail-value">
                    {formatTime(pass.programStartTime)} — {formatTime(pass.programEndTime)}
                  </span>
                </div>
              </div>

              <div className="pass-view-detail-row">
                <div className="pass-view-detail-icon">
                  <MapPin className="w-5 h-5" />
                </div>
                <div className="pass-view-detail-content">
                  <span className="pass-view-detail-label">Venue</span>
                  <span className="pass-view-detail-value">{pass.programVenue}</span>
                </div>
              </div>

              <div className="pass-view-detail-row">
                <div className="pass-view-detail-icon">
                  <DollarSign className="w-5 h-5" />
                </div>
                <div className="pass-view-detail-content">
                  <span className="pass-view-detail-label">Price Paid</span>
                  <span className="pass-view-detail-value pass-view-price">
                    ${pass.passTypePrice.toFixed(2)}
                  </span>
                </div>
              </div>
            </div>

            {/* Pass ID Footer */}
            <div className="pass-view-footer">
              <span className="pass-view-footer-label">Pass ID</span>
              <code className="pass-view-pass-id">{pass.id}</code>
            </div>
          </div>

          {/* Description Card */}
          {pass.passTypeDescription && (
            <div className="pass-view-description-card">
              <h3>About This Pass</h3>
              <p>{pass.passTypeDescription}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DashboardViewPassPage;
