let url = 'https://192.168.75.61/api/v3/';
let token = null;
let loginName = null;


// Function to log the user into the API
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
			window.location.assign("#monCompte");
			$("#login").val("");
			$("#nom").val("");
		})
		.catch((error) => {
			document.getElementById("errMsg").innerHTML = "Erreur lors de la connection: " + error;
		});

}

function filterHash(hash){
	switch (hash) {
		case '#index':
			break;
		case '#monCompte':
			chargeCompte();
			break;
		case '#entree':
			break;
		case '#sortie':
			break;
		case '#passages':
			getPassages('user');
			break;
		case '#deco':
			break;
		default:
			window.location.assign("#index");
			break;
	}
}

function chargeCompte(){

	document.getElementById("monCompteAffichage").innerHTML = 'En cours de chargement...';

	let myInit = { method: 'GET',
		headers: { 'Authorization': token, 'Accept': 'application/json' },
		mode: 'cors',
		cache: 'default'
	};

	let myRequest = new Request(url + "users/" + loginName);

	fetch(myRequest,myInit)
		.then(resp => resp.json())
		.then(data => {
			var templ = document.getElementById('monCompteTemplate').innerHTML;
			var res = "";

			res += Mustache.render(templ, data);

			document.getElementById('monCompteAffichage').innerHTML = res;
		})
		.catch((error) => {
			document.getElementById('monCompteAffichage').innerHTML = "Une erreur s'est produite: " + error;
		});
}

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

function getPassages(filtre){
	document.getElementById("passagesAffichage").innerHTML = 'Chargement des passages...';
	let newUrl = url;
	let textAffichage;
	let isUniquePassage = false;

	switch (filtre) {
		case 'user':
			newUrl += "passages/byUser/" + loginName;
			textAffichage = "Affichage des passages par l'utilisateur " + loginName;
			break;
		case 'encours':
			newUrl += "passages/byUser/" + loginName + '/enCours';
			textAffichage = "Affichage des passages par l'utilisateur " + loginName + " en cours";
			break;
		case 'all':
			newUrl += "passages";
			textAffichage = "Affichage de tout les passages existants";
			break;
		default:
			newUrl += "passages/" + filtre;
			textAffichage = "Affichage du passage " + filtre;
			isUniquePassage = true;
			break;
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

			if (data.length === 0){
				res += "Aucun passage actuellement";
			} else if (isUniquePassage === true){
				var templ = document.getElementById('passageTemplate').innerHTML;
				res += Mustache.render(templ, data);
			} else {
				for (var i = 0; i < data.length; i++){
					var templ = document.getElementById('passagesTemplate').innerHTML;
					res += "<li>" + Mustache.render(templ, data[i].replace("passages/", "")) + "</li>";
				}
			}

			document.getElementById('passagesAffichage').innerHTML = res;
		})
		.catch((error) => {
			document.getElementById('passagesAffichage').innerHTML = "Une erreur s'est produite: " + error;
		});
}
