let url = 'https://192.168.75.61/api/v3/';
let token = null;
let loginName = null;


/**
 * Fonction de filtre permettant d'appeler d'autres fonctions en fonction du hash indiqué dans l'URL.
 * @param hash
 */
function filterHash(hash){
	switch (hash) {
		case '#index':
			updateViewIndex();
			break;
		case '#monCompte':
			affichage('compte', 'monCompteAffichage', 'monCompteTemplate', true);
			break;
		case '#entree':
			break;
		case '#sortie':
			break;
		case '#passages':
			affichage('user', 'passagesAffichage', 'passagesTemplate', false);
			break;
		case '#deco':
			break;
		default:
			window.location.assign("#index");
			break;
	}
}

/**
 * Fonction de mise à jour de la page #index.
 */
function updateViewIndex(){
	if (loginName !== null && token !== null){
		$("#indexLogged").removeClass('inactive').addClass('active');
		$("#indexLogin").removeClass('active').addClass('inactive');

		affichage('encours', 'indexPassagesAffichage', 'indexPassagesTemplate', false);
	} else {
		$("#indexLogged").removeClass('active').addClass('inactive');
		$("#indexLogin").removeClass('inactive').addClass('active');
	}
}

/**
 * Connecte l'utilisateur.
 */
function login() {

	let myData = {
		login: $("#login").val(),
		nom: $("#nom").val(),
		admin: true
	};


	let myInit = { method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		mode: 'cors',
		cache: 'default',
		body: JSON.stringify(myData)
	};

	let myRequest = new Request(url + "users/login");

	fetch(myRequest,myInit)
		.then((resp) => {
			token = resp.headers.get("Authorization");
			loginName = $("#login").val();
			window.location.assign("#index");
			$("#login").val("");
			$("#nom").val("");
		})
		.catch((error) => {
			document.getElementById("errMsg").innerHTML = "Erreur lors de la connection: " + error;
		});

}

/**
 * Déconnecte l'utilisateur.
 */
function logout(){
	let myData = {
		login: $("#login").val(),
		nom: $("#nom").val(),
		admin: true
	};


	let myInit = { method: 'POST',
		headers: { 'Accept': '*/*', 'Authorization': token },
		mode: 'cors',
		cache: 'default',
		body: JSON.stringify(myData)
	};

	let myRequest = new Request(url + "users/logout");

	fetch(myRequest,myInit)
		.then((resp) => {
			token = null;
			loginName = null;
			window.location.assign("#index");
		})
		.catch((error) => {
			document.getElementById("errMsg").innerHTML = "Erreur lors de la déconnexion: " + error;
		});
}

/**
 * Met à jour le nom de l'utilisateur connecté.
 */
function updateNom(){

	document.getElementById("monCompteNom").innerHTML = 'Mise à jour du nom en cours...';

	let jsonData = {
		nom: $("#nomUpdate").val()
	};

	let myInit = { method: 'PUT',
		headers: { 'Authorization': token, 'Content-Type': 'application/json' },
		mode: 'cors',
		cache: 'default',
		body: JSON.stringify(jsonData)
	};

	let myRequest = new Request(url + "users/" + loginName);

	fetch(myRequest,myInit)
		.then(resp => {
			document.getElementById('monCompteNom').innerHTML = $("#nomUpdate").val();
			$("#nomUpdate").val("");
		})
		.catch((error) => {
			document.getElementById('monCompteAffichage').innerHTML = "Une erreur s'est produite: " + error;
		});
}

/**
 * Permet l'ajout d'un passage.
 * @param entreeOuSortie un booléen permettant de savoir s'il s'agit d'une entrée ou d'une sortie.
 */
function updatePassage(entreeOuSortie){
	//S'il s'agit d'une entrée alors on vérifie s'il y a un passage en cours (si oui on update la date) sinon on ajoute le passage
	//Sinon on doit récupérer le passage en cours existant et ajoutons une sortie associée. Si le passage n'existe pas alors on a un souci
	let textSalle;
	let jsonData;
	let myInit;

	if (entreeOuSortie === 'entree'){
		textSalle = $("#entreeSalle").val();
		jsonData = {
			user: loginName,
			salle: textSalle
		};
		myInit = { method: 'POST',
			headers: { 'Content-Type': 'application/json', 'Authorization': token },
			mode: 'cors',
			cache: 'default',
			body: JSON.stringify(jsonData)
		};
	} else {
		textSalle = $("#sortieSalle").val();
		jsonData = "user=" + loginName + "&salle=" + textSalle + "&dateSortie=now";
		myInit = { method: 'POST',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'Authorization': token },
			mode: 'cors',
			cache: 'default',
			body: jsonData
		};
	}



	let myRequest = new Request(url + "passages");

	fetch(myRequest,myInit)
		.then((resp) => {
			$("#" + textSalle).val("");
			window.location.assign("#passages");
		})
		.catch((error) => {
			document.getElementById("errMsg").innerHTML = "Erreur lors de l'ajout d'un passage: " + error;
		});

}

/**
 * Affichage des vues (liste des passages selon un utilisateur).
 * @param filtre.
 * @param affichageDiv l'élement pour l'affichage des données récupérées.
 * @param templateDiv l'élément qui contient le squelette pour l'affichage des données.
 */
function affichage(filtre, affichageDiv, templateDiv, affichageUneLigne){
	document.getElementById(affichageDiv).innerHTML = 'Chargement en cours...';
	let newUrl = url;
	let textAffichage;

	switch (filtre) {
		case 'user':
			newUrl += "passages/byUser/" + loginName;
			textAffichage = "Affichage des passages par l'utilisateur " + loginName;
			break;
		case 'encours':
			newUrl += "passages/byUser/" + loginName + '/enCours';
			textAffichage = "Affichage des passages par l'utilisateur " + loginName + " en cours";
			break;
		case 'compte':
			newUrl += "users/" + loginName;
			textAffichage = "Informations sur votre compte";
			break;
		case 'salle':
			newUrl += "salles/" + $("#indexSalleId").val();
			textAffichage = "Informations sur la salle " + $("#indexSalleId").val();
			break;
		default:
			textAffichage = "Erreur interne: Mauvaise utilisation de la foncion d'affichages...";
			document.getElementById(affichageDiv).innerHTML = textAffichage;
			return;
	}

	let myInit = { method: 'GET',
		headers: { 'Authorization': token, 'Accept': 'application/json' },
		mode: 'cors',
		cache: 'default'
	};

	let myRequest = new Request(newUrl);

	fetch(myRequest,myInit)
		.then(resp => resp.json())
		.then(data => {
			var res = textAffichage + "<br/>";
			var templ = document.getElementById(templateDiv).innerHTML;

			if (data.length === 0){
				res += "Aucun résultat retourné";
			} else if (affichageUneLigne === true){
				res += Mustache.render(templ, data);
			} else {
				for (var i = 0; i < data.length; i++){
					res += "<li>" + Mustache.render(templ, data[i].replace("passages/", "")) + "</li>";
				}
			}

			document.getElementById(affichageDiv).innerHTML = res;
		})
		.catch((error) => {
			document.getElementById(affichageDiv).innerHTML = "Une erreur s'est produite: " + error;
		});
}
