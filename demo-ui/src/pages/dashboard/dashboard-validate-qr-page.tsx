import NavBar from "@/components/common/nav-bar";
import {Input} from "@/components/ui/input";
import {useEffect, useState} from "react";
import {Scanner} from "@yudiel/react-qr-scanner";
import {PassValidationMethod, PassValidationStatus, SpringBootPagination, StaffProgramSummary} from "@/domain/domain";
import {AlertCircle, Check, ChevronDown, X} from "lucide-react";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {listStaffPrograms, validatePass} from "@/lib/api";
import {useAuth} from "react-oidc-context";

const DashboardValidateQrPage: React.FC = () => {
  const { isLoading, user } = useAuth();
  const [isManual, setIsManual] = useState(false);
  const [manualCode, setManualCode] = useState<string>("");
  const [error, setError] = useState<string | undefined>();
  const [validationStatus, setValidationStatus] = useState<
    PassValidationStatus | undefined
  >();
  const [validationMessage, setValidationMessage] = useState<string | undefined>();
  
  // Program selection
  const [programs, setPrograms] = useState<SpringBootPagination<StaffProgramSummary> | undefined>();
  const [selectedProgram, setSelectedProgram] = useState<StaffProgramSummary | undefined>();
  const [isProgramDropdownOpen, setIsProgramDropdownOpen] = useState(false);

  // Load programs on mount
  useEffect(() => {
    if (!user?.access_token) return;
    
    const loadPrograms = async () => {
      try {
        const result = await listStaffPrograms(user.access_token, 0);
        setPrograms(result);
        // Auto-select first program if available
        if (result.content.length > 0) {
          setSelectedProgram(result.content[0]);
        }
      } catch (err) {
        handleError(err);
      }
    };
    
    loadPrograms();
  }, [user?.access_token]);

  const handleReset = () => {
    setIsManual(false);
    setManualCode("");
    setError(undefined);
    setValidationStatus(undefined);
    setValidationMessage(undefined);
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

  const handleQrScan = async (qrCodeId: string) => {
    if (!user?.access_token || !selectedProgram) {
      setError("Please select a program first");
      return;
    }
    try {
      setError(undefined);
      const response = await validatePass(user.access_token, {
        programId: selectedProgram.id,
        qrCodeId,
        method: PassValidationMethod.QR_SCAN,
      });
      setValidationStatus(response.status);
      setValidationMessage(response.message);
    } catch (err) {
      handleError(err);
    }
  };

  const handleManualValidate = async () => {
    if (!user?.access_token || !selectedProgram) {
      setError("Please select a program first");
      return;
    }
    if (!manualCode.trim()) {
      setError("Please enter a manual code");
      return;
    }
    try {
      setError(undefined);
      const response = await validatePass(user.access_token, {
        programId: selectedProgram.id,
        manualCode: manualCode.trim(),
        method: PassValidationMethod.MANUAL,
      });
      setValidationStatus(response.status);
      setValidationMessage(response.message);
    } catch (err) {
      handleError(err);
    }
  };

  if (isLoading || !user?.access_token) {
    return (
      <div className="dashboard-qr-page">
        <NavBar />
        <div className="dashboard-qr-container">
          <p>Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard-qr-page">
      <NavBar />
      
      <div className="dashboard-qr-container">
        <div className="dashboard-qr-card">
          <div className="dashboard-qr-header">
            <h1 className="dashboard-qr-title">Validate Pass</h1>
            <p className="dashboard-qr-subtitle">Scan a QR code or enter manual code</p>
          </div>

          {/* Program Selector */}
          <div className="dashboard-qr-program-selector">
            <label className="dashboard-qr-label">Select Program</label>
            <div className="dashboard-qr-dropdown">
              <button 
                className="dashboard-qr-dropdown-trigger"
                onClick={() => setIsProgramDropdownOpen(!isProgramDropdownOpen)}
              >
                <span>{selectedProgram?.name || "Select a program..."}</span>
                <ChevronDown className={`w-4 h-4 transition-transform ${isProgramDropdownOpen ? 'rotate-180' : ''}`} />
              </button>
              {isProgramDropdownOpen && (
                <div className="dashboard-qr-dropdown-menu">
                  {programs?.content.map(program => (
                    <button
                      key={program.id}
                      className={`dashboard-qr-dropdown-item ${selectedProgram?.id === program.id ? 'selected' : ''}`}
                      onClick={() => {
                        setSelectedProgram(program);
                        setIsProgramDropdownOpen(false);
                        handleReset();
                      }}
                    >
                      {program.name}
                    </button>
                  ))}
                  {programs?.content.length === 0 && (
                    <div className="dashboard-qr-dropdown-empty">
                      No programs available
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>

          {error && (
            <Alert variant="destructive" className="manage-program-error mb-4">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}

          {selectedProgram && (
            <>
              {/* Scanner */}
              <div className="dashboard-qr-scanner">
                <Scanner
                  key={`scanner-${validationStatus}`}
                  onScan={(result) => {
                    if (result) {
                      const qrCodeId = result[0].rawValue;
                      handleQrScan(qrCodeId);
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
                    {validationMessage && (
                      <p className="dashboard-qr-result-message">{validationMessage}</p>
                    )}
                  </div>
                )}
              </div>

              {/* Actions */}
              <div className="dashboard-qr-actions">
                {isManual ? (
                  <>
                    <Input
                      className="dashboard-qr-input"
                      placeholder="Enter manual code..."
                      value={manualCode}
                      onChange={(e) => setManualCode(e.target.value)}
                    />
                    <button
                      className="dashboard-qr-btn primary"
                      onClick={handleManualValidate}
                    >
                      Validate Pass
                    </button>
                    <button
                      className="dashboard-qr-btn secondary"
                      onClick={() => {
                        setIsManual(false);
                        setManualCode("");
                      }}
                    >
                      Back to Scanner
                    </button>
                  </>
                ) : (
                  <button
                    className="dashboard-qr-btn secondary"
                    onClick={() => setIsManual(true)}
                  >
                    Enter Code Manually
                  </button>
                )}
                
                <button
                  className="dashboard-qr-btn secondary"
                  onClick={handleReset}
                >
                  Reset
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default DashboardValidateQrPage;
