
  var FacadeCore = function() {
    
	var cacheService = undefined;
	  
	function FacadeCore(cacheS) {
		cacheService = cacheS;
	}
	
	FacadeCore.prototype.Storage_set = function(name,value) {
		try{
			return window.localStorage.setItem(name,JSON.stringify(value));
		}catch(err)
		  {
			return this.Cache_set(name,value);
		  }
	};
	
	FacadeCore.prototype.Storage_get = function(name) {
		try{
			return JSON.parse(window.localStorage.getItem(name));
		} catch(err)
		  {
			return this.Cache_get(name);
		  }
	};
	
	FacadeCore.prototype.Storage_remove = function(name) {
		try{
			return window.localStorage.removeItem(name);
		} catch(err)
		  {
			return this.Cache_remove(name);
		  }
	};
	
	FacadeCore.prototype.Cache_set = function(name,value) {
		return cacheService.put(name,value);
	};
	
	FacadeCore.prototype.Cache_get = function(name) {
		return cacheService.get(name);
	};
	
	FacadeCore.prototype.Cache_remove = function(name) {
		return cacheService.remove(name);
	};
	
	FacadeCore.prototype.Router_section = function(section) {
      return Lungo.Router.section(section);
    };
    
	FacadeCore.prototype.Router_article = function(section,article) {
	   return Lungo.Router.article(section,article);
	};
	
	FacadeCore.prototype.Router_back = function() {
	   return Lungo.Router.back();
	};
    
	FacadeCore.prototype.Service_Settings_async = function(value) {
		return Lungo.Service.Settings.async = value;
	};
	
	FacadeCore.prototype.Service_Settings_asyncFalse = function() {
		asyn = Lungo.Service.Settings.async;
		this.Service_Settings_async(false);
		return asyn; 
	};
	
	FacadeCore.prototype.Service_Settings_timeout = function(value) {
		return Lungo.Service.Settings.timeout = value;
	};
	
	FacadeCore.prototype.isSwipeLeft = function(event, ignoreRange) {
		if (ignoreRange){
			return event.iniTouch && event.currentTouch && event.iniTouch.x > event.currentTouch.x;
		} else{
			return Math.abs(event.iniTouch.x - event.currentTouch.x)>30 && event.iniTouch.x > event.currentTouch.x;
		}	
	};
	
	FacadeCore.prototype.isSwipeRight = function(event, ignoreRange) {
		if (ignoreRange){
			return event.iniTouch && event.currentTouch && event.iniTouch.x < event.currentTouch.x;
		} else{
			return Math.abs(event.iniTouch.x - event.currentTouch.x)>30 && event.iniTouch.x < event.currentTouch.x;
		}
	};
	
	FacadeCore.prototype.isDoubleTap = function(event) {
		return !event.iniTouch || Math.abs(event.iniTouch.x - event.currentTouch.x)<30
	};
	
	FacadeCore.prototype.count = function(selector,count) {
		var element = Lungo.dom(selector);
		if (element){
			element.children(".tag.count").remove();
			if (count){
				var binding = Lungo.Constants.BINDING.SELECTOR;
				var html = Lungo.Attributes.count.html.replace(binding, count);
				element.append (html);
			}
		}	
	};
	
	
	return FacadeCore;

}();

__FacadeCore = undefined;

