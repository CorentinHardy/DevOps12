Projet DevOps :
Spécifications



Description de la chaine de build : 


Les outils que nous utiliserons dans cette chaine seront :
Shell
Maven
Junit
Spoon
Bootstrap

Un script shell sera utilisé afin de lancer notre chaîne de build et d'automatiser les étapes décrite ci-après.
Différentes options seront disponibles qui nous permettrons de : 
 donner le Path du code source à tester.
 créer différents codes mutants à partir du code source.
 passer le code source dans la batterie de test
 passer tous les codes mutants dans la batterie de test
 rediriger les résultats des différents test vers un fichier de sortie 

Nous utiliserons les différentes fonctionnalités de maven dans ce projet et nous rajouterons à celle-ci la possibilité de créer un code mutant grâce à l'outil spoon.
Ainsi, il est nécessaire dans un premier temps de builder le projet dans son ensemble sans créer de programme mutant afin de bien vérifier que celui-ci passe notre batterie de test. Puis ensuite, nous créerons plusieurs mutants et nous les passerons dans notre batterie de test. Dans l'idéal, ces mutant devront préserver la syntaxe et le typage afin de ne pas être tués à la  compilation car cela induirait un biais dans l'analyse des résultats obtenus pour ces tests. 
Considérant le grand nombre de mutants possible à créer à partir d'un même code source, il paraît nécessaire ne pas présenter le mutant déjà tué dans les tests restants. Cela présenterait une charge en terme d’exécution ainsi que d'analyse des résultats.


Mutations : 

Comme nous l'avons dit, les mutations que nous appliquerons devrons préserver la syntaxe et le typage pour ne pas que les programmes mutants soient tués lors de la phase de compilation. Cela ne présenterait aucun intérêts dans l'analyse des résultats. 

Les mutations sur les expressions arithmétiques :
* sera transformé en +
+ sera transformé en -
/ sera transformé en *
% sera transformé en /
++ sera transformé en +2
– sera transformé en -2

les mutations sur les  comparateurs: 
> sera transformé ≥ ; ≥ sera transformé >
<  sera transformé ≤ ;≤  sera transformé <
=  sera transformé != et inversement.

Les mutations sur les constantes : 
les constantes seront transformé par une constantes différentes

Les mutations sur les booléans
changer une conditions sur un booléan
changer un booléan de true à false et inversement.

Mutations sur les instructions : 
Suppression d'instructions

Selecteur : 

Afin de pouvoir interpréter les résultats, il est préférable de ne pas appliquer toutes les mutations possibles à dans un même fichier mais plutôt de créer un nombre important de mutant.
Ainsi, il paraît nécessaire de pouvoir appliquer la mutation qu'à une certaine partie du code : à une méthode, à une instruction, à une expression, une variable.




 
