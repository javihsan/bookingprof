app.controller("MenuBehaviour", [
  		"$scope", "$location", "$rootScope", 
		function($scope, $location, $rootScope) {
  			
  			$scope.previusPath = "/booking";
  			
  			$scope.isActive = function(viewLocation) {
				var pattern = '/' + viewLocation, re = new RegExp(pattern);
				var path = $location.path();
  				return re.test(path);
			};
			

			// Obtenemos el previusPath cuando se ha pulsado sobre el menu
		    $scope.$on('$locationChangeStart', function(evt, absNewUrl, absOldUrl) {
		    	//console.log('absNewUrl absOldUrl: ', absNewUrl, absOldUrl);
		    	var hashIndex = absNewUrl.indexOf('#');
		    	var newRoute = absNewUrl.substr(hashIndex + 1);
		    	hashIndex = absOldUrl.indexOf('#');
		    	var oldRoute = absOldUrl.substr(hashIndex + 1);
		    	if (newRoute!=oldRoute){
		    		if (oldRoute!=$scope.previusPath){
		    			$scope.previusPath = oldRoute;
		    			//console.log('previusPath: ', $scope.previusPath);
		    		}
		    	}
		    });
		   			
	} ]);