varying vec3 vColor;
varying float vAlpha;

void main() {
  // Create circular point sprite
  float distanceToCenter = length(gl_PointCoord - vec2(0.5));
  
  // Soft glow falloff
  float strength = 1.0 - smoothstep(0.0, 0.5, distanceToCenter);
  strength = pow(strength, 1.5); // Adjust glow intensity
  
  // Add inner core brightness
  float core = 1.0 - smoothstep(0.0, 0.15, distanceToCenter);
  strength += core * 0.5;
  
  // Discard pixels outside circle
  if (strength < 0.01) discard;
  
  // Apply color and alpha
  vec3 finalColor = vColor * strength;
  float finalAlpha = vAlpha * strength;
  
  gl_FragColor = vec4(finalColor, finalAlpha);
}
