var appHost = location.host;
var AppAdmin = {firmDomain: 'demo'};
var lngAppName = 'BookingProf';
var appFirmDomain = AppAdmin.firmDomain;
var appName = 'BookingProf-'+AppAdmin.firmDomain;
var langApp = '';
try{
	Lungo.init({
		name: lngAppName
	});
}
catch(err)
  {
	alert("Unsupported browser");
  }
Lungo.ready(function() {
	
	//console.log ("readyApp",Lungo.Core.environment());
	
	// set Text multiLanguaje
	changeLang(langApp);
	
	Lungo.Service.Settings.error = function(type, xhr){
		errorCode = xhr.status;
		if (!errorCode){
			errorText = findLangTextElement("label.notification.errorBase.text");
		} else { // Buscamos el errror dentro del response porque 
			responseText = xhr.responseText;
			strErrorCode = errorCode.toString()
			indx1 = responseText.indexOf("<title>");
			indx2 = responseText.indexOf("</title>");
			errorText = responseText.substring(indx1,indx2);
			indx1 = errorText.indexOf(strErrorCode)+strErrorCode.length;
			errorText = errorText.substring(indx1);
		}
		//console.log("errorText", xhr, errorCode, errorText);
		if (errorCode!=409){
			Lungo.Notification.error(findLangTextElement("label.notification.errorBase.title"), errorText, null, 3, function(){
				if (Lungo.dom("#booking")[0]){
					__FacadeCore.Router_article("booking","table-month");
				}
			});
		} else { // Estamos en el caso de no refrescar la pantalla por errores de formulario
			Lungo.Notification.error(findLangTextElement("label.notification.errorBase.title"), errorText, null, 3, function(){
				buttonSave = Lungo.dom("a[data-action=save]");
				if (buttonSave){
					buttonCancel = Lungo.dom("a[data-action=cancel]");
					buttonSave.html (__FacadeCore.Cache_get (appName + "elementSave"));
					buttonCancel.html (__FacadeCore.Cache_get (appName + "elementCancel"));
				}
			});	
		}
	};
	
	__FacadeCore.Service_Settings_async(true);
	__FacadeCore.Service_Settings_timeout(45*1000);
			
});

