import NavBar from "@/components/common/nav-bar";
import {Pagination} from "@/components/common/pagination";
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
import {ProgramSummary, SpringBootPagination,} from "@/domain/domain";
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
  const [itemsPerPage, setItemsPerPage] = useState(4);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [programToDelete, setProgramToDelete] = useState<
    ProgramSummary | undefined
  >();

  useEffect(() => {
    if (isLoading || !user?.access_token) {
      return;
    }
    refreshPrograms(user.access_token);
  }, [isLoading, user, page, itemsPerPage]);

  const refreshPrograms = async (accessToken: string) => {
    try {
      setPrograms(await listPrograms(accessToken, page, itemsPerPage));
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

      {/* Hero Header */}
      <div className="dashboard-list-hero">
        <div className="dashboard-list-hero-content">
          <div className="dashboard-list-hero-left">
            <h1 className="dashboard-list-hero-title">Your Programs</h1>
            <p className="dashboard-list-hero-subtitle">Manage and organize all your events</p>
          </div>
          <Link to="/dashboard/programs/create">
            <Button className="dashboard-list-create-btn">
              <Calendar className="w-4 h-4" />
              Create Program
            </Button>
          </Link>
        </div>
      </div>

      {/* Programs Grid */}
      <div className="dashboard-list-container">
        {programs?.content.length === 0 ? (
          <div className="dashboard-list-empty">
            <Calendar className="w-16 h-16 opacity-20" />
            <h3>No programs yet</h3>
            <p>Create your first program to get started</p>
            <Link to="/dashboard/programs/create">
              <Button className="dashboard-list-create-btn mt-4">
                Create Program
              </Button>
            </Link>
          </div>
        ) : (
          <div className="dashboard-list-grid">
            {programs?.content.map((programItem) => (
              <div key={programItem.id} className="dashboard-program-card">
                <div className="dashboard-program-card-header">
                  <h3 className="dashboard-program-card-title">{programItem.name}</h3>
                  <span className={`dashboard-program-status ${programItem.status.toLowerCase()}`}>
                    {programItem.status}
                  </span>
                </div>

                <div className="dashboard-program-card-body">
                  <div className="dashboard-program-meta-item">
                    <Calendar className="w-4 h-4" />
                    <div>
                      <span className="label">Event Date</span>
                      <span className="value">
                        {formatDate(programItem.startTime)} - {formatDate(programItem.endTime)}
                      </span>
                    </div>
                  </div>

                  <div className="dashboard-program-meta-item">
                    <Clock className="w-4 h-4" />
                    <div>
                      <span className="label">Time</span>
                      <span className="value">
                        {formatTime(programItem.startTime)} - {formatTime(programItem.endTime)}
                      </span>
                    </div>
                  </div>

                  <div className="dashboard-program-meta-item">
                    <MapPin className="w-4 h-4" />
                    <div>
                      <span className="label">Venue</span>
                      <span className="value">{programItem.venue || "TBD"}</span>
                    </div>
                  </div>

                  {programItem.passTypes.length > 0 && (
                    <div className="dashboard-program-passes">
                      <Tag className="w-4 h-4" />
                      <div className="dashboard-program-passes-list">
                        {programItem.passTypes.map((passType) => (
                          <span key={passType.id} className="dashboard-program-pass-tag">
                            {passType.name} · ${passType.price}
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                </div>

                <div className="dashboard-program-card-footer">
                  <Link to={`/dashboard/programs/update/${programItem.id}`}>
                    <button className="dashboard-program-action-btn edit">
                      <Edit className="w-4 h-4" />
                      Edit
                    </button>
                  </Link>
                  <button
                    className="dashboard-program-action-btn delete"
                    onClick={() => handleOpenDeleteProgramDialog(programItem)}
                  >
                    <Trash className="w-4 h-4" />
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {programs && programs.content.length > 0 && (
          <Pagination
            currentPage={programs.number}
            totalPages={programs.totalPages}
            onPageChange={setPage}
            itemsPerPage={itemsPerPage}
            totalItems={programs.totalElements}
            onItemsPerPageChange={setItemsPerPage}
          />
        )}
      </div>

      {/* Delete Dialog */}
      <AlertDialog open={dialogOpen}>
        <AlertDialogContent className="dashboard-delete-dialog">
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Program</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete "{programToDelete?.name}"? This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          {deleteProgramError && (
            <Alert variant="destructive" className="dashboard-delete-error">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>{deleteProgramError}</AlertDescription>
            </Alert>
          )}
          <AlertDialogFooter>
            <AlertDialogCancel 
              className="dashboard-delete-cancel"
              onClick={handleCancelDeleteProgramDialog}
            >
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction 
              className="dashboard-delete-confirm"
              onClick={() => handleDeleteProgram()}
            >
              Delete Program
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
};

export default DashboardListProgramsPage;
