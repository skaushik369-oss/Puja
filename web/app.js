(() => {
  const $ = id => document.getElementById(id);
  const sel = $('sel'), count = $('count'), bar = $('bar'), status = $('status'), note = $('note'),
        playBtn = $('play'), stopBtn = $('stop'), speed = $('speed'), speedLabel = $('speedlabel'),
        warn = $('warn'), text = $('text');
  const synth = window.speechSynthesis;

  let idx = 0, state = 'idle', rep = 0, part = 0, gen = 0, rate = 1, voice = null, wake = null;

  MANTRAS.forEach((m, i) => sel.add(new Option(`${m.number}. ${m.title}`, i)));

  function pickVoice() {
    if (!synth) return null;
    const vs = synth.getVoices();
    return vs.find(v => v.lang.replace('_', '-').toLowerCase() === 'hi-in') ||
           vs.find(v => v.lang.toLowerCase().startsWith('hi')) || null;
  }
  if (synth) {
    synth.onvoiceschanged = () => { voice = pickVoice(); };
    voice = pickVoice();
  }

  function total() { return MANTRAS[idx].repeat || DEFAULT_REPEAT_COUNT; }

  function render() {
    const t = total();
    count.textContent = `${rep} / ${t}`;
    bar.max = t; bar.value = rep;
    status.textContent = {
      idle: 'Press Play to begin', playing: 'Chanting…', paused: 'Paused', done: 'Complete 🙏'
    }[state];
    playBtn.textContent = {idle: '▶ Play', playing: '❚❚ Pause', paused: '▶ Resume', done: '▶ Play again'}[state];
    stopBtn.disabled = state === 'idle';
    note.textContent = MANTRAS[idx].note || '';
  }

  function showText() {
    text.textContent = MANTRAS[idx].text;
    text.scrollTop = 0;
  }

  function problem(msg) {
    warn.hidden = !msg;
    warn.textContent = msg || '';
  }

  async function keepAwake(on) {
    try {
      if (on && 'wakeLock' in navigator) wake = await navigator.wakeLock.request('screen');
      else if (wake) { await wake.release(); wake = null; }
    } catch (e) {}
  }
  document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'visible' && state === 'playing') keepAwake(true);
  });

  function speakPart() {
    const m = MANTRAS[idx];
    const myGen = gen;
    const u = new SpeechSynthesisUtterance(m.speech[part]);
    u.lang = 'hi-IN';
    if (voice) u.voice = voice;
    u.rate = rate;
    u.onend = () => {
      if (myGen !== gen || state !== 'playing') return;
      part++;
      if (part >= m.speech.length) {
        part = 0; rep++;
        if (rep > total()) { rep = total(); state = 'done'; keepAwake(false); render(); return; }
        render();
      }
      speakPart();
    };
    u.onerror = e => {
      if (myGen !== gen || e.error === 'canceled' || e.error === 'interrupted') return;
      gen++; state = 'paused'; render();
      problem('The Hindi voice could not speak. On iPhone: Settings → Accessibility → Spoken Content → Voices → Hindi, download a voice, then press Resume.');
    };
    synth.speak(u);
  }

  function play() {
    if (!synth) { problem('This browser cannot speak. Please open the page in Safari or Chrome.'); return; }
    voice = pickVoice();
    if (!voice) {
      problem('No Hindi voice found on this phone. On iPhone: Settings → Accessibility → Spoken Content → Voices → Hindi, download a voice, then come back and press Play.');
      // still try: the system may have a fallback voice
    } else problem('');
    if (state === 'paused') { gen++; state = 'playing'; render(); keepAwake(true); synth.cancel(); speakPart(); return; }
    gen++; synth.cancel();
    rep = 1; part = 0; state = 'playing';
    render(); keepAwake(true); speakPart();
  }

  function pause() { gen++; synth.cancel(); state = 'paused'; render(); }
  function stop() { gen++; if (synth) synth.cancel(); state = 'idle'; rep = 0; part = 0; keepAwake(false); render(); }

  playBtn.onclick = () => state === 'playing' ? pause() : play();
  stopBtn.onclick = stop;
  sel.onchange = () => { stop(); idx = +sel.value; showText(); render(); problem(''); };
  speed.oninput = () => { rate = +speed.value; speedLabel.textContent = `Speed ${rate.toFixed(1)}×`; };

  showText(); render();
  if ('serviceWorker' in navigator) navigator.serviceWorker.register('sw.js').catch(() => {});
})();
