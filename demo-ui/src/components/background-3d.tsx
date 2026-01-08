import {Canvas, useFrame} from '@react-three/fiber';
import {Float, MeshDistortMaterial, Sphere, Stars} from '@react-three/drei';
import {useMemo, useRef} from 'react';
import * as THREE from 'three';

// Animated floating orb
function FloatingOrb({ position, scale, color, speed = 1 }: { 
  position: [number, number, number]; 
  scale: number; 
  color: string;
  speed?: number;
}) {
  const meshRef = useRef<THREE.Mesh>(null);
  
  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.x = state.clock.elapsedTime * 0.3 * speed;
      meshRef.current.rotation.y = state.clock.elapsedTime * 0.2 * speed;
    }
  });

  return (
    <Float speed={2} rotationIntensity={0.5} floatIntensity={2}>
      <mesh ref={meshRef} position={position} scale={scale}>
        <icosahedronGeometry args={[1, 1]} />
        <MeshDistortMaterial
          color={color}
          attach="material"
          distort={0.4}
          speed={2}
          roughness={0.2}
          metalness={0.8}
        />
      </mesh>
    </Float>
  );
}

// Glowing sphere
function GlowingSphere({ position, color, size = 0.5 }: { 
  position: [number, number, number]; 
  color: string;
  size?: number;
}) {
  const meshRef = useRef<THREE.Mesh>(null);
  
  useFrame((state) => {
    if (meshRef.current) {
      const scale = 1 + Math.sin(state.clock.elapsedTime * 2) * 0.1;
      meshRef.current.scale.setScalar(scale * size);
    }
  });

  return (
    <Float speed={1.5} floatIntensity={1.5}>
      <Sphere ref={meshRef} args={[1, 32, 32]} position={position}>
        <meshStandardMaterial
          color={color}
          emissive={color}
          emissiveIntensity={0.5}
          transparent
          opacity={0.8}
        />
      </Sphere>
    </Float>
  );
}

// Particle field
function ParticleField({ count = 200 }: { count?: number }) {
  const points = useRef<THREE.Points>(null);
  
  const particlesPosition = useMemo(() => {
    const positions = new Float32Array(count * 3);
    for (let i = 0; i < count; i++) {
      positions[i * 3] = (Math.random() - 0.5) * 20;
      positions[i * 3 + 1] = (Math.random() - 0.5) * 20;
      positions[i * 3 + 2] = (Math.random() - 0.5) * 20;
    }
    return positions;
  }, [count]);

  useFrame((state) => {
    if (points.current) {
      points.current.rotation.y = state.clock.elapsedTime * 0.02;
      points.current.rotation.x = state.clock.elapsedTime * 0.01;
    }
  });

  const geometry = useMemo(() => {
    const geo = new THREE.BufferGeometry();
    geo.setAttribute('position', new THREE.BufferAttribute(particlesPosition, 3));
    return geo;
  }, [particlesPosition]);

  return (
    <points ref={points} geometry={geometry}>
      <pointsMaterial
        size={0.03}
        color="#a855f7"
        sizeAttenuation
        transparent
        opacity={0.8}
      />
    </points>
  );
}

// Animated ring
function AnimatedRing({ position, color }: { 
  position: [number, number, number];
  color: string;
}) {
  const meshRef = useRef<THREE.Mesh>(null);
  
  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.rotation.x = state.clock.elapsedTime * 0.5;
      meshRef.current.rotation.z = state.clock.elapsedTime * 0.3;
    }
  });

  return (
    <Float speed={1} floatIntensity={1}>
      <mesh ref={meshRef} position={position}>
        <torusGeometry args={[1, 0.02, 16, 100]} />
        <meshStandardMaterial
          color={color}
          emissive={color}
          emissiveIntensity={0.3}
          transparent
          opacity={0.6}
        />
      </mesh>
    </Float>
  );
}

// Main scene component
function Scene() {
  return (
    <>
      {/* Environment and lighting */}
      <ambientLight intensity={0.2} />
      <pointLight position={[10, 10, 10]} intensity={1} color="#a855f7" />
      <pointLight position={[-10, -10, -10]} intensity={0.5} color="#6366f1" />
      
      {/* Stars background */}
      <Stars
        radius={100}
        depth={50}
        count={3000}
        factor={4}
        saturation={0}
        fade
        speed={0.5}
      />
      
      {/* Particle field */}
      <ParticleField count={300} />
      
      {/* Main floating orbs */}
      <FloatingOrb position={[-4, 2, -3]} scale={1.5} color="#a855f7" speed={0.8} />
      <FloatingOrb position={[4, -1, -4]} scale={1.2} color="#6366f1" speed={1.2} />
      <FloatingOrb position={[0, 3, -5]} scale={0.8} color="#8b5cf6" speed={1} />
      
      {/* Glowing spheres */}
      <GlowingSphere position={[-3, -2, -2]} color="#c084fc" size={0.3} />
      <GlowingSphere position={[3, 1, -3]} color="#818cf8" size={0.4} />
      <GlowingSphere position={[1, -3, -4]} color="#a855f7" size={0.25} />
      
      {/* Animated rings */}
      <AnimatedRing position={[-2, 0, -6]} color="#a855f7" />
      <AnimatedRing position={[3, 2, -7]} color="#6366f1" />
    </>
  );
}

// Main exported component
export default function Background3D() {
  return (
    <div className="background-3d">
      <Canvas
        camera={{ position: [0, 0, 6], fov: 60 }}
        dpr={[1, 2]}
        gl={{ antialias: true, alpha: true }}
      >
        <Scene />
      </Canvas>
    </div>
  );
}
