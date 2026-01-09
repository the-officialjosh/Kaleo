import {StrictMode} from "react";
import {createRoot} from "react-dom/client";
import "./index.css";
import AttendeeLandingPage from "./pages/attendee-landing-page.tsx";
import {AuthProvider} from "react-oidc-context";
import {createBrowserRouter, RouterProvider} from "react-router";
import OrganizersLandingPage from "./pages/organizers-landing-page.tsx";
import DashboardManageProgramPage from "./pages/dashboard-manage-program-page.tsx";
import LoginPage from "./pages/login-page.tsx";
import ProtectedRoute from "./components/protected-route.tsx";
import CallbackPage from "./pages/callback-page.tsx";
import DashboardListProgramsPage from "./pages/dashboard-list-programs-page.tsx";
import PublishedProgramsPage from "./pages/published-programs-page.tsx";
import PurchasePassPage from "./pages/purchase-pass-page.tsx";
import DashboardListPasses from "./pages/dashboard-list-passes.tsx";
import DashboardPage from "./pages/dashboard-page.tsx";
import DashboardViewPassPage from "./pages/dashboard-view-pass-page.tsx";
import DashboardValidateQrPage from "./pages/dashboard-validate-qr-page.tsx";
import CustomCursor from "./components/custom-cursor.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    Component: AttendeeLandingPage,
  },
  {
    path: "/callback",
    Component: CallbackPage,
  },
  {
    path: "/login",
    Component: LoginPage,
  },
  {
    path: "/programs/:id",
    Component: PublishedProgramsPage,
  },
  {
    path: "/programs/:programId/purchase/:passTypeId",
    element: (
      <ProtectedRoute>
        <PurchasePassPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/organizers",
    Component: OrganizersLandingPage,
  },
  {
    path: "/dashboard",
    element: (
      <ProtectedRoute>
        <DashboardPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/programs",
    element: (
      <ProtectedRoute>
        <DashboardListProgramsPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/passes",
    element: (
      <ProtectedRoute>
        <DashboardListPasses />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/passes/:id",
    element: (
      <ProtectedRoute>
        <DashboardViewPassPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/validate-qr",
    element: (
      <ProtectedRoute>
        <DashboardValidateQrPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/programs/create",
    element: (
      <ProtectedRoute>
        <DashboardManageProgramPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/dashboard/programs/update/:id",
    element: (
      <ProtectedRoute>
        <DashboardManageProgramPage />
      </ProtectedRoute>
    ),
  },
]);

const oidcConfig = {
  authority: "http://localhost:9095/realms/kaleo-events",
  client_id: "kaleo-event-app",
  redirect_uri: "http://localhost:5173/callback",
};

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider {...oidcConfig}>
      <div id="cursor-ring"></div>
      <div id="cursor-dots">
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
        <div className="cursor-dot"></div>
      </div>
      <CustomCursor />
      <RouterProvider router={router} />
    </AuthProvider>
  </StrictMode>,
);

