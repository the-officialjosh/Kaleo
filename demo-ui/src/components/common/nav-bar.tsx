import {useAuth} from "react-oidc-context";
import {Avatar, AvatarFallback} from "../ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import {LogOut} from "lucide-react";
import {useRoles} from "@/hooks/use-roles";
import {Link} from "react-router";

const NavBar: React.FC = () => {
  const { user, signoutRedirect } = useAuth();
  const { isOrganizer } = useRoles();

  return (
    <div className="dashboard-nav">
      <div className="dashboard-nav-container">
        <div className="dashboard-nav-left">
          <Link to="/" className="dashboard-nav-brand">
            <h1>Kaleo</h1>
          </Link>
          <nav className="dashboard-nav-links">
            {isOrganizer && <Link to="/dashboard/programs" className="dashboard-nav-link">Programs</Link>}
            <Link to="/dashboard/passes" className="dashboard-nav-link">Passes</Link>
          </nav>
        </div>

        <DropdownMenu>
          <DropdownMenuTrigger className="dashboard-nav-avatar-trigger">
            <Avatar className="dashboard-nav-avatar">
              <AvatarFallback className="dashboard-nav-avatar-fallback">
                {user?.profile?.preferred_username?.slice(0, 2).toUpperCase()}
              </AvatarFallback>
            </Avatar>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="dashboard-user-dropdown"
            align="end"
          >
            <div className="dashboard-user-dropdown-header">
              <Avatar className="dashboard-user-dropdown-avatar">
                <AvatarFallback className="dashboard-user-dropdown-avatar-fallback">
                  {user?.profile?.preferred_username?.slice(0, 2).toUpperCase()}
                </AvatarFallback>
              </Avatar>
              <div className="dashboard-user-dropdown-info">
                <p className="dashboard-user-dropdown-name">
                  {user?.profile?.preferred_username}
                </p>
                <p className="dashboard-user-dropdown-email">{user?.profile?.email}</p>
              </div>
            </div>
            <DropdownMenuSeparator className="dashboard-user-dropdown-separator" />
            <DropdownMenuItem
              className="dashboard-user-dropdown-logout"
              onClick={() => signoutRedirect()}
            >
              <LogOut className="w-4 h-4" />
              <span>Log Out</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  );
};

export default NavBar;
