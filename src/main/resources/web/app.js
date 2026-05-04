const apiBase = '/api';
const state = {
  formations: [],
};

const elements = {
  stats: document.getElementById('stats'),
  formationList: document.getElementById('formation-list'),
  formationBadge: document.getElementById('formation-badge'),
  apiOutput: document.getElementById('api-output'),
  apiStatus: document.getElementById('api-status'),
  apiStatusDot: document.getElementById('api-status-dot'),
  apiHint: document.getElementById('api-hint'),
  refreshBtn: document.getElementById('refresh-btn'),
  loadHealthBtn: document.getElementById('load-health-btn'),
  formationForm: document.getElementById('formation-form'),
  purchaseForm: document.getElementById('purchase-form'),
  contentForm: document.getElementById('content-form'),
  completeForm: document.getElementById('complete-form'),
};

function prettyPrint(value) {
  return JSON.stringify(value, null, 2);
}

async function requestJson(path, options = {}) {
  const response = await fetch(`${apiBase}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  });

  const text = await response.text();
  let payload = null;

  if (text) {
    try {
      payload = JSON.parse(text);
    } catch (error) {
      payload = text;
    }
  }

  if (!response.ok) {
    throw new Error(typeof payload === 'string' ? payload : payload?.error || `HTTP ${response.status}`);
  }

  return payload;
}

function setApiState(online, message) {
  elements.apiStatus.textContent = online ? 'Connectée' : 'Hors ligne';
  elements.apiStatusDot.classList.toggle('ok', online);
  elements.apiHint.textContent = message;
}

function setOutput(title, payload) {
  elements.apiOutput.textContent = `${title}\n\n${prettyPrint(payload)}`;
}

function computeStats(formations) {
  const totalFormations = formations.length;
  const totalContents = formations.reduce((count, formation) => count + (formation.contenuePedagogiques?.length || 0), 0);
  const totalStudents = formations.reduce((count, formation) => count + (formation.lesEleves?.length || 0), 0);
  const averagePrice = totalFormations
    ? formations.reduce((sum, formation) => sum + (formation.prix || 0), 0) / totalFormations
    : 0;

  elements.stats.innerHTML = `
    <div class="stat-card">
      <p class="section-kicker">Formations</p>
      <span class="stat-value">${totalFormations}</span>
      <p class="stat-note">Catalogue initial + créations locales</p>
    </div>
    <div class="stat-card">
      <p class="section-kicker">Contenus</p>
      <span class="stat-value">${totalContents}</span>
      <p class="stat-note">Vidéos, PDF et quiz disponibles</p>
    </div>
    <div class="stat-card">
      <p class="section-kicker">Étudiants</p>
      <span class="stat-value">${totalStudents}</span>
      <p class="stat-note">Inscriptions et achat gérés par l'API</p>
    </div>
    <div class="stat-card">
      <p class="section-kicker">Prix moyen</p>
      <span class="stat-value">${averagePrice.toFixed(2)}€</span>
      <p class="stat-note">Avant réductions décorées</p>
    </div>
  `;
}

function renderFormationList(formations) {
  if (!formations.length) {
    elements.formationList.innerHTML = '<p class="formation-meta">Aucune formation disponible.</p>';
    return;
  }

  elements.formationList.innerHTML = formations.map((formation) => {
    const contents = (formation.contenuePedagogiques || [])
      .map((content) => `<span class="tag">${content.titre} · ${content.estTermine ? 'terminé' : 'en cours'}</span>`)
      .join('');

    const students = (formation.lesEleves || []).map((student) => `${student.prenom} ${student.nom}`).join(', ') || 'Aucun étudiant';

    return `
      <article class="formation-card">
        <div class="formation-top">
          <div>
            <h3>${formation.titre}</h3>
            <p class="formation-meta">${formation.description}</p>
          </div>
          <span class="badge">#${formation.id}</span>
        </div>
        <p class="formation-meta"><strong>${formation.prix.toFixed(2)}€</strong> · ${formation.contenuePedagogiques?.length || 0} contenus · progression ${Number(formation.tauxProgression || 0).toFixed(0)}%</p>
        <p class="formation-meta"><strong>Étudiants :</strong> ${students}</p>
        <div class="tag-row">${contents || '<span class="tag">Aucun contenu</span>'}</div>
      </article>
    `;
  }).join('');
}

async function loadDashboard() {
  try {
    const formations = await requestJson('/formations');
    state.formations = formations;
    computeStats(formations);
    renderFormationList(formations);
    elements.formationBadge.textContent = `${formations.length} formation(s)`;
    setApiState(true, 'Les données sont récupérées depuis /api/formations.');
    setOutput('GET /formations', formations);
  } catch (error) {
    setApiState(false, error.message);
    elements.formationBadge.textContent = 'API indisponible';
    elements.stats.innerHTML = '';
    elements.formationList.innerHTML = '<p class="formation-meta">Impossible de charger les données pour le moment.</p>';
    setOutput('Erreur', { message: error.message });
  }
}

async function pingApi() {
  try {
    const health = await requestJson('/health');
    setApiState(true, 'API saine et accessible.');
    setOutput('GET /health', health);
  } catch (error) {
    setApiState(false, error.message);
    setOutput('Erreur /health', { message: error.message });
  }
}

async function createFormation(event) {
  event.preventDefault();
  const formData = new FormData(event.currentTarget);
  const body = {
    titre: formData.get('titre'),
    description: formData.get('description'),
    prix: Number(formData.get('prix')),
  };

  try {
    const created = await requestJson('/formations', {
      method: 'POST',
      body: JSON.stringify(body),
    });

    setOutput('POST /formations', created);
    event.currentTarget.reset();
    event.currentTarget.querySelector('input[name="prix"]').value = '149.99';
    await loadDashboard();
  } catch (error) {
    setOutput('Erreur création', { message: error.message });
  }
}

async function purchaseFormation(event) {
  event.preventDefault();
  const formData = new FormData(event.currentTarget);
  const formationId = Number(formData.get('formationId'));
  const body = {
    paymentType: formData.get('paymentType'),
    amount: Number(formData.get('amount')),
    currency: 'EUR',
    orderId: `WEB-${Date.now()}`,
    student: {
      nom: formData.get('nom'),
      prenom: formData.get('prenom'),
      adresse: formData.get('adresse'),
      email: formData.get('email'),
      dateNaissance: formData.get('dateNaissance'),
    },
  };

  try {
    const result = await requestJson(`/formations/${formationId}/purchase`, {
      method: 'POST',
      body: JSON.stringify(body),
    });

    setOutput(`POST /formations/${formationId}/purchase`, result);
    await loadDashboard();
  } catch (error) {
    setOutput('Erreur achat', { message: error.message });
  }
}

async function addContent(event) {
  event.preventDefault();
  const formData = new FormData(event.currentTarget);
  const formationId = Number(formData.get('formationId'));
  const body = {
    type: formData.get('type'),
    titre: formData.get('titre'),
    estTermine: false,
    value: Number(formData.get('value')),
  };

  try {
    const result = await requestJson(`/formations/${formationId}/contents`, {
      method: 'POST',
      body: JSON.stringify(body),
    });

    setOutput(`POST /formations/${formationId}/contents`, result);
    await loadDashboard();
  } catch (error) {
    setOutput('Erreur contenu', { message: error.message });
  }
}

async function completeContent(event) {
  event.preventDefault();
  const formData = new FormData(event.currentTarget);
  const formationId = Number(formData.get('formationId'));
  const body = {
    titre: formData.get('titre'),
  };

  try {
    const result = await requestJson(`/formations/${formationId}/contents/complete`, {
      method: 'POST',
      body: JSON.stringify(body),
    });

    setOutput(`POST /formations/${formationId}/contents/complete`, result);
    await loadDashboard();
  } catch (error) {
    setOutput('Erreur progression', { message: error.message });
  }
}

elements.refreshBtn.addEventListener('click', loadDashboard);
elements.loadHealthBtn.addEventListener('click', pingApi);
elements.formationForm.addEventListener('submit', createFormation);
elements.purchaseForm.addEventListener('submit', purchaseFormation);
elements.contentForm.addEventListener('submit', addContent);
elements.completeForm.addEventListener('submit', completeContent);

pingApi();
loadDashboard();
