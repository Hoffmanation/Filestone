//Main login angularJs module
var loginApp = angular.module("loginApp", [ 'ui.bootstrap' ,'ngCookies' ])

//$location configuration to work in html5-Mode
loginApp.config(function($locationProvider) {
	  $locationProvider.html5Mode(true);
	});

//Main login angularJs module-controller
loginApp.controller("loginAppController",function($scope, $http, $rootScope, $window,$location,$cookies) {

		/**
		 * Global variables
		 */
		$scope.restUrl = $location.protocol() + '://'+ $location.host() +':'+  $location.port()+"/filestone" ;
		var token = $cookies['XSRF-TOKEN'];
		$scope.dupPass="";
		$scope.emptyField="";
		$scope.nameNotEnChars="";
		$scope.passDontMatch="";
		$scope.passNotEnChars="";
		$scope.resetPasswrodError="";
		$scope.resetPasswrodResponse="";
		$scope.loader = true ;
					
					
		/**
		 * Functions 
		 */	
	    $scope.cleanErrors = function() {
	    	$scope.email = "" ;
	    	$scope.forgotMyPasswordError="";
			$scope.resetPasswrodError="";
			$scope.resetPasswrodResponse="";
			
			$scope.dupPass="";
			$scope.emptyField="";
			$scope.nameNotEnChars="";
			$scope.passDontMatch="";
			$scope.passNotEnChars="";
			$scope.resetPasswrodError="";
			$scope.resetPasswrodResponse="";
		}
	    
	    $scope.goTo = function(page) {
	    	$window.location.href = '/' + page+'.html';
	    }
		
	    //Login 
		$scope.login = function() {
			 $scope.cleanErrors();
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
									

		 //Registration
		$scope.register = function() {
			$scope.cleanErrors();
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

		
		//Forgot My Password - will send user 'Reset password' email 
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
		
		 
		 //Update New password from 'Reset.html' page
		 $scope.updateMyPassword = function(passwordToUpdate) {
			 $scope.cleanErrors()
			 $scope.loader = false ;
			 $scope.resetPasswordRequest = {
						newPassword: passwordToUpdate,	 
						token: $location.search().token,
						email: $location.search().email
					 }
			$http({
			  method : "POST",
			  url : $scope.restUrl +"/updatePassword",
			  data: $scope.resetPasswordRequest,
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).then(function mySuccess(response) {
				$scope.loader = true ;
				if (response.data.status== 200) {
					$scope.resetPasswrodResponse = response.data.entity.message
				}
				else if(response.data.status == 401){
					$scope.resetPasswrodError = response.data.entity.message
				}
			}, function myError(response) {
				$scope.loader = true ;
				if (response.data.status == 200) {
					$scope.resetPasswrodResponse = response.data.entity.message
				}
				else if(response.data.status == 401){
					$scope.resetPasswrodError = response.data.entity.message
				}
			});
		}
});


