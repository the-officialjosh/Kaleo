export interface ErrorResponse {
  error: string;
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const isErrorResponse = (obj: any): obj is ErrorResponse => {
  return (
    obj &&
    typeof obj === "object" &&
    "error" in obj &&
    typeof obj.error === "string"
  );
};

export enum ProgramStatusEnum {
  DRAFT = "DRAFT",
  PUBLISHED = "PUBLISHED",
  CANCELLED = "CANCELLED",
  COMPLETED = "COMPLETED",
}

export interface CreatePassTypeRequest {
  name: string;
  price: number;
  description: string;
  totalAvailable?: number;
}

export interface CreateProgramRequest {
  name: string;
  startTime: string;
  endTime: string;
  venue: string;
  registrationStart?: string;
  registrationEnd?: string;
  status: ProgramStatusEnum;
  passTypes: CreatePassTypeRequest[];
}

export interface UpdatePassTypeRequest {
  id: string | undefined;
  name: string;
  price: number;
  description: string;
  totalAvailable?: number;
}

export interface UpdateProgramRequest {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  venue: string;
  registrationStart?: string;
  registrationEnd?: string;
  status: ProgramStatusEnum;
  passTypes: UpdatePassTypeRequest[];
}

export interface PassTypeSummary {
  id: string;
  name: string;
  price: number;
  description: string;
  totalAvailable?: number;
}

export interface ProgramSummary {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  venue: string;
  registrationStart?: string;
  registrationEnd?: string;
  status: ProgramStatusEnum;
  passTypes: PassTypeSummary[];
}

export interface PublishedProgramSummary {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  venue: string;
}

export interface PassTypeDetails {
  id: string;
  name: string;
  price: number;
  description: string;
  totalAvailable?: number;
}

export interface ProgramDetails {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  venue: string;
  registrationStart?: string;
  registrationEnd?: string;
  status: ProgramStatusEnum;
  passTypes: PassTypeDetails[];
  createdAt: string;
  updatedAt: string;
}

export interface SpringBootPagination<T> {
  content: T[]; // The actual data items for the current page
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean; // Whether this is the last page
  totalElements: number; // Total number of items across all pages
  totalPages: number; // Total number of pages
  size: number; // Page size (items per page)
  number: number; // Current page number (zero-based)
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean; // Whether this is the first page
  numberOfElements: number; // Number of items in the current page
  empty: boolean; // Whether the current page has no items
}

export interface PublishedProgramPassTypeDetails {
  id: string;
  name: string;
  price: number;
  description: string;
}

export interface PublishedProgramDetails {
  id: string;
  name: string;
  startTime: string;
  endTime: string;
  venue: string;
  registrationStart?: string;
  registrationEnd?: string;
  passTypes: PublishedProgramPassTypeDetails[];
}

export enum PassStatus {
  ACTIVE = "ACTIVE",
  PURCHASED = "PURCHASED",
  CANCELLED = "CANCELLED",
  USED = "USED",
}

export interface PassSummary {
  id: string;
  status: PassStatus;
  manualCode: string;
  createdAt: string;
  passTypeName: string;
  passTypePrice: number;
  programId: string;
  programName: string;
  programStartTime: string;
  programEndTime: string;
  programVenue: string;
}

export interface PassDetails {
  id: string;
  status: PassStatus;
  manualCode: string;
  createdAt: string;
  passTypeName: string;
  passTypePrice: number;
  programId: string;
  programName: string;
  programStartTime: string;
  programEndTime: string;
  programVenue: string;
}

export enum PassValidationMethod {
  QR_SCAN = "QR_SCAN",
  MANUAL = "MANUAL",
}

export enum PassValidationStatus {
  VALID = "VALID",
  INVALID = "INVALID",
  EXPIRED = "EXPIRED",
}

// For staff program listing in pass validation
export interface StaffProgramSummary {
  id: string;
  name: string;
}

export interface PassValidationRequest {
  programId: string;
  qrCodeId?: string;
  manualCode?: string;
  method: PassValidationMethod;
}

export interface PassValidationResponse {
  passId: string;
  status: PassValidationStatus;
  message?: string;
}
