import {useEffect, useRef} from 'react';

// Optimized custom cursor - stops animation when mouse is idle
export function useCursorEffect() {
  const rafId = useRef<number>(0);
  const isAnimating = useRef(false);
  const idleTimeout = useRef<ReturnType<typeof setTimeout> | undefined>(undefined);
  
  useEffect(() => {
    const ring = document.getElementById('cursor-ring');
    const dotsContainer = document.getElementById('cursor-dots');
    const dots = dotsContainer?.querySelectorAll('.cursor-dot');
    
    if (!ring || !dots || dots.length === 0) return;
    
    let mouseX = 0;
    let mouseY = 0;
    let ringX = 0;
    let ringY = 0;
    
    // Store positions for each dot
    const dotPositions = Array.from(dots).map(() => ({ x: 0, y: 0 }));
    
    const startAnimation = () => {
      if (!isAnimating.current) {
        isAnimating.current = true;
        animate();
      }
      // Reset idle timeout
      if (idleTimeout.current) {
        clearTimeout(idleTimeout.current);
      }
      // Stop animation after 500ms of no mouse movement
      idleTimeout.current = setTimeout(() => {
        isAnimating.current = false;
        cancelAnimationFrame(rafId.current);
      }, 500);
    };
    
    const handleMouseMove = (e: MouseEvent) => {
      mouseX = e.clientX;
      mouseY = e.clientY;
      startAnimation();
    };
    
    const animate = () => {
      if (!isAnimating.current) return;
      
      // Smooth follow for ring
      ringX += (mouseX - ringX) * 0.3;
      ringY += (mouseY - ringY) * 0.3;
      ring.style.left = `${ringX}px`;
      ring.style.top = `${ringY}px`;
      
      // Update dot positions
      dots.forEach((dot, i) => {
        const speed = 0.12 - (i * 0.012);
        dotPositions[i].x += (ringX - dotPositions[i].x) * speed;
        dotPositions[i].y += (ringY - dotPositions[i].y) * speed;
        (dot as HTMLElement).style.left = `${dotPositions[i].x}px`;
        (dot as HTMLElement).style.top = `${dotPositions[i].y}px`;
      });
      
      rafId.current = requestAnimationFrame(animate);
    };
    
    // Handle hover states
    const handleMouseEnter = () => {
      ring.style.transform = 'translate(-50%, -50%) scale(1.5)';
    };
    
    const handleMouseLeave = () => {
      ring.style.transform = 'translate(-50%, -50%) scale(1)';
    };
    
    const addHoverListeners = () => {
      const interactiveElements = document.querySelectorAll('a, button, input, [role="button"]');
      interactiveElements.forEach(el => {
        el.addEventListener('mouseenter', handleMouseEnter);
        el.addEventListener('mouseleave', handleMouseLeave);
      });
      return interactiveElements;
    };
    
    const elements = addHoverListeners();
    window.addEventListener('mousemove', handleMouseMove);
    
    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
      cancelAnimationFrame(rafId.current);
      if (idleTimeout.current) clearTimeout(idleTimeout.current);
      elements.forEach(el => {
        el.removeEventListener('mouseenter', handleMouseEnter);
        el.removeEventListener('mouseleave', handleMouseLeave);
      });
    };
  }, []);
}

export default function CustomCursor() {
  useCursorEffect();
  return null;
}
