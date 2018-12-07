 - esconder idiomas al ir a otra pagina de menú ¿info legal?
 
 - no hay citas, ventana proxima cita disponible en el día x
   si es admins, además, posibilidad de ir a una hora "fuera de horario" de ese dia
   
- nuevo campo para galanovias fecha novia
- info legal
- mail con citas previas
  y posibiliadad cancelar 

grunt serve  arrancar normal
grunt serve:dist --force construir y arrancar con construido
grunt build --force construir

hay que contruir con el grunt build cualquier cambio en la static app si queremos que lo coja el pom del "java"

en app: 

//var domainLocalOfi = 'localhost:8888'; // arrancar en local con el java delante
var domainLocalOfi = 'localhost:9001'; // arrancar en local solo el front

$rootScope.changeLang ruta de los txtos 
//var url = protocol_url + appHost + "/multiText/listLocaleTexts";
var url = "/js/lang_es.json" // Para rapidez al debugear solo con front

				
				console.log ("Sin dominio propio");
				
				a = location.pathname.split("/");
				appFirmDomain = a[1];
cuidado con esto				appFirmDomain = 'demo' // Para local arrancado solo con front
cuidado con esto				appHost = 'localhost:8888'; //Para tirar de un determinado back
