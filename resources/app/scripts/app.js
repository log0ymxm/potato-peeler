'use strict';

angular.module('peelerFrontendApp', ['ui.bootstrap'])
    .config(['$routeProvider', function ($routeProvider) {
	$routeProvider
	    .when('/', {
		templateUrl: 'views/main.html',
		controller: 'MainCtrl'
	    })
	    .otherwise({
		redirectTo: '/'
	    });
    }]);
