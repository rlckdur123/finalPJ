/*
//전역 변수로 담는 배열들을 미리 선언해두고, 계속 사용할 것임.
//추가된 파일들을 담아줄 배열. File객체로 하나씩 담음
let uploadFiles = [];

//기존 첨부파일 배열 - 새 게시글에서 사용하는 첨부파일 배열.
let originFiles = [];
//각 게시글들의 첨부파일들을 모은 것을 따로 담을 것임..
let originFileList = [];
//변경된 첨부파일 배열
let changedFiles = [];
*/
//스크립트에서 설정한 파일은 스크립트에서 다시 사용하는 것이 불가능하며, 따로 만들어서 초기화 해줘야함.
	$(function() {
		let formData;
		//let flagList = false;
		let insertFlag = false;
		//파일 추가 입력단 생성.
		let fileFlagList = false;
		
		//$("#imagePreview").hide();
		//$("#btnAttForm").hide();
		//파일 리스트 변경 버튼 이벤트 대신 처리.
		//버튼을 누르면 파일을 누르는 버튼의 이벤트를 발생시킴.
		$.newPostbutton = function() {
			$("#postFileUpdate").click(function(e) {
				e.preventDefault();
				//$.btnAtt();
				insertFlag = !insertFlag;
				$("#btnAtt").click();
			});
		}

		//게시글 수정에서 파일을 수정하는 버튼의 파일관리 이벤트 처리 함수.
		$(".updateBtnAtt").on("change", function(e) {
			//input type=file에 추가된 파일들을 변수로 받아옴
			const files = e.target.files;
			//변수로 받아온 파일들을 배열 형태로 변환
			const fileArr = Array.prototype.slice.call(files);
			
			const postId = $(e.target).attr("data-post-id");
			
			//배열에 있는 파일들을 하나씩 꺼내서 처리
			for(f of fileArr) {
				postImageLoader(f, postId);
			}

		});
		
		$.updateBtnAtt = function(targetPostId) {
			$("#updateBtnAtt" + targetPostId).on("change", function(e) {
				console.log("새로운 파일 등록 감지");
				//input type=file에 추가된 파일들을 변수로 받아옴
				const files = e.target.files;
				//변수로 받아온 파일들을 배열 형태로 변환
				const fileArr = Array.prototype.slice.call(files);
				
				const postId = $(e.target).attr("data-post-id");
				
				//배열에 있는 파일들을 하나씩 꺼내서 처리
				for(f of fileArr) {
					postImageLoader(f, postId);
				}
			});
		}

		
		$.btnAtt = function() {
			$("#btnAtt").on("change", function(e) {
				//input type=file에 추가된 파일들을 변수로 받아옴
				const files = e.currentTarget.files;
				//변수로 받아온 파일들을 배열 형태로 변환
				const fileArr = Array.prototype.slice.call(files);

				//배열에 있는 파일들을 하나씩 꺼내서 처리
				for(f of fileArr) {
					console.log(f);
					imageLoader(f);
				}
				$("#attZone").css("display", "inline-block");
			});
		}

		$.changeFilesBtn = function(postFileId) {
			$("#changedFile" + postId);
			fnImgChange(postFileId)
		}
	});
	
	//파일 추가창을 활성화하는 이벤트 생성 함수
	function fileTag() {
		let text = "";
		text += `<div id="image_preview">
					<input type="file" id="btnAtt" name="uploadFiles" multiple="multiple">
					<div id="attZone" data-placeholder="파일을 첨부하려면 파일선택 버튼을 누르세요."></div>
				</div>`;
		return text;
	}
	
	//파일 리스트 배열을 갱신하는 함수.
	function refreshFileList (targetPostFileId) {

	}
	
	//게시글 작성 영역의 파일 이미지 미리보기 로드하는 함수.
	//미리보기 영역에 들어갈 img태그 생성 및 선택된 파일을 Base64 인코딩된 문자열 형태로 변환하여
	//미리보기가 가능하게 해줌
	function imageLoader(file) {
		console.log("file 미리 보기 로드 사용중.");
		inputUploadFiles.push(file);
		
		let reader = new FileReader();
		
		reader.onload = function(e) {
			//이미지를 표출해줄 img태그 선언
			let img = document.createElement("img");
			img.setAttribute("style", "width: 100%; height: 100%; z-index: none;");
			
			//이미지 파일인지 아닌지 체크
			if(file.name.toLowerCase().match(/(.*?)\.(jpg|jpeg|png|gif|svg|bmp)$/)) {
				//이미지 파일 미리보기 처리
				img.src = e.currentTarget.result;
			} else {
				//일반 파일 미리보기 처리
				img.src = "../img/defaultFileImg.png";
			}
			
			//미리보기 영역에 추가
			//미리보기 이미지 태그와 삭제 버튼 그리고 파일명을 표출하는 p태그를 묶어주는 div 만들어서
			//미리보기 영역에 추가
			//미리보기 영역을 다른거로 대체해야함.
			//$("#imagePrview").append(makeDiv(img,file));
			$("#attZone").append(makeDiv(img, file));
		};
		//파일을 Base64 인코딩된 문자열로 변경
		reader.readAsDataURL(file);
	}
	
	//게시글 작성이 아닌 이미 게시된 게시글의 파일 입력 미리보기 로드.
	//배열안에 배열을 생성.
	function postImageLoader(file, postId) {
		uploadFiles.push(file);
		//uploadFiles.push(file);
		
		let reader = new FileReader();
		
		reader.onload = function(e) {
			//이미지를 표출해줄 img태그 선언
			let img = document.createElement("img");
			img.setAttribute("style", "width: 100%; height: 100%; z-index: none;");
			
			//이미지 파일인지 아닌지 체크
			if(file.name.toLowerCase().match(/(.*?)\.(jpg|jpeg|png|gif|svg|bmp)$/)) {
				//이미지 파일 미리보기 처리
				img.src = e.target.result;
			} else {
				//일반 파일 미리보기 처리
				img.src = "../img/defaultFileImg.png";
			}
			console.log(postId + "번 게시글에 이미지 로드중......");
			//미리보기 영역에 추가
			//미리보기 이미지 태그와 삭제 버튼 그리고 파일명을 표출하는 p태그를 묶어주는 div 만들어서
			//미리보기 영역에 추가
			//미리보기 영역을 다른거로 대체해야함.
			$("#postAttZone" + postId).append(makePostDiv(img, file, postId));
			//로딩된 이미지의 이벤트를 추가함.
		};
		//파일을 Base64 인코딩된 문자열로 변경
		reader.readAsDataURL(file);
	}
	
	/*
	function fnGetChangedFileInfo(postFileId, e) {
		//변경된 파일 받아오기
		const files = e.target.files;
		//받아온 파일 배열 형태로 변경(싱글파일 업로드여서 파일배열 한개의 인자만 담김)
		const fileArr = Array.prototype.slice.call(files);

		//변경된 파일들은 변경된 파일 배열에 담아준다.
		changedFiles.push(fileArr[0]);

		//미리보기 화면에서 변경된 파일의 이미지 출력
		const reader = new FileReader();

		reader.onload = function (ee) {
			const img = document.getElementById("img" + postFileId);
			const p = document.getElementById("fileNm" + postFileId);

			p.textContent = fileArr[0].name;

			//이미지인지 체크
			if (fileArr[0].name.match(/(.*?)\.(jpg|jpeg|png|gif|bmp|svg)$/))
				img.src = ee.target.result;
			else
				img.src = "/assets/img/defaultFileImg.png";
		}

		reader.readAsDataURL(fileArr[0]);

		//기존 파일을 담고있는 배열에서 변경이 일어난 파일 수정
		console.log("게시글의 개수 : " + originFiles.length);
		for (let i = 0; i < originFiles.length; i++) {
			if (postFileId == originFiles[i].postFileId) {
				originFiles[i].postFileStatus = "U";
				originFiles[i].newFileNm = fileArr[0].name;
			}
		}

		console.log("변경된 파일 다시 정렬 중");
	}
	*/
	//게시글의 인덱스 번호를 가져온뒤 해당 이미지들의 이벤트를 작동시킬수 있게 해주는 함수를 jquery단계에서 이벤트를 설정해주는 함수다.
	
	//게시글의 인덱스 번호를 매개변수로 더 가져온 뒤 수정하려는 게시글만 따로 저장한다.
	//게시글의 아이디를 가지고 온다음, 그것을 저장한 postIdList에서 인덱스 값을 가지고 온다음, 임시로 저장한 인덱스 번호를 사용한다.
	function fnGetChangedFileInfo(postFileId, index, e) {
		console.log("바꾸는 파일의 게시글 자리 : " + index);
		console.log("바꾸는 파일 아이디 : " + postFileId);
		//변경된 파일 받아오기
		const files = e.target.files;
		//받아온 파일 배열 형태로 변경(싱글파일 업로드여서 파일배열 한개의 인자만 담김)
		const fileArr = Array.prototype.slice.call(files);
	
		//변경된 파일들은 변경된 파일 배열에 담아준다.
		changedFiles.push(fileArr[0]);
		console.log("바꾼 파일의 변경사항을 저장하였습니다. 몇번째 게시글의 : "  + index);
		console.log(changedFiles);
		//미리보기 화면에서 변경된 파일의 이미지 출력
		const reader = new FileReader();

		reader.onload = function (ee) {
			const img = document.getElementById("img" + postFileId);
			const p = document.getElementById("fileNm" + postFileId);

			p.textContent = fileArr[0].name;

			//이미지인지 체크
			if (fileArr[0].name.match(/(.*?)\.(jpg|jpeg|png|gif|bmp|svg)$/))
				img.src = ee.target.result;
			else
				img.src = "/assets/img/defaultFileImg.png";
		}

		reader.readAsDataURL(fileArr[0]);

		//기존 파일을 담고있는 배열에서 변경이 일어난 파일 수정
		
		for(let i = 0; i < originFiles.length; i++) {
			if(postFileId == originFiles[i].postFileId) {
				originFiles[i].postFileStatus = "U";
				originFiles[i].newFileNm = fileArr[0].name;
			}
		}

	}
	
	//게시된 파일들에 해당함.
	//x버튼 클릭시 동작하는 메소드
	function fnImgDel(e) {
		//클릭된 태그 가져오기
		let ele = e.currentTarget;
		//delFile속성 값 가져오기(boardFileNo)
		let delFile = ele.getAttribute("data-del-file");	
		console.log("게시글 삭제할 이미지 파일 번호 : " + delFile);
		
		for(let i = 0; i < inputOriginFiles.length; i++) {
			if(delFile == inputOriginFiles[i].postFileId) {
				inputOriginFiles[i].postFileStatus = "D";
				console.log("삭제할 이미지 파일 번호 : " + delFile);
				console.log(inputOriginFiles[i]);
			}
		}
		
		//부모 요소인 div 삭제
		let div = ele.parentNode;
		$(div).remove();
	}	
	
	//이미 게시된 포스트의 x버튼 클릭시 일어나는 이벤트
	function fnPostImgDel(e) {
		//클릭된 태그 가져오기
		let ele = e.currentTarget;
		//delFile속성 값 가져오기(boardFileNo)
		let delFile = ele.getAttribute("data-del-file");	
	
		console.log("이미지 파일 번호 : " + delFile);
		for(let i = 0; i < originFiles.length; i++) {
			if(delFile == originFiles[i].postFileId) {
				originFiles[i].postFileStatus = "D";
				console.log("삭제할 이미지 파일 번호 : " + delFile);
				console.log(originFiles[i]);
			}
		}
		
		//부모 요소인 div 삭제
		let div = ele.parentNode;
		$(div).remove();
	}
	
	function fnImgChange(postFileId) {
		console.log("변경하는 파일의 아이디 : " + postFileId)
		//기존 파일의 이미지를 클릭했을 때 같은 레벨의 input type="file"을 가져온다.
		let changedFile = document.getElementById("changedFile" + postFileId);
		//위에서 가져온 input 강제클릭 이벤트 발생시킴
		changedFile.click();
	}

	//이미 게시된 자신의 게시글을 수정하는 로직 함수.
	//함수에 있는 스크립트는 jQuery선택자가 아닌 배열 내의 데이터를 가지고다루면 됨.
	//한개의 게시글에 대한 수정작업
	function fnUpdatePost(postId, index) {   //파일 입출력이나 수정을 위한 ajax 데이터 묶음 처리
		dt = new DataTransfer();
		console.log(postId + "번의 업로드 되는 파일 목록 : ");
		console.log(uploadFiles);
		for (f in uploadFiles) {
		   let file = uploadFiles[f];
		   dt.items.add(file);
		}

		$("#updateBtnAtt" + postId)[0].files = dt.files;

		//dt.clearData();
		//clearData() 사용하면 배열의 모든 내용이 담기지 않고
		//파일 하나씩만 담기는 현상이 발생해서 dt를 두 개로 분리하여 사용
		dt2 = new DataTransfer();

		for (f in changedFiles) {
		   let file = changedFiles[f];
		   dt2.items.add(file);
		}

		$("#changedFiles" + postId)[0].files = dt2.files;
		console.log(postId + "번 게시글의 파일 수정을 진행하고 있습니다.");
		
		console.log("해당 포스트 아이디만 필터링 하기 전 originFiles 배열 : ");
		console.log(postId);
		console.log(typeof(postId));
		console.log(originFiles);
		//해당 postId에 대한 originsFiles만 남김
		//===은 값과 형태 둘다 같아야 해당 값을 저장함.
		//this.val로 변환한 값은 string 이므로 해당 값은 db의 형태와 통일시켜 비교하여야한다.
		originFiles = originFiles.filter(file => file.postId == postId);
		console.log(originFiles);
		//변경된 파일정보와 삭제된 파일정보를 담고있는 배열 전송
		//배열 형태로 전송 시 백단(Java)에서 처리불가
		//JSON String 형태로 변환하여 전송한다.
		$("#originFiles" + postId).val(JSON.stringify(originFiles));
		console.log($("#originFiles" + postId).val());
		//ajax에서 multipart/form-data형식을 전송하기 위해서는
		//new FormData()를 사용하여 직접 폼데이터 객체를 만들어준다.
		//form.serialize()는 multipart/form-data 전송불가
		//let formData = new FormData($("#updateForm")[0]);
		let formData = new FormData($("#updateForm" + postId)[0]);

		//ajax에 enctype: multipart/form-data, 
		//processData: false, contentType: false로 설정               
		//console.log("수정 할 내용 : " + $("#contentIn" + postId).val());
		//console.log(originFileList[index]);
		$.ajax({
			enctype: 'multipart/form-data',
			url: '/post/updatePost',
			type: 'put',
			data: formData,
			processData: false,
			contentType: false,
			success: function (obj) {
				alert("수정작업을 성공하였습니다.");
				console.log(obj);
				
				$("#imgArea" + postId).html(imageTag(obj.item, obj.item.fileSize));
				$("#postId").val('' + obj.item.getPost.postId);
				$("#userId").val('' + obj.item.getPost.userId);
				
				let content_format = obj.item.getPost.postContent;
				content_format = content_format.replaceAll("&lt;", "<");
				content_format = content_format.replaceAll("&gt;", ">");
				
				$("#postContentIn" + postId).val(content_format);
				$("#postContent" + postId).text(content_format);
				$("#contentIn" + postId).text(content_format);
				$("#restNmIn").val(obj.item.getPost.restNm);
				//수정 다하면 태그들을 다시 원래대로 돌린다.
				$("#postAttZone" + postId).html('');
				$("#upTitle" + postId).hide();
				$("#buttonBox" + postId).hide();
				$("#postContent" + postId).show();
				$("#contentIn" + postId).hide();
				//$("#deleteButton" + postId).remove();
				$("#updateButton" + postId).remove();
				$("#fileRequest" + postId).hide();
				$(".btnDel" + postId).hide();
				flagList[index] = false;
				$($(".updateBtn")[index]).text("게시글 수정");
			},
			error: function (e) {
				console.log(e);
			},
			done: function (result) {
				console.log(result);
				$("#postAttZone" + postId).replaceWith(result);
			}
		});
		return false;
	}
	
	//미리보기 영역에 들어가 div(img+button+p)를 생성하고 리턴
	function makeDiv(img, file) {
		//div 생성
		let div = document.createElement("div");
		div.setAttribute("style", "display: inline-block; position: relative;"
		+ " width: 150px; height: 120px; margin: 5px; border: 1px solid #00f; z-index: 1;");
		
		//button 생성
		let btn = document.createElement("input");
		btn.setAttribute("type", "button");
		btn.setAttribute("value", "x");
		btn.setAttribute("data-del-file", file.name);
		btn.setAttribute("style", "width: 30px; height: 30px; position: absolute;"
		+ " right: 0px; bottom: 0px; z-index: 999; background-color: rgba(255, 255, 255, 0.1);"
		+ " color: #f00;");
		
		//버튼 클릭 이벤트
		//버튼 클릭 시 해당 파일이 삭제되도록 설정
		btn.onclick = function(e) {
			//클릭된 버튼
			const ele = e.srcElement;
			//delFile(파일이름) 속성 꺼내오기: 삭제될 파일명
			const delFile = ele.getAttribute("data-del-file");
			
			for(let i = 0; i < inputUploadFiles.length; i++) {
				//배열에 담아놓은 파일들중에 해당 파일 삭제
				if(delFile == inputUploadFiles[i].name) {
					//배열에서 i번째 한개만 제거
					inputUploadFiles.splice(i, 1);
				}
			}
			
			//버튼 클릭 시 btnAtt에 첨부된 파일도 삭제
			//input type=file은 첨부된 파일들을 fileList 형태로 관리
			//fileList에 일반적인 File객체를 넣을 수 없고
			//DataTransfer라는 클래스를 이용하여 완전한 fileList 형태로 만들어서
			//input.files에 넣어줘야 된다.
			dt3 = new DataTransfer();
			
			for(f in inputUploadFiles) {
				const file = inputUploadFiles[f];
				dt3.items.add(file);
			}
			
			$("#btnAtt")[0].files = dt3.files;
			
			//해당 img를 담고있는 부모태그인 div 삭제
			const parentDiv = ele.parentNode;
			$(parentDiv).remove();
			
			if(inputUploadFiles.length ==  0) {
				$("#attZone").hide();
			}
		}
		
		//파일명 표출할 p태그 생성
		const fName = document.createElement("p");
		fName.setAttribute("style", "display: inline-block; font-size: 8px;");
		fName.textContent = file.name;
		
		//div에 하나씩 추가
		div.appendChild(img);
		div.appendChild(btn);
		div.appendChild(fName);
		
		//완성된 div 리턴
		return div;
	}
	
	//이미 게시된 게시글의 미리보기 영역에 들어가 div(img+button+p)를 생성하고 리턴
	function makePostDiv(img, file, postId) {
		//div 생성
		let div = document.createElement("div");
		div.setAttribute("style", "display: inline-block; position: relative;"
		+ " width: 150px; height: 120px; margin: 5px; border: 1px solid #00f; z-index: 1;");
		
		//button 생성
		let btn = document.createElement("input");
		btn.setAttribute("type", "button");
		btn.setAttribute("value", "x");
		btn.setAttribute("data-del-file", file.name);
		btn.setAttribute("style", "width: 30px; height: 30px; position: absolute;"
		+ " right: 0px; bottom: 0px; z-index: 999; background-color: rgba(255, 255, 255, 0.1);"
		+ " color: #f00;");
		
		//버튼 클릭 이벤트
		//버튼 클릭 시 해당 파일이 삭제되도록 설정
		btn.onclick = function(e) {
			//클릭된 버튼
			const ele = e.currentTarget;
			console.log(e.currentTarget);
			//delFile(파일이름) 속성 꺼내오기: 삭제될 파일명
			const delFile = ele.getAttribute("data-del-file");

			for(let i = 0; i < uploadFiles.length; i++) {
				//배열에 담아놓은 파일들중에 해당 파일 삭제
				if(delFile == uploadFiles[i].name) {
					console.log("del-file : " + delFile + ", uploadFiles : " + uploadFiles[i].name);
					//배열에서 i번째 한개만 제거
					let del = uploadFiles.splice(i, 1);
					console.log(del[0].name);
				}
				
			}
			//console.log(uploadPostFileList[index]);
			//버튼 클릭 시 btnAtt에 첨부된 파일도 삭제
			//input type=file은 첨부된 파일들을 fileList 형태로 관리
			//fileList에 일반적인 File객체를 넣을 수 없고
			//DataTransfer라는 클래스를 이용하여 완전한 fileList 형태로 만들어서
			//input.files에 넣어줘야 된다.
			dt = new DataTransfer();
			
			for(f in uploadFiles) {
				const file = uploadFiles[f];
				dt.items.add(file);
			}
			
			$("#postAttZone" + postId)[0].files = dt.files;
			
			//해당 img를 담고있는 부모태그인 div 삭제
			const parentDiv = ele.parentNode;
			$(parentDiv).remove();
		};
		
		//파일명 표출할 p태그 생성
		const fName = document.createElement("p");
		fName.setAttribute("style", "display: inline-block; font-size: 8px;");
		fName.textContent = file.name;
		
		//div에 하나씩 추가
		div.appendChild(img);
		div.appendChild(btn);
		div.appendChild(fName);
		
		//완성된 div 리턴
		return div;
	}

