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
			affichage('compte', 'monCompteAffichage', 'monCompteTemplate', true, 'notifCompte');
			break;
		case '#entree':
			break;
		case '#sortie':
			break;
		case '#passages':
			affichage('user', 'passagesAffichage', 'passagesTemplate', false, 'notifPassages');
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
		document.getElementById("indexText").innerHTML = "Bienvenue " + loginName + "!";

		affichage('encours', 'indexPassagesAffichage', 'indexPassagesTemplate', false, 'notifPassagesEnCours');
	} else {
		$("#indexLogged").removeClass('active').addClass('inactive');
		$("#indexLogin").removeClass('inactive').addClass('active');
		document.getElementById("indexText").innerHTML = "Connexion à Présence UCBL";
	}
}

/**
 * Connecte l'utilisateur.
 */
function login(notificationDiv) {

	notificateInProcess(notificationDiv);
	document.getElementById(notificationDiv).innerHTML = 'Connexion en cours...';

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
			window.location.assign("#logged");
			removeAlert(notificationDiv);
		})
		.catch((error) => {
			sendErrorMessage(notificationDiv, error);
		});

}

/**
 * Déconnecte l'utilisateur.
 */
function logout(notificationDiv){

	notificateInProcess(notificationDiv);
	document.getElementById(notificationDiv).innerHTML = 'Déconnexion en cours...';

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
			removeAlert(notificationDiv);
		})
		.catch((error) => {
			sendErrorMessage(notificationDiv, error);
		});
}

/**
 * Met à jour le nom de l'utilisateur connecté.
 */
function updateNom(notificationDiv){

	notificateInProcess(notificationDiv);
	document.getElementById(notificationDiv).innerHTML = 'Mise à jour du nom en cours...';

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
		.then(() => {
			document.getElementById('monCompteNom').innerHTML = $("#nomUpdate").val();
			removeAlert(notificationDiv);
		})
		.catch((error) => {
			sendErrorMessage(notificationDiv, error);
		});
}

/**
 * Permet l'ajout d'un passage.
 * @param entreeOuSortie un booléen permettant de savoir s'il s'agit d'une entrée ou d'une sortie.
 * @param notificationDiv
 */
function updatePassage(entreeOuSortie, notificationDiv){
	//S'il s'agit d'une entrée alors on vérifie s'il y a un passage en cours (si oui on update la date) sinon on ajoute le passage
	//Sinon on doit récupérer le passage en cours existant et ajoutons une sortie associée. Si le passage n'existe pas alors on a un souci
	let textSalle;
	let jsonData;
	let myInit;

	notificateInProcess(notificationDiv);
	document.getElementById(notificationDiv).innerHTML = 'Enregistrement de votre passage en cours...';

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
		.then(() => {
			removeAlert(notificationDiv);
			window.location.assign("#passages");
		})
		.catch((error) => {
			sendErrorMessage(notificationDiv, error);
		});

}

/**
 * Lance une notification à l'utilisateur que sa requête est en cours de traitement.
 * @param notificationDiv element de notification à afficher.
 */
function notificateInProcess(notificationDiv){
	let div = $("#" + notificationDiv);
	div.removeClass('alert').removeClass('alert-warning').removeClass('alert-danger');
	div.addClass('alert').addClass('alert-warning');
}

function removeAlert(notificationDiv){
	let div = $("#" + notificationDiv);
	div.removeClass('alert').removeClass('alert-warning').removeClass('alert-danger');
	document.getElementById(notificationDiv).innerHTML = null;
}

/**
 * Affichage des vues (liste des passages selon un utilisateur).
 * @param filtre.
 * @param affichageDiv l'élement pour l'affichage des données récupérées.
 * @param templateDiv l'élément qui contient le squelette pour l'affichage des données.
 * @param affichageUneLigne booléen permettant de savoir s'il s'agit d'un affichage multiple ou d'un simple affichage détaillé.
 * @param notificationDiv élément de notification pour annoncer au client qu'on traite sa requête.
 */
function affichage(filtre, affichageDiv, templateDiv, affichageUneLigne, notificationDiv){

	notificateInProcess(notificationDiv);

	let newUrl = url;
	let textAffichage;

	switch (filtre) {
		case 'user':
			newUrl += "passages/byUser/" + loginName;
			textAffichage = "Affichage des passages par l'utilisateur " + loginName + " en cours...";
			break;
		case 'encours':
			newUrl += "passages/byUser/" + loginName + '/enCours';
			textAffichage = "Affichage des passages par l'utilisateur " + loginName + " en cours...";
			break;
		case 'compte':
			newUrl += "users/" + loginName;
			textAffichage = "Récupération des informations sur votre compte en cours...";
			break;
		case 'salle':
			let content = $("#indexSalleId").val();
			newUrl += "salles/" + content;
			textAffichage = "Informations sur la salle " + content + " en cours...";
			break;
	}

	document.getElementById(notificationDiv).innerHTML = textAffichage;

	let myInit = { method: 'GET',
		headers: { 'Authorization': token, 'Accept': 'application/json' },
		mode: 'cors',
		cache: 'default'
	};

	let myRequest = new Request(newUrl);

	fetch(myRequest,myInit)
		.then(resp => resp.json())
		.then(data => {
			let res;
			let templ = document.getElementById(templateDiv).innerHTML;

			if (data.length === 0){
				res = "<h4>Aucun résultat en retour</h4>";
			} else if (affichageUneLigne === true){
				res = Mustache.render(templ, data);
			} else {
				res = "";
				for (let i = 0; i < data.length; i++){
					res += "<li class='list-group-item text-dark bg-light'>" + Mustache.render(templ, data[i].replace("passages/", "")) + "</li>";
				}
			}

			document.getElementById(affichageDiv).innerHTML = res;
			removeAlert(notificationDiv);

		})
		.catch((error) => {
			document.getElementById(affichageDiv).innerHTML = null;
			sendErrorMessage(notificationDiv, error);
		});
}

/**
 * Fonction utilisant jquery permettant l'affichage de sections (et la disparition d'autres).
 * @param hash.
 */
function show(hash) {
	$('.active').removeClass('active').addClass('inactive');
	$(hash).removeClass('inactive').addClass('active');
}

/**
 * Envoie sur la page statique un message d'erreur qu'il va afficher à l'utilisateur.
 * @param affichageDiv element ou le message va s'afficher.
 * @param error l'erreur en question.
 */
function sendErrorMessage(notificationDiv, error){
	$("#" + notificationDiv).removeClass('alert-warning').addClass('alert-danger');
	document.getElementById(notificationDiv).innerHTML = "Nous venons de rencontrer une erreur: "
		+ error
		+ ".";
}

window.addEventListener('hashchange', () => {
	show(window.location.hash);
	//Cette fonction récupère la valeur du hash et appelle une certaine fonction selon ce qu'on a en valeur
	filterHash(window.location.hash);
});
