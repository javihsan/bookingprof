 - esconder idiomas al ir a otra pagina de menú ¿info legal?
 
 - no hay citas, ventana proxima cita disponible en el día x
   si es admins, además, posibilidad de ir a una hora "fuera de horario" de ese dia
   
- info legal
- mail con citas previas
  y posibiliadad cancelar 

grunt serve  arrancar normal
grunt serve:dist --force construir y arrancar con construido
grunt build --force construir

hay que contruir con el grunt build cualquier cambio en la static app si queremos que lo coja el pom del "java"

npm install bower -g  (archivo .bowerrc para proxy)
npm install grunt -g
instalar ruby en windows (scoop ruby)
SET HTTP_PROXY=http://proxy.santander.corp:8080
gem install compass
npm install coffee-script@1.12.7 -g


en app: 

//var domainLocalOfi = 'localhost:8888'; // arrancar en local con el java delante
var domainLocalOfi = 'localhost:9001'; // arrancar en local solo el front

$rootScope.changeLang ruta de los textos 
//var url = protocol_url + appHost + "/multiText/listLocaleTexts";
var url = "/js/lang_es.json" // Para rapidez al debugear solo con front

				
				console.log ("Sin dominio propio");
				
				a = location.pathname.split("/");
				appFirmDomain = a[1];
cuidado con esto				appFirmDomain = 'demo' // Para local arrancado solo con front
cuidado con esto				appHost = 'localhost:8888'; //Para tirar de un determinado back

Para Operator en local solo con el front
en applicationContext-security.xml

<!--<intercept-url pattern="/**/**operator**/**" access="hasAnyRole('ADMIN','OPERATOR','OPERATOR_READ')" />-->


hora editable
ir a ese dia con fuera d ehoea cuando no hay