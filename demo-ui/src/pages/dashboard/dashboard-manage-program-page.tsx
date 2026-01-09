import NavBar from "@/components/common/nav-bar";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {Button} from "@/components/ui/button";
import {Calendar} from "@/components/ui/calendar";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {Popover, PopoverContent, PopoverTrigger,} from "@/components/ui/popover";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from "@/components/ui/select";
import {Switch} from "@/components/ui/switch";
import {Textarea} from "@/components/ui/textarea";
import {
    CreatePassTypeRequest,
    CreateProgramRequest,
    ProgramDetails,
    ProgramStatusEnum,
    UpdatePassTypeRequest,
    UpdateProgramRequest,
} from "@/domain/domain";
import {createProgram, getProgram, updateProgram} from "@/lib/api";
import {format} from "date-fns";
import {AlertCircle, CalendarIcon, Edit, Plus, Ticket, Trash,} from "lucide-react";
import {useEffect, useState} from "react";
import {useAuth} from "react-oidc-context";
import {useNavigate, useParams} from "react-router";

interface DateTimeSelectProperties {
  date: Date | undefined;
  setDate: (date: Date) => void;
  time: string | undefined;
  setTime: (time: string) => void;
  enabled: boolean;
  setEnabled: (isEnabled: boolean) => void;
}

interface RequiredDateTimeSelectProperties {
  date: Date | undefined;
  setDate: (date: Date) => void;
  time: string | undefined;
  setTime: (time: string) => void;
}

const RequiredDateTimeSelect: React.FC<RequiredDateTimeSelectProperties> = ({
  date,
  setDate,
  time,
  setTime,
}) => {
  return (
    <div className="w-full flex gap-2">
      {/* Date */}
      <Popover>
        <PopoverTrigger asChild>
          <Button className="bg-gray-900 border-gray-700 border">
            <CalendarIcon />
            {date ? format(date, "PPP") : <span>Pick a Date</span>}
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-auto p-0" align="start">
          <Calendar
            mode="single"
            selected={date}
            onSelect={(selectedDate) => {
              if (!selectedDate) {
                return;
              }
              const displayedYear = selectedDate.getFullYear();
              const displayedMonth = selectedDate.getMonth();
              const displayedDay = selectedDate.getDate();

              const correctedDate = new Date(
                Date.UTC(displayedYear, displayedMonth, displayedDay),
              );

              setDate(correctedDate);
            }}
            className="rounded-md border shadow"
          />
        </PopoverContent>
      </Popover>
      {/* Time */}
      <div className="flex gap-2 items-center">
        <Input
          type="time"
          step="60"
          className="w-[90px] bg-gray-900 text-white border-gray-700 border [&::-webkit-calendar-picker-indicator]:invert"
          value={time}
          onChange={(e) => setTime(e.target.value)}
          required
        />
      </div>
    </div>
  );
};

const DateTimeSelect: React.FC<DateTimeSelectProperties> = ({
  date,
  setDate,
  time,
  setTime,
  enabled,
  setEnabled,
}) => {
  return (
    <div className="flex gap-2 items-center">
      <Switch checked={enabled} onCheckedChange={setEnabled} />

      {enabled && (
        <div className="w-full flex gap-2">
          {/* Date */}
          <Popover>
            <PopoverTrigger asChild>
              <Button className="bg-gray-900 border-gray-700 border">
                <CalendarIcon />
                {date ? format(date, "PPP") : <span>Pick a Date</span>}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                mode="single"
                selected={date}
                onSelect={(selectedDate) => {
                  if (!selectedDate) {
                    return;
                  }
                  const displayedYear = selectedDate.getFullYear();
                  const displayedMonth = selectedDate.getMonth();
                  const displayedDay = selectedDate.getDate();

                  const correctedDate = new Date(
                    Date.UTC(displayedYear, displayedMonth, displayedDay),
                  );

                  setDate(correctedDate);
                }}
                className="rounded-md border shadow"
              />
            </PopoverContent>
          </Popover>
          {/* Time */}
          <div className="flex gap-2 items-center">
            <Input
              type="time"
              step="60"
              className="w-[90px] bg-gray-900 text-white border-gray-700 border [&::-webkit-calendar-picker-indicator]:invert"
              value={time}
              onChange={(e) => setTime(e.target.value)}
            />
          </div>
        </div>
      )}
    </div>
  );
};

const generateTempId = () => `temp_${crypto.randomUUID()}`;
const isTempId = (id: string | undefined) => id && id.startsWith("temp_");

interface PassTypeData {
  id: string | undefined;
  name: string;
  price: number;
  totalAvailable?: number;
  description: string;
}

interface ProgramData {
  id: string | undefined;
  name: string;
  startDate: Date | undefined;
  startTime: string | undefined;
  endDate: Date | undefined;
  endTime: string | undefined;
  venueDetails: string;
  registrationStartDate: Date | undefined;
  registrationStartTime: string | undefined;
  registrationEndDate: Date | undefined;
  registrationEndTime: string | undefined;
  passTypes: PassTypeData[];
  status: ProgramStatusEnum;
  createdAt: string | undefined;
  updatedAt: string | undefined;
}

const DashboardManageProgramPage: React.FC = () => {
  const { isLoading, user } = useAuth();
  const { id } = useParams();
  const isEditMode = !!id;
  const navigate = useNavigate();

  const [programData, setProgramData] = useState<ProgramData>({
    id: undefined,
    name: "",
    startDate: undefined,
    startTime: undefined,
    endDate: undefined,
    endTime: undefined,
    venueDetails: "",
    registrationStartDate: undefined,
    registrationStartTime: undefined,
    registrationEndDate: undefined,
    registrationEndTime: undefined,
    passTypes: [],
    status: ProgramStatusEnum.DRAFT,
    createdAt: undefined,
    updatedAt: undefined,
  });

  const [currentPassType, setCurrentPassType] = useState<
    PassTypeData | undefined
  >();

  const [dialogOpen, setDialogOpen] = useState(false);

  const [programRegistrationDateEnabled, setProgramRegistrationDateEnabled] =
    useState(false);

  const [error, setError] = useState<string | undefined>();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const updateField = (field: keyof ProgramData, value: any) => {
    setProgramData((prev) => ({ ...prev, [field]: value }));
  };

  useEffect(() => {
    if (isEditMode && !isLoading && user?.access_token) {
      const fetchProgram = async () => {
        const program: ProgramDetails = await getProgram(user.access_token, id);
        setProgramData({
          id: program.id,
          name: program.name,
          startDate: new Date(program.startTime),
          startTime: formatTimeFromDate(new Date(program.startTime)),
          endDate: new Date(program.endTime),
          endTime: formatTimeFromDate(new Date(program.endTime)),
          venueDetails: program.venue,
          registrationStartDate: program.registrationStart
            ? new Date(program.registrationStart)
            : undefined,
          registrationStartTime: program.registrationStart
            ? formatTimeFromDate(new Date(program.registrationStart))
            : undefined,
          registrationEndDate: program.registrationEnd
            ? new Date(program.registrationEnd)
            : undefined,
          registrationEndTime: program.registrationEnd
            ? formatTimeFromDate(new Date(program.registrationEnd))
            : undefined,
          status: program.status,
          passTypes: program.passTypes.map((pass) => ({
            id: pass.id,
            name: pass.name,
            description: pass.description,
            price: pass.price,
            totalAvailable: pass.totalAvailable,
          })),
          createdAt: program.createdAt,
          updatedAt: program.updatedAt,
        });
        setProgramRegistrationDateEnabled(
          !!(program.registrationStart || program.registrationEnd),
        );
      };
      fetchProgram();
    }
  }, [id, user]);

  const formatTimeFromDate = (date: Date): string => {
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");
    return `${hours}:${minutes}`;
  };

  const combineDateTime = (date: Date, time: string): string => {
    const [hours, minutes] = time
      .split(":")
      .map((num) => Number.parseInt(num, 10));

    const year = date.getUTCFullYear();
    const month = String(date.getUTCMonth() + 1).padStart(2, "0");
    const day = String(date.getUTCDate()).padStart(2, "0");
    const hoursStr = String(hours).padStart(2, "0");
    const minutesStr = String(minutes).padStart(2, "0");

    // Return LocalDateTime format: yyyy-MM-ddTHH:mm:ss
    return `${year}-${month}-${day}T${hoursStr}:${minutesStr}:00`;
  };

  const handleProgramUpdateSubmit = async (accessToken: string, id: string) => {
    const passTypes: UpdatePassTypeRequest[] = programData.passTypes.map(
      (passType) => {
        return {
          id: isTempId(passType.id) ? undefined : passType.id,
          name: passType.name,
          price: passType.price,
          description: passType.description,
          totalAvailable: passType.totalAvailable,
        };
      },
    );

    const request: UpdateProgramRequest = {
      id: id,
      name: programData.name,
      startTime: combineDateTime(
        programData.startDate!,
        programData.startTime!,
      ),
      endTime: combineDateTime(programData.endDate!, programData.endTime!),
      venue: programData.venueDetails,
      registrationStart:
        programData.registrationStartDate && programData.registrationStartTime
          ? combineDateTime(
              programData.registrationStartDate,
              programData.registrationStartTime,
            )
          : undefined,
      registrationEnd:
        programData.registrationEndDate && programData.registrationEndTime
          ? combineDateTime(
              programData.registrationEndDate,
              programData.registrationEndTime,
            )
          : undefined,
      status: programData.status,
      passTypes: passTypes,
    };

    try {
      await updateProgram(accessToken, id, request);
      navigate("/dashboard/programs");
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

  const handleProgramCreateSubmit = async (accessToken: string) => {
    const passTypes: CreatePassTypeRequest[] = programData.passTypes.map(
      (passType) => {
        return {
          name: passType.name,
          price: passType.price,
          description: passType.description,
          totalAvailable: passType.totalAvailable,
        };
      },
    );

    const request: CreateProgramRequest = {
      name: programData.name,
      startTime: combineDateTime(
        programData.startDate!,
        programData.startTime!,
      ),
      endTime: combineDateTime(programData.endDate!, programData.endTime!),
      venue: programData.venueDetails,
      registrationStart:
        programData.registrationStartDate && programData.registrationStartTime
          ? combineDateTime(
              programData.registrationStartDate,
              programData.registrationStartTime,
            )
          : undefined,
      registrationEnd:
        programData.registrationEndDate && programData.registrationEndTime
          ? combineDateTime(
              programData.registrationEndDate,
              programData.registrationEndTime,
            )
          : undefined,
      status: programData.status,
      passTypes: passTypes,
    };

    try {
      await createProgram(accessToken, request);
      navigate("/dashboard/programs");
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

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(undefined);

    if (isLoading || !user || !user.access_token) {
      console.error("User not found!");
      return;
    }

    if (isEditMode) {
      if (!programData.id) {
        setError("Program does not have an ID");
        return;
      }
      await handleProgramUpdateSubmit(user.access_token, programData.id);
    } else {
      await handleProgramCreateSubmit(user.access_token);
    }
  };

  const handleAddPassType = () => {
    setCurrentPassType({
      id: undefined,
      name: "",
      price: 0,
      totalAvailable: 0,
      description: "",
    });
    setDialogOpen(true);
  };

  const handleSavePassType = () => {
    if (!currentPassType) {
      return;
    }

    const newPassTypes = [...programData.passTypes];

    if (currentPassType.id) {
      const index = newPassTypes.findIndex((t) => t.id === currentPassType.id);
      if (index !== -1) {
        newPassTypes[index] = currentPassType;
      }
    } else {
      newPassTypes.push({
        ...currentPassType,
        id: generateTempId(),
      });
    }

    updateField("passTypes", newPassTypes);
    setDialogOpen(false);
  };

  const handleEditPassType = (passType: PassTypeData) => {
    setCurrentPassType(passType);
    setDialogOpen(true);
  };

  const handleDeletePassType = (id: string | undefined) => {
    if (!id) {
      return;
    }
    updateField(
      "passTypes",
      programData.passTypes.filter((t) => t.id !== id),
    );
  };

  return (
    <div className="dashboard-page">
      <NavBar />
      
      {/* Hero Header */}
      <div className="manage-program-hero">
        <div className="manage-program-hero-content">
          <div className="manage-program-hero-badge">
            {isEditMode ? <Edit className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
            <span>{isEditMode ? "Edit Mode" : "New Program"}</span>
          </div>
          <h1 className="manage-program-hero-title">
            {isEditMode ? "Edit Program" : "Create a New Program"}
          </h1>
          <p className="manage-program-hero-subtitle">
            {isEditMode 
              ? "Update your program details and settings below."
              : "Fill out the form below to create an amazing event experience."}
          </p>
          {isEditMode && programData.id && (
            <div className="manage-program-meta">
              <span className="manage-program-meta-item">ID: {programData.id.slice(0, 8)}...</span>
              {programData.createdAt && (
                <span className="manage-program-meta-item">Created: {format(new Date(programData.createdAt), "MMM d, yyyy")}</span>
              )}
              {programData.updatedAt && (
                <span className="manage-program-meta-item">Updated: {format(new Date(programData.updatedAt), "MMM d, yyyy")}</span>
              )}
            </div>
          )}
        </div>
      </div>

      {/* Main Form Container */}
      <div className="manage-program-container">
        <form onSubmit={handleFormSubmit} className="manage-program-form">
          
          {/* Basic Info Section */}
          <div className="manage-program-section">
            <div className="manage-program-section-header">
              <div className="manage-program-section-icon">
                <Ticket className="w-5 h-5" />
              </div>
              <div>
                <h2 className="manage-program-section-title">Basic Information</h2>
                <p className="manage-program-section-subtitle">Give your program a name and venue</p>
              </div>
            </div>
            
            <div className="manage-program-section-content">
              <div className="manage-program-field">
                <label htmlFor="program-name" className="manage-program-label">
                  Program Name
                </label>
                <Input
                  id="program-name"
                  className="manage-program-input"
                  placeholder="Enter your program name..."
                  value={programData.name}
                  onChange={(e) => updateField("name", e.target.value)}
                  required
                />
                <p className="manage-program-hint">This is the public name attendees will see.</p>
              </div>
              
              <div className="manage-program-field">
                <label htmlFor="venue-details" className="manage-program-label">
                  Venue Details
                </label>
                <Textarea
                  id="venue-details"
                  className="manage-program-textarea"
                  placeholder="Enter venue name, address, and any special instructions..."
                  value={programData.venueDetails}
                  onChange={(e) => updateField("venueDetails", e.target.value)}
                />
                <p className="manage-program-hint">Include full address and any directions or parking info.</p>
              </div>
            </div>
          </div>

          {/* Schedule Section */}
          <div className="manage-program-section">
            <div className="manage-program-section-header">
              <div className="manage-program-section-icon">
                <CalendarIcon className="w-5 h-5" />
              </div>
              <div>
                <h2 className="manage-program-section-title">Schedule</h2>
                <p className="manage-program-section-subtitle">Set when your program starts and ends</p>
              </div>
            </div>
            
            <div className="manage-program-section-content">
              <div className="manage-program-date-grid">
                <div className="manage-program-field">
                  <label className="manage-program-label">Program Start</label>
                  <RequiredDateTimeSelect
                    date={programData.startDate}
                    setDate={(date) => updateField("startDate", date)}
                    time={programData.startTime}
                    setTime={(time) => updateField("startTime", time)}
                  />
                </div>
                
                <div className="manage-program-field">
                  <label className="manage-program-label">Program End</label>
                  <RequiredDateTimeSelect
                    date={programData.endDate}
                    setDate={(date) => updateField("endDate", date)}
                    time={programData.endTime}
                    setTime={(time) => updateField("endTime", time)}
                  />
                </div>
              </div>
            </div>
          </div>

          {/* Registration Section */}
          <div className="manage-program-section">
            <div className="manage-program-section-header">
              <div className="manage-program-section-icon">
                <AlertCircle className="w-5 h-5" />
              </div>
              <div>
                <h2 className="manage-program-section-title">Registration Window</h2>
                <p className="manage-program-section-subtitle">Optional: Control when passes can be purchased</p>
              </div>
            </div>
            
            <div className="manage-program-section-content">
              <div className="manage-program-date-grid">
                <div className="manage-program-field">
                  <label className="manage-program-label">Registration Opens</label>
                  <DateTimeSelect
                    date={programData.registrationStartDate}
                    setDate={(date) => updateField("registrationStartDate", date)}
                    time={programData.registrationStartTime}
                    setTime={(time) => updateField("registrationStartTime", time)}
                    enabled={programRegistrationDateEnabled}
                    setEnabled={setProgramRegistrationDateEnabled}
                  />
                </div>
                
                <div className="manage-program-field">
                  <label className="manage-program-label">Registration Closes</label>
                  <DateTimeSelect
                    date={programData.registrationEndDate}
                    setDate={(date) => updateField("registrationEndDate", date)}
                    time={programData.registrationEndTime}
                    setTime={(time) => updateField("registrationEndTime", time)}
                    enabled={programRegistrationDateEnabled}
                    setEnabled={setProgramRegistrationDateEnabled}
                  />
                </div>
              </div>
            </div>
          </div>

          {/* Pass Types Section */}
          <div className="manage-program-section">
            <div className="manage-program-section-header">
              <div className="manage-program-section-icon">
                <Ticket className="w-5 h-5" />
              </div>
              <div>
                <h2 className="manage-program-section-title">Pass Types</h2>
                <p className="manage-program-section-subtitle">Define the different ticket options</p>
              </div>
              <Button
                type="button"
                onClick={() => handleAddPassType()}
                className="manage-program-add-btn"
              >
                <Plus className="w-4 h-4" /> Add Pass
              </Button>
            </div>
            
            <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
              <div className="manage-program-section-content">
                {programData.passTypes.length === 0 ? (
                  <div className="manage-program-empty-state">
                    <Ticket className="w-12 h-12 opacity-30" />
                    <p>No pass types yet</p>
                    <span>Add your first pass type to get started</span>
                  </div>
                ) : (
                  <div className="manage-program-passes-grid">
                    {programData.passTypes.map((passType) => (
                      <div key={passType.id} className="manage-program-pass-card">
                        <div className="manage-program-pass-header">
                          <h3 className="manage-program-pass-name">{passType.name}</h3>
                          <div className="manage-program-pass-actions">
                            <button
                              type="button"
                              className="manage-program-pass-action"
                              onClick={() => handleEditPassType(passType)}
                            >
                              <Edit className="w-4 h-4" />
                            </button>
                            <button
                              type="button"
                              className="manage-program-pass-action delete"
                              onClick={() => handleDeletePassType(passType.id)}
                            >
                              <Trash className="w-4 h-4" />
                            </button>
                          </div>
                        </div>
                        <div className="manage-program-pass-price">
                          <span className="currency">$</span>
                          <span className="amount">{passType.price}</span>
                        </div>
                        {passType.totalAvailable && (
                          <p className="manage-program-pass-inventory">
                            {passType.totalAvailable} passes available
                          </p>
                        )}
                        {passType.description && (
                          <p className="manage-program-pass-description">{passType.description}</p>
                        )}
                      </div>
                    ))}
                  </div>
                )}
              </div>
              
              <DialogContent className="manage-program-dialog">
                <DialogHeader>
                  <DialogTitle>
                    {currentPassType?.id ? "Edit Pass Type" : "Add Pass Type"}
                  </DialogTitle>
                  <DialogDescription>
                    Configure the details for this pass type
                  </DialogDescription>
                </DialogHeader>

                <div className="manage-program-dialog-form">
                  <div className="manage-program-field">
                    <Label htmlFor="pass-type-name">Pass Name</Label>
                    <Input
                      id="pass-type-name"
                      className="manage-program-input"
                      value={currentPassType?.name}
                      onChange={(e) =>
                        setCurrentPassType(
                          currentPassType
                            ? { ...currentPassType, name: e.target.value }
                            : undefined,
                        )
                      }
                      placeholder="e.g. General Admission, VIP, Early Bird"
                    />
                  </div>

                  <div className="manage-program-dialog-row">
                    <div className="manage-program-field">
                      <Label htmlFor="pass-type-price">Price ($)</Label>
                      <Input
                        id="pass-type-price"
                        type="number"
                        value={currentPassType?.price}
                        onChange={(e) =>
                          setCurrentPassType(
                            currentPassType
                              ? {
                                  ...currentPassType,
                                  price: Number.parseFloat(e.target.value),
                                }
                              : undefined,
                          )
                        }
                        className="manage-program-input"
                      />
                    </div>

                    <div className="manage-program-field">
                      <Label htmlFor="pass-type-total-available">Quantity</Label>
                      <Input
                        id="pass-type-total-available"
                        type="number"
                        value={currentPassType?.totalAvailable}
                        onChange={(e) =>
                          setCurrentPassType(
                            currentPassType
                              ? {
                                  ...currentPassType,
                                  totalAvailable: Number.parseFloat(e.target.value),
                                }
                              : undefined,
                          )
                        }
                        className="manage-program-input"
                        placeholder="Leave blank for unlimited"
                      />
                    </div>
                  </div>

                  <div className="manage-program-field">
                    <Label htmlFor="pass-type-description">Description</Label>
                    <Textarea
                      id="pass-type-description"
                      className="manage-program-textarea"
                      value={currentPassType?.description}
                      onChange={(e) =>
                        setCurrentPassType(
                          currentPassType
                            ? {
                                ...currentPassType,
                                description: e.target.value,
                              }
                            : undefined,
                        )
                      }
                      placeholder="What's included with this pass?"
                    />
                  </div>
                </div>
                
                <DialogFooter>
                  <Button
                    type="button"
                    className="manage-program-dialog-save"
                    onClick={handleSavePassType}
                  >
                    Save Pass Type
                  </Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>

          {/* Status Section */}
          <div className="manage-program-section">
            <div className="manage-program-section-header">
              <div className="manage-program-section-icon">
                <AlertCircle className="w-5 h-5" />
              </div>
              <div>
                <h2 className="manage-program-section-title">Publish Status</h2>
                <p className="manage-program-section-subtitle">Control when your program is visible</p>
              </div>
            </div>
            
            <div className="manage-program-section-content">
              <div className="manage-program-status-selector">
                <Select
                  value={programData.status}
                  onValueChange={(value) => updateField("status", value)}
                >
                  <SelectTrigger className="manage-program-status-trigger">
                    <SelectValue placeholder="Select Status" />
                  </SelectTrigger>
                  <SelectContent className="manage-program-status-content">
                    <SelectItem value={ProgramStatusEnum.DRAFT}>
                      <div className="manage-program-status-option">
                        <span className="status-dot draft"></span>
                        Draft
                      </div>
                    </SelectItem>
                    <SelectItem value={ProgramStatusEnum.PUBLISHED}>
                      <div className="manage-program-status-option">
                        <span className="status-dot published"></span>
                        Published
                      </div>
                    </SelectItem>
                  </SelectContent>
                </Select>
                <p className="manage-program-hint">
                  Draft programs are only visible to you. Published programs are live and can accept registrations.
                </p>
              </div>
            </div>
          </div>

          {/* Error Alert */}
          {error && (
            <Alert variant="destructive" className="manage-program-error">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}

          {/* Submit Button */}
          <div className="manage-program-submit">
            <Button type="submit" className="manage-program-submit-btn">
              {isEditMode ? "Update Program" : "Create Program"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default DashboardManageProgramPage;
