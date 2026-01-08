import {PassDetails, PassStatus} from "@/domain/domain";
import {getPass, getPassQr} from "@/lib/api";
import {format} from "date-fns";
import {Calendar, DollarSign, MapPin, Tag} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {useParams} from "react-router";

const DashboardViewPassPage: React.FC = () => {
  const [pass, setPass] = useState<PassDetails | undefined>();
  const [qrCodeUrl, setQrCodeUrl] = useState<string | undefined>();
  const [isQrLoading, setIsQrCodeLoading] = useState(true);
  const [error, setError] = useState<string | undefined>();

  const { id } = useParams();
  const { isLoading, user } = useAuth();

  useEffect(() => {
    if (isLoading || !user?.access_token || !id) {
      return;
    }

    const doUseEffect = async (accessToken: string, id: string) => {
      try {
        setIsQrCodeLoading(true);
        setError(undefined);

        setPass(await getPass(accessToken, id));
        setQrCodeUrl(URL.createObjectURL(await getPassQr(accessToken, id)));
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

    doUseEffect(user?.access_token, id);

    return () => {
      if (qrCodeUrl) {
        URL.revokeObjectURL(qrCodeUrl);
      }
    };
  }, [user?.access_token, isLoading, id]);

  const getStatusColor = (status: PassStatus) => {
    switch (status) {
      case PassStatus.PURCHASED:
        return "text-green-400";
      case PassStatus.CANCELLED:
        return "text-red-400";
      default:
        return "text-gray-400";
    }
  };

  if (!pass) {
    return <p>Loading..</p>;
  }

  return (
    <div className="dashboard-page flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="relative bg-gradient-to-br from-purple-900 via-purple-800 to-indigo-900 rounded-3xl p-8 shadow-2xl">
          {/* Status */}
          <div className="bg-black/30 backdrop-blur-sm px-3 py-1 rounded-full mb-8 text-center">
            <span
              className={`text-sm font-medium ${getStatusColor(pass.status)}`}
            >
              {pass?.status}
            </span>
          </div>

          <div className="mb-2">
            <h1 className="text-2xl font-bold mb-2">{pass.programName}</h1>
            <div className="flex items-center gap-2 text-purple-200">
              <MapPin className="w-4" />
              <span>{pass.programVenue}</span>
            </div>
          </div>

          <div className="flex items-center gap-2 text-purple-300 mb-8">
            <Calendar className="w-4 text-purple-200" />
            <div>
              {format(pass.programStart, "Pp")} -{" "}
              {format(pass.programEnd, "Pp")}
            </div>
          </div>

          <div className="flex justify-center mb-8">
            <div className="bg-white p-4 rounded-2xl shadow-lg">
              <div className="w-32 h-32 flex items-center justify-center">
                {/* Loading */}
                {isQrLoading && (
                  <div className="text-xs text-center p2">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 mb-2 mx-auto"></div>
                    <div className="text-gray-800">Loading QR...</div>
                  </div>
                )}
                {/* error */}
                {error && (
                  <div className="text-red-400 text-sm text-center p-2">
                    <div className="mb-1">⚠️</div>
                    {error}
                  </div>
                )}
                {/* Display QR */}
                {qrCodeUrl && !isQrLoading && !error && (
                  <img
                    src={qrCodeUrl}
                    alt="QR Code for program"
                    className="w-full h-full object-contain rounded-large"
                  />
                )}
              </div>
            </div>
          </div>

          <div className="text-center mb-8">
            <p className="text-purple-200 text-sm">
              Present this QR code at the venue for entry
            </p>
          </div>

          <div className="space-y-2 mb-8">
            <div className="flex items-center gap-2">
              <Tag className="w-5 text-purple-200" />
              <span className="font-semibold">{pass.description}</span>
            </div>
            <div className="flex items-center gap-2">
              <DollarSign className="w-5 text-purple-200" />
              <span className="font-semibold">{pass.price}</span>
            </div>
          </div>

          <div className="text-center mb-2">
            <h4 className="text-sm font-semibold font-mono">Pass ID</h4>
            <p className="text-purple-200 text-sm font-mono">{pass.id}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardViewPassPage;
