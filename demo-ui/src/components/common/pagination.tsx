import React from "react";
import {ArrowLeft, ArrowRight, ChevronsLeft, ChevronsRight,} from "lucide-react";

interface PaginationProps {
  currentPage: number; // 0-based (Spring Boot format)
  totalPages: number;
  onPageChange: (page: number) => void; // Returns 0-based page number
  itemsPerPage: number;
  totalItems: number;
  onItemsPerPageChange: (itemsPerPage: number) => void;
}

export const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  itemsPerPage,
  totalItems,
  onItemsPerPageChange,
}) => {
  // Convert to 1-based for display
  const displayPage = currentPage + 1;
  const startItem = currentPage * itemsPerPage + 1;
  const endItem = Math.min((currentPage + 1) * itemsPerPage, totalItems);

  // Don't render if there's nothing to paginate
  if (totalItems === 0) return null;

  return (
    <div className="pagination-container"  style={{background: "transparent"}}>
      <div className="pagination-info">
        <span className="pagination-showing">
          Showing {startItem} to {endItem} of {totalItems} results
        </span>
        <div className="pagination-per-page">
          <span className="pagination-label">Show:</span>
          <select
            value={itemsPerPage}
            onChange={(e) => {
              onItemsPerPageChange(Number(e.target.value));
              onPageChange(0); // Reset to first page when changing page size
            }}
            className="pagination-select"
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
          <span className="pagination-label">per page</span>
        </div>
      </div>

      <div className="pagination-controls">
        <button
          onClick={() => onPageChange(0)}
          disabled={currentPage === 0}
          className="pagination-btn"
          aria-label="First page"
        >
          <ChevronsLeft className="h-4 w-4" />
        </button>
        <button
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 0}
          className="pagination-btn"
          aria-label="Previous page"
        >
          <ArrowLeft className="h-4 w-4" />
        </button>

        <div className="pagination-numbers">
          {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
            let pageNum; // 1-based for display
            if (totalPages <= 5) {
              pageNum = i + 1;
            } else if (displayPage <= 3) {
              pageNum = i + 1;
            } else if (displayPage >= totalPages - 2) {
              pageNum = totalPages - 4 + i;
            } else {
              pageNum = displayPage - 2 + i;
            }

            return (
              <button
                key={pageNum}
                onClick={() => onPageChange(pageNum - 1)} // Convert back to 0-based
                className={`pagination-number ${
                  displayPage === pageNum ? "active" : ""
                }`}
              >
                {pageNum}
              </button>
            );
          })}
        </div>

        <button
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage >= totalPages - 1}
          className="pagination-btn"
          aria-label="Next page"
        >
          <ArrowRight className="h-4 w-4" />
        </button>
        <button
          onClick={() => onPageChange(totalPages - 1)}
          disabled={currentPage >= totalPages - 1}
          className="pagination-btn"
          aria-label="Last page"
        >
          <ChevronsRight className="h-4 w-4" />
        </button>
      </div>
    </div>
  );
};

export default Pagination;
