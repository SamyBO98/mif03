# Readme du TP7 (correspondant au code du TP5)

Nous avons au sommaire:
- [Déploiement sur Tomcat](#déploiement-sur-tomcat)
- [Déploiement sur Nginx](#déploiement-sur-nginx)
- [Amélioration](#amélioration)


## Déploiement sur Tomcat

Voici les mesures prises lors du déploiement sur Tomcat.

*Pour rappel: les différentes mesures sont les suivantes:*
- *Le temps de chargement de la page HTML initiale*
- *Le temps d'affichage de l'app shell*
- *Le temps d'affichage du chemin critique de rendu (CRP)*

##### Temps de chargement de la page HTML initiale

Défini à 386.70 millisecondes.

##### Temps d'affichage de l'app shell

Défini à 1526.364 millisecondes.

##### Temps d'affichage du CRP

Défini à 1895.70 millisecondes.


## Déploiement sur Nginx

Après déploiement sur Tomcat, il est nécessaire de les tester en faisant le déploiement sur Nginx.

Nous aurons en plus une comparaison entre le déploiement sur Tomcat et celui sur Nginx.

##### Temps de chargement de la page HTML initiale

Défini à 119.365 millisecondes.

##### Temps d'affichage de l'app shell

Défini à 969.609 millisecondes.

##### Temps d'affichage du CRP

Défini à 1253.240 millisecondes.

##### Comparaison

**Un gain signifie que les performances lors du déploiement sur Nginx sont meilleures que sur Tomcat.**

- Temps de chargement de la page HTML initiale: Gain de 70.14%
- Temps d'affichage de l'app shell: Gain de 36.47%
- Temps d'affichage du CRP: Gain de 33.90%

## Amélioration

##### Utilisation d'attributs asynchrones

Nous avons utilisé l'attribut **async** dans les fonctions suivantes faisant appel à l'API pour récupérer ou transmettre des données.

Voici les temps notés:
- Temps de chargement de la page HTML initiale: 80.35 millisecondes.
- Temps d'affichage de l'app shell: 821.18 millisecondes.
- Temps d'affichage du CRP: 994.09 millisecondes.

On peut voir un **meilleur temps d'affichage du CRP** (*estimé à quasiment 1/4 du chargement initial*), une légère amélioration de l'app shell et de la page HTML initiale par rapport au déploiement de l'application sur Nginx sans améliorations.

##### Refactoring de l'application

Dans les fichiers javascript, nous avons remplacé jQuery pour l'affichage de texte dans des balises HTML ainsi que pour l'appel à certains menus (par exemple si on clique dans "Mes passages", alors on a bien le menu des passages en cachant l'ancien menu sur lequel l'utilisateur était).

Voici les temps notés:
- Temps de chargement de la page HTML initiale: 110.64 millisecondes.
- Temps d'affichage de l'app shell: 620.41 millisecondes.
- Temps d'affichage du CRP: 958.22 millisecondes.

On peut voir ici une **petite amélioration de l'affichage de l'app shell**. Rien de concret en ce qui concerne le CRP. Pour le chargement de la page HTML initiale, cette donnée stagne du au fait que la connexion au réseau peut fortement influer sur ces chiffres (*la connexion utilisée pour les tests n'est pas très stable ce qui peut donner à des chiffres un peu plus mauvais que ce qui est censé être...*)
