$( document ).ready(function() {
	
	//console.log ("readyWeb");
	
    $("*[data-router]").click( 
		function() { 
			__FacadeWeb.RouterMenu(this);
		}
    );
});
 