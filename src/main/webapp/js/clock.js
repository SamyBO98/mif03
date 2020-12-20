//Bilblioth�que de fonctions AJAX permettant l'envoi de requ�tes au serveur de mani�re asynchrone
//et le traitement (ajout au corpos du document appelant) de r�ponses en XML conformes � la structure d�crite dans l'�nonc� du devoir.

//--------------------Fonctions principales---------------------

//fonction principale, qui envoie la  requ�te au serveur de fa�on asynchrone et positionne la fonction qui va traiter les donn�es
function loadXMLAsynchroneously(method, request, parameters, id)
{
    console.log(window.location.href);

    //initialisation de l'objet XMLXhttpRequest
    var xhr = initRequete();

    //sp�cification de la fonction de traitement � appeler au retour serveur (car chargement asynchrone)
    var XMLDoc = null;
    xhr.onreadystatechange = function() { getXMLDocument(xhr, XMLDoc, id); };

    //envoi de la requ�te de chargement du fichier XML au serveur
    //le dernier param�tre est true ; le chargement du fichier se fera en asynchrone
    xhr.open(method, request, true);
    //encodage des param�tres dans la requ�te, si la m�thode est post
    if(parameters && (method == "post" || method == "POST"))
        xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send(parameters);
}

//autre fonction principale, plus simple, qui envoie la requ�te au serveur de fa�on asynchrone et ne se pr�occupe pas de la r�ponse
//remarque : l'utilisation de cette fonction n'est pas n�cessaire pour r�aliser le devoir.
function sendRequestAsynchroneously(method, request, parameters)
{
    //initialisation de l'objet XMLXhttpRequest
    var xhr = initRequete();

    //envoi de la requ�te de chargement du fichier XML au serveur
    //le dernier param�tre est true ; le chargement du fichier se fera en asynchrone
    xhr.open(method, request, true);
    //encodage des param�tres dans la requ�te, si la m�thode est post
    if(parameters && (method == "post" || method == "POST"))
        xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xhr.send(parameters);
}

//--------------------------fonctions secondaires---------------------------

//fonction appel�e lors de la r�ception de la r�ponse, si la fonction principale loadXMLAsynchroneously() a �t� utilis�e.
function getXMLDocument(xhr, XMLDoc, id)
{
    // teste si la r�ponse est disponible
    if (xhr.readyState==4) {
        // teste si la r�ponse est arriv�e et contient des donn�es (code HTTP = 200 : OK)
        if (xhr.status == 200) {
            // teste si la r�ponse est arriv�e en XML ou en texte (peut arriver pour certaines configurations d'Apache)
            if (xhr.responseXML != null) {
                XMLDoc= xhr.responseXML;
            } else if (xhr.responseText != null) {
                //si la r�ponse est en texte, transformation en XML (voir fonction fa�ade plus bas)
                XMLDoc= parseFromString(xhr.responseText);
            }
            //D�commentez la ligne suivante pour voir le contenu XML obtenu (ne marche qu'avec FF)
            //alert((new XMLSerializer()).serializeToString(XMLDoc));

            //appel de la fonction de traitement qui va ajouter les donn�es au corps de la page (� �crire)
            traiteXML (XMLDoc, id);

            //teste si le code de statut est autre que le code renvoy� en cas d'absence de nouveaux messages.
            //Remarque : le code 1223 provient d'un bug avec IE : http://trac.dojotoolkit.org/ticket/2418
        } else if (xhr.status >= 400 && xhr.status != 1223) {
            alert("Un probl�me est survenu avec la requ�te : ");
        }
    }
}

//----------------------Fonctions fa�ades----------------------------
//permettent de masquer les diff�rences entre les navigateurs
//remarque : ces fonctions ont uniquement �t� test�es avec FF et IE7

//fonction fa�ade qui teste le type de navigateur et renvoie un objet capable de se charger de l'envoi de la requ�te.
function initRequete()
{
    var xhr = null;
    if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return xhr;
}

//fonction fa�ade qui re�oit une cha�ne de caract�res et la parse en XML pour renvoyer un objet DOM.
//remarque : le XML doit �tre bien form�, sans quoi l'erreur de parsing bloque l'ex�cution du script.
function parseFromString (docTXT) {
    // code for IE
    if (window.ActiveXObject)
    {
        var XMLDoc=new ActiveXObject("Microsoft.XMLDOM");
        XMLDoc.async="false";
        XMLDoc.loadXML(docTXT);
    }
    // code for Mozilla, Firefox, Opera, etc.
    else
    {
        var parser=new DOMParser();
        var XMLDoc=parser.parseFromString(docTXT,"text/xml");
    }
    return XMLDoc;
}

//fonction fa�ade qui renvoie le texte contenu dans un �l�ment XML
function getXMLTextContent(source)
{
    if (source.textContent != null) {
        //Gecko
        return source.textContent;
    } else {
        //IE
        return source.text;
    }
}

function traiteXML(XMLDoc, id) {

    const divElement = document.getElementById(id);

    var hours = XMLDoc.getElementsByTagName('h')[0].childNodes[0].nodeValue;
    var minutes = XMLDoc.getElementsByTagName('m')[0].childNodes[0].nodeValue;
    var seconds = XMLDoc.getElementsByTagName('s')[0].childNodes[0].nodeValue;

    // Create an object with each hand and it's angle in degrees
    var hands = [
        {
            hand: 'hours',
            angle: (hours * 30) + (minutes / 2)
        },
        {
            hand: 'minutes',
            angle: (minutes * 6)
        },
        {
            hand: 'seconds',
            angle: (seconds * 6)
        }
    ];
    // Loop through each of these hands to set their angle
    for (var j = 0; j < hands.length; j++) {
        var elements = document.querySelectorAll('.' + hands[j].hand);
        for (var k = 0; k < elements.length; k++) {
            elements[k].style.webkitTransform = 'rotateZ('+ hands[j].angle +'deg)';
            elements[k].style.transform = 'rotateZ('+ hands[j].angle +'deg)';
            // If this is a minute hand, note the seconds position (to calculate minute position later)
            if (hands[j].hand === 'minutes') {
                elements[k].parentNode.setAttribute('data-second-angle', hands[j + 1].angle);
            }
        }
    }

    $("#clockContent").addClass('inactive');
    $("#clock").removeClass('inactive').addClass('active');
}

