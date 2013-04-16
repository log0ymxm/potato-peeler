'use strict';

angular.module('peelerFrontendApp', ['ui.bootstrap'])
    .config(['$routeProvider', function ($routeProvider) {
	$routeProvider
	    .when('/', {
		templateUrl: 'views/main.html',
		controller: 'MainCtrl'
	    })
	    .when('/schedule', {
		templateUrl: 'views/schedule.html',
		controller: 'ScheduleCtrl'
	    })
	    .when('/states', {
		templateUrl: 'views/states.html',
		controller: 'StatesCtrl'
	    })
	    .when('/schools', {
		templateUrl: 'views/schools.html',
		controller: 'SchoolsCtrl'
	    })
	    .when('/teachers', {
		templateUrl: 'views/teachers.html',
		controller: 'TeachersCtrl'
	    })
	    .otherwise({
		redirectTo: '/'
	    });
    }]);
