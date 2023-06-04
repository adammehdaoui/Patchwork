# Manuel utilisateur

## 1. Lancer le jeu

---

> Pour lancer le jeu, il suffit d'aller dans le répertoire du jeu puis dans le dossier
**target** et de lancer le fichier Patchwork.jar avec la commande suivante :

```shell
java -jar Patchwork.jar
```

## 2. En jeu

---

Une fois le jeu lancé, il vous sera demandé de choisir un mode de jeu. Vous pouvez alors
choisir entre la version `1` (base), `2` (complète) ou `3` (personnalisée). Si vous choisissez une version invalide, le jeu vous le signalera et s'arrêtera.
Le jeu vous demande ensuite l'affichage souhaité pour le jeu. Vous pouvez choisir entre `1` (console) et `2` (graphique). Si vous choisissez une version invalide, pour le mode ou l'affichage, le jeu vous le signalera et s'arrêtera.

### 1. Console

---

L'affichage de l'état du jeu se fait dans la console. Il se fait dans l'ordre suivant :

- Plateau de jeu du joueur 1
- Plateau de jeu principal (`"TimeBoard"`)
- Plateau de jeu du joueur 2
- Indicateur du joueur qui joue
- Liste des pièces jouables

À ce moment, il y a deux possibilités pour le joueur :

#### 1. Acheter une pièce

Pour acheter une pièce le joueur saisit **"oui"** puis choisit la pièce qu'il veut acheter en entrant sa position (1, 2 ou 3) dans l'affichage tel qu'il est dans le terminal.

Le joueur a ensuite le choix entre trois possibilités :

1. Effectuer une rotation de la pièce choisie (en saisissant `rotate`)
2. Retourner la pièce choisie (en saisissant `invert`)
3. Placer la pièce choisie avec les modifications effectuées (en saisissant `validate`)

#### 2. Passer son tour

Pour passer son tour, le joueur saisit **"non"**.

#### 3. Fin de partie

La partie se termine lorsque les deux joueurs sont arrivés à la fin du `TimeBoard`.
Le joueur gagnant est alors affiché dans le terminal.

### 2. Graphique

---

#### 1. Affichages

- Le plateau du joueur 1 est affiché en haut à gauche de la fenêtre et le plateau du joueur 2 est affiché en haut à droite de la fenêtre (avec chacun en dessous le nombre de boutons que possède le joueur correspondant).
- Le joueur dont c'est le tour est affiché en bas à droite de la fenêtre.
- Les commandes du jeu sont affichées à droite ou en bas à gauche de la fenêtre.

#### 2. Commandes en jeu

O : Acheter une pièce
<br>
N (ou autre) : Passer son tour

- Lorsque le joueur passe sur une case `Patch` : cliquer sur une case pour poser la pièce.
- Lorsque le joueur souhaite acheter une pièce : cliquer sur la pièce pour la choisir. Plusieurs commandes sont alors affichées : 

R : Effectuer une rotation de la pièce choisie
<br>
I : Retourner la pièce choisie
<br>
V : Valider la pièce

> Une fois la pièce validée, le joueur peut la poser sur son plateau en cliquant sur une case.

#### 3. Fin de partie

La partie se termine lorsque les deux joueurs sont arrivés à la fin du `"TimeBoard"`. Une fenêtre s'affiche alors pour indiquer le joueur gagnant avec son nombre de points.