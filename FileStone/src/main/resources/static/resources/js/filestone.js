	var fileStone = angular.module('fileStone', ['ngCookies']);
	fileStone.controller("fileStoneController", function($compile,$scope, $http,$rootScope, $window, fileReader ,$location,$timeout,$cookies) {

		//Initialize variables
		$scope.restUrl =$location.protocol() + '://'+ $location.host() +':'+  $location.port()+"/filestone/";
		var token = $cookies['XSRF-TOKEN'];
		$scope.printAllFiles = [];
		$scope.errorMessage ;
		$scope.infoMessage  ;
		jQuery('#progressBar').hide(); 
		jQuery('.lds-roller').hide();
		jQuery('#error').hide();
		$scope.tag="";
		$scope.sortField = 'fileName' ;
		$scope.space = ' ' ;
		$scope.reverse =true ; 
		$scope.progress = 0;
		$scope.invoked = false;
		$scope.delay = ms => new Promise(res => setTimeout(res, ms));
		$scope.operations = {delete: 'Deleting' ,upload: 'Uploading'} ;
		$scope.fileContentElement = angular.element(document.getElementById("content"));
		$scope.dot = "." ;
		$scope.messages = {
			fileToBig: 	"*This file is too big for us, Please upload A smaller file that A 400MB",
			glogalError: "*Something went wrong" ,
			deleteError : "*Could not delete file",
			missingDescription : "*Plaese write down a description for the file.",
			missingFile : "*Plaese Choose a file to upload.",
			inProgress : "File please wait",
			totalRepoSize : "Total size of your repository: ",
			totalRepoFiles : "Total files in your repository: ",
            lastLogins:  "Last login: "
		};
		
		
		/**
		 * HELPER METHODS AND CSS MANIPULATIONS
		 */
		
		//Register "close modal" function
        var modal = document.getElementById('myModal');
        var span = document.getElementsByClassName("close")[0];
        span.onclick = function() { 
            modal.style.display = "none";
            jQuery('.modal-backdrop').css("position", "inherit");
    		var contentToRemove = angular.element(document.querySelector("#content").firstChild).remove();
        }
        
        //Clear user's error message content
        $scope.clearErrorMessage= function(){
        	$scope.errorMessage = "" ;
        }
        
        //Clear user's info message content
        $scope.clearInfoMessage= function(){
        	$scope.infoMessage = "" ;
        }
        
        //Hide user error message
        $scope.hideErrorMessage = function(){
        	jQuery('#error').hide();
        }
        
        //Show user error message
        $scope.showErrorMessage = function(){
        	jQuery('#error').show();
        }
        
        //Hide CSS loader
        $scope.hideLoader = function(){
        	jQuery('.lds-roller').hide();
        }
        
        //Show CSS loader
        $scope.showLoader = function(){
        	jQuery('.lds-roller').show();
        }
        
        //Hide Progress Bar
        $scope.hideProgressBar = function(){
        	jQuery('#progressBar').hide();
        }
        
        //Show Progress Bar
        $scope.showProgressBar = function(){
        	jQuery('#progressBar').show();
        }
        
        
        //Show the 'content' bootsrap Modal
        $scope.showFilePreview = function(){
        	jQuery('#myModal').modal('show');
        }
        
      //Append 'Uploading/Deleting file, Please wait' and add a dot every 500 ml when doing a CRUD Operations
        $scope.appendInProgressLine = async (operation) => {
			while($scope.invoked) {
				var baseLine =  $scope.operations[operation] + $scope.space +  $scope.messages['inProgress'] ;
				var infoElm = jQuery('#info') ; 
				var lineToAppend = baseLine ;
				infoElm.text(baseLine);
			for (var i = 0; i <=6 ; i++) {
					await $scope.delay(500);
					lineToAppend = lineToAppend + $scope.dot ;
					infoElm.text(lineToAppend);
				}	
        	};
        	infoElm.text($scope.space);
        }
        
        
        // Clean up file input,progress lines and user Messages
		$scope.clearUploadFileArea = function(){
			jQuery('input[type="file"]').val("");
			jQuery('#chooseAfile').text("Choose a file…");
			jQuery('#progress').css("width", '0%');
			jQuery('#progress').text('0% uploaded') ;
			$scope.hideProgressBar(); 
			$scope.description = "" ;
			$scope.progress = 0 ;
			$scope.file = null ;
		} ;
		
		
		//Compile content tag into an HTML element and inject it to the 'content' <div>
		$scope.injectFileContant = function(displayContents){
	      	$scope.compiledElement = $compile(displayContents)($scope);
	      	$scope.fileContentElement.empty()
	        $scope.fileContentElement.append($scope.compiledElement);
		}
		
		
		//Register A file Progress Listener to Append file upload progress value
		$scope.$on("fileProgress", function(e, progress) {
				var progAsPercentage = progress.loaded / progress.total * 100 ;
				$scope.progress = parseInt(progAsPercentage, 10)
				jQuery('#progress').css("width", $scope.progress+'%');
				jQuery('#progress').text($scope.progress+'% uploaded') ;
				//Console log the upload progress
				console.log("File upload progress ---> Total file: "  + progress.total + "(bytes)| Already uploaded: " +progress.loaded+"(bytes)") ;
		});
		
		
		//Validate user inputs suck as file zise and description
		$scope.validateInputs = function(description){
			if(!description){
				$scope.errorMessage = $scope.messages['missingDescription'];
				$scope.showErrorMessage();
				return false;
			}
			
			if($scope.file == undefined){
				$scope.errorMessage = $scope.messages['missingFile'] ;
				$scope.showErrorMessage();
				return false;
			}
			
			if ($scope.file.size > 4e+8) {
				$scope.file = null ; 
				$scope.errorMessage = $scope.messages['fileToBig'];
				$scope.showErrorMessage();
				return false;
			}
			
			return true;
		}
        	
        
		/**
		 * AJAX CALLS FROM HERE
		 */
		
		//Get requested html tag (already filled with content) from server.
		$scope.getPreviewTag = function(id){
			$scope.hideLoader();
			$scope.showFilePreview ();
		    
			$http({
				method : 'GET',
				url : $scope.restUrl + "getPreviewTag/"+id ,
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).then(function(response) {
					$scope.displayContents = response.data.entity;
					$scope.injectFileContant($scope.displayContents);
			        $scope.hideLoader(); 
			});	
		}
		
		
		//Delete File 
		$scope.deleteFile = function(fileId) {
			$http({
				method : 'DELETE',
				url : $scope.restUrl + "deleteFile/"+fileId,
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(
					function(response, data, status, headers,config) {
							$scope.getAllFiles();
							$scope.getRepositoryInfo();
					}).error(
					function(response, data, status, headers,config) {
						  $scope.errormessage = response.entity ;
					});
		};

		
		//Get All Files
		$scope.getAllFiles = function() {
			url = $scope.restUrl + "getAllFiles";
			$http.get(url).then(function(response) {
				$scope.printAllFiles = response.data;
			});
		};
		
		
		//Get Info About the user's repository (Such as file's amount and size)
		$scope.getRepositoryInfo = function() {
			url = $scope.restUrl + "getRepositoryInfo";
			$http.get(url).then(function(response) {
				    $scope.totalSize  = $scope.messages['totalRepoSize'] + response.data.entity.totalSize ;
			        $scope.fileQuantity = $scope.messages['totalRepoFiles'] + "Total files in your repository: " + response.data.entity.fileQuantity ;
			        $scope.lastLogin= $scope.messages['lastLogins'] + "Last login: " + response.data.entity.lastLogin ;
			        $scope.username = response.data.entity.username ;
			});
		}; 

		
		
		
		$scope.uploadFile = function(description) {
			if(!$scope.validateInputs(description)){
				return ;
			}
			$scope.showProgressBar();
			$scope.invoked = true;
			$scope.appendInProgressLine('upload') ;
			
			//Register A file Reader Listener in case we want to manipulate file's data before uploading
			fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
				//Uncomment the blow to retrieve the current uploaded file 'Data URL' as a  Base64-encoded @String
				/*console.log("Chosen file as Base64-encoded data: " +result)*/
			});
			
	
			$scope.clearErrorMessage();
			var fd = new FormData();
			fd.append('file', $scope.file);
			url = $scope.restUrl + "uploadFile";
			
			$http.post(url, fd, {
				transformRequest : angular.identity,
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : undefined
				}
			})
			.success(function(response) {
				$scope.invoked = false;
				$scope.createFileInfo(description,$scope.file);
				$scope.clearUploadFileArea() ;
			})
			.error(function(response) {
				$scope.invoked = false;
				console.log("error accured at uploadFile(): " +response)
			});
		}
		

		
		
		//On Every file that was uploaded create new 'FileInfo' entry in the DB
		$scope.createFileInfo = function(description,file) {
			$scope.fileArray = file.name.split('.') ;
			$scope.type = file.name.split('.')[$scope.fileArray.length -1];
			$scope.fileInfo = {
					fileName: file.name,
			        type: $scope.type ,
			        data: new Date(),
			        description: description,
					size: file.size 
				};
			
			$http({
				method : 'POST',
				data : $scope.fileInfo,
				url : $scope.restUrl + "createFileInfo",
				headers : {
					'X-CSRF-TOKEN' : token,
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(response, data, status, headers,config) {
						$scope.getAllFiles();
						$scope.getRepositoryInfo();
			}).error(function(response, data, status, headers,config) {
						console.log("error accured at createFileInfo(): " +response)
						$scope.getAllFiles();
					});
		};
		
		
		//Logout user
		$scope.logout = function(fileInfo) {
			url = $scope.restUrl + "logout";
			$http.get(url, {
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			})
			.success(function(response) {
				$window.location.href = '/login.html';
			})
			.error(function() {
				$window.location.href = '/login.html';
			});
		};
		
	});



	//File Upload Directive Service.
	fileStone.directive("ngFileSelect", function() {
		return {
			link : function($scope, el) {
				el.bind("change", function(e) {
					$scope.file = (e.srcElement || e.target).files[0];
				})
			}
		}
	})