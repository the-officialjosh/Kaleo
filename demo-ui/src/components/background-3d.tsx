import {Canvas, useFrame} from '@react-three/fiber';
import {Suspense, useEffect, useMemo, useRef} from 'react';
import * as THREE from 'three';
import gsap from 'gsap';

// Premium flowing particle field with GSAP-enhanced animations
function FlowingParticles() {
  const pointsRef = useRef<THREE.Points>(null);
  const materialRef = useRef<THREE.ShaderMaterial>(null);
  const mouseRef = useRef({ x: 0, y: 0 });
  const targetMouseRef = useRef({ x: 0, y: 0 });
  
  // Reduced from 12,000 to 4,000 for better performance
  const particleCount = 4000;
  
  // Create geometry with layered positions
  const { positions, randoms, speeds } = useMemo(() => {
    const pos = new Float32Array(particleCount * 3);
    const rand = new Float32Array(particleCount);
    const spd = new Float32Array(particleCount);
    
    for (let i = 0; i < particleCount; i++) {
      // Create layers of particles at different depths
      const layer = Math.floor(Math.random() * 4);
      const depth = layer * 4 + Math.random() * 4;
      
      // Spread in a wide field
      pos[i * 3] = (Math.random() - 0.5) * 40;
      pos[i * 3 + 1] = (Math.random() - 0.5) * 30;
      pos[i * 3 + 2] = -depth;
      
      rand[i] = Math.random();
      spd[i] = 0.2 + Math.random() * 0.8;
    }
    
    return { positions: pos, randoms: rand, speeds: spd };
  }, []);
 
  // Shader material with flowing noise and mouse interaction
  const shaderMaterial = useMemo(() => {
    return new THREE.ShaderMaterial({
      uniforms: {
        uTime: { value: 0 },
        uMouse: { value: new THREE.Vector2(0, 0) },
        uPixelRatio: { value: Math.min(window.devicePixelRatio, 2) },
        uColorA: { value: new THREE.Color('#8b5cf6') }, // Purple
        uColorB: { value: new THREE.Color('#06b6d4') }, // Cyan
        uColorC: { value: new THREE.Color('#ffffff') }, // White - for mouse glow
      },
      vertexShader: `
        uniform float uTime;
        uniform vec2 uMouse;
        uniform float uPixelRatio;
        
        attribute float aRandom;
        attribute float aSpeed;
        
        varying float vAlpha;
        varying float vMouseProximity;
        varying float vDepth;
        varying float vRandom;
        
        // Simplex noise
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
          p0 *= norm.x; p1 *= norm.y; p2 *= norm.z; p3 *= norm.w;
          
          vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);
          m = m * m;
          return 42.0 * dot(m*m, vec4(dot(p0,x0), dot(p1,x1), dot(p2,x2), dot(p3,x3)));
        }
        
        void main() {
          vec3 pos = position;
          float time = uTime * aSpeed * 0.3;
          
          // Flowing noise displacement
          float noiseScale = 0.15;
          vec3 noisePos = pos * noiseScale + time;
          float noise = snoise(noisePos);
          
          // Gentle wave motion
          pos.x += sin(time + pos.y * 0.5) * 0.3;
          pos.y += cos(time * 0.7 + pos.x * 0.3) * 0.2;
          pos.y += noise * 0.5;
          
          // Mouse influence - calculate distance to mouse
          vec2 mouseWorld = uMouse * 12.0;
          float mouseDistance = length(vec2(pos.x, pos.y) - mouseWorld);
          
          // Particles drift away from cursor slightly
          float mouseInfluence = smoothstep(6.0, 0.0, mouseDistance) * 0.4;
          pos.xy += normalize(vec2(pos.x, pos.y) - mouseWorld + 0.001) * mouseInfluence;
          
          vec4 mvPosition = modelViewMatrix * vec4(pos, 1.0);
          gl_Position = projectionMatrix * mvPosition;
          
          // Smaller particles
          float depthFactor = 1.0 + (-pos.z) * 0.03;
          float size = (0.2 + aRandom * 0.6) * uPixelRatio * depthFactor;
          gl_PointSize = size * (80.0 / -mvPosition.z);
          
          // Mouse proximity for orange color effect (0 = far, 1 = close)
          vMouseProximity = smoothstep(8.0, 0.0, mouseDistance);
          
          // Twinkle
          float twinkle = sin(uTime * 3.0 + aRandom * 20.0) * 0.2 + 0.8;
          
          vDepth = -pos.z / 15.0;
          vAlpha = (0.5 + aRandom * 0.5) * twinkle * (1.0 - vDepth * 0.3);
          vRandom = aRandom;
        }
      `,
      fragmentShader: `
        uniform vec3 uColorA;
        uniform vec3 uColorB;
        uniform vec3 uColorC;
        
        varying float vAlpha;
        varying float vMouseProximity;
        varying float vDepth;
        varying float vRandom;
        
        void main() {
          // Soft circular point
          float dist = length(gl_PointCoord - vec2(0.5));
          float strength = 1.0 - smoothstep(0.0, 0.5, dist);
          strength = pow(strength, 2.0);
          
          if (strength < 0.05) discard;
          
          // Base color - mix between purple and cyan
          vec3 baseColor = mix(uColorA, uColorB, vRandom);
          
          // Orange glow near mouse! Smooth transition
          vec3 color = mix(baseColor, uColorC, vMouseProximity * 0.9);
          
          // Boost brightness near mouse
          float brightnessFactor = 1.0 + vMouseProximity * 0.8;
          color *= brightnessFactor;
          
          // Add more glow for brighter particles
          color += vec3(0.1, 0.05, 0.15) * strength;
          
          // Increase alpha near mouse for more visible effect
          float finalAlpha = vAlpha * strength * (1.0 + vMouseProximity * 0.5);
          
          gl_FragColor = vec4(color, finalAlpha);
        }
      `,
      transparent: true,
      depthWrite: false,
      blending: THREE.AdditiveBlending,
    });
  }, []);
  
  // Track mouse with GSAP smoothing
  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      targetMouseRef.current.x = (e.clientX / window.innerWidth) * 2 - 1;
      targetMouseRef.current.y = -(e.clientY / window.innerHeight) * 2 + 1;
    };
    
    window.addEventListener('mousemove', handleMouseMove);
    
    // GSAP ticker for smooth mouse interpolation
    gsap.ticker.add(() => {
      mouseRef.current.x += (targetMouseRef.current.x - mouseRef.current.x) * 0.08;
      mouseRef.current.y += (targetMouseRef.current.y - mouseRef.current.y) * 0.08;
    });
    
    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
    };
  }, []);
  
  // Animate
  useFrame((state) => {
    if (shaderMaterial) {
      shaderMaterial.uniforms.uTime.value = state.clock.elapsedTime;
      shaderMaterial.uniforms.uMouse.value.set(mouseRef.current.x, mouseRef.current.y);
    }
  });
  
  return (
    <points ref={pointsRef}>
      <bufferGeometry>
        <bufferAttribute attach="attributes-position" args={[positions, 3]} />
        <bufferAttribute attach="attributes-aRandom" args={[randoms, 1]} />
        <bufferAttribute attach="attributes-aSpeed" args={[speeds, 1]} />
      </bufferGeometry>
      <primitive object={shaderMaterial} attach="material" ref={materialRef} />
    </points>
  );
}

// Main component
export default function Background3D() {
  return (
    <div className="background-3d-fixed">
      <Canvas
        camera={{
          position: [0, 0, 12],
          fov: 55,
          near: 0.1,
          far: 100,
        }}
        dpr={[1, 2]}
        gl={{
          antialias: false,
          alpha: true,
          powerPreference: 'high-performance',
        }}
        style={{ background: 'transparent' }}
      >
        <Suspense fallback={null}>
          <FlowingParticles />
        </Suspense>
      </Canvas>
    </div>
  );
}
