var FacadeWeb = function() {
    
	function FacadeWeb() {
	}
	
	FacadeWeb.prototype.RouterMenu = function(obj) { 
		
		targetLink = $(obj).attr("href");
		targetLinkObj = $(targetLink);
		//console.log ("targetLinkObj.offset()",targetLinkObj.offset().top, targetLinkObj.height(), targetLinkObj.offset().top-targetLinkObj.height()+225);	
		//$('html,body').animate({scrollTop: targetLinkObj.offset().top-targetLinkObj.height()+225}, 100);
		$('html,body').animate({scrollTop: targetLinkObj.offset().top-175}, 100);
		
		activeLink = $("a[data-router].active");
		activeLink.removeClass("active");
		if ($(obj).parent()[0].tagName.indexOf("NAV")==-1){
			obj = $("nav a[href="+targetLink+"]");
			return $(obj).addClass("active");
		} else {
			return $(obj).addClass("active");
		}
		
	};
		
	return FacadeWeb;
	
}();

__FacadeWeb = new FacadeWeb();


