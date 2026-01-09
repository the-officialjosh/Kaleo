import {Baby, Church, GraduationCap, Heart, Users2} from 'lucide-react';

const useCases = [
  {
    icon: Church,
    title: "Worship Services",
    description: "Manage Sunday services, special worship nights, and holiday celebrations."
  },
  {
    icon: Users2,
    title: "Small Groups",
    description: "Organize and track small group meetings, home fellowships, and prayer groups."
  },
  {
    icon: GraduationCap,
    title: "Bible Studies",
    description: "Host scripture studies, discipleship classes, and faith courses."
  },
  {
    icon: Heart,
    title: "Retreats & Conferences",
    description: "Plan church retreats, women's/men's conferences, and spiritual gatherings."
  },
  {
    icon: Baby,
    title: "Youth & Children",
    description: "Manage VBS, youth camps, children's ministry events, and family activities."
  }
];

export default function UseCases() {
  return (
    <section className="use-cases-section">
      <div className="use-cases-container">
        <div className="use-cases-header">
          <h2 className="use-cases-title">For Every Ministry Need</h2>
          <p className="use-cases-subtitle">Supporting your church's mission in every way</p>
        </div>

        <div className="use-cases-grid">
          {useCases.map((useCase, index) => (
            <div key={index} className="use-case-card">
              <div className="use-case-icon">
                <useCase.icon size={28} />
              </div>
              <h3 className="use-case-title">{useCase.title}</h3>
              <p className="use-case-desc">{useCase.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
