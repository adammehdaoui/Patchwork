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
choisir entre la version `1` (base) et `2` (complète). Si vous choisissez une version invalide, le jeu vous le signalera et s'arrêtera.
Une fois le mode choisi, la partie commence.

L'affichage de l'état du jeu se fait dans la console. Il se fait dans l'ordre suivant :

- Plateau de jeu du joueur 1
- Plateau de jeu principal ("TimeBoard")
- Plateau de jeu du joueur 2
- Indicateur du joueur qui joue
- Liste des pièces jouables

À ce moment, il y a deux possibilités pour le joueur :

### ***1. Acheter une pièce***

Pour acheter une pièce le joueur saisit **"oui"** puis choisit la pièce qu'il veut acheter en entrant sa position (1, 2 ou 3) dans l'affichage tel qu'il est dans le terminal.

Le joueur a ensuite le choix entre trois possibilités :

1. Effectuer une rotation de la pièce choisie (en saisissant `rotate`)
2. Retourner la pièce choisie (en saisissant `invert`)
3. Placer la pièce choisie avec les modifications effectuées (en saisissant `validate`)

### ***2. Passer son tour***

Pour passer son tour, le joueur saisit **"non"**.

## 3. Fin de partie

---

La partie se termine lorsque les deux joueurs sont arrivés à la fin du "TimeBoard".
Le joueur gagnant est alors affiché dans le terminal.

