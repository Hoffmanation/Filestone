var loginApp = angular.module("loginApp", [ 'ui.bootstrap' ,'ngCookies' ])
loginApp.controller("loginAppController",function($scope, $http, $rootScope, $window,$location,$cookies) {

		// Var's
		$scope.restUrl = $location.protocol() + '://'+ $location.host() +':'+  $location.port()+"/filestone" ;
		var token = $cookies['XSRF-TOKEN'];
		$scope.dupPass="";
		$scope.emptyField="";
		$scope.nameNotEnChars="";
		$scope.passDontMatch="";
		$scope.passNotEnChars="";
		$scope.loader = true ;
					
					
		//Functions		
	    $scope.cleanErrors = function() {
	    	$scope.email = "" ;
	    	$scope.forgotMyPasswordError="";
		}
				    
		$scope.login = function() {	
			$scope.loader = false ;
			$scope.userDetails = {
					username : $scope.username,
					password : $scope.password,
					passwordConfirm : $scope.passwordConfirm,
				}
			
			$http({
				method : 'POST',
				data: $scope.userDetails,
				url : $scope.restUrl + "/login",
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(response, data, status, headers,config) {
						if (response.status ==200) {
							$window.location.href = '/index.html';
						}
						else if (response.status ==401){
							$scope.loginMessage = response.entity ;							
						}
						$scope.loader = true ;
			}).error(function(response, data, status, headers,config) {
						if(response.status == 403){
							$window.location.href = '/login.html';
						}
						$scope.errorMessage = response.entity ;
						$scope.loader = true ;
					});
		 };
									

		$scope.register = function() {
			$scope.loader = false ;
			$scope.userDetails = {
				username : $scope.username,
				password : $scope.password,
				passwordConfirm : $scope.passwordConfirm,
			}
			
			$http({
				method : 'POST',
				data : $scope.userDetails,
				url : $scope.restUrl + "/registration",
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(response, data, status, headers,config) {
						if (response.status ==200) {
							$window.location.href = '/index.html';
						}
						else if (response.status ==401){
							$scope.notEmpty	= response.entity.notEmpty ;
							$scope.sizeUsername	= response.entity.sizeUsername;
							$scope.duplicateUsername	= response.entity.duplicateUsername;
							$scope.sizePassword	= response.entity.sizePassword;
							$scope.dontMatchPasswordConfirm	= response.entity.dontMatchPasswordConfirm;
							$scope.emailNotValid = response.entity.emailNotValid ;
						}
						$scope.loader = true ;
		   }).error(function(response, data, status, headers,config) {
						if(response.status == 403){
							$window.location.href = '/registration.html';
						}
						$scope.errorMessage = response.entity ;
						$scope.loader = true ;
					});
		};

		
		 $scope.forgotMyPassword = function(email) {
			$http({
			  method : "post",
			  url : $scope.restUrl +"/forgotMyPassword",
			  data: email,
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).then(function mySuccess(response) {
			  $scope.forgotMyPasswordError = response.data.entity.message
			}, function myError(response) {
			  $scope.myWelcome = response.statusText;
			});
		}
				 
		 $scope.updateMyPassword = function(newPassword) {
			 var passToken = window.location.search;
			 passToken = passToken.replace("?", '').split("?"); 
			$http({
			  method : "post",
			  url : $scope.restUrl +"/updatePassword/"+ passToken[0]+"/"+passToken[1]+"/",
			  data: newPassword,
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).then(function mySuccess(response) {
			  $scope.updatedPassword = response.data.entity.message
			}, function myError(response) {
			  $scope.updatedPassword = response.data.entity.message
			});
		}


});


