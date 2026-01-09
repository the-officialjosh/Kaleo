import NavBar from "@/components/common/nav-bar";
import {Input} from "@/components/ui/input";
import {useState} from "react";
import {Scanner} from "@yudiel/react-qr-scanner";
import {PassValidationMethod, PassValidationStatus} from "@/domain/domain";
import {AlertCircle, Check, X} from "lucide-react";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {validatePass} from "@/lib/api";
import {useAuth} from "react-oidc-context";

const DashboardValidateQrPage: React.FC = () => {
  const { isLoading, user } = useAuth();
  const [isManual, setIsManual] = useState(false);
  const [data, setData] = useState<string | undefined>();
  const [error, setError] = useState<string | undefined>();
  const [validationStatus, setValidationStatus] = useState<
    PassValidationStatus | undefined
  >();

  const handleReset = () => {
    setIsManual(false);
    setData(undefined);
    setError(undefined);
    setValidationStatus(undefined);
  };

  const handleError = (err: unknown) => {
    if (err instanceof Error) {
      setError(err.message);
    } else if (typeof err === "string") {
      setError(err);
    } else {
      setError("An unknown error occurred");
    }
  };

  const handleValidate = async (id: string, method: PassValidationMethod) => {
    if (!user?.access_token) {
      return;
    }
    try {
      const response = await validatePass(user.access_token, {
        id,
        method,
      });
      setValidationStatus(response.status);
    } catch (err) {
      handleError(err);
    }
  };

  if (isLoading || !user?.access_token) {
    <p>Loading...</p>;
  }

  return (
    <div className="dashboard-qr-page">
      <NavBar />
      
      <div className="dashboard-qr-container">
        <div className="dashboard-qr-card">
          <div className="dashboard-qr-header">
            <h1 className="dashboard-qr-title">Validate Pass</h1>
            <p className="dashboard-qr-subtitle">Scan a QR code or enter ID manually</p>
          </div>

          {error && (
            <Alert variant="destructive" className="manage-program-error mb-4">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}

          {/* Scanner */}
          <div className="dashboard-qr-scanner">
            <Scanner
              key={`scanner-${data}-${validationStatus}`}
              onScan={(result) => {
                if (result) {
                  const qrCodeId = result[0].rawValue;
                  setData(qrCodeId);
                  handleValidate(qrCodeId, PassValidationMethod.QR_SCAN);
                }
              }}
              onError={handleError}
            />

            {validationStatus && (
              <div className="dashboard-qr-result">
                <div className={`dashboard-qr-result-icon ${validationStatus === PassValidationStatus.VALID ? 'valid' : 'invalid'}`}>
                  {validationStatus === PassValidationStatus.VALID ? (
                    <Check />
                  ) : (
                    <X />
                  )}
                </div>
              </div>
            )}
          </div>

          {/* Result Display */}
          <div className="dashboard-qr-display">
            {data || "Waiting for scan..."}
          </div>

          {/* Actions */}
          <div className="dashboard-qr-actions">
            {isManual ? (
              <>
                <Input
                  className="dashboard-qr-input"
                  placeholder="Enter pass ID..."
                  onChange={(e) => setData(e.target.value)}
                />
                <button
                  className="dashboard-qr-btn primary"
                  onClick={() => handleValidate(data || "", PassValidationMethod.MANUAL)}
                >
                  Validate Pass
                </button>
                <button
                  className="dashboard-qr-btn secondary"
                  onClick={() => setIsManual(false)}
                >
                  Back to Scanner
                </button>
              </>
            ) : (
              <button
                className="dashboard-qr-btn secondary"
                onClick={() => setIsManual(true)}
              >
                Enter ID Manually
              </button>
            )}
            
            <button
              className="dashboard-qr-btn secondary"
              onClick={handleReset}
            >
              Reset
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardValidateQrPage;
