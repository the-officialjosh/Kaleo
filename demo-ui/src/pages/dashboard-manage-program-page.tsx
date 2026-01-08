import NavBar from "@/components/nav-bar";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";
import {Calendar} from "@/components/ui/calendar";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
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
    <div className="min-h-screen bg-black text-white">
      <NavBar />
      <div className="container mx-auto px-4 py-8 max-w-xl">
        <div className="mb-6">
          <h1 className="text-2xl font-bold">
            {isEditMode ? "Edit Program" : "Create a New Program"}
          </h1>
          {isEditMode ? (
            <>
              {programData.id && (
                <p className="text-sm text-gray-400">ID: {programData.id}</p>
              )}
              {programData.createdAt && (
                <p className="text-sm text-gray-400">
                  Created At: {format(new Date(programData.createdAt), "PPP")}
                </p>
              )}
              {programData.updatedAt && (
                <p className="text-sm text-gray-400">
                  Updated At: {format(new Date(programData.updatedAt), "PPP")}
                </p>
              )}
            </>
          ) : (
            <p>Fill out the form below to create your program</p>
          )}
        </div>

        <form onSubmit={handleFormSubmit} className="space-y-4">
          {/* Program Name */}
          <div>
            <div>
              <label htmlFor="program-name" className="text-sm font-medium">
                Program Name
              </label>
              <Input
                id="program-name"
                className="bg-gray-900 border-gray-700 text-white"
                placeholder="Program Name"
                value={programData.name}
                onChange={(e) => updateField("name", e.target.value)}
                required
              />
            </div>
            <p className="text-gray-400 text-xs">
              This is the public name of your program.
            </p>
          </div>

          {/* Program Start Date Time */}
          <div>
            <label className="text-sm font-medium">Program Start</label>
            <RequiredDateTimeSelect
              date={programData.startDate}
              setDate={(date) => updateField("startDate", date)}
              time={programData.startTime}
              setTime={(time) => updateField("startTime", time)}
            />
            <p className="text-gray-400 text-xs">
              The date and time that the program starts.
            </p>
          </div>

          {/* Program End Date Time */}
          <div>
            <label className="text-sm font-medium">Program End</label>
            <RequiredDateTimeSelect
              date={programData.endDate}
              setDate={(date) => updateField("endDate", date)}
              time={programData.endTime}
              setTime={(time) => updateField("endTime", time)}
            />
            <p className="text-gray-400 text-xs">
              The date and time that the program ends.
            </p>
          </div>

          <div>
            <label htmlFor="venue-details" className="text-sm font-medium">
              Venue Details
            </label>
            <Textarea
              id="venue-details"
              className="bg-gray-900 border-gray-700 min-h-[100px]"
              value={programData.venueDetails}
              onChange={(e) => updateField("venueDetails", e.target.value)}
            />
            <p className="text-gray-400 text-xs">
              Details about the venue, please include as much detail as
              possible.
            </p>
          </div>

          {/* Program Registration Start Date Time */}
          <div>
            <label className="text-sm font-medium">Registration Start</label>
            <DateTimeSelect
              date={programData.registrationStartDate}
              setDate={(date) => updateField("registrationStartDate", date)}
              time={programData.registrationStartTime}
              setTime={(time) => updateField("registrationStartTime", time)}
              enabled={programRegistrationDateEnabled}
              setEnabled={setProgramRegistrationDateEnabled}
            />
            <p className="text-gray-400 text-xs">
              The date and time that pass are available to purchase for the
              program.
            </p>
          </div>

          {/* Program Registration End Date Time */}
          <div>
            <label className="text-sm font-medium">Registration End</label>
            <DateTimeSelect
              date={programData.registrationEndDate}
              setDate={(date) => updateField("registrationEndDate", date)}
              time={programData.registrationEndTime}
              setTime={(time) => updateField("registrationEndTime", time)}
              enabled={programRegistrationDateEnabled}
              setEnabled={setProgramRegistrationDateEnabled}
            />
            <p className="text-gray-400 text-xs">
              The date and time that pass are available to purchase for the
              program.
            </p>
          </div>

          {/* pass Types */}
          <div>
            <Card className="bg-gray-900 border-gray-700 text-white">
              <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
                <CardHeader>
                  <div className="flex justify-between">
                    <CardTitle className="flex gap-2 items-center text-sm">
                      <Ticket />
                      Pass Types
                    </CardTitle>
                    <Button
                      type="button"
                      onClick={() => handleAddPassType()}
                      className="bg-gray-800 border-gray-700 text-white"
                    >
                      <Plus /> Add Pass Type
                    </Button>
                  </div>
                </CardHeader>

                <CardContent className="space-y-2">
                  {programData.passTypes.map((passType) => {
                    return (
                      <div className="bg-gray-700 w-full p-4 rounded-lg border-gray-600">
                        <div className="flex justify-between items-center">
                          {/* Left */}
                          <div>
                            <div className="flex gap-4">
                              <p className="text-small font-medium">
                                {passType.name}
                              </p>
                              <Badge
                                variant="outline"
                                className="border-gray-600 text-white font-normal text-xs"
                              >
                                ${passType.price}
                              </Badge>
                            </div>
                            {passType.totalAvailable && (
                              <p className="text-gray-400">
                                {passType.totalAvailable} passes available
                              </p>
                            )}
                          </div>
                          {/* Right */}
                          <div className="flex gap-2">
                            <Button
                              type="button"
                              variant="ghost"
                              onClick={() => handleEditPassType(passType)}
                            >
                              <Edit />
                            </Button>
                            <Button
                              type="button"
                              variant="ghost"
                              className="text-red-400"
                              onClick={() => handleDeletePassType(passType.id)}
                            >
                              <Trash />
                            </Button>
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </CardContent>
                <DialogContent className="bg-gray-900 border-gray-700 text-white">
                  <DialogHeader>
                    <DialogTitle>Add Pass Type</DialogTitle>
                    <DialogDescription className="text-gray-400">
                      Please enter details of the pass type
                    </DialogDescription>
                  </DialogHeader>

                  {/* Pass Type Name */}
                  <div className="space-y-1">
                    <Label htmlFor="pass-type-name">Name</Label>
                    <Input
                      id="pass-type-name"
                      className="bg-gray-800 border-gray-700"
                      value={currentPassType?.name}
                      onChange={(e) =>
                        setCurrentPassType(
                          currentPassType
                            ? { ...currentPassType, name: e.target.value }
                            : undefined,
                        )
                      }
                      placeholder="e.g General Admission, VIP, etc."
                    />
                  </div>

                  <div className="flex gap-4">
                    {/* Price */}
                    <div className="space-y-1 w-full">
                      <Label htmlFor="pass-type-price">Price</Label>
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
                        className="bg-gray-800 border-gray-700"
                      />
                    </div>

                    {/* Total Available */}
                    <div className="space-y-1 w-full">
                      <Label htmlFor="pass-type-total-available">
                        Total Available
                      </Label>
                      <Input
                        id="pass-type-total-available"
                        type="number"
                        value={currentPassType?.totalAvailable}
                        onChange={(e) =>
                          setCurrentPassType(
                            currentPassType
                              ? {
                                  ...currentPassType,
                                  totalAvailable: Number.parseFloat(
                                    e.target.value,
                                  ),
                                }
                              : undefined,
                          )
                        }
                        className="bg-gray-800 border-gray-700"
                      />
                    </div>
                  </div>

                  {/* Pass Type Description */}
                  <div className="space-y-1">
                    <Label htmlFor="pass-type-description">Description</Label>
                    <Textarea
                      id="pass-type-description"
                      className="bg-gray-800 border-gray-700"
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
                    />
                  </div>
                  <DialogFooter>
                    <Button
                      className="bg-white text-black hover:bg-gray-300"
                      onClick={handleSavePassType}
                    >
                      Save
                    </Button>
                  </DialogFooter>
                </DialogContent>
              </Dialog>
            </Card>
          </div>

          {/* Status */}
          <div className="space-y-1">
            <Label>Status</Label>
            <Select
              value={programData.status}
              onValueChange={(value) => updateField("status", value)}
            >
              <SelectTrigger className="w-[180px] bg-gray-900 border-gray-700 text-white">
                <SelectValue placeholder="Select Program Status" />
              </SelectTrigger>
              <SelectContent className="bg-gray-900 border-gray-700 text-white">
                <SelectItem value={ProgramStatusEnum.DRAFT}>Draft</SelectItem>
                <SelectItem value={ProgramStatusEnum.PUBLISHED}>
                  Published
                </SelectItem>
              </SelectContent>
            </Select>
            <p className="text-gray-400 text-xs">
              Please select the status of the new program.
            </p>
          </div>

          {error && (
            <Alert variant="destructive" className="bg-gray-900 border-red-700">
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}

          <div>
            <Button onClick={handleFormSubmit}>
              {isEditMode ? "Update" : "Submit"}
            </Button>
          </div>
        </form>
        {/* For Development Only */}
        {/* <p className="mt-8 font-mono text-white">{JSON.stringify(programData)}</p> */}
      </div>
    </div>
  );
};

export default DashboardManageProgramPage;
