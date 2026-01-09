const trustedBy = [
  { name: "Grace Community Church", type: "Mega Church" },
  { name: "New Life Fellowship", type: "Community" },
  { name: "Cornerstone Church", type: "Youth Focus" },
  { name: "Faith Tabernacle", type: "Catholic" },
  { name: "Living Word Church", type: "Non-Denom" },
  { name: "Redeemer's Chapel", type: "Baptist" }
];

export default function SocialProof() {
  return (
    <section className="social-proof-section">
      <div className="social-proof-container">
        <p className="social-proof-label">Trusted by churches everywhere</p>
        <div className="social-proof-logos">
          {trustedBy.map((org, index) => (
            <div key={index} className="social-proof-item">
              <span className="org-name">{org.name}</span>
              <span className="org-type">{org.type}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
