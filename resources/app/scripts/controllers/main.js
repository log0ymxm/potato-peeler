'use strict';

angular.module('peelerFrontendApp').factory('apiUrl', function() {
    // TODO will be different for production
    return 'http://' + window.location.hostname + ':8030/api/';
});

angular.module('peelerFrontendApp').directive('watch', function() {
    var directive = {
	restrict: "A",
	attrs: {
	    ngModel: "=",
	    ngWatch: "&"
	},
	link: function(scope, iElement, iAttrs) {
	    console.log('dir', scope, iAttrs);
	    iAttrs.$observe('ngModel', function() {
		console.log('observer', scope, iAttrs);
		if (scope.ngWatch) scope.ngWatch();
	    });
	}
    };
    return directive;
});

angular.module('peelerFrontendApp')
    .controller('MainCtrl', ['$scope', '$http', 'apiUrl', function ($scope, $http, apiUrl) {

	// initial state interaction
	$http.get(apiUrl + 'states').then(function(response) {
	    $scope.states = response.data;
	});
	$scope.unselectState = function(state) { $scope.selectedState = null; };

	// find all schools when a state is selected
	$scope.$watch('selectedState', function() {
	    if ($scope.selectedState) {
		var state_id = $scope.states.filter(function(state) {return (state.name === $scope.selectedState);})[0].id;
                console.log('selected_state', state_id);
		$http.get(apiUrl + 'states/' + state_id + '/schools').then(function(response) {
		    console.log('schools', response.data);
		    $scope.schools = response.data;
		});
	    } else {
		$scope.unselectSchool();
	    }
	});

	// school interaction
	$scope.unselectSchool = function(school) {
	    $scope.selectedSchool = null;
	};

	// find all teachers once we have a school
	$scope.$watch('selectedSchool', function() {
	    if ($scope.selectedSchool) {
		var school_id = $scope.schools.filter(function(school) {return (school.name === $scope.selectedSchool);})[0].id;
                console.log('selected school', school_id);
		$http.get(apiUrl + 'schools/' + school_id + '/teachers').then(function(response) {
		    // combine first & last name for display
		    $scope.teachers = response.data.map(function(teacher) {
			teacher.name = teacher.first_name + ' ' + teacher.last_name;
			return teacher;
		    });
		    $scope.newRecord = true;
		    console.log('teachers', response.data);
		});
	    }
	});

	// teacher interaction
	$scope.$watch('newTeacher', function() {
	    if ($scope.newTeacher) {
		var teacher = $scope.teachers.filter(function(teacher) { return (teacher.name === $scope.newTeacher); })[0];
                console.log('selected teacher', teacher);
		$http.get(apiUrl + 'teachers/' + teacher.id + '/classes').then(function(response) {
		    console.log('classes', response.data.length, response.data);
		    $scope.classes = response.data;
		});
	    }
	});

        $scope.grades = ["A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"];

	$scope.transcript = [];
	$scope.addNewRecord = function() {
            var record = {
                teacher: $scope.newTeacher,
                class: $scope.newClass,
                grade: $scope.newGrade,
                comment: $scope.newComments,
                date: $scope.newDate
            };

            $scope.newTeacher = null;
            $scope.newClass = null;
            $scope.newGrade = null;
            $scope.newComments = null;
            $scope.newDate = null;

	    $scope.transcript.push(record);
	};

        $scope.editRecord = function(record) {
            // TODO
        };

        $scope.deleteRecord = function(record) {
            // TODO
        };

    }]);
