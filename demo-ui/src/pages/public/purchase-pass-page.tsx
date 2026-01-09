import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {purchasePass} from "@/lib/api";
import {ArrowLeft, Calendar, CheckCircle, CreditCard, Lock, Sparkles, User} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {useNavigate, useParams} from "react-router";

const PurchasePassPage: React.FC = () => {
  const { programId, passTypeId } = useParams();
  const { isLoading, user } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState<string | undefined>();
  const [isPurchaseSuccess, setIsPurchaseASuccess] = useState(false);

  useEffect(() => {
    if (!isPurchaseSuccess) {
      return;
    }
    const timer = setTimeout(() => {
      navigate("/");
    }, 3000);

    return () => clearTimeout(timer);
  }, [isPurchaseSuccess]);

  const handlePurchase = async () => {
    if (isLoading || !user?.access_token || !programId || !passTypeId) {
      return;
    }
    try {
      await purchasePass(user.access_token, programId, passTypeId);
      setIsPurchaseASuccess(true);
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

  if (isPurchaseSuccess) {
    return (
      <div className="purchase-page">
        <div className="purchase-container">
          <div className="purchase-card purchase-success-card">
            <div className="success-icon-wrapper">
              <CheckCircle className="success-icon" />
            </div>
            <h2 className="success-title">Thank You!</h2>
            <p className="success-message">
              Your pass purchase was successful.
            </p>
            <p className="success-redirect">
              Redirecting to home page in a few seconds...
            </p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="purchase-page">
      {/* Background Effect */}
      <div className="purchase-bg-gradient" />
      
      <div className="purchase-container">
        {/* Back Button */}
        <button className="purchase-back-btn" onClick={() => navigate(-1)}>
          <ArrowLeft size={20} />
          <span>Back</span>
        </button>

        {/* Header */}
        <div className="purchase-header">
          <div className="purchase-header-icon">
            <Sparkles size={24} />
          </div>
          <h1 className="purchase-title">Complete Your Purchase</h1>
          <p className="purchase-subtitle">Enter your payment details to get your pass</p>
        </div>

        {/* Card */}
        <div className="purchase-card">
          {error && (
            <div className="purchase-error">
              <strong>Error:</strong> {error}
            </div>
          )}

          {/* Credit Card Number */}
          <div className="purchase-field">
            <Label className="purchase-label">Card Number</Label>
            <div className="purchase-input-wrapper">
              <CreditCard className="purchase-input-icon" size={18} />
              <Input
                type="text"
                placeholder="1234 5678 9012 3456"
                maxLength={19}
                className="purchase-input"
              />
            </div>
          </div>

          {/* Cardholder Name */}
          <div className="purchase-field">
            <Label className="purchase-label">Cardholder Name</Label>
            <div className="purchase-input-wrapper">
              <User className="purchase-input-icon" size={18} />
              <Input
                type="text"
                placeholder="John Smith"
                className="purchase-input"
              />
            </div>
          </div>

          {/* Expiry & CVV Row */}
          <div className="purchase-row">
            <div className="purchase-field">
              <Label className="purchase-label">Expiry Date</Label>
              <div className="purchase-input-wrapper">
                <Calendar className="purchase-input-icon" size={18} />
                <Input
                  type="text"
                  placeholder="MM/YY"
                  maxLength={5}
                  className="purchase-input"
                />
              </div>
            </div>
            <div className="purchase-field">
              <Label className="purchase-label">CVV</Label>
              <div className="purchase-input-wrapper">
                <Lock className="purchase-input-icon" size={18} />
                <Input
                  type="text"
                  placeholder="123"
                  maxLength={4}
                  className="purchase-input"
                />
              </div>
            </div>
          </div>

          {/* Purchase Button */}
          <Button
            className="purchase-submit-btn"
            onClick={handlePurchase}
          >
            <Lock size={16} />
            <span>Purchase Pass</span>
          </Button>

          {/* Security Note */}
          <div className="purchase-security-note">
            <Lock size={14} />
            <span>Your payment information is secure and encrypted</span>
          </div>

          {/* Mock Warning */}
          <div className="purchase-mock-warning">
            This is a demo page - no real payment will be processed
          </div>
        </div>
      </div>
    </div>
  );
};

export default PurchasePassPage;
