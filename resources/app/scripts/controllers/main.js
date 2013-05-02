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
            console.log('states', response.data);
	    $scope.states = response.data;
	});
	$scope.unselectState = function(state) { $scope.selectedState = null; };

	// find all schools when a state is selected
	$scope.$watch('selectedState', function() {
	    if ($scope.selectedState) {
		var state = $scope.states.filter(function(state) {return (state.name === $scope.selectedState);})[0];
                console.log('selected_state', state);
		$http.get(apiUrl + 'states/' + state.id + '/schools').then(function(response) {
		    console.log('schools', response.data);
		    $scope.schools = response.data;
		});
	    } else {
		$scope.unselectSchool();
	    }
	});
	$scope.$watch('transcriptState', function() {
	    if ($scope.transcriptState) {
		var state = $scope.states.filter(function(state) {return (state.name === $scope.transcriptState);})[0];
                console.log('selected_state', state);
		$http.get(apiUrl + 'states/' + state.id + '/schools').then(function(response) {
		    console.log('schools', response.data);
		    $scope.schools = response.data;
		});
	    } else {
		$scope.unselectTranscriptState();
	    }
	});

	// school interaction
	$scope.unselectSchool = function(school) {
	    $scope.selectedSchool = null;
	};

	// find all classes once we have a school
	$scope.$watch('selectedSchool', function() {
	    if ($scope.selectedSchool) {
		var school = $scope.schools.filter(function(school) {return (school.name === $scope.selectedSchool);})[0];
                console.log('selected school', school);
		$http.get(apiUrl + 'schools/' + school.id + '/classes').then(function(response) {
		    $scope.classes = response.data;
		    $scope.newRecord = true;
		    console.log('classes', response.data);
		});
	    }
	});

	$scope.$watch('transcriptSchool', function() {
	    if ($scope.transcriptSchool) {
		var school = $scope.schools.filter(function(school) {return (school.name === $scope.transcriptSchool);})[0];
                console.log('transcript school', school);
		$http.get(apiUrl + 'schools/' + school.id + '/classes').then(function(response) {
		    $scope.classes = response.data;
		    $scope.newRecord = true;
		    console.log('classes', response.data);
		});
	    }
	});

        $scope.unselectClass = function() {
            $scope.selectedClass = null;
        }

        $scope.unselectTerm = function() {
            $scope.selectedTerm = null;
        }

        $scope.unselectYear = function() {
            $scope.selectedYear = null;
        }

        $scope.unselectTranscriptState = function() {$scope.transcriptState = null;}
        $scope.unselectTranscriptSchool = function() {$scope.transcriptSchool = null;}
        $scope.unselectTranscriptClass = function() {$scope.transcriptClass = null;}
        $scope.unselectTranscriptTerm = function() {$scope.transcriptTerm = null;}
        $scope.unselectTranscriptYear = function() {$scope.transcriptYear = null;}

        $scope.grades = ["A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"];
        $scope.terms = ["Spring", "Summer", "Fall", "Winter"];
        $scope.years = [];
        for (var i = 1990; i < 2014; i++) {
            $scope.years.push(i+"");
        }

	$scope.transcript = [];
	$scope.addNewRecord = function() {
            var record = {
                state: $scope.selectedState,
                school: $scope.selectedSchool,
                class: $scope.selectedClass,
                term: $scope.selectedTerm,
                year: $scope.selectedYear
            };

            $scope.selectedState = null;
            $scope.selectedSchool = null;
            $scope.selectedClass = null;
            $scope.selectedTerm = null;
            $scope.selectedYear = null;

	    $scope.transcript.push(record);
	};

        $scope.schedule = [];
        $scope.addNewTranscriptRecord = function() {
            var transcriptRecord = {
                state: $scope.transcriptState,
                school: $scope.transcriptSchool,
                class: $scope.transcriptClass,
                term: $scope.transcriptTerm,
                year: $scope.transcriptYear
            };
            $scope.transcriptState = null;
            $scope.transcriptSchool = null;
            $scope.transcriptClass = null;
            $scope.transcriptTerm = null;
            $scope.transcriptYear = null;

            $scope.schedule.push(transcriptRecord);
            $scope.checkEasiness();
        }

        $scope.checkEasiness = function() {
            var transcript = $scope.transcript.map(function(record) {
                var transcriptRecord = {};
		transcriptRecord.state = $scope.states.filter(function(state) {return (state.name === record.state);})[0].id;
		transcriptRecord.school = $scope.schools.filter(function(school) {return (school.name === record.school);})[0].id;
		transcriptRecord.class = $scope.classes.filter(function(schoolClass) {return (schoolClass.level === record.class);})[0].id;
                transcriptRecord.term = record.term;
                transcriptRecord.year = record.year;
                return transcriptRecord;
            });
            var schedule = $scope.schedule.map(function(record) {
                var scheduleRecord = {};
		scheduleRecord.state = $scope.states.filter(function(state) {return (state.name === record.state);})[0].id;
		scheduleRecord.school = $scope.schools.filter(function(school) {return (school.name === record.school);})[0].id;
		scheduleRecord.class = $scope.classes.filter(function(schoolClass) {return (schoolClass.level === record.class);})[0].id;
                scheduleRecord.term = record.term;
                scheduleRecord.year = record.year;
                return scheduleRecord;
            });
            var data = {
                transcript: transcript,
                schedule: schedule
            }
            $http.post(apiUrl + 'transcripts', data).then(function(response) {
                console.log("prediction response", response.data);
            });
        };

    }]);
