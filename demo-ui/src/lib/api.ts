import {
  CreateProgramRequest,
  isErrorResponse,
  PassDetails,
  PassSummary,
  PassValidationRequest,
  PassValidationResponse,
  ProgramDetails,
  ProgramSummary,
  PublishedProgramDetails,
  PublishedProgramSummary,
  SpringBootPagination,
  StaffProgramSummary,
  UpdateProgramRequest,
} from "@/domain/domain";

export const createProgram = async (
  accessToken: string,
  request: CreateProgramRequest,
): Promise<void> => {
  const response = await fetch("/api/v1/programs", {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const responseBody = await response.json();

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

export const updateProgram = async (
  accessToken: string,
  id: string,
  request: UpdateProgramRequest,
): Promise<void> => {
  const response = await fetch(`/api/v1/programs/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const responseBody = await response.json();

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

// Helper function to transform Spring Boot 4.x pagination response to our interface
// eslint-disable-next-line @typescript-eslint/no-explicit-any
function transformPaginationResponse<T>(data: any): SpringBootPagination<T> {
  // Handle new Spring Boot 4.x format with nested page object
  if (data.page && data.content) {
    return {
      content: data.content,
      number: data.page.number,
      size: data.page.size,
      totalElements: data.page.totalElements,
      totalPages: data.page.totalPages,
      first: data.page.number === 0,
      last: data.page.number >= data.page.totalPages - 1,
      numberOfElements: data.content.length,
      empty: data.content.length === 0,
      pageable: {
        pageNumber: data.page.number,
        pageSize: data.page.size,
        offset: data.page.number * data.page.size,
        paged: true,
        unpaged: false,
        sort: { empty: true, sorted: false, unsorted: true },
      },
      sort: { empty: true, sorted: false, unsorted: true },
    } as SpringBootPagination<T>;
  }
  // Already in the expected format
  return data as SpringBootPagination<T>;
}

export const listPrograms = async (
  accessToken: string,
  page: number,
  size: number = 10,
): Promise<SpringBootPagination<ProgramSummary>> => {
  const response = await fetch(`/api/v1/programs?page=${page}&size=${size}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  const data = await response.json();
  return transformPaginationResponse<ProgramSummary>(data);
};

export const getProgram = async (
  accessToken: string,
  id: string,
): Promise<ProgramDetails> => {
  const response = await fetch(`/api/v1/programs/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  return (await response.json()) as ProgramDetails;
};

export const deleteProgram = async (
  accessToken: string,
  id: string,
): Promise<void> => {
  const response = await fetch(`/api/v1/programs/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (!response.ok) {
    const text = await response.text(); // SAFE
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // ignore JSON parse error
      }
    }
    throw new Error(`Delete failed with status ${response.status}`);
  }
};

export const listPublishedPrograms = async (
  page: number,
): Promise<SpringBootPagination<PublishedProgramSummary>> => {
  const response = await fetch(
    `/api/v1/published-programs?page=${page}&size=6`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    },
  );

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  const data = await response.json();
  return transformPaginationResponse<PublishedProgramSummary>(data);
};

export const searchPublishedPrograms = async (
  query: string,
  page: number,
): Promise<SpringBootPagination<PublishedProgramSummary>> => {
  const response = await fetch(
    `/api/v1/published-programs?q=${encodeURIComponent(query)}&page=${page}&size=6`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    },
  );

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  const data = await response.json();
  return transformPaginationResponse<PublishedProgramSummary>(data);
};

export const getPublishedProgram = async (
  id: string,
): Promise<PublishedProgramDetails> => {
  const response = await fetch(`/api/v1/published-programs/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  return (await response.json()) as PublishedProgramDetails;
};

export const purchasePass = async (
  accessToken: string,
  programId: string,
  passTypeId: string,
): Promise<void> => {
  const response = await fetch(
    `/api/v1/programs/${programId}/pass-types/${passTypeId}/passes`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
    },
  );

  if (!response.ok) {
    const responseBody = await response.json();
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }
};

export const listPasses = async (
  accessToken: string,
  page: number,
  size: number = 10,
): Promise<SpringBootPagination<PassSummary>> => {
  const response = await fetch(`/api/v1/passes?page=${page}&size=${size}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  const data = await response.json();
  return transformPaginationResponse<PassSummary>(data);
};

export const getPass = async (
  accessToken: string,
  id: string,
): Promise<PassDetails> => {
  const response = await fetch(`/api/v1/passes/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  return (await response.json()) as PassDetails;
};

export const getPassQr = async (
  accessToken: string,
  id: string,
): Promise<Blob> => {
  const response = await fetch(`/api/v1/passes/${id}/qr-codes`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (response.ok) {
    return await response.blob();
  } else {
    throw new Error("Unable to get pass QR code");
  }
};

// List programs available for staff validation
export const listStaffPrograms = async (
  accessToken: string,
  page: number,
): Promise<SpringBootPagination<StaffProgramSummary>> => {
  const response = await fetch(`/api/v1/pass-validations?page=${page}&size=20`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    if (text) {
      try {
        const body = JSON.parse(text);
        if (isErrorResponse(body)) {
          throw new Error(body.error);
        }
      } catch {
        // Not a JSON error response
      }
    }
    throw new Error(
      `Request failed with status ${response.status}: ${text || "No response body"}`,
    );
  }

  const data = await response.json();
  return transformPaginationResponse<StaffProgramSummary>(data);
};

export const validatePass = async (
  accessToken: string,
  request: PassValidationRequest,
): Promise<PassValidationResponse> => {
  const response = await fetch(`/api/v1/pass-validations`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const responseBody = await response.json();

  if (!response.ok) {
    if (isErrorResponse(responseBody)) {
      throw new Error(responseBody.error);
    } else {
      console.error(JSON.stringify(responseBody));
      throw new Error("An unknown error occurred");
    }
  }

  return responseBody as PassValidationResponse;
};
