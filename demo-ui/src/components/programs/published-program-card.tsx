import {PublishedProgramSummary} from "@/domain/domain";
import {ArrowRight, Calendar, Heart, MapPin, Share2} from "lucide-react";
import {format} from "date-fns";
import {Link} from "react-router";
import RandomProgramImage from "./random-program-image";

interface PublishedProgramCardProperties {
  publishedProgram: PublishedProgramSummary;
}

const PublishedProgramCard: React.FC<PublishedProgramCardProperties> = ({
  publishedProgram,
}) => {
  return (
    <Link to={`/programs/${publishedProgram.id}`} className="program-card-link">
      <article className="program-card">
        {/* Card Image */}
        <div className="program-card-image">
          <RandomProgramImage />
          <div className="program-card-image-overlay" />
          
          {/* Date Badge */}
          {publishedProgram.startTime && (
            <div className="program-card-date-badge">
              <Calendar className="w-3.5 h-3.5" />
              <span>{format(publishedProgram.startTime, "MMM d")}</span>
            </div>
          )}
        </div>

        {/* Card Content */}
        <div className="program-card-content">
          <h3 className="program-card-title">{publishedProgram.name}</h3>
          
          <div className="program-card-meta">
            <div className="program-card-venue">
              <MapPin className="w-4 h-4" />
              <span>{publishedProgram.venue || "Venue TBD"}</span>
            </div>
            
            {publishedProgram.startTime && publishedProgram.endTime && (
              <div className="program-card-dates">
                <Calendar className="w-4 h-4" />
                <span>
                  {format(publishedProgram.startTime, "PP")} - {format(publishedProgram.endTime, "PP")}
                </span>
              </div>
            )}
          </div>

          {/* Card Actions */}
          <div className="program-card-actions">
            <div className="program-card-actions-left">
              <button 
                className="program-card-action-btn" 
                onClick={(e) => e.preventDefault()}
                aria-label="Save to favorites"
              >
                <Heart className="w-5 h-5" />
              </button>
              <button 
                className="program-card-action-btn" 
                onClick={(e) => e.preventDefault()}
                aria-label="Share program"
              >
                <Share2 className="w-5 h-5" />
              </button>
            </div>
            <div className="program-card-view-btn">
              <span>View</span>
              <ArrowRight className="w-4 h-4" />
            </div>
          </div>
        </div>
      </article>
    </Link>
  );
};

export default PublishedProgramCard;
