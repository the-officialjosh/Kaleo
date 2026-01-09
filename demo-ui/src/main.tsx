import {StrictMode} from "react";
import {createRoot} from "react-dom/client";
import "./index.css";
import {AuthProvider} from "react-oidc-context";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router";

// Pages
import AttendeeLandingPage from "./pages/public/attendee-landing-page";
import OrganizersLandingPage from "./pages/public/organizers-landing-page";
import PublishedProgramsPage from "./pages/public/published-programs-page";
import PurchasePassPage from "./pages/public/purchase-pass-page";
import LoginPage from "./pages/auth/login-page";
import CallbackPage from "./pages/auth/callback-page";
import DashboardPage from "./pages/dashboard/dashboard-page";
import DashboardListProgramsPage from "./pages/dashboard/dashboard-list-programs-page";
import DashboardManageProgramPage from "./pages/dashboard/dashboard-manage-program-page";
import DashboardListPasses from "./pages/dashboard/dashboard-list-passes";
import DashboardViewPassPage from "./pages/dashboard/dashboard-view-pass-page";
import DashboardValidateQrPage from "./pages/dashboard/dashboard-validate-qr-page";

// Components
import {CustomCursor, ProtectedRoute} from "./components/common";
import {ErrorBoundary, ErrorPage, NotFoundPage} from "./components/errors";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Outlet />,
    errorElement: <ErrorPage />,
    children: [
      {
        index: true,
        Component: AttendeeLandingPage,
      },
      {
        path: "callback",
        Component: CallbackPage,
      },
      {
        path: "login",
        Component: LoginPage,
      },
      {
        path: "programs/:id",
        Component: PublishedProgramsPage,
      },
      {
        path: "programs/:programId/purchase/:passTypeId",
        element: (
          <ProtectedRoute>
            <PurchasePassPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "organizers",
        Component: OrganizersLandingPage,
      },
      {
        path: "dashboard",
        element: (
          <ProtectedRoute>
            <DashboardPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "dashboard/programs",
        element: (
          <ProtectedRoute>
            <DashboardListProgramsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "dashboard/passes",
        element: (
          <ProtectedRoute>
            <DashboardListPasses />
          </ProtectedRoute>
        ),
      },
      {
        path: "dashboard/passes/:id",
        element: (
          <ProtectedRoute>
            <DashboardViewPassPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "dashboard/validate-qr",
        element: (
          <ProtectedRoute>
            <DashboardValidateQrPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "dashboard/programs/create",
        element: (
          <ProtectedRoute>
            <DashboardManageProgramPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "dashboard/programs/update/:id",
        element: (
          <ProtectedRoute>
            <DashboardManageProgramPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "*",
        Component: NotFoundPage,
      },
    ],
  },
]);


const oidcConfig = {
  authority: "http://localhost:9095/realms/kaleo-events",
  client_id: "kaleo-event-app",
  redirect_uri: "http://localhost:5173/callback",
};

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ErrorBoundary>
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
    </ErrorBoundary>
  </StrictMode>,
);

