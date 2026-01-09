import {Github, Globe, Heart} from 'lucide-react';

export default function Footer() {
  return (
    <footer className="site-footer">
      <div className="footer-container">
        <div className="footer-brand">
          <img src="/images/kaleo-logo.png" alt="Kaleo" className="footer-logo" />
          <p className="footer-tagline">Making event management simple and beautiful.</p>
        </div>
        
        <div className="footer-links">
          <a 
            href="https://github.com/the-officialjosh/Kaleo" 
            target="_blank" 
            rel="noopener noreferrer"
            className="footer-link"
          >
            <Github size={18} />
            <span>GitHub</span>
          </a>
          <a 
            href="https://joshuaonyema.dev" 
            target="_blank" 
            rel="noopener noreferrer"
            className="footer-link"
          >
            <Globe size={18} />
            <span>Portfolio</span>
          </a>
        </div>
        
        <div className="footer-bottom">
          <p>
            Built with <Heart size={14} className="footer-heart" /> by Joshua Onyema
          </p>
          <p className="footer-year">© {new Date().getFullYear()} Kaleo. Demo project for API testing.</p>
        </div>
      </div>
    </footer>
  );
}
