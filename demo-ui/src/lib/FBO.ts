import * as THREE from 'three';

export default class FBO {
  width: number;
  height: number;
  renderer: THREE.WebGLRenderer;
  simulationMaterial: THREE.ShaderMaterial;
  renderMaterial: THREE.ShaderMaterial;
  scene: THREE.Scene;
  camera: THREE.OrthographicCamera;
  rtt: THREE.WebGLRenderTarget;
  mesh: THREE.Mesh;
  particles: THREE.Points;

  constructor(
    width: number,
    height: number,
    renderer: THREE.WebGLRenderer,
    simulationMaterial: THREE.ShaderMaterial,
    renderMaterial: THREE.ShaderMaterial
  ) {
    this.width = width;
    this.height = height;
    this.renderer = renderer;
    this.simulationMaterial = simulationMaterial;
    this.renderMaterial = renderMaterial;

    this.scene = new THREE.Scene();
    this.camera = new THREE.OrthographicCamera(-1, 1, 1, -1, 1 / Math.pow(2, 53), 1);
    this.rtt = null!;
    this.mesh = null!;
    this.particles = null!;

    this.init();
  }

  init() {
    this.createTarget();
    this.simSetup();
    this.createParticles();
  }

  createTarget() {
    this.rtt = new THREE.WebGLRenderTarget(this.width, this.height, {
      minFilter: THREE.NearestFilter,
      magFilter: THREE.NearestFilter,
      generateMipmaps: false,
      depthBuffer: false,
      stencilBuffer: false,
      format: THREE.RGBAFormat,
      type: THREE.FloatType,
    });
  }

  simSetup() {
    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute(
      'position',
      new THREE.BufferAttribute(
        new Float32Array([
          -1, -1, 0, 1, -1, 0, 1, 1, 0,
          -1, -1, 0, 1, 1, 0, -1, 1, 0,
        ]),
        3
      )
    );
    geometry.setAttribute(
      'uv',
      new THREE.BufferAttribute(
        new Float32Array([
          0, 1, 1, 1, 1, 0,
          0, 1, 1, 0, 0, 0,
        ]),
        2
      )
    );

    this.mesh = new THREE.Mesh(geometry, this.simulationMaterial);
    this.scene.add(this.mesh);
  }

  createParticles() {
    const length = this.width * this.height;
    const vertices = new Float32Array(length * 3);
    for (let i = 0; i < length; i++) {
      const i3 = i * 3;
      vertices[i3 + 0] = (i % this.width) / this.width;
      vertices[i3 + 1] = Math.floor(i / this.width) / this.height;
    }

    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute('position', new THREE.BufferAttribute(vertices, 3));
    this.particles = new THREE.Points(geometry, this.renderMaterial);
  }

  resize() {
    this.rtt.setSize(this.width, this.height);
  }

  update() {
    this.renderer.setRenderTarget(this.rtt);
    this.renderer.clear();
    this.renderer.render(this.scene, this.camera);
    this.renderer.setRenderTarget(null);

    (this.particles.material as THREE.ShaderMaterial).uniforms.uPositions.value = this.rtt.texture;
  }
}
