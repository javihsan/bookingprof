class __View.ClientSearchListView extends Monocle.View
    
	container: "section#searchClient article#search-clients ul"
	
	template_url: "/app/templates/clientList.mustache"
	
	events:
        "singleTap li": "selectClient"
         
   	selectClient: (event) ->
    	#console.log "Search.selectClient", @model
    	
    	router = __FacadeCore.Cache_get appName + "routerSearchClient"
    	__FacadeCore.Cache_remove appName + "routerSearchClient"
    	__FacadeCore.Cache_remove appName + "selectClient"
    	__FacadeCore.Cache_set appName + "selectClient", @model

    	__FacadeCore.Router_section router	
 	    		
    
   
	
  