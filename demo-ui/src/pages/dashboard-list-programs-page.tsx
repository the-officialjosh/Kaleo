import NavBar from "@/components/nav-bar";
import {SimplePagination} from "@/components/simple-pagination";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardFooter, CardHeader,} from "@/components/ui/card";
import {ProgramStatusEnum, ProgramSummary, SpringBootPagination,} from "@/domain/domain";
import {deleteProgram, listPrograms} from "@/lib/api";
import {AlertCircle, Calendar, Clock, Edit, MapPin, Tag, Trash,} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {Link} from "react-router";

const DashboardListProgramsPage: React.FC = () => {
  const { isLoading, user } = useAuth();
  const [programs, setPrograms] = useState<
    SpringBootPagination<ProgramSummary> | undefined
  >();
  const [error, setError] = useState<string | undefined>();
  const [deleteProgramError, setDeleteProgramError] = useState<
    string | undefined
  >();

  const [page, setPage] = useState(0);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [programToDelete, setProgramToDelete] = useState<
    ProgramSummary | undefined
  >();

  useEffect(() => {
    if (isLoading || !user?.access_token) {
      return;
    }
    refreshPrograms(user.access_token);
  }, [isLoading, user, page]);

  const refreshPrograms = async (accessToken: string) => {
    try {
      setPrograms(await listPrograms(accessToken, page));
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

  const formatDate = (date?: string) => {
    if (!date) {
      return "TBD";
    }
    return new Date(date).toLocaleDateString("en-US", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
  };

  const formatTime = (date?: string) => {
    if (!date) {
      return "";
    }
    return new Date(date).toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    });
  };

  const formatStatusBadge = (status: ProgramStatusEnum) => {
    switch (status) {
      case ProgramStatusEnum.DRAFT:
        return "bg-gray-700 text-gray-200";
      case ProgramStatusEnum.PUBLISHED:
        return "bg-green-700 text-green-100";
      case ProgramStatusEnum.CANCELLED:
        return "bg-red-700 text-red-100";
      case ProgramStatusEnum.COMPLETED:
        return "bg-blue-700 text-blue-100";
      default:
        return "bg-gray-700 text-gray-200";
    }
  };

  const handleOpenDeleteProgramDialog = (programToDelete: ProgramSummary) => {
    setProgramToDelete(programToDelete);
    setDialogOpen(true);
  };

  const handleCancelDeleteProgramDialog = () => {
    setProgramToDelete(undefined);
    setDialogOpen(false);
  };

  const handleDeleteProgram = async () => {
    if (!programToDelete || isLoading || !user?.access_token) {
      return;
    }

    try {
      setDeleteProgramError(undefined);
      await deleteProgram(user.access_token, programToDelete.id);
      setProgramToDelete(undefined);
      setDialogOpen(false);
      refreshPrograms(user.access_token);
    } catch (err) {
      if (err instanceof Error) {
        setDeleteProgramError(err.message);
      } else if (typeof err === "string") {
        setDeleteProgramError(err);
      } else {
        setDeleteProgramError("An unknown error has occurred");
      }
    }
  };

  if (error) {
    return (
      <div className="dashboard-page">
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

      <div className="max-w-lg mx-auto px-4">
        {/* Title */}
        <div className="py-8 px-4 flex justify-between">
          <div>
            <h1 className="text-2xl font-bold">Your Programs</h1>
            <p>Programs you have created</p>
          </div>
          <div>
            <Link to="/dashboard/programs/create">
              <Button className="bg-purple-700 hover:bg-purple-500 cursor-pointer">
                Create Program
              </Button>
            </Link>
          </div>
        </div>

        {/* Program Cards */}
        <div className="space-y-2">
          {programs?.content.map((programItem) => (
            <Card className="bg-gray-900 border-gray-700 text-white">
              <CardHeader>
                <div className="flex justify-between">
                  <h3 className="font-bold text-xl">{programItem.name}</h3>
                  <span
                    className={`flex items-center px-2 py-1 rounded-lg text-xs ${formatStatusBadge(programItem.status)}`}
                  >
                    {programItem.status}
                  </span>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {/* Program Start & End */}
                <div className="flex space-x-2">
                  <Calendar className="h-5 w-5 text-gray-400" />
                  <div>
                    <p className="font-medium">
                      {formatDate(programItem.startTime)} to{" "}
                      {formatDate(programItem.endTime)}
                    </p>
                    <p className="text-gray-400">
                      {formatTime(programItem.startTime)} -{" "}
                      {formatTime(programItem.endTime)}
                    </p>
                  </div>
                </div>
                {/* Registration start and end */}
                <div className="flex space-x-2">
                  <Clock className="h-5 w-5 text-gray-400" />
                  <div>
                    <h4 className="font-medium">Registration Period</h4>
                    <p className="text-gray-400">
                      {formatDate(programItem.registrationStart)} to{" "}
                      {formatDate(programItem.registrationEnd)}
                    </p>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <MapPin className="h-5 w-5 text-gray-400" />
                  <div>
                    <p className="font-medium">{programItem.venue}</p>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <Tag className="h-5 w-5 text-gray-400" />
                  <div>
                    <h4 className="font-medium">Pass Types</h4>
                    <ul>
                      {programItem.passTypes.map((passType) => (
                        <li
                          key={passType.id}
                          className="flex gap-2 text-gray-400"
                        >
                          <span>{passType.name}</span>
                          <span>${passType.price}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              </CardContent>
              <CardFooter className="flex justify-end gap-2">
                <Link to={`/dashboard/programs/update/${programItem.id}`}>
                  <Button
                    type="button"
                    className="bg-gray-700 hover:bg-gray-500 cursor-pointer"
                  >
                    <Edit />
                  </Button>
                </Link>
                <Button
                  type="button"
                  className="bg-red-700/80 hover:bg-red-500 cursor-pointer"
                  onClick={() => handleOpenDeleteProgramDialog(programItem)}
                >
                  <Trash />
                </Button>
              </CardFooter>
            </Card>
          ))}
        </div>
      </div>
      <div className="flex justify-center py-8">
        {programs && (
          <SimplePagination pagination={programs} onPageChange={setPage} />
        )}
      </div>
      <AlertDialog open={dialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This will delete your program '{programToDelete?.name}' and cannot
              be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          {deleteProgramError && (
            <Alert variant="destructive" className="border-red-700">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>{deleteProgramError}</AlertDescription>
            </Alert>
          )}
          <AlertDialogFooter>
            <AlertDialogCancel onClick={handleCancelDeleteProgramDialog}>
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction onClick={() => handleDeleteProgram()}>
              Continue
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
};

export default DashboardListProgramsPage;
