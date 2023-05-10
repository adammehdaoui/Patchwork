# Architecture technique

## 1. Architecture demandée

L'architecture demandée pour le projet est le modèle `MVC` (Modèle-Vue-Contrôleur). Le modèle `MVC` est un modèle d'architecture logicielle destiné aux interfaces graphiques. Il est utilisé pour le développement d'applications et repose sur la séparation du traitement des données (Modèle), de l'interface de l'utilisateur (Vue) et de la logique de contrôle destinée à la liaison entre les deux autres couches (Contrôleur).

## 2. Mise en place

## 2.1. Contrôleurs

> Voici un diagramme logique des contrôleurs montrant comment ces derniers sont liés et s'appellent entre eux :

![diagram_controllers](diagram_controllers.png "Diagramme logique des contrôleurs")

# 2.2. Modèles

> En ce qui concerne la couche "modèle", on a décidé de séparer les différents objets du jeu de cette manière :

- `Cell` : objet représentant une cellule du plateau de jeu (le `TimeBoard`).
- `TimeBoard` : objet représentant le plateau de jeu (le plateau du temps où les pions chronos avancent).
- `Player` : objet représentant un joueur du jeu.
- `PlayerBoard` : objet représentant le plateau de jeu d'un joueur (celui où il pose ses pièces).
- `Piece` : objet représentant une pièce du jeu.
- `PieceSet` : objet représentant un ensemble des pièces disponibles dans le jeu et traite les pièces disponibles à chaque étape.
- `PieceSetFactory` : objet dédié à la création d'un `PieceSet` à partir d'un fichier de configuration (pieces.txt).

# 2.3. Vues

> La couche "vue" n'est pour l'instant pas exploitée, car la phase 2 se limite à un affichage console et à une interface en ligne de commande. La phase 3 sera dédiée à l'implémentation de la couche "vue" avec une interface graphique. 

