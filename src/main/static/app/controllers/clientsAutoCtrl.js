app
		.controller(
				"ClientsAutoController",
				[
						"$scope",
						"$state",
						"httpService",
						"$rootScope",
						"$timeout",
						"$q",
						function($scope, $state, httpService, $rootScope, $timeout, $q) {
													
						    var self = this;

						    // list of `state` value/display objects
						    self.querySearch   = querySearch;
				
						    function newClient(state) {
						      alert("Sorry! You'll need to create a Constitution for " + state + " first!");
						    }

						    // ******************************
						    // Internal methods
						    // ******************************

						    /**
						     * Search for states... use $timeout to simulate
						     * remote dataservice call.
						     */
						    function querySearch (query) {
						    	//var results = $rootScope.clients;
							    var results = query ? $rootScope.clients.filter(createFilterFor(query)) : $rootScope.clients,
						          deferred;
				       			return results;
							    
//						    	var urlClients = protocol_url + appHost + "/client/operator/list"
//							    var data = {domain:appFirmDomain}
//						        return httpService.GET(urlClients,data).then(
//						       		function(response) {
//						       			var results = response.data;
//						       			return results;
//									}
//								)
						    }

						    /**
						     * Create filter function for a query string
						     */
						    function createFilterFor(query) {
						      var lowercaseQuery = query.toLowerCase();

						      return function filterFn(client) {
						    	 var lowercaseClient = client.whoName.toLowerCase();
						         return (lowercaseClient.indexOf(lowercaseQuery) !== -1);
						      };

						    }
						    
						    function selectedItemChange(item) {
						    	if (item){
						    		console.log('Item changed to ' + JSON.stringify(item));
						    		$scope.client = {};
						    		$scope.client.name = item.whoName;
						    		$scope.client.email = item.whoEmail;
						    		$scope.client.telf = item.whoTelf1;
						    	}
						    }
							
						} ]);

