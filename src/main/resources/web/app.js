const apiBase = '/api';
const page = document.documentElement.dataset.page || document.body.dataset.page || 'home';
const sessionStorageKey = 'horizon-savoir-session';
const accountsStorageKey = 'horizon-savoir-accounts';
const profileStorageKey = 'horizon-savoir-profile';
const seededAccounts = [
  { email: 'admin@horizon.local', password: 'Admin123!', role: 'admin', displayName: 'Administrateur' },
  { email: 'user@horizon.local', password: 'User123!', role: 'user', displayName: 'Utilisateur démo' },
];

const state = {
  formations: [],
  selectedFormationId: null,
  profile: loadProfile(),
  session: loadSession(),
  accounts: loadAccounts(),
};

const elements = {
  stats: document.getElementById('stats'),
  formationList: document.getElementById('formation-list'),
  formationBadge: document.getElementById('formation-badge'),
  catalogList: document.getElementById('catalog-list'),
  myFormations: document.getElementById('my-formations'),
  selectedFormationTitle: document.getElementById('selected-formation-title'),
  selectedFormationBody: document.getElementById('selected-formation-body'),
  selectedFormationBadge: document.getElementById('selected-formation-badge'),
  profileSummary: document.getElementById('profile-summary'),
  profileForm: document.getElementById('profile-form'),
  profileEmail: document.getElementById('profile-email'),
  profileNom: document.getElementById('profile-nom'),
  profilePrenom: document.getElementById('profile-prenom'),
  profileAdresse: document.getElementById('profile-adresse'),
  profileDateNaissance: document.getElementById('profile-date-naissance'),
  loginForm: document.getElementById('login-form'),
  registerForm: document.getElementById('register-form'),
  authSummary: document.getElementById('auth-summary'),
  authMessage: document.getElementById('auth-message'),
  logoutBtn: document.getElementById('logout-btn'),
  apiOutput: document.getElementById('api-output'),
  apiStatus: document.getElementById('api-status'),
  apiStatusDot: document.getElementById('api-status-dot'),
  apiHint: document.getElementById('api-hint'),
  refreshBtn: document.getElementById('refresh-btn'),
  loadHealthBtn: document.getElementById('load-health-btn'),
  formationForm: document.getElementById('formation-form'),
  contentForm: document.getElementById('content-form'),
  completeForm: document.getElementById('complete-form'),
  purchaseForm: document.getElementById('purchase-form'),
};

function loadAccounts() {
  try {
    const rawAccounts = localStorage.getItem(accountsStorageKey);
    if (!rawAccounts) {
      localStorage.setItem(accountsStorageKey, JSON.stringify(seededAccounts));
      return [...seededAccounts];
    }

    const parsedAccounts = JSON.parse(rawAccounts);
    if (!Array.isArray(parsedAccounts) || !parsedAccounts.length) {
      localStorage.setItem(accountsStorageKey, JSON.stringify(seededAccounts));
      return [...seededAccounts];
    }

    return parsedAccounts;
  } catch (error) {
    localStorage.setItem(accountsStorageKey, JSON.stringify(seededAccounts));
    return [...seededAccounts];
  }
}

function saveAccounts(accounts) {
  state.accounts = accounts;
  localStorage.setItem(accountsStorageKey, JSON.stringify(accounts));
}

function loadSession() {
  try {
    const rawSession = sessionStorage.getItem(sessionStorageKey);
    return rawSession ? JSON.parse(rawSession) : null;
  } catch (error) {
    return null;
  }
}

function saveSession(session) {
  state.session = session;
  sessionStorage.setItem(sessionStorageKey, JSON.stringify(session));
}

function clearSession() {
  state.session = null;
  sessionStorage.removeItem(sessionStorageKey);
}

function isAdmin() {
  return state.session?.role === 'admin';
}

function canAccessUserPage() {
  return state.session?.role === 'user' || state.session?.role === 'admin';
}

function redirectToRoleHome() {
  if (state.session?.role === 'admin') {
    window.location.replace('/web/admin.html');
    return;
  }

  if (state.session?.role === 'user') {
    window.location.replace('/web/user.html');
  }
}

function requireSession(allowedRoles) {
  if (!state.session) {
    window.location.replace('/');
    return false;
  }

  if (!allowedRoles.includes(state.session.role)) {
    window.location.replace('/');
    return false;
  }

  return true;
}

function updateAuthSummary() {
  if (!elements.authSummary) {
    return;
  }

  if (!state.session) {
    elements.authSummary.textContent = 'Compte invité';
    return;
  }

  elements.authSummary.textContent = `${state.session.displayName} · ${state.session.role}`;
}

function setAuthMessage(message, type = 'info') {
  if (!elements.authMessage) {
    return;
  }

  elements.authMessage.textContent = message;
  elements.authMessage.dataset.state = type;
}

function findAccount(email, password) {
  const normalizedEmail = String(email || '').trim().toLowerCase();
  return state.accounts.find((account) =>
    account.email.toLowerCase() === normalizedEmail && account.password === password
  ) || null;
}

function createUserAccount(formData) {
  const email = String(formData.get('email') || '').trim().toLowerCase();
  const password = String(formData.get('password') || '');

  if (!email || !password) {
    throw new Error('Email et mot de passe requis.');
  }

  if (state.accounts.some((account) => account.email.toLowerCase() === email)) {
    throw new Error('Ce compte existe déjà.');
  }

  const newAccount = {
    email,
    password,
    role: 'user',
    displayName: `${String(formData.get('prenom') || '').trim()} ${String(formData.get('nom') || '').trim()}`.trim() || 'Utilisateur',
  };

  saveAccounts([...state.accounts, newAccount]);
  return newAccount;
}

function loadProfile() {
  try {
    const rawProfile = localStorage.getItem(profileStorageKey);
    if (!rawProfile) {
      return { nom: '', prenom: '', adresse: '', email: '', dateNaissance: '' };
    }

    return { nom: '', prenom: '', adresse: '', email: '', dateNaissance: '', ...JSON.parse(rawProfile) };
  } catch (error) {
    return { nom: '', prenom: '', adresse: '', email: '', dateNaissance: '' };
  }
}

function saveProfile(profile) {
  state.profile = { ...state.profile, ...profile };
  localStorage.setItem(profileStorageKey, JSON.stringify(state.profile));
}

function prettyPrint(value) {
  return JSON.stringify(value, null, 2);
}

function escapeHtml(value) {
  return String(value ?? '')
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function formatMoney(value) {
  return `${Number(value || 0).toFixed(2)}€`;
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
  if (elements.apiStatus) {
    elements.apiStatus.textContent = online ? 'Connectée' : 'Hors ligne';
  }

  if (elements.apiStatusDot) {
    elements.apiStatusDot.classList.toggle('ok', online);
  }

  if (elements.apiHint) {
    elements.apiHint.textContent = message;
  }
}

function setOutput(title, payload) {
  if (!elements.apiOutput) {
    return;
  }

  elements.apiOutput.textContent = `${title}\n\n${prettyPrint(payload)}`;
}

function getUserFormations(formations) {
  const email = state.profile.email?.trim().toLowerCase();
  if (!email) {
    return [];
  }

  return formations.filter((formation) =>
    (formation.lesEleves || []).some((student) => String(student.email || '').trim().toLowerCase() === email)
  );
}

function setSelectedFormation(formationId) {
  state.selectedFormationId = formationId;
  document.querySelectorAll('input[name="formationId"]').forEach((input) => {
    input.value = String(formationId);
  });
  updateSelectedFormationCard();
}

function updateSelectedFormationCard() {
  if (!elements.selectedFormationTitle || !elements.selectedFormationBody || !elements.selectedFormationBadge) {
    return;
  }

  const formation = state.formations.find((item) => item.id === state.selectedFormationId) || state.formations[0] || null;

  if (!formation) {
    elements.selectedFormationTitle.textContent = 'Aucune formation sélectionnée';
    elements.selectedFormationBody.textContent = 'Choisissez une formation dans le catalogue pour afficher ses détails.';
    elements.selectedFormationBadge.textContent = 'Vide';
    return;
  }

  const contents = (formation.contenuePedagogiques || [])
    .map((content) => `${content.titre} (${content.estTermine ? 'terminé' : 'à suivre'})`)
    .join(' • ') || 'Aucun contenu pour le moment';
  const students = (formation.lesEleves || []).length;

  elements.selectedFormationTitle.textContent = formation.titre;
  elements.selectedFormationBody.textContent = `${formation.description || 'Sans description'}\n${formatMoney(formation.prix)} · ${formation.contenuePedagogiques?.length || 0} contenus · ${students} étudiant(s) · progression ${Number(formation.tauxProgression || 0).toFixed(0)}%\n${contents}`;
  elements.selectedFormationBadge.textContent = `#${formation.id}`;
}

function computeStats(formations, mode) {
  if (!elements.stats) {
    return;
  }

  const totalFormations = formations.length;
  const totalContents = formations.reduce((count, formation) => count + (formation.contenuePedagogiques?.length || 0), 0);
  const totalStudents = formations.reduce((count, formation) => count + (formation.lesEleves?.length || 0), 0);
  const averagePrice = totalFormations
    ? formations.reduce((sum, formation) => sum + (formation.prix || 0), 0) / totalFormations
    : 0;

  const myFormations = getUserFormations(formations);
  const averageProgress = myFormations.length
    ? myFormations.reduce((sum, formation) => sum + Number(formation.tauxProgression || 0), 0) / myFormations.length
    : 0;

  const cards = mode === 'user'
    ? [
        { label: 'Catalogue', value: totalFormations, note: 'Formations visibles par tous' },
        { label: 'Mes formations', value: myFormations.length, note: 'Inscrites avec mon email' },
        { label: 'Progression moyenne', value: `${averageProgress.toFixed(0)}%`, note: 'Moyenne des cours suivis' },
        { label: 'Contenus suivis', value: myFormations.reduce((count, formation) => count + (formation.contenuePedagogiques?.length || 0), 0), note: 'Vidéos, PDF et quiz' },
      ]
    : [
        { label: 'Formations', value: totalFormations, note: 'Catalogue administré' },
        { label: 'Contenus', value: totalContents, note: 'Modules pédagogiques' },
        { label: 'Étudiants', value: totalStudents, note: 'Inscriptions enregistrées' },
        { label: 'Prix moyen', value: formatMoney(averagePrice), note: 'Avant promotions' },
      ];

  elements.stats.innerHTML = cards.map((card) => `
    <div class="stat-card">
      <p class="section-kicker">${escapeHtml(card.label)}</p>
      <span class="stat-value">${escapeHtml(card.value)}</span>
      <p class="stat-note">${escapeHtml(card.note)}</p>
    </div>
  `).join('');
}

function renderFormationCards(container, formations, mode) {
  if (!container) {
    return;
  }

  if (!formations.length) {
    container.innerHTML = '<p class="empty-state">Aucune formation à afficher pour le moment.</p>';
    return;
  }

  container.innerHTML = formations.map((formation) => {
    const contents = (formation.contenuePedagogiques || [])
      .map((content) => `<span class="tag">${escapeHtml(content.titre)} · ${content.estTermine ? 'terminé' : 'en cours'}</span>`)
      .join('');
    const students = (formation.lesEleves || []).map((student) => `${escapeHtml(student.prenom)} ${escapeHtml(student.nom)}`).join(', ') || 'Aucun étudiant';

    const actions = mode === 'user'
      ? `
        <button class="button primary small" data-enroll-formation="${formation.id}">Choisir pour l'achat</button>
        <button class="button ghost small" data-pick-formation="${formation.id}">Voir le détail</button>
      `
      : `
        <button class="button ghost small" data-pick-formation="${formation.id}">Préparer les formulaires</button>
      `;

    return `
      <article class="formation-card ${state.selectedFormationId === formation.id ? 'is-selected' : ''}">
        <div class="formation-top">
          <div>
            <h3>${escapeHtml(formation.titre)}</h3>
            <p class="formation-meta">${escapeHtml(formation.description || 'Sans description')}</p>
          </div>
          <span class="badge">#${formation.id}</span>
        </div>
        <div class="formation-meta-line">
          <strong>${formatMoney(formation.prix)}</strong>
          <span>${formation.contenuePedagogiques?.length || 0} contenus</span>
          <span>${Number(formation.tauxProgression || 0).toFixed(0)}% progression</span>
        </div>
        <p class="formation-meta"><strong>Étudiants :</strong> ${students}</p>
        <div class="tag-row">${contents || '<span class="tag">Aucun contenu</span>'}</div>
        <div class="card-actions">${actions}</div>
      </article>
    `;
  }).join('');
}

function renderHome(formations) {
  computeStats(formations, 'home');
  renderFormationCards(elements.formationList, formations.slice(0, 6), 'home');
  if (elements.formationBadge) {
    elements.formationBadge.textContent = `${formations.length} formation(s)`;
  }
}

function renderAdmin(formations) {
  computeStats(formations, 'admin');
  renderFormationCards(elements.formationList, formations, 'admin');
  if (elements.formationBadge) {
    elements.formationBadge.textContent = `${formations.length} formation(s)`;
  }
  updateSelectedFormationCard();
}

function renderUser(formations) {
  const myFormations = getUserFormations(formations);
  computeStats(formations, 'user');
  renderFormationCards(elements.catalogList, formations, 'user');
  renderFormationCards(elements.myFormations, myFormations, 'user');

  if (elements.profileSummary) {
    elements.profileSummary.textContent = state.profile.email
      ? `Profil actif: ${state.profile.prenom || 'Utilisateur'} ${state.profile.nom || ''} (${state.profile.email})`
      : 'Renseignez votre profil pour voir vos formations suivies.';
  }

  if (elements.formationBadge) {
    elements.formationBadge.textContent = `${myFormations.length} suivi(s)`;
  }

  updateSelectedFormationCard();
}

function syncProfileInputs() {
  if (elements.profileNom) {
    elements.profileNom.value = state.profile.nom || '';
  }
  if (elements.profilePrenom) {
    elements.profilePrenom.value = state.profile.prenom || '';
  }
  if (elements.profileAdresse) {
    elements.profileAdresse.value = state.profile.adresse || '';
  }
  if (elements.profileEmail) {
    elements.profileEmail.value = state.profile.email || '';
  }
  if (elements.profileDateNaissance) {
    elements.profileDateNaissance.value = state.profile.dateNaissance || '';
  }
}

async function loadFormations() {
  try {
    const formations = await requestJson('/formations');
    state.formations = formations;

    if (page === 'user' && !state.selectedFormationId && formations.length) {
      setSelectedFormation(formations[0].id);
    }

    if (page === 'home') {
      renderHome(formations);
      setApiState(true, 'Le catalogue public est chargé depuis /api/formations.');
      setOutput('GET /formations', formations);
      return;
    }

    if (page === 'admin') {
      renderAdmin(formations);
      setApiState(true, 'Le panel admin lit directement le catalogue et les inscriptions.');
      setOutput('GET /formations', formations);
      return;
    }

    if (page === 'user') {
      renderUser(formations);
      setApiState(true, 'La vue utilisateur filtre le catalogue et les formations suivies.');
      setOutput('GET /formations', formations);
    }
  } catch (error) {
    setApiState(false, error.message);
    if (elements.stats) {
      elements.stats.innerHTML = '';
    }
    if (elements.formationList) {
      elements.formationList.innerHTML = '<p class="empty-state">Impossible de charger les données pour le moment.</p>';
    }
    if (elements.catalogList) {
      elements.catalogList.innerHTML = '<p class="empty-state">Impossible de charger le catalogue.</p>';
    }
    if (elements.myFormations) {
      elements.myFormations.innerHTML = '<p class="empty-state">Aucune formation suivie ne peut être affichée.</p>';
    }
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
    await loadFormations();
  } catch (error) {
    setOutput('Erreur création', { message: error.message });
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
    await loadFormations();
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
    await loadFormations();
  } catch (error) {
    setOutput('Erreur progression', { message: error.message });
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
      nom: formData.get('nom') || state.profile.nom,
      prenom: formData.get('prenom') || state.profile.prenom,
      adresse: formData.get('adresse') || state.profile.adresse,
      email: formData.get('email') || state.profile.email,
      dateNaissance: formData.get('dateNaissance') || state.profile.dateNaissance,
    },
  };

  try {
    const result = await requestJson(`/formations/${formationId}/purchase`, {
      method: 'POST',
      body: JSON.stringify(body),
    });

    setOutput(`POST /formations/${formationId}/purchase`, result);
    saveProfile(body.student);
    syncProfileInputs();
    await loadFormations();
  } catch (error) {
    setOutput('Erreur achat', { message: error.message });
  }
}

function bindCardActions(container) {
  if (!container) {
    return;
  }

  container.addEventListener('click', (event) => {
    const pickButton = event.target.closest('[data-pick-formation]');
    const enrollButton = event.target.closest('[data-enroll-formation]');

    if (pickButton) {
      setSelectedFormation(Number(pickButton.dataset.pickFormation));
      return;
    }

    if (enrollButton) {
      const formationId = Number(enrollButton.dataset.enrollFormation);
      setSelectedFormation(formationId);
      elements.purchaseForm?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  });
}

function bindEvents() {
  elements.refreshBtn?.addEventListener('click', loadFormations);
  elements.loadHealthBtn?.addEventListener('click', pingApi);
  elements.formationForm?.addEventListener('submit', createFormation);
  elements.contentForm?.addEventListener('submit', addContent);
  elements.completeForm?.addEventListener('submit', completeContent);
  elements.purchaseForm?.addEventListener('submit', purchaseFormation);
  elements.profileForm?.addEventListener('submit', (event) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    saveProfile({
      nom: formData.get('nom'),
      prenom: formData.get('prenom'),
      adresse: formData.get('adresse'),
      email: formData.get('email'),
      dateNaissance: formData.get('dateNaissance'),
    });
    syncProfileInputs();
    loadFormations();
    setOutput('Profil utilisateur', state.profile);
  });

  bindCardActions(elements.formationList);
  bindCardActions(elements.catalogList);
  bindCardActions(elements.myFormations);
}

function bindAuthEvents() {
  elements.loginForm?.addEventListener('submit', (event) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const account = findAccount(formData.get('email'), formData.get('password'));

    if (!account) {
      setAuthMessage('Identifiants invalides.', 'error');
      return;
    }

    saveSession({
      email: account.email,
      role: account.role,
      displayName: account.displayName,
    });
    setAuthMessage('Connexion réussie.', 'success');
    redirectToRoleHome();
  });

  elements.registerForm?.addEventListener('submit', (event) => {
    event.preventDefault();

    try {
      const formData = new FormData(event.currentTarget);
      const account = createUserAccount(formData);
      saveSession({
        email: account.email,
        role: account.role,
        displayName: account.displayName,
      });
      saveProfile({
        nom: formData.get('nom'),
        prenom: formData.get('prenom'),
        email: formData.get('email'),
        adresse: '',
        dateNaissance: '',
      });
      setAuthMessage('Compte créé. Redirection en cours…', 'success');
      redirectToRoleHome();
    } catch (error) {
      setAuthMessage(error.message, 'error');
    }
  });
}

function bindLogout() {
  elements.logoutBtn?.addEventListener('click', () => {
    clearSession();
    window.location.replace('/');
  });
}

function initPage() {
  updateAuthSummary();

  if (page === 'auth') {
    if (state.session) {
      redirectToRoleHome();
      return;
    }

    setAuthMessage('Utilise un compte existant ou crée un compte user.', 'info');
    bindAuthEvents();
    return;
  }

  if (page === 'admin') {
    if (!requireSession(['admin'])) {
      return;
    }

    bindLogout();
    if (elements.authSummary) {
      elements.authSummary.textContent = `${state.session.displayName} · admin`;
    }
  }

  if (page === 'user') {
    if (!requireSession(['user', 'admin'])) {
      return;
    }

    bindLogout();
    if (elements.authSummary) {
      elements.authSummary.textContent = `${state.session.displayName} · ${state.session.role}`;
    }
  }

  syncProfileInputs();
  bindEvents();
  pingApi();
  loadFormations();
}

initPage();
