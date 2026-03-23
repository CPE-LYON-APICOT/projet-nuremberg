# Mise en place de GitHub Copilot

Ce guide vous accompagne pour installer et configurer GitHub Copilot sur **VS Code** et **IntelliJ IDEA**.

---

## 1. Obtenir l'accès à GitHub Copilot

GitHub Copilot est **gratuit pour les étudiants** via le GitHub Student Developer Pack.

### Étapes

1. Rendez-vous sur [education.github.com/pack](https://education.github.com/pack).
2. Cliquez sur **"Get your Pack"** et connectez-vous avec votre compte GitHub.
3. Vérifiez votre statut étudiant (email académique, carte étudiante, ou certificat de scolarité).
4. Une fois approuvé (quelques minutes à quelques jours), GitHub Copilot sera activé sur votre compte.

> **Vérification** : allez sur [github.com/settings/copilot](https://github.com/settings/copilot). Vous devriez voir "Copilot Active".

> Si vous n'arrivez pas à obtenir l'accès étudiant, vous pouvez utiliser l'essai gratuit de 30 jours.

---

## 2. Installation sur VS Code

### Installer l'extension

1. Ouvrez VS Code.
2. Allez dans l'onglet **Extensions** (`Ctrl+Shift+X`).
3. Recherchez **"GitHub Copilot"** et installez l'extension officielle (éditeur : GitHub).
4. Installez aussi **"GitHub Copilot Chat"** pour le mode conversationnel.
5. VS Code vous demandera de vous connecter à GitHub — acceptez et autorisez.

### Vérifier que ça fonctionne

1. Ouvrez un fichier `.java` dans le projet.
2. Commencez à taper du code — des suggestions grises devraient apparaître.
3. Appuyez sur `Tab` pour accepter une suggestion, `Esc` pour la refuser.

### Utiliser le Chat (mode tuteur)

1. Ouvrez le panneau Chat : `Ctrl+Shift+I` (ou cliquez sur l'icône Copilot dans la barre latérale).
2. Posez vos questions en français, par exemple :
   - *"Explique-moi le pattern Observer"*
   - *"J'ai cette erreur NullPointerException, qu'est-ce que ça signifie ?"*
   - *"Comment fonctionne GraphicsContext dans JavaFX ?"*

> **Important** : Le fichier `.github/copilot-instructions.md` du repo configure automatiquement Copilot en mode tuteur. Il ne vous donnera pas de code complet — c'est voulu.

---

## 3. Installation sur IntelliJ IDEA

### Installer le plugin

1. Ouvrez IntelliJ IDEA.
2. Allez dans **File → Settings** (ou `Ctrl+Alt+S`).
3. Naviguez vers **Plugins → Marketplace**.
4. Recherchez **"GitHub Copilot"** et cliquez sur **Install**.
5. Redémarrez IntelliJ.
6. Après le redémarrage, allez dans **File → Settings → GitHub Copilot** et connectez-vous à votre compte GitHub.

### Vérifier que ça fonctionne

1. Ouvrez un fichier `.java`.
2. Commencez à taper — des suggestions grises apparaissent en ligne.
3. `Tab` pour accepter, `Esc` pour refuser.

### Utiliser le Chat

1. Ouvrez le panneau Copilot Chat via **View → Tool Windows → GitHub Copilot Chat** (ou l'icône dans la barre d'outils).
2. Posez vos questions comme sur VS Code.

> Le fichier `.github/copilot-instructions.md` est aussi lu par Copilot dans IntelliJ. Le mode tuteur s'applique dans les deux IDE.

---

## 4. Bonnes pratiques

### À faire ✅

- **Poser des questions conceptuelles** : *"Quelle est la différence entre Strategy et State ?"*
- **Demander des explications d'erreurs** : copiez le message d'erreur et demandez ce qu'il signifie.
- **Demander une revue** : sélectionnez votre code et demandez *"Qu'est-ce qui pourrait poser problème ici ?"*
- **Explorer des pistes** : *"Si je veux que mon jeu supporte plusieurs modes, quel pattern serait adapté ?"*

### À ne pas faire ❌

- **Demander de générer du code complet** : Copilot est configuré pour refuser. Si vous contournez ça (ChatGPT, autre outil sans instructions), le code sera détecté et pénalisé.
- **Copier-coller sans comprendre** : si vous ne pouvez pas expliquer chaque ligne, ne l'utilisez pas.
- **Faire rédiger vos documents** : pitch, conception, bilan et réversibilité doivent être écrits par vous.

---

## 5. Troubleshooting

| Problème | Solution |
|---|---|
| Pas de suggestions dans l'éditeur | Vérifiez que Copilot est activé (icône dans la barre de statut). Vérifiez votre connexion GitHub. |
| "Copilot is not available" | Votre accès n'est peut-être pas encore activé. Vérifiez sur [github.com/settings/copilot](https://github.com/settings/copilot). |
| Le Chat ne répond pas | Vérifiez votre connexion internet. Redémarrez l'IDE. |
| Les suggestions sont en anglais | Posez vos questions en français, Copilot répondra en français. |
| Copilot génère du code complet malgré les instructions | Les instructions repo-level ne s'appliquent qu'au Chat, pas à l'autocomplétion inline. L'autocomplétion propose toujours des suggestions — c'est à vous de ne pas accepter aveuglément. |
