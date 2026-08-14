/**
 * SOLO LEVELING EXAM SYSTEM - PROCEDURAL WEB AUDIO SYNTHESIZER
 * 100% Zero-dependency, offline-ready procedural sound effects & focus ambient generators.
 */

class SystemAudioEngine {
  constructor() {
    this.ctx = null;
    this.soundEnabled = true;
    this.ambientPlaying = false;
    this.ambientNodes = [];
  }

  init() {
    if (!this.ctx) {
      const AudioCtx = window.AudioContext || window.webkitAudioContext;
      if (AudioCtx) {
        this.ctx = new AudioCtx();
      }
    }
    if (this.ctx && this.ctx.state === 'suspended') {
      this.ctx.resume();
    }
  }

  toggleSound() {
    this.soundEnabled = !this.soundEnabled;
    return this.soundEnabled;
  }

  // 1. Holographic System Alert Chime
  playSystemAlert() {
    if (!this.soundEnabled) return;
    this.init();
    if (!this.ctx) return;

    const t = this.ctx.currentTime;
    const osc1 = this.ctx.createOscillator();
    const osc2 = this.ctx.createOscillator();
    const gain = this.ctx.createGain();

    osc1.type = 'sine';
    osc1.frequency.setValueAtTime(880, t); // A5
    osc1.frequency.exponentialRampToValueAtTime(1760, t + 0.15);

    osc2.type = 'triangle';
    osc2.frequency.setValueAtTime(1320, t);
    osc2.frequency.exponentialRampToValueAtTime(2640, t + 0.15);

    gain.gain.setValueAtTime(0.2, t);
    gain.gain.exponentialRampToValueAtTime(0.001, t + 0.4);

    osc1.connect(gain);
    osc2.connect(gain);
    gain.connect(this.ctx.destination);

    osc1.start(t);
    osc2.start(t);
    osc1.stop(t + 0.4);
    osc2.stop(t + 0.4);
  }

  // 2. Button Click / Sci-fi Tick
  playClick() {
    if (!this.soundEnabled) return;
    this.init();
    if (!this.ctx) return;

    const t = this.ctx.currentTime;
    const osc = this.ctx.createOscillator();
    const gain = this.ctx.createGain();

    osc.type = 'sine';
    osc.frequency.setValueAtTime(1200, t);
    osc.frequency.exponentialRampToValueAtTime(400, t + 0.04);

    gain.gain.setValueAtTime(0.12, t);
    gain.gain.exponentialRampToValueAtTime(0.001, t + 0.04);

    osc.connect(gain);
    gain.connect(this.ctx.destination);

    osc.start(t);
    osc.stop(t + 0.04);
  }

  // 3. Level Up Orchestral Fanfare
  playLevelUp() {
    if (!this.soundEnabled) return;
    this.init();
    if (!this.ctx) return;

    const notes = [523.25, 659.25, 783.99, 1046.50, 1318.51, 1567.98]; // C5, E5, G5, C6, E6, G6
    const t = this.ctx.currentTime;

    notes.forEach((freq, idx) => {
      const osc = this.ctx.createOscillator();
      const gain = this.ctx.createGain();
      const startTime = t + idx * 0.08;

      osc.type = 'triangle';
      osc.frequency.setValueAtTime(freq, startTime);

      gain.gain.setValueAtTime(0.2, startTime);
      gain.gain.exponentialRampToValueAtTime(0.001, startTime + 0.5);

      osc.connect(gain);
      gain.connect(this.ctx.destination);

      osc.start(startTime);
      osc.stop(startTime + 0.5);
    });
  }

  // 4. Boss Attack Hit / Damage Impact
  playBossHit(isCrit = false) {
    if (!this.soundEnabled) return;
    this.init();
    if (!this.ctx) return;

    const t = this.ctx.currentTime;
    const osc = this.ctx.createOscillator();
    const gain = this.ctx.createGain();

    osc.type = isCrit ? 'sawtooth' : 'square';
    osc.frequency.setValueAtTime(isCrit ? 300 : 180, t);
    osc.frequency.exponentialRampToValueAtTime(40, t + 0.25);

    gain.gain.setValueAtTime(isCrit ? 0.35 : 0.2, t);
    gain.gain.exponentialRampToValueAtTime(0.001, t + 0.25);

    osc.connect(gain);
    gain.connect(this.ctx.destination);

    osc.start(t);
    osc.stop(t + 0.25);
  }

  // 5. ICONIC "ARISE" (Shadow Extraction) Deep Sub-bass Resonant Surge
  playAriseSound() {
    if (!this.soundEnabled) return;
    this.init();
    if (!this.ctx) return;

    const t = this.ctx.currentTime;

    // Sub bass drop
    const subOsc = this.ctx.createOscillator();
    const subGain = this.ctx.createGain();
    subOsc.type = 'sine';
    subOsc.frequency.setValueAtTime(140, t);
    subOsc.frequency.exponentialRampToValueAtTime(32, t + 1.2);
    subGain.gain.setValueAtTime(0.4, t);
    subGain.gain.exponentialRampToValueAtTime(0.001, t + 1.8);

    subOsc.connect(subGain);
    subGain.connect(this.ctx.destination);
    subOsc.start(t);
    subOsc.stop(t + 1.8);

    // Ethereal mystical shimmer
    const etherealOsc = this.ctx.createOscillator();
    const etherealGain = this.ctx.createGain();
    etherealOsc.type = 'sawtooth';
    etherealOsc.frequency.setValueAtTime(220, t + 0.2);
    etherealOsc.frequency.exponentialRampToValueAtTime(880, t + 1.2);
    etherealGain.gain.setValueAtTime(0.01, t);
    etherealGain.gain.linearRampToValueAtTime(0.18, t + 0.6);
    etherealGain.gain.exponentialRampToValueAtTime(0.001, t + 1.6);

    etherealOsc.connect(etherealGain);
    etherealGain.connect(this.ctx.destination);
    etherealOsc.start(t + 0.2);
    etherealOsc.stop(t + 1.6);
  }

  // 6. Ambient Lo-Fi Study Drone (Binaural Focus Frequency)
  startAmbientFocus() {
    this.init();
    if (!this.ctx) return;
    if (this.ambientPlaying) {
      this.stopAmbientFocus();
      return false;
    }

    const t = this.ctx.currentTime;
    
    // Left ear (196 Hz) + Right ear (206 Hz) = 10 Hz Alpha Wave Binaural Beat
    const oscL = this.ctx.createOscillator();
    const oscR = this.ctx.createOscillator();
    const merger = this.ctx.createChannelMerger(2);
    const gain = this.ctx.createGain();

    oscL.type = 'sine';
    oscL.frequency.setValueAtTime(196, t);
    oscR.type = 'sine';
    oscR.frequency.setValueAtTime(206, t);

    gain.gain.setValueAtTime(0.06, t);

    oscL.connect(merger, 0, 0);
    oscR.connect(merger, 0, 1);
    merger.connect(gain);
    gain.connect(this.ctx.destination);

    oscL.start();
    oscR.start();

    this.ambientNodes = [oscL, oscR, gain];
    this.ambientPlaying = true;
    return true;
  }

  stopAmbientFocus() {
    if (!this.ambientPlaying) return;
    this.ambientNodes.forEach(node => {
      try {
        if (node.stop) node.stop();
        node.disconnect();
      } catch (e) {}
    });
    this.ambientNodes = [];
    this.ambientPlaying = false;
  }
}

// Global Audio Instance
window.SystemAudio = new SystemAudioEngine();
