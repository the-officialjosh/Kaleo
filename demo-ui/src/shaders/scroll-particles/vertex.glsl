uniform float uTime;
uniform float uScrollProgress;
uniform float uPixelRatio;
uniform vec2 uResolution;

attribute float aSize;
attribute float aRandom;
attribute vec3 aTargetPosition;

varying vec3 vColor;
varying float vAlpha;

// Simplex noise functions
vec3 mod289(vec3 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec4 mod289(vec4 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec4 permute(vec4 x) { return mod289(((x*34.0)+1.0)*x); }
vec4 taylorInvSqrt(vec4 r) { return 1.79284291400159 - 0.85373472095314 * r; }

float snoise(vec3 v) {
  const vec2 C = vec2(1.0/6.0, 1.0/3.0);
  const vec4 D = vec4(0.0, 0.5, 1.0, 2.0);
  
  vec3 i  = floor(v + dot(v, C.yyy));
  vec3 x0 = v - i + dot(i, C.xxx);
  
  vec3 g = step(x0.yzx, x0.xyz);
  vec3 l = 1.0 - g;
  vec3 i1 = min(g.xyz, l.zxy);
  vec3 i2 = max(g.xyz, l.zxy);
  
  vec3 x1 = x0 - i1 + C.xxx;
  vec3 x2 = x0 - i2 + C.yyy;
  vec3 x3 = x0 - D.yyy;
  
  i = mod289(i);
  vec4 p = permute(permute(permute(
    i.z + vec4(0.0, i1.z, i2.z, 1.0))
    + i.y + vec4(0.0, i1.y, i2.y, 1.0))
    + i.x + vec4(0.0, i1.x, i2.x, 1.0));
  
  float n_ = 0.142857142857;
  vec3 ns = n_ * D.wyz - D.xzx;
  
  vec4 j = p - 49.0 * floor(p * ns.z * ns.z);
  
  vec4 x_ = floor(j * ns.z);
  vec4 y_ = floor(j - 7.0 * x_);
  
  vec4 x = x_ *ns.x + ns.yyyy;
  vec4 y = y_ *ns.x + ns.yyyy;
  vec4 h = 1.0 - abs(x) - abs(y);
  
  vec4 b0 = vec4(x.xy, y.xy);
  vec4 b1 = vec4(x.zw, y.zw);
  
  vec4 s0 = floor(b0)*2.0 + 1.0;
  vec4 s1 = floor(b1)*2.0 + 1.0;
  vec4 sh = -step(h, vec4(0.0));
  
  vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy;
  vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww;
  
  vec3 p0 = vec3(a0.xy, h.x);
  vec3 p1 = vec3(a0.zw, h.y);
  vec3 p2 = vec3(a1.xy, h.z);
  vec3 p3 = vec3(a1.zw, h.w);
  
  vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2,p2), dot(p3,p3)));
  p0 *= norm.x;
  p1 *= norm.y;
  p2 *= norm.z;
  p3 *= norm.w;
  
  vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
  m = m * m;
  return 42.0 * dot(m*m, vec4(dot(p0,x0), dot(p1,x1), dot(p2,x2), dot(p3,x3)));
}

// Curl noise for fluid-like motion
vec3 curlNoise(vec3 p) {
  const float e = 0.1;
  vec3 dx = vec3(e, 0.0, 0.0);
  vec3 dy = vec3(0.0, e, 0.0);
  vec3 dz = vec3(0.0, 0.0, e);
  
  float n1 = snoise(p + dy) - snoise(p - dy);
  float n2 = snoise(p + dz) - snoise(p - dz);
  float n3 = snoise(p + dz) - snoise(p - dz);
  float n4 = snoise(p + dx) - snoise(p - dx);
  float n5 = snoise(p + dx) - snoise(p - dx);
  float n6 = snoise(p + dy) - snoise(p - dy);
  
  return normalize(vec3(n1 - n2, n3 - n4, n5 - n6));
}

void main() {
  vec3 pos = position;
  vec3 target = aTargetPosition;
  
  // Scroll phases
  float dissolveStart = 0.0;
  float dissolveEnd = 0.4;
  float driftEnd = 0.6;
  float reformEnd = 1.0;
  
  float scroll = uScrollProgress;
  
  // Phase 1: Dissolve (0 - 0.4)
  float dissolveProgress = smoothstep(dissolveStart, dissolveEnd, scroll);
  
  // Phase 2: Drift (0.4 - 0.6)
  float driftProgress = smoothstep(dissolveEnd, driftEnd, scroll);
  
  // Phase 3: Reform (0.6 - 1.0)
  float reformProgress = smoothstep(driftEnd, reformEnd, scroll);
  
  // Add per-particle timing offset for staggered effect
  float particleOffset = aRandom * 0.15;
  dissolveProgress = smoothstep(dissolveStart + particleOffset, dissolveEnd + particleOffset, scroll);
  reformProgress = smoothstep(driftEnd + particleOffset, reformEnd + particleOffset, scroll);
  
  // Curl noise displacement for organic motion
  float noiseScale = 2.0;
  float noiseSpeed = uTime * 0.3;
  vec3 noisePos = pos * noiseScale + noiseSpeed;
  vec3 curl = curlNoise(noisePos);
  
  // Dissolve: scatter outward with noise
  float scatterAmount = dissolveProgress * (1.0 - reformProgress);
  vec3 scatterOffset = curl * 4.0 * scatterAmount;
  scatterOffset += normalize(pos) * scatterAmount * 2.0; // Expand outward
  
  // Add gentle floating motion
  float floatY = sin(uTime * 0.5 + aRandom * 6.28) * 0.2 * scatterAmount;
  scatterOffset.y += floatY;
  
  // Reform: interpolate to target position
  vec3 finalPos = pos + scatterOffset;
  finalPos = mix(finalPos, target, reformProgress);
  
  // Subtle breathing motion when static
  float staticBreath = (1.0 - dissolveProgress) * sin(uTime * 0.8 + aRandom * 6.28) * 0.05;
  finalPos += normalize(pos) * staticBreath;
  
  vec4 mvPosition = modelViewMatrix * vec4(finalPos, 1.0);
  gl_Position = projectionMatrix * mvPosition;
  
  // Dynamic point size
  float baseSize = aSize * uPixelRatio;
  float sizeMultiplier = 1.0 + scatterAmount * 0.5; // Slightly larger when scattered
  gl_PointSize = baseSize * sizeMultiplier * (300.0 / -mvPosition.z);
  
  // Color based on scroll phase
  vec3 goldColor = vec3(0.95, 0.75, 0.3);
  vec3 purpleColor = vec3(0.6, 0.4, 0.9);
  vec3 whiteColor = vec3(1.0, 0.95, 0.9);
  
  // Transition from gold to purple to white
  vColor = mix(goldColor, purpleColor, dissolveProgress);
  vColor = mix(vColor, whiteColor, reformProgress * 0.3);
  
  // Alpha based on position and phase
  vAlpha = 0.8 - scatterAmount * 0.3;
  vAlpha = max(vAlpha, 0.3);
}
