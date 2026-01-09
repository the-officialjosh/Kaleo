import {useEffect, useState} from 'react';
import {ChevronLeft, ChevronRight, Quote} from 'lucide-react';

interface Testimonial {
  id: number;
  name: string;
  role: string;
  organization: string;
  content: string;
  avatar: string;
}

const testimonials: Testimonial[] = [
  {
    id: 1,
    name: "Pastor Michael Johnson",
    role: "Senior Pastor",
    organization: "Grace Community Church",
    content: "Kaleo transformed how we manage our church events. The QR check-in system is flawless, and our congregation loves the seamless registration experience.",
    avatar: "MJ"
  },
  {
    id: 2,
    name: "Sister Grace Okonkwo",
    role: "Women's Ministry Leader",
    organization: "New Life Fellowship",
    content: "Organizing our women's retreats has never been easier. We can track registrations, send updates, and manage everything from one place.",
    avatar: "GO"
  },
  {
    id: 3,
    name: "Youth Pastor David",
    role: "Youth Director",
    organization: "Cornerstone Church",
    content: "The mobile passes are perfect for our youth group. Kids just show their phones - no more lost paper tickets or manual check-ins.",
    avatar: "YD"
  },
  {
    id: 4,
    name: "Deacon James Adebayo",
    role: "Church Administrator",
    organization: "Faith Tabernacle",
    content: "We've streamlined our entire registration process. From bible studies to special services, Kaleo handles it all beautifully.",
    avatar: "JA"
  },
  {
    id: 5,
    name: "Minister Sarah Chen",
    role: "Small Groups Coordinator",
    organization: "Living Word Church",
    content: "Managing 30+ small groups used to be a nightmare. Now I can see attendance, track growth, and communicate with leaders in one dashboard.",
    avatar: "SC"
  }
];

export default function TestimonialCarousel() {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isAutoPlaying, setIsAutoPlaying] = useState(true);

  useEffect(() => {
    if (!isAutoPlaying) return;
    
    const interval = setInterval(() => {
      setCurrentIndex((prev) => (prev + 1) % testimonials.length);
    }, 5000);

    return () => clearInterval(interval);
  }, [isAutoPlaying]);

  const goToPrevious = () => {
    setIsAutoPlaying(false);
    setCurrentIndex((prev) => (prev - 1 + testimonials.length) % testimonials.length);
  };

  const goToNext = () => {
    setIsAutoPlaying(false);
    setCurrentIndex((prev) => (prev + 1) % testimonials.length);
  };

  const goToSlide = (index: number) => {
    setIsAutoPlaying(false);
    setCurrentIndex(index);
  };

  return (
    <section className="testimonial-section">
      <div className="testimonial-container">
        <div className="testimonial-header">
          <h2 className="testimonial-title">Trusted by Churches</h2>
          <p className="testimonial-subtitle">See how churches are using Kaleo</p>
        </div>

        <div className="testimonial-carousel">
          <button className="carousel-btn carousel-btn-prev" onClick={goToPrevious}>
            <ChevronLeft size={24} />
          </button>

          <div className="testimonial-cards-container">
            {testimonials.map((testimonial, index) => (
              <div
                key={testimonial.id}
                className={`testimonial-card ${index === currentIndex ? 'active' : ''} ${
                  index === (currentIndex - 1 + testimonials.length) % testimonials.length ? 'prev' : ''
                } ${index === (currentIndex + 1) % testimonials.length ? 'next' : ''}`}
              >
                <Quote className="testimonial-quote-icon" size={32} />
                <p className="testimonial-content">{testimonial.content}</p>
                <div className="testimonial-author">
                  <div className="testimonial-avatar">{testimonial.avatar}</div>
                  <div className="testimonial-author-info">
                    <h4 className="testimonial-name">{testimonial.name}</h4>
                    <p className="testimonial-role">{testimonial.role}</p>
                    <p className="testimonial-org">{testimonial.organization}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <button className="carousel-btn carousel-btn-next" onClick={goToNext}>
            <ChevronRight size={24} />
          </button>
        </div>

        <div className="testimonial-dots">
          {testimonials.map((_, index) => (
            <button
              key={index}
              className={`dot ${index === currentIndex ? 'active' : ''}`}
              onClick={() => goToSlide(index)}
            />
          ))}
        </div>
      </div>
    </section>
  );
}
