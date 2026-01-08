import NavBar from "@/components/nav-bar";
import {SimplePagination} from "@/components/simple-pagination";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {PassSummary, SpringBootPagination} from "@/domain/domain";
import {listPasses} from "@/lib/api";
import {AlertCircle, DollarSign, Tag, Ticket} from "lucide-react";
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
      <div className="bg-black min-h-screen text-white">
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
    <div className="bg-black min-h-screen text-white">
      <NavBar />

      {/* Title */}
      <div className="py-8 px-4">
        <h1 className="text-2xl font-bold">Your Passes</h1>
        <p>Passes you have purchased</p>
      </div>

      <div className="max-w-lg mx-auto">
        {passes?.content.map((passItem) => (
          <Link to={`/dashboard/passes/${passItem.id}`}>
            <Card key={passItem.id} className="bg-gray-900 text-white">
              <CardHeader>
                <div className="flex justify-between">
                  <div className="flex items-center gap-2">
                    <Ticket className="h-5 w-5 text-gray-400" />
                    <h3 className="font-bold text-xl">
                      {passItem.passType.name}
                    </h3>
                  </div>
                  <span>{passItem.status}</span>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {/* Price */}
                <div className="flex items-center gap-2">
                  <DollarSign className="h-5 w-5 text-gray-400" />
                  <p className="font-medium">${passItem.passType.price}</p>
                </div>

                {/* Pass ID */}
                <div className="flex items-center gap-2">
                  <Tag className="h-5 w-5 text-gray-400" />
                  <div>
                    <h4 className="font-medium">Pass ID</h4>
                    <p className="text-gray-400 font-mono text-sm">
                      {passItem.id}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>
      <div className="flex justify-center py-8">
        {passes && (
          <SimplePagination pagination={passes} onPageChange={setPage} />
        )}
      </div>
    </div>
  );
};

export default DashboardListPasses;
