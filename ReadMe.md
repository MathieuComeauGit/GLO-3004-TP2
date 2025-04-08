DON'T FORGET TO CHANGE TO LISEZ MOI

**Equipe 18**\
*Membres de l'équipe*\
Maude Beaulieu (536 893 388)\
Mathieu Comeau (111 269 609)\
Sylviane Chedjoun Guiatchuing (111 287 581)

# Simulation de Production de Chocolat – README

## 📦 Prérequis

Ce projet est une simulation de processus de fabrication de chocolat utilisant des **threads Java** pour simuler l'interaction entre des chocolatiers, tempereuses et mouleuses.

Le projet utilise :

- **Java 17 ou +**
- **Maven**
- **Spark Java** (pour le serveur web léger)
- **Frontend statique** en HTML/CSS/JS

---

## 🚀 Lancer la simulation

### 1. Prérequis d’installation

Assurez-vous d’avoir les éléments suivants installés sur votre machine :

- **Java 17 ou plus**
- **Maven**

#### ✅ Installer Java

- **Windows / Mac / Linux** : Téléchargez le JDK depuis https://adoptium.net ou utilisez un gestionnaire de paquets :

```bash
# macOS avec Homebrew
brew install openjdk@17

# Ubuntu / Debian
sudo apt update
sudo apt install openjdk-17-jdk

# Windows
# Télécharger et installer depuis https://adoptium.net
```

Ensuite, vérifiez :

```bash
java -version
```

#### ✅ Installer Maven

```bash
# macOS
brew install maven

# Ubuntu / Debian
sudo apt install maven

# Windows
# Télécharger Maven : https://maven.apache.org/download.cgi
# Ajouter `bin` de Maven à votre PATH
```

Vérifiez :

```bash
mvn -v
```

---

### 2. Cloner et lancer le projet

1. Dézippez le projet dans un dossier local et l'ouvrir. Ne pas oublier de cd dans le projet.
```bash
cd GLO-3004-TP2
```

2. Lancez le serveur Java :

```bash
mvn clean compile exec:java
```

Une fois démarré, ouvrez un navigateur à l’adresse :

```
http://localhost:4567

```

---

## Interface utilisateur

L’interface web vous permet de **contrôler et observer le système de fabrication de chocolat**.

### Configuration des paramètres

En haut de la page, vous trouverez un panneau d’initialisation avec des champs numériques :

- `Chocolatiers N / B` : Nombre de chocolatiers pour les groupes **N** et **B**.
- `Tempereuses N / B` : Nombre de machines qui tempèrent le chocolat, séparées par groupe.
- `Mouleuses N / B` : Nombre de machines qui moulent le chocolat, également séparées par groupe.

Chaque groupe (N et B) utilise **ses propres machines**. Aucun partage entre les groupes.

Après avoir configuré les valeurs, cliquez sur **Lancer Simulation**. Pour tout réinitialiser à zéro, cliquez sur **Réinitialiser**.

---

### Visualisation de la simulation

L’interface affiche deux colonnes : **Groupe N** et **Groupe B**.

#### Chocolatiers

Chaque chocolatier est représenté par une **barre de progression horizontale**. C’est **la progression principale** que vous devez suivre.

Ils traversent les étapes suivantes :

```
AUCUNE → REQUIERE_TEMPEREUSE → TEMPERE_CHOCOLAT → DONNE_CHOCOLAT →
REQUIERE_MOULEUSE → REMPLIT → GARNIT → FERME → AUCUNE
```

- Les étapes en **violet** indiquent qu’ils sont temporairement sous le **contrôle d’une machine**.
- Un chocolatier peut rester bloqué s’il attend une machine occupée.

#### Machines (Tempereuses et Mouleuses)

Chaque machine est représentée par une **mini barre de progression** avec son ID et l’ID du chocolatier en cours d’utilisation (si applicable).

- `TEMPERE_CHOCOLAT` → `DONNE_CHOCOLAT` : Travail de la tempereuse.
- `REMPLIT` → `GARNIT` → `FERME` : Travail de la mouleuse.

Les machines avancent automatiquement, indépendamment, en suivant leur propre logique métier.

---

### Boucle et comportement

- Tous les threads tournent en continu avec un délai aléatoire entre 1 et 5 secondes.
- Chaque entité (chocolatier, tempereuse, mouleuse) évolue **en fonction des règles métier**.
- Un chocolatier ne peut progresser que si les ressources nécessaires sont disponibles.
- Lorsqu'une machine termine son étape, elle **débloque automatiquement** le chocolatier associé.

---

## Réinitialisation

- La route `POST /api/reset` vide tous les repositories
- Les threads sont terminés automatiquement (non daemon actuellement)
- Permet un redémarrage complet

---
