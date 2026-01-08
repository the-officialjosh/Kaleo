import RandomProgramImage from "@/components/random-program-image";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {PublishedProgramDetails, PublishedProgramPassTypeDetails,} from "@/domain/domain";
import {getPublishedProgram} from "@/lib/api";
import {AlertCircle, MapPin} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {Link, useNavigate, useParams} from "react-router";

const PublishedProgramsPage: React.FC = () => {
  const { isAuthenticated, isLoading, signinRedirect, signoutRedirect } =
    useAuth();
  const navigate = useNavigate();
  const { id } = useParams();
  const [error, setError] = useState<string | undefined>();
  const [publishedProgram, setPublishedProgram] = useState<
    PublishedProgramDetails | undefined
  >();
  const [selectedPassType, setSelectedPassType] = useState<
    PublishedProgramPassTypeDetails | undefined
  >();

  useEffect(() => {
    if (!id) {
      setError("ID must be provided!");
      return;
    }

    const doUseEffect = async () => {
      try {
        const programData = await getPublishedProgram(id);
        setPublishedProgram(programData);
        if (programData.passTypes.length > 0) {
          setSelectedPassType(programData.passTypes[0]);
        }
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
    doUseEffect();
  }, [id]);

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
    return <p>Loading...</p>;
  }

  return (
    <div className="bg-black min-h-screen text-white">
      {/* Nav */}
      <div className="flex justify-end p-4 container mx-auto">
        {isAuthenticated ? (
          <div className="flex gap-4">
            <Button
              onClick={() => navigate("/dashboard/programs")}
              className="cursor-pointer"
            >
              Dashboard
            </Button>
            <Button
              className="cursor-pointer"
              onClick={() => signoutRedirect()}
            >
              Log out
            </Button>
          </div>
        ) : (
          <div className="flex gap-4">
            <Button className="cursor-pointer" onClick={() => signinRedirect()}>
              Log in
            </Button>
          </div>
        )}
      </div>

      <main className="container mx-auto px-4 py-16">
        {/* Header */}
        <div className="grid grid-cols-2 gap-8 max-w-5xl mx-auto mb-8">
          {/* Left Column */}
          <div className="space-y-4">
            <h1 className="text-3xl font-bold">{publishedProgram?.name}</h1>
            <p className="text-lg flex gap-2 text-gray-300">
              <MapPin />
              {publishedProgram?.venue}
            </p>
          </div>
          {/* Right Column */}
          <div className="bg-gray-600 rounded-lg w-full max-w-sm overflow-hidden">
            <RandomProgramImage />
          </div>
        </div>

        <h2 className="text-2xl font-bold mb-6">Available Passes</h2>
        <div className="flex gap-2">
          {/* Left */}
          <div className="w-1/2">
            {publishedProgram?.passTypes?.map((passType) => (
              <Card
                className="bg-gray-800 border-gray-600 hover:bg-gray-700 text-white cursor-pointer gap-0 mb-2"
                key={passType.id}
                onClick={() => setSelectedPassType(passType)}
              >
                <CardHeader>
                  <div className="flex justify-between items-center">
                    <h3 className="text-lg font-semibold">{passType.name}</h3>
                    <span className="text-xl font-bold ">
                      ${passType.price}
                    </span>
                  </div>
                </CardHeader>
                <CardContent>
                  <p className="text-gray-300 text-sm">
                    {passType.description}
                  </p>
                </CardContent>
              </Card>
            ))}
          </div>

          {/* Right */}
          <div className="w-1/2 text-white">
            <div className="bg-gray-800 rounded-lg p-6 border border-gray-600">
              <h2 className="text-2xl font-bold">{selectedPassType?.name}</h2>
              <div className="mb-6">
                <span className="text-3xl font-bold">
                  ${selectedPassType?.price}
                </span>
              </div>
              <div className="mb-6">
                <p className="text-gray-300">{selectedPassType?.description}</p>
              </div>
              <Link
                to={`/programs/${publishedProgram?.id}/purchase/${selectedPassType?.id}`}
              >
                <Button className="w-full bg-purple-600 hover:bg-purple-700 cursor-pointer">
                  Purchase Pass
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default PublishedProgramsPage;
