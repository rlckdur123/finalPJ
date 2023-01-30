
$(function() {
	$('.fileBtns').hide();

	//ajax로 이벤트 함수를 다시 빌드하는 객체를 따로 정의
	//작성한 게시글의 이벤트 처리를 담당하는 함수
	$.update_post = function(postId) {
		console.log("새로 등록한 게시글 입력 확인 이벤트");
		$($('.uploadFileSpace')[0]).hide();
		$($('.changedFileSpace')[0]).hide();
		$("#contentIn" + postId).hide();
		$("#fileRequest" + postId).hide();
		$($(".fileBtns")[0]).hide();
		$($(".updateBtn")[0]).click(function(e) {
			//const postId = Number(postId);
			//const postId = e.target.postId;
			console.log("회원의 수정버튼 이벤트 함수 적용 확인.");

			if (!flagList[0]) {
				$("<button type='button' id='updateButton" + postId + "' class='btn' style='float:right'>").appendTo($("#modify_content" + postId));
				$("#updateButton" + postId).text("완료");
			} else {
				$("#updateButton" + postId).remove();
			}

			//$("#postContent" + $(this).val()).text();

			if (!flagList[0]) {
				$("#postContent" + postId).hide();
				$("#modify_content" + postId).show();
				$("#contentIn" + postId).show();
				$("#fileRequest" + postId).show();
				$("#updateButtonToggle" + postId).text("편집 모드 비활성화");
				$("#btnFileDel" + postId).show();
				
			} else {
				$("#postContent" + postId).show();
				$("#modify_content" + postId).hide();
				$("#contentIn" + postId).hide();
				$("#fileRequest" + postId).hide();
				$("#updateButtonToggle" + postId).text("게시글 수정");
				$("#btnFileDel" + postId).hide();
			}

			flagList[0] = !flagList[0];
			console.log("버튼 이벤트 html단 활성화");

			//내 게시글 파일 관리 버튼
			$("#fileRequest" + postId).click(function(e) {
				console.log("파일 요청 조작 활성화" + postId);
				$("#updateBtnAtt" + postId).click();
			});
		
			$("#fileRemove" + postId).click(function(e) {
				console.log("파일 삭제 요청 활성화");
			});
		
			//내 게시물 수정, 삭제, 돌아가기 결정 버튼
			$("#updateButton" + postId).click(function(e) {
				//console.log("update될 내용 : " + $("#contentIn" + postIdList[i]).val());\
				fnUpdatePost(postId, i);
			});
		
			//글 내용 수정하는 키입력을 받음.
			$("#contentIn" + postId).keyup(function(e) {
				$("#postContent" + postId).text(postId);
				console.log(postId);
				fnChangeContent(this, postId);
			});
		});
	}
	
	$(".uploadFileSpace").each(function(i, e) {
		$(this).hide();
	});
	$("div[class='changedFileSpace']").each(function(i, e) {
		$(this).hide();
	});

	//게시글 수정과 삭제를 활성화하는 버튼을 생성함. 게시글이 자기꺼인 것만 표시함.
	$(".updateBtn").each(function(i, e) {
		console.log("게시글의 수정버튼 이벤트 생성");
		//$($('.uploadFileSpace')[i]).hide();
		//$($("div[class='changedFileSpace']")[i]).hide();
		$("#upTitle" + $(this).val()).hide();
		$("#contentIn" + $(this).val()).hide();
		$("#fileRequest" + $(this).val()).hide();
		$(this).on('click', function() {
			console.log("초기 화면 수정 버튼 활성화.");
			const postId = $(this).val();
			flagList[i] = !flagList[i];
			if (flagList[i]) {
				$("#postAttZone" + postId).show();
				$("<button type='button' id='updateButton" + $(this).val() + "' class='btn' style='float:right'>").appendTo($("#modify_content" + postId));
				$("#updateButton" + $(this).val()).text("완료");
				$($(".fileBtns")[i]).show();

			} else {
				$(this).text("게시글 수정");
				$("#postAttZone" + postId).hide();
				$("#updateButton" + $(this).val()).remove();
				$($(".fileBtns")[i]).hide();

			}

			$("#postContent" + $(this).val()).text();

			if (!flagList[i]) {
				$("#postContent" + $(this).val()).show();
				$("#contentIn" + $(this).val()).hide();
				$("#fileRequest" + $(this).val()).hide();
				$(this).text("게시글 수정");

			} else {
				$("#postContent" + $(this).val()).hide();
				$("#contentIn" + $(this).val()).show();
				$("#fileRequest" + $(this).val()).show();
				$(this).text("편집 모드 비활성화");
			}
			console.log("버튼 이벤트 html단 활성화");

			console.log("버튼 파일 조작 화면단 활성화" + postIdList[0]);
			//$.updateBtnAtt(postIdList[i], i);
			//내 게시글 파일 관리 버튼
			$("#fileRequest" + postId).click(function(e) {
				console.log("파일 요청 조작 활성화" + $(this).val());
				$("#updateBtnAtt" + postId).click();
			});

			$("#fileRemove" + $(this).val()).click(function(e) {
				console.log("파일 삭제 요청 활성화");
			});

			//내 게시물 수정, 삭제, 돌아가기 결정 버튼
			$("#updateButton" + postId).click(function(e) {
				//console.log("update될 내용 : " + $("#contentIn" + postIdList[i]).val());\
				fnUpdatePost(postId, i);
			});

			//글 내용 수정하는 키입력을 받음.
			$("#contentIn" + postId).keyup(function(e) {
				$("#postContent" + postId).text($(this).val());
				console.log($(this).val());
				fnChangeContent(this, postId);
			});

		});

	});
	//포스트 삭제
	$(".post_deleteButton").click(function(e) {
		console.log("삭제버튼 클릭")
		$("#delete_form" + e.target.value).submit();
	});
	//추가되는 게시글의 삭제 작업 이벤트를 부여.
	$.deleteButtonSet = function(postId) {
		$("#deleteButton" + postId).click(function(e) {
			$("#delete_form" + e.target.value).submit();
		});
	}

	//내 게시물 외의 모든 게시글의 
	$.followingEvent = function(targetIndex, postId) {
		console.log("현재 인덱스 : " + targetIndex + ", 현재 게시글의 아이디 : " + postId);
		$($(".uploadFileSpace")[targetIndex]).hide();
		$($(".changedFileSpace")[targetIndex]).hide();
		$("#updateForm" + postId).hide();
		$("#upTitle" + postId).hide();
		$("#contentIn" + postId).hide();
		$("#fileRequest" + $(this).val()).hide();
		$("#fileRequest" + postId).hide();
	}
	

	//스크롤 확장시 다시 이벤트를 발생시킬 스크립트를 다시 로드함.
	$.updateBtn = function(startIndex, size) {
		for (let i = startIndex; i < startIndex + size; i++) {
			console.log("this확인- 업데이트 관련 이벤트 적용중" + i);
			flagList[i] = false;
			$($(".uploadFileSpace")[i]).hide();
			$($(".changedFileSpace")[i]).hide();
			$("#contentIn" + $(this).val()).hide();
			$("#fileRequest" + $(this).val()).hide();
			$($(".fileBtns")[i]).hide();
			$($(".updateBtn")[i]).on('click', function(e) {
				console.log("초기 화면 수정 버튼 활성화.");
				const postId = Number($(this).val());
				flagList[i] = !flagList[i];
				if (flagList[i]) {
					$("<button type='button' id='updateButton" + $(this).val() + "' class='btn' style='float:right'>").appendTo($("#modify_content" + postId));
					$("#updateButton" + $(this).val()).text("완료");
				} else {
					$("#updateButton" + $(this).val()).remove();
				}
				$("#postContent" + $(this).val()).text();

				if (!flagList[i]) {
					$("#postContent" + $(this).val()).show();
					$("#contentIn" + $(this).val()).hide();
					$("#fileRequest" + $(this).val()).hide();
					$(this).text("게시글 수정");

				} else {
					$("#postContent" + $(this).val()).hide();
					$("#contentIn" + $(this).val()).show();
					$("#fileRequest" + $(this).val()).show();
					$(this).text("편집 모드 비활성화");
				}
				console.log("버튼 이벤트 html단 활성화");

				console.log("버튼 파일 조작 화면단 활성화" + postIdList[0]);
				//$.updateBtnAtt(postIdList[i], i);
				//내 게시글 파일 관리 버튼
				$("#fileRequest" + postId).click(function(e) {
					console.log("파일 요청 조작 활성화" + $(this).val());
					$("#updateBtnAtt" + postId).click();
				});

				$("#fileRemove" + $(this).val()).click(function(e) {
					console.log("파일 삭제 요청 활성화");
				});

				//내 게시물 수정, 삭제, 돌아가기 결정 버튼
				$("#updateButton" + postId).click(function(e) {
					//console.log("update될 내용 : " + $("#contentIn" + postIdList[i]).val());\
					fnUpdatePost(postId, i);
				});

				//글 내용 수정하는 키입력을 받음.
				$("#contentIn" + postId).keyup(function(e) {
					$("#postContent" + postId).text($(this).val());
					console.log($(this).val());
					fnChangeContent(this, postId);
				});

			});

		}

	}
	
});//J쿼리 종료

//스크롤 확장시 한 게시글마다 각각 이벤트를 적용시킬 함수 생성
function updateBtnEvent(i, size, postId) {
	console.log(postId + "게시글 확인- 업데이트 관련 이벤트 적용중" + i + ", 몇개 당 적용 : " + size);
	console.log("this확인- 업데이트 관련 이벤트 적용중" + i);
	//$($(".fileBtns")[i]).hide();
	$("#updateButtonToggle" + postId).on('click', function(e) {
		const value = e.target.value;
		console.log("초기 화면 수정 버튼 활성화." + value);
		if (!flagList[i]) {
			console.log("수정 버튼 내용 활성화");
			$("<button type='button' id='updateButton" + postId + "' class='btn' style='float:right;'>").appendTo($("div[id='modify_content" + postId + "']"));
			$("#updateButton" + postId).text("완료");
			
		} else {
			console.log("수정 버튼 내용 비활성화");
			$("div[id='updateButton" + postId + "']").remove();
		}
		
		//$("#postContent" + postId).text();

		if (!flagList[i]) {
			$("#postContent" + postId).hide();
			$("#modify_content" + postId).show();
			$("#contentIn" + postId).show();
			$("#fileRequest" + postId).show();
			$("#updateButtonToggle" + postId).text("편집 모드 비활성화");
			$("#btnFileDel" + postId).show();
			$(".btnDel" + postId).show();
		} else {
			$("#postContent" + postId).show();
			$("#modify_content" + postId).hide();
			$("#contentIn" + postId).hide();
			$("#fileRequest" + postId).hide();
			$("#updateButtonToggle" + postId).text("게시글 수정");
			$("#btnFileDel" + postId).hide();
			$(".btnDel" + postId).hide();
		}
		console.log("버튼 이벤트 html단 활성화");
		flagList[i] = !flagList[i];
		console.log(flagList);
		//$.updateBtnAtt(postIdList[i], i);
		//내 게시글 파일 관리 버튼
		$("#fileRequest" + postId).click(function(e) {
			console.log("파일 요청 조작 활성화" + postId);
			$("#updateBtnAtt" + postId).click();
		});

		$("#fileRemove" + postId).click(function(e) {
			console.log("파일 삭제 요청 활성화");
		});

		//내 게시물 수정, 삭제, 돌아가기 결정 버튼
		$("#updateButton" + postId).click(function(e) {
			//console.log("update될 내용 : " + $("#contentIn" + postIdList[i]).val());\
			fnUpdatePost(postId, i);
		});

		//글 내용 수정하는 키입력을 받음.
		$("#contentIn" + postId).keyup(function(e) {
			$("#postContent" + postId).text(postId);
			console.log(postId);
			fnChangeContent(this, postId);
		});

	});
}


//수정모드에서  내용 변경시 실행될 메소드
function fnChangeContent(input, postId) {
	console.log("내용 변경 이벤트 발생");
	//글을 변경하기위해 텍스트박스를 수정할 경우 해당 텍스트박스의 내용을 숨겨진 태그들에 뿌려줌.
	//$("#postContent").text($(input).text() + "");
	$("#postContentIn" + postId).val($(input).val());
	//console.log($("#post").val($(input).val()));
	$("#postContent" + postId).val()
}


//수정작업을 진행한후 이미지 갱신을 위한 태그 생성. 수정작업때만 사용되는 html단 텍스트 이므로 로그인 여부를 사용하지 않아도됨.
function imageTag(item, fileLength) {
	let tag = "";
	console.log("fileLength : " + fileLength);
	console.log(item.updateFileList);
	if(fileLength > 0) {
		tag += `<div id="carouselExampleIndicators${item.getPost.postId}" class="carousel carousel-dark slide" data-bs-ride="carousel">`;
		tag += `<div class="carousel-indicators">`;
		for(let i = 0; i < fileLength; i++) {
			if(i < 1) {
				tag +=  `<button type="button" data-bs-target="#carouselExampleIndicators${item.getPost.postId}" data-bs-slide-to="${i}" class="active" aria-current="true" aria-label="Slide ${i+1}"></button>`;
			} else {
				tag +=  `<button type="button" data-bs-target="#carouselExampleIndicators${item.getPost.postId}" data-bs-slide-to="${i}" aria-label="Slide ${i+1}"></button>`;
			}
		}
		tag += `</div>`; 
		tag += `<div class="carousel-inner">`; 
		for (let i = 0; i < fileLength; i++) {
			console.log(item);
			if(i < 1) {
				tag += `<div class="carousel-item active" data-bs-interval="4000" value="${item.updateFileList[i].postFileId}">`;
			} else {
				tag += `<div class="carousel-item" data-bs-interval="4000" value="${item.updateFileList[i].postFileId}">`;
			}
			//tag += `<div class="fileList${item.getPost.postId}" value="${item.updateFileList[i].postFileId}">`;
			//tag += `<div style="position: relative;">`;
			tag += `<input type="hidden" id="postFileId${item.updateFileList[i].postFileId}" 
						class="postFileId${item.updateFileList[i].postId}" name="postFileId" value="${item.updateFileList[i].postFileId}">`;
			tag += `<input type="hidden" id="postFileNm${item.updateFileList[i].postFileId}" 
						class="postFileNm" name="postFileNm" value="${item.updateFileList[i].postFileNm}">`;
			tag += `<input type="hidden" id="postId${item.updateFileList[i].postFileId}" 
						class="postId${item.getPost.postId}" name="postId" value="${item.updateFileList[i].postId}">`;
			if (item.updateFileList[i].postFileCate == 'img') {
				tag += `<input type="file" id="changedFile${item.updateFileList[i].postFileId}" name="changedFile${item.updateFileList[i].postFileId}" style="display: none;" 
						onchange="fnGetChangedFileInfo(${item.updateFileList[i].postFileId}, ${i}, event)">`;
				tag += `<img id="img${item.updateFileList[i].postFileId}" 
					src="/upload/${item.updateFileList[i].postFileNm}" 
			 		style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
					class="fileImg d-block w-100" 
					onclick="fnImgChange(${item.updateFileList[i].postFileId})">`;
			} else {
				tag += `<img id="img${item.updateFileList[i].postFileId}" 
					src="/assets/img/defaultFileImg.png" 
					style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
					class="fileImg d-block w-100" 
				 	onclick="fnImgChange(${item.updateFileList[i].postFileId})">`;
			}
			tag += `<input type="button" class="btnDel${item.getPost.postId}" value="x" data-del-file="${item.updateFileList[i].postFileId}"
			   style="width: 30px; height: 30px; position: absolute; right: 0px; bottom: 0px; 
			   z-index: 999; background-color: rgba(255, 255, 255, 0.1); color: #f00;"
			   onclick="fnPostImgDel(event)">`;
			tag += `<p id="fileNm${item.updateFileList[i].postFileId}" style="display: none; font-size: 8px; cursor: pointer;" 
						onclick="fnFileDown(${item.updateFileList[i].postId}, ${item.updateFileList[i].postFileId})"
						>${item.updateFileList[i].postFileOriginNm}</p>`;
			tag += `</div>`;
		}
		tag += `</div>`;
		tag += `<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators${item.getPost.postId}" data-bs-slide="prev">
					    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
					    <span class="visually-hidden">Previous</span>
						</button>`;
		tag += `<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators${item.getPost.postId}" data-bs-slide="next">
						  <span class="carousel-control-next-icon" aria-hidden="true"></span>
						  <span class="visually-hidden">Next</span>
						  </button>`;
		tag += `</div>`;
	}

	return tag;
}

//새로운 게시글을 등록하는 html 단 태그를 함수화 하여 메인 페이지, 나의 페이지, 이외에 쓸 가능성을 남겨둔 페이지에 추가할 수 있게함.
function callInsertPost(loginUser) {
	let inputText = "";
	inputText += `<div class="col-12">
						<!-- 작성 하는 부분 -->
						<div class="card recent-sales overflow-auto">
							<div class="card-body">
									<h5 class="card-title" style="margin-bottom: -10px;">What's happening?</h5>
									<!-- Quill Editor Default(퀼 에디터 텍스트 에어리어) -->
									<div class="quill-editor-default">
										<h2>write your daily life</h2>
										<p></p>
									</div>
									<button type="button" id="postFileUpdate"
										style="background: none; border:none;">
										<i class="ri-image-2-fill" style="font-size: 20px; color:#000069;"></i>
									</button>
									<button type="button" id="res_select_btn" onclick="new_window()"
										style="background: none; border:none;">
										<i class="ri-map-pin-2-fill" style="font-size: 20px; color:#000069;"></i>
									</button>
									<!-- form을 위로 올리면 파일 개수를 0개인데 1개로 받아서 파일 처리를 못함. 그냥 여기로 냅두는게 좋을 듯. -->
								<form id="insert_form" enctype="multipart/form-data" action="/post/insertPost" method="post">
									<div class="box" id="imagePreview">
										<!-- 이미지를 따로 미리 임시 저장하는 공간 -->
										<div id="image_preview" style="margin:10px 0;">
											<input type="file" id="btnAtt" name="uploadFiles" multiple="multiple"
												style="display: none;">
											<div id="attZone" data-placeholder="파일을 첨부하려면 파일선택 버튼을 누르세요."
												style="display: none;"></div>
										</div>
									</div>
									<!-- End Quill Editor Default -->
									<input type="hidden" id="restNm" name="restNm" value="">
									<input type="hidden" id="postContent" name="postContent" value="">
									<div style="margin-bottom: 5px;">
										<label id="hashTag_label1">&emsp;# <input type="text" id=hashTag1
												name="hashTag1" class="hashTag_text" placeholder="태그 입력"
												onfocus="this.placeholder=''"
												onblur="this.placeholder='태그 입력'"></label>
										<label id="hashTag_label2" style="display: none;">&emsp;# <input type="text"
												id=hashTag2 name="hashTag2" class="hashTag_text" placeholder="태그 입력"
												onfocus="this.placeholder=''"
												onblur="this.placeholder='태그 입력'"></label>
										<label id="hashTag_label3" style="display: none;">&emsp;# <input type="text"
												id=hashTag3 name="hashTag3" class="hashTag_text" placeholder="태그 입력"
												onfocus="this.placeholder=''"
												onblur="this.placeholder='태그 입력'"></label>
										<label id="hashTag_label4" style="display: none;">&emsp;# <input type="text"
												id=hashTag4 name="hashTag4" class="hashTag_text" placeholder="태그 입력"
												onfocus="this.placeholder=''"
												onblur="this.placeholder='태그 입력'"></label>
										<label id="hashTag_label5" style="display: none;">&emsp;# <input type="text"
												id=hashTag5 name="hashTag5" class="hashTag_text" placeholder="태그 입력"
												onfocus="this.placeholder=''"
												onblur="this.placeholder='태그 입력'"></label>

									</div>
									<!--식당정보 후에 필요 없는것은 hidden예정-->
									<input type="hidden" id="res_place_name" name="resName" value="">
									<input type="hidden" id="res_address_name" name="resAddress">
									<input type="hidden" id="res_road_address_name" name="resRoadAddress">
									<input type="hidden" id="res_phone" name="resPhone">
									<input type="hidden" id="res_category_name" name="resCategory">

									<input type="button" class="btn" id="insert_board" value="작성"
										style="background-color: #4154f1; color: white; float: right;">
								</form>
							</div>
						</div><!-- End Recent Sales -->
					</div>`;
	return inputText;
}

/*----------------------------------------------------------------------------------------
		창혁 작업
-----------------------------------------------------------------------------------------*/

//게시글 등록시 새로 등록된 1개의 장성된 글을 맨위 상단에 표시하는 태그
function get_post_current(post) {
	console.log(post);
	let post_text = "";
	let post_date = new Date(post.insertPost.postDate);
	//날짜 데이터 밀리초
	console.log(post_date);
	post_date = now - post_date;

	let content_format = post.insertPost.postContent;
	content_format = content_format.replaceAll("&lt;", "<");
	content_format = content_format.replaceAll("&gt;", ">");
	
	post_text += `<div class="col-12 post">`;
	post_text += `<input type="hidden" id="fileList${post.insertPost.postId}" value="${post.postFileList.length}">`;
	post_text += `<div class="card recent-sales">`
	post_text += `<div class="card-body">`
	post_text += `<div class="filter" style="margin-top: 15px;">`
	if (post_date / (1000 * 60 * 60 * 24 * 30 * 12) > 1) {
		post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30 * 12))}년 전</a>`
	}
	else if (post_date / (1000 * 60 * 60 * 24 * 30) > 1) {
		post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30))}달 전</a>`
	}
	else if (post_date / (1000 * 60 * 60 * 24) > 1) {
		post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24))}일 전</a>`
	}
	else if (post_date / (1000 * 60 * 60) > 1) {
		post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60))}시간 전</a>`
	}
	else {
		post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60))}분전</a>`
	}
	post_text += `<a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a>`
	post_text += `<ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">`

	if(post.loginUser.userId == post.insertPost.userId){
		post_text += `<button type="button" class="updateBtn dropdown-item" id="updateButtonToggle${post.insertPost.postId}" value="${post.insertPost.postId}" style="text-align:center">게시글 수정</button>`
		post_text += `<button type="button" class="dropdown-item post_deleteButton" id="deleteButton${post.insertPost.postId}" value="${post.insertPost.postId}" style="text-align:center">글 삭제</button>`
	}
	else {
		post_text += `<form action="/user/reportUser?userId=${post.insertPost.userId}" method="post">`
		post_text += `<input type="submit" class="dropdown-item" value="유저 신고하기" style="text-align:center"></form>`
		post_text += `<form action="/post/reportPost?postId=${post.insertPost.postId}" method="post">`
		post_text += `<input type="submit" class="dropdown-item" value="포스트 신고하기" style="text-align:center"></form>`
	}
	post_text += `</ul></div>`
	post_text += `<div class="card-title">`;
	post_text += `<img class="img-fluid rounded-circle" src="/upload/${post.profile.userProfileNm}"
					style="width: 40px;">
					<a href="#" class="card-title">${post.loginUser.userNick}</a>`;
	post_text += `</div>`;
	post_text += `<form id="updateForm${post.insertPost.postId}" enctype="multipart/form-data">`;
	//<!-- 게시글 사진 부분 -->
	//<!-- imgArea는 반복문을 사용해 2차원 배열 처럼 사용되어 파일의 내용을 표시하게됨. -->
	post_text += `<div class="activity" style="margin-bottom: 10px;"
					id="restImgBox${post.insertPost.postId}">`;
	post_text += `<div id="imgArea${post.insertPost.postId}">`;
	if(post.postFileList.length > 0) {
		post_text += `<div id="carouselExampleIndicators${post.postId}" class="carousel slide" data-bs-ride="carousel">`;
		post_text += `<div class="carousel-indicators">`;
		for(let i = 0; i < post.postFileList.length; i++) {
			if(i < 1) {
				post_text +=  `<button type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide-to="${i}" class="active" aria-current="true" aria-label="Slide ${i+1}"></button>`;
			} else {
				post_text +=  `<button type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide-to="${i}" aria-label="Slide ${i+1}"></button>`;
			}
		}
		post_text += `</div>`; 
		post_text += `<div class="carousel-inner">`; 
		if (loginUserId == post.insertPost.userId) {
			for (let i = 0; i <post.postFileList.length; i++) {
				if(i < 1) {
					post_text += `<div class="fileList${post.insertPost.postId} carousel-item active" value="${post.postFileList[i].postFileId}">`;
				} else {
					post_text += `<div class="fileList${post.insertPost.postId} carousel-item" value="${post.postFileList[i].postFileId}">`;
				}
				//<!--<input type="text" th:id="'postFileNm' + ${post.insertPost.postId}" value="">
				//<input type="text" th:id="'postFileId' + ${post.insertPost.postId}" value="">-->
				//post_text += `<div style="position: relative;">`;
				post_text += `<div class="fileList${post.postId}" style="position: relative;">`;
				post_text += `<input type="hidden" id="postFileId${post.postFileList[i].postFileId}"
								class="postFileId${post.postFileList[i].postId}" name="postFileId"
								value="${post.postFileList[i].postFileId}">`;
				post_text += `<input type="hidden"id="postFileNm${post.postFileList[i].postFileId}"
								class="postFileNm" name="postFileNm"
								value="${post.postFileList[i].postFileNm}">`;
				post_text += `<input type="hidden" id="postId${post.postFileList[i].postFileId}"
								class="postId${post.postFileList[i].postId}" name="postId"
								value="${post.postFileList[i].postId}">`;
				if (post.postFileList[i].postFileCate == "img") {
					post_text += `<input type="file" id="changedFile${post.postFileList[i].postFileId}"
								name="changedFile${post.postFileList[i].postFileId}"
								style="display: none;"
								onchange="fnGetChangedFileInfo(${post.postFileList[i].postFileId}, ${i}, event)">`;
					post_text += `<img id="img${post.postFileList[i].postFileId}"
									src="/upload/${post.postFileList[i].postFileNm}"
									style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
									class="fileImg d-block w-100"
									onclick="fnImgChange(${post.postFileList[i].postFileId})">`;
				} else {
					post_text += `<img id="img${post.postFileList[i].postFileId}"
									src="/upload/${post.postFileList[i].postFileNm}"
									style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
									class="fileImg d-block w-100"
									onclick="fnImgChange(${post.postFileList[i].postFileId})">`;
				}
				post_text += `<input type="button" class="btnDel${post.insertPost.postId}" id="btnFileDel${post.postFileList[i].postFileId}" value="x"
								data-del-file="${post.postFileList[i].postFileId}" style="width: 30px; height: 30px; position: absolute; right: 0px; bottom: 0px; 
								z-index: 999; background-color: rgba(255, 255, 255, 0.1); color: #f00; display: none;"
								onclick="fnPostImgDel(event)">`;
				post_text += `<p id="fileNm${post.postFileList[i].postFileId}"
								style="display: none; font-size: 8px; cursor: pointer;"
								onclick="fnFileDown(${post.postFileList[i].postId}, ${post.postFileList[i].postFileId})">
								${post.postFileList[i].postFileOriginNm}</p>`;
				post_text += `</div>`;
				post_text += `</div>`;
			}
		} else {
			for (let i = 0; i < post.fileLength; i++) {
				if(i < 1) {
					post_text += `<div class="carousel-item active">`;
				} else {
					post_text += `<div class="carousel-item">`;
				}
				if (post.postFileList[i].postFileCate == "img") {
					post_text += `<img id="img${post.postFileList[i].postFileId}" 
						 src="/upload/${post.postFileList[i].postFileNm}"
					 	 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
						 class="fileImg" 
						 onclick="fnImgChange(${post.postFileList[i].postFileId})">`;
				} else {
					post_text += `<img id="img${post.postFileList[i].postFileId}"
						 src="/assets/img/defaultFileImg.png"
						 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
						 class="fileImg" 
						 onclick="fnImgChange(${post.postFileList[i].postFileId})">`;
				}
				post_text += `</div>`;
			}
		}
		post_text += `<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide="prev">
					    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
					    <span class="visually-hidden">Previous</span>
						</button>`;
		post_text += `<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide="next">
						  <span class="carousel-control-next-icon" aria-hidden="true"></span>
						  <span class="visually-hidden">Next</span>
					  </button>`;
		post_text += `</div>`;
				
	}
	post_text += `</div>`;
	post_text += `</div>`;
	post_text += `<div class="uploadFileSpace" data-post-id="${post.insertPost.postId}">
					<input type="file" id="updateBtnAtt${post.insertPost.postId}"
						class="updateBtnAtt" data-post-id="${post.insertPost.postId}" name="uploadFiles"
						multiple="multiple">
					</div>`;
	post_text += `<div class="changedFileSpace">
					<input type="file" id="changedFiles${post.insertPost.postId}"
						name="changedFiles" value="" multiple="multiple">
					</div>`;
	post_text += `<div id="postAttZone${post.insertPost.postId}"
					data-placeholder="파일을 첨부하려면 파일선택 버튼을 누르세요."></div>`;
	post_text += `<div style="margin-bottom: 5px; display: none;">
				<label id="hashTag_label1">&emsp;# <input type="text" id=hashTag1
						name="hashTag1" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label2" style="display: none;">&emsp;# <input type="text"
						id=hashTag2 name="hashTag2" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label3" style="display: none;">&emsp;# <input type="text"
						id=hashTag3 name="hashTag3" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label4" style="display: none;">&emsp;# <input type="text"
						id=hashTag4 name="hashTag4" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label5" style="display: none;">&emsp;# <input type="text"
						id=hashTag5 name="hashTag5" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>

			</div>`;
	post_text += `<input type="hidden" name="postId" value="${post.insertPost.postId}">`;
	post_text += `<input type="hidden" name="originFiles" id="originFiles${post.insertPost.postId}">`;
	post_text += `<input type="hidden" id="userId" name="userId" value="${post.insertPost.userId}">`;
	post_text += `<input type="hidden" id="postContentIn${post.insertPost.postId}" name="postContent" value="${post.insertPost.postContent}">`;
	post_text += `<input type="hidden" name="postDate" value="${post.insertPost.postDate}"></form>`;

	post_text += `<div class="activity">`
	post_text += `<div id="postContent${post.insertPost.postId}">${content_format}</div>`
	if (post.loginUser.userId == post.insertPost.userId) {
		post_text += `<div id="modify_content${post.insertPost.postId}">
							<textarea id="contentIn${post.insertPost.postId}" class="form-control" style="width: 100%; resize: none;"
								spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)"
							>${post.insertPost.postContent}</textarea>
							<button type="button" id="fileRequest${post.insertPost.postId}" value="${post.insertPost.postId}"
								style="background:none; border:none;">
								<i class="ri-image-2-fill" style="font-size: 20px; color:#000069;"></i>
							</button>
						</div>`;
		post_text += `<form id="delete_form${post.insertPost.postId}" action="/post/deletePost" method="post">
							<input type="hidden" id="postId" name="postId" value="${post.insertPost.postId}">
							<input type="hidden" id="restNmIn" name="restNm" value="${post.insertPost.restNm}">
							<input type="hidden" id="postDate" name="postDate"
								value="${post.insertPost.postDate}">
							<input type="hidden" name="fileSize" id="fileSize"
								value="${post.postFileList.length}">
						</form>`
	}
	post_text += `</div>`
	//<!--해시태그-->

	post_text += `<div class="activity">`
		if(post.insertPost.hashTag1 != ""){
			post_text += `<br><a href="/search/searchByPost?searchKeyword=${post.insertPost.hashTag1}" style="color: blue;">&emsp;#<span>${post.insertPost.hashTag1}</span></a>`
		}
		if(post.insertPost.hashTag2 != ""){
			post_text += `<a href="/search/searchByPost?searchKeyword=${post.insertPost.hashTag2}" style="color: blue;">&emsp;#<span>${post.insertPost.hashTag2}</span></a>`
		}
		if(post.insertPost.hashTag3 != ""){
			post_text += `<a href="/search/searchByPost?searchKeyword=${post.insertPost.hashTag3}" style="color: blue;">&emsp;#<span>${post.insertPost.hashTag3}</span></a>`
		}
		if(post.insertPost.hashTag4 != ""){
			post_text += `<a href="/search/searchByPost?searchKeyword=${post.insertPost.hashTag4}" style="color: blue;">&emsp;#<span>${post.insertPost.hashTag4}</span></a>`
		}
		if(post.insertPost.hashTag5 != ""){
			post_text += `<a href="/search/searchByPost?searchKeyword=${post.insertPost.hashTag5}" style="color: blue;">&emsp;#<span>${post.insertPost.hashTag5}</span></a>`
		}
	post_text += `</div>`
	//<!--좋아요 댓글 지도-->	
	post_text += `<div class="activity">`
	if (post.postLike == "Y") {
		post_text += `<i class="ri-heart-3-line post_like" id="${post.insertPost.postId}" style="font-size: 30px; margin-right: 5px; color:red; cursor: pointer;"></i>`
	}
	else if (post.postLike == "N") {
		post_text += `<i class="ri-heart-3-line post_like" id="${post.insertPost.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
	}
	post_text += `<i class="ri-message-3-line msg_icon" id="${post.insertPost.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
	//식당 관련 내용을 적용하는 버튼	
	if (post.restaurant == "Y")
		post_text += `<i class="ri-map-pin-2-line map_icon" id="${post.insertPost.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
	post_text += `<br>`
	post_text += `<a>좋아요 <span id="likeCnt${post.insertPost.postId}">${post.likeCnt}</span>개</a>`;
	post_text += `</div>`
	/*
	//내부 서버로 옮기는 데이터를 모음. 추후에 이미지도 다룸. 
	post_text += `<form class="data" action="/post/deletePost" method="post" id="delete_form${post.postId}">`
	post_text += `<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">`
	post_text += `<input type="hidden" id="postContentIn" name="postContent" value="${post.postContent}">`
	post_text += `<input type="hidden" id="userId" name="userId" value="''+${post.userId}">`
	post_text += `<input type="hidden" id="postId" name="postId" value="''+${post.postId}">`
	post_text += `<input type="hidden" id="postDate" name="postDate" value="''+${post.postDate}">`
	post_text += `</form>`
	*/
	//<!-- 친구 식사 했는지 확인 필드 -->
	if (post.resCnt != 0) {
		post_text += `<div class="activity" style="text-align: center;"><hr>
			<a class="eatClick" id="${post.restaurantInfo.resName}"><span th:text="${post.loginUser.userNick}"></span>님의 친구 <span>${post.resCnt}</span>명이
				<span>${post.restaurantInfo.resName}</span> 에서 식사하셨어요!</a></div>`;
	}
	post_text += `</div></div></div></div></div>`;
	
	return post_text;
}

$.get_post = function(obj){
	console.log(obj);
	totalPages = obj.totalPages;
	let elements = page_num * obj.pageable.pageSize;
	
	for (let post of obj.content) {
		let post_date = new Date(post.postDate);
		//날짜 데이터 밀리초
		post_date = now - post_date;
	
		let content_format = post.postContent;
		content_format = content_format.replaceAll("&lt;", "<");
		content_format = content_format.replaceAll("&gt;", ">");
		
		post_text += `<div class="col-12 post">`;
		post_text += `<input type="hidden" value="${post.betweenDate}">`;
		post_text += `<input type="hidden" id="fileList${post.postId}" value="${post.fileLength}">`;
		post_text += `<div class="card recent-sales">`
		post_text += `<div class="card-body">`
		post_text += `<div class="filter" style="margin-top: 15px;">`
		if (post_date / (1000 * 60 * 60 * 24 * 30 * 12) > 1) {
			post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30 * 12))}년 전</a>`
		}
		else if (post_date / (1000 * 60 * 60 * 24 * 30) > 1) {
			post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30))}달 전</a>`
		}
		else if (post_date / (1000 * 60 * 60 * 24) > 1) {
			post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24))}일 전</a>`
		}
		else if (post_date / (1000 * 60 * 60) > 1) {
			post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60))}시간 전</a>`
		}
		else {
			post_text += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60))}분전</a>`
		}
		post_text += `<a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a>`
		post_text += `<ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">`
	
		if(loginUserId == post.userId){
			post_text += `<button type="button" class="updateBtn dropdown-item" id="updateButtonToggle${post.postId}" value="${post.postId}" style="text-align:center">게시글 수정</button>`
			post_text += `<button type="button" class="dropdown-item post_deleteButton" id="deleteButton${post.postId}" value="${post.postId}" style="text-align:center">글 삭제</button>`
		}
		else {
			post_text += `<form action="/user/reportUser?userId=${post.userId}" method="post">`
			post_text += `<input type="submit" class="dropdown-item" value="유저 신고하기" style="text-align:center"></form>`
			post_text += `<form action="/post/reportPost?postId=${post.postId}" method="post">`
			post_text += `<input type="submit" class="dropdown-item" value="포스트 신고하기" style="text-align:center"></form>`
		}
		post_text += `</ul></div>`
		post_text += `<div class="card-title">`;
		post_text += `<img class="img-fluid rounded-circle" src="/upload/${post.profile.userProfileNm}"
						style="width: 40px;">
						<a href="/social/otherUser?userId=${post.userId}" class="card-title">${post.userNick}</a>`;
		post_text += `</div>`;
		post_text += `<form id="updateForm${post.postId}" enctype="multipart/form-data">`;
		//<!-- 게시글 사진 부분 -->
		//<!-- imgArea는 반복문을 사용해 2차원 배열 처럼 사용되어 파일의 내용을 표시하게됨. -->
		post_text += `<div class="activity" style="margin-bottom: 10px;" id="restImgBox${post.postId}">`;
		post_text += `<div id="imgArea${post.postId}">`;
		if(post.fileLength > 0 ) {
			post_text += `<div id="carouselExampleIndicators${post.postId}" class="carousel carousel-dark slide" data-bs-ride="carousel">`;
			post_text += `<div class="carousel-indicators">`;
			for(let i = 0; i < post.fileLength; i++) {
				if(i < 1) {
					post_text +=  `<button type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide-to="${i}" class="active" aria-current="true" aria-label="Slide ${i+1}"></button>`;
				} else {
					post_text +=  `<button type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide-to="${i}" aria-label="Slide ${i+1}"></button>`;
				}
			}
			post_text += `</div>`; 
			post_text += `<div class="carousel-inner">`; 
			if (loginUserId == post.userId) {
				for (let i = 0; i < post.fileLength; i++) {
					if(i < 1) {
						post_text += `<div class="carousel-item active" data-bs-interval="4000" value="${post.fileList[i].postFileId}">`;
					} else {
						post_text += `<div class="carousel-item" data-bs-interval="4000" value="${post.fileList[i].postFileId}">`;
					}
					//<!--<input type="text" th:id="'postFileNm' + ${post.postId}" value="">
					//<input type="text" th:id="'postFileId' + ${post.postId}" value="">-->
					post_text += `<div class="fileList${post.postId}" style="position: relative;">`;
					post_text += `<input type="hidden" id="postFileId${post.fileList[i].postFileId}"
									class="postFileId${post.fileList[i].postId}" name="postFileId"
									value="${post.fileList[i].postFileId}">`;
					post_text += `<input type="hidden"id="postFileNm${post.fileList[i].postFileId}"
									class="postFileNm" name="postFileNm"
									value="${post.fileList[i].postFileNm}">`;
					post_text += `<input type="hidden" id="postId${post.fileList[i].postFileId}"
									class="postId${post.fileList[i].postId}" name="postId"
									value="${post.fileList[i].postId}">`;
					if (post.fileList[i].postFileCate == "img") {
						post_text += `<input type="file" id="changedFile${post.fileList[i].postFileId}"
									name="changedFile${post.fileList[i].postFileId}"
									style="display: none;"
									onchange="fnGetChangedFileInfo(${post.fileList[i].postFileId}, ${i}, event)">`;
						post_text += `<img id="img${post.fileList[i].postFileId}"
										src="/upload/${post.fileList[i].postFileNm}"
										style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
										class="d-block w-100"
										onclick="fnImgChange(${post.fileList[i].postFileId})">`;

					} else {
						post_text += `<img id="img${post.fileList[i].postFileId}"
										src="/upload/${post.fileList[i].postFileNm}"
										style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
										class="d-block w-100"
										onclick="fnImgChange(${post.fileList[i].postFileId})">`;
					}
					post_text += `<input type="button" class="btnDel${post.postId}" id="btnFileDel${post.fileList[i].postFileId}" value="x"
									data-del-file="${post.fileList[i].postFileId}" style="width: 30px; height: 30px; position: absolute; right: 0px; bottom: 0px; 
									z-index: 999; background-color: rgba(255, 255, 255, 0.1); color: #f00; display: none;" 
									onclick="fnPostImgDel(event)">`;

					post_text += `<p id="fileNm${post.fileList[i].postFileId}"
									style="display: none; font-size: 8px; cursor: pointer;"
									onclick="fnFileDown(${post.fileList[i].postId}, ${post.fileList[i].postFileId})">
									${post.fileList[i].postFileOriginNm}</p>`;
					post_text += `</div>`;
					post_text += `</div>`;
				}
			} else {
				for (let i = 0; i < post.fileLength; i++) {
					if(i < 1) {
						post_text += `<div class="carousel-item active">`;
					} else {
						post_text += `<div class="carousel-item">`;
					}
					if (post.fileList[i].postFileCate == "img") {
						post_text += `<img id="img${post.fileList[i].postFileId}" 
							 src="/upload/${post.fileList[i].postFileNm}"
						 	 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
							 class="fileImg d-block w-100" 
							 onclick="fnImgChange(${post.fileList[i].postFileId})">`;
					} else {
						post_text += `<img id="img${post.fileList[i].postFileId}"
							 src="/assets/img/defaultFileImg.png"
							 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
							 class="fileImg d-block w-100" 
							 onclick="fnImgChange(${post.fileList[i].postFileId})">`;
					}
					post_text += `</div>`;
				}
			}
			post_text += `</div>`;
			post_text += `<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide="prev">
						    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
						    <span class="visually-hidden">Previous</span>
							</button>`;
			post_text += `<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators${post.postId}" data-bs-slide="next">
							  <span class="carousel-control-next-icon" aria-hidden="true"></span>
							  <span class="visually-hidden">Next</span>
						  </button>`;
			post_text += `</div>`;
		}
		post_text += `</div>`;
		post_text += `</div>`;
		post_text += `<div class="uploadFileSpace" data-post-id="${post.postId}">
						<input type="file" id="updateBtnAtt${post.postId}"
							class="updateBtnAtt" data-post-id="${post.postId}" name="uploadFiles"
							multiple="multiple">
						</div>`;
		post_text += `<div class="changedFileSpace">
						<input type="file" id="changedFiles${post.postId}"
							name="changedFiles" value="" multiple="multiple">
						</div>`;
		post_text += `<div id="postAttZone${post.postId}"
						data-placeholder="파일을 첨부하려면 파일선택 버튼을 누르세요."></div>`;
		post_text += `<div style="margin-bottom: 5px; display: none;">
				<label id="hashTag_label1">&emsp;# <input type="text" id=hashTag1
						name="hashTag1" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label2" style="display: none;">&emsp;# <input type="text"
						id=hashTag2 name="hashTag2" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label3" style="display: none;">&emsp;# <input type="text"
						id=hashTag3 name="hashTag3" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label4" style="display: none;">&emsp;# <input type="text"
						id=hashTag4 name="hashTag4" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
				<label id="hashTag_label5" style="display: none;">&emsp;# <input type="text"
						id=hashTag5 name="hashTag5" class="hashTag_text" placeholder="태그 입력"
						onfocus="this.placeholder=''"
						onblur="this.placeholder='태그 입력'"></label>
			</div>`;
		post_text += `<input type="hidden" name="postId" value="${post.postId}">`;
		post_text += `<input type="hidden" name="originFiles" id="originFiles${post.postId}">`;
		post_text += `<input type="hidden" id="userId" name="userId" value="${post.userId}">`;
		post_text += `<input type="hidden" id="postContentIn${post.postId}" name="postContent" value="${post.postContent}">`;
		post_text += `<input type="hidden" name="postDate" value="${post.postDate}"></form>`;
	
		post_text += `<div class="activity">`
		post_text += `<div id="postContent${post.postId}">${content_format}</div>`
		if (loginUserId == post.userId) {
			post_text += `<div id="modify_content${post.postId}">
								<textarea id="contentIn${post.postId}" class="form-control" style="width: 100%; resize: none;"
									spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)"
								>${post.postContent}</textarea>
								<button type="button" id="fileRequest${post.postId}" value="${post.postId}"
									style="background:none; border:none;">
									<i class="ri-image-2-fill" style="font-size: 20px; color:#000069;"></i>
								</button>
							</div>`;
			post_text += `<form id="delete_form${post.postId}" action="/post/deletePost" method="post">
								<input type="hidden" id="postId" name="postId" value="${post.postId}">
								<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">
								<input type="hidden" id="postDate" name="postDate"
									value="${post.postDate}">
								<input type="hidden" name="fileSize" id="fileSize"
									value="${post.fileLength}">
							</form>`
		}
		post_text += `</div>`
		//<!--해시태그-->	
		post_text += `<div class="activity">`
			if(post.hashTag1 != ""){
				post_text += `<br><a href="/search/search?searchKeyword=${post.hashTag1}" style="color: blue;">&emsp;#<span>${post.hashTag1}</span></a>`
			}
			if(post.hashTag2 != ""){
				post_text += `<a href="/search/search?searchKeyword=${post.hashTag2}" style="color: blue;">&emsp;#<span>${post.hashTag2}</span></a>`
			}
			if(post.hashTag3 != ""){
				post_text += `<a href="/search/search?searchKeyword=${post.hashTag3}" style="color: blue;">&emsp;#<span>${post.hashTag3}</span></a>`
			}
			if(post.hashTag4 != ""){
				post_text += `<a href="/search/search?searchKeyword=${post.hashTag4}" style="color: blue;">&emsp;#<span>${post.hashTag4}</span></a>`
			}
			if(post.hashTag5 != ""){
				post_text += `<a href="/search/search?searchKeyword=${post.hashTag5}" style="color: blue;">&emsp;#<span>${post.hashTag5}</span></a>`
			}
		post_text += `</div>`
		//<!--좋아요 댓글 지도-->	
		post_text += `<div class="activity">`;
		if (post.postLike == "Y")
			post_text += `<i class="ri-heart-3-line post_like" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:red; cursor: pointer;"></i>`
		else if (post.postLike == "N")
			post_text += `<i class="ri-heart-3-line post_like" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		post_text += `<i class="ri-message-3-line msg_icon" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		//식당 관련 내용을 적용하는 버튼	
		if (post.restaurant == "Y")
			post_text += `<i class="ri-map-pin-2-line map_icon" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		post_text += `<br>`
		post_text += `<a>좋아요 <span id="likeCnt${post.postId}">${post.likeCnt}</span>개</a>`;
		post_text += `</div>`
		/*
		//내부 서버로 옮기는 데이터를 모음. 추후에 이미지도 다룸. 
		post_text += `<form class="data" action="/post/deletePost" method="post" id="delete_form${post.postId}">`
		post_text += `<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">`
		post_text += `<input type="hidden" id="postContentIn" name="postContent" value="${post.postContent}">`
		post_text += `<input type="hidden" id="userId" name="userId" value="''+${post.userId}">`
		post_text += `<input type="hidden" id="postId" name="postId" value="''+${post.postId}">`
		post_text += `<input type="hidden" id="postDate" name="postDate" value="''+${post.postDate}">`
		post_text += `</form>`
		*/
		//<!-- 친구 식사 했는지 확인 필드 -->
		if (post.resCnt != 0) {
			post_text += `<div class="activity" style="text-align: center;"><hr>
				<a class="eatClick" id="${post.resName}"><span th:text="${post.userNick}"></span>님의 친구 <span>${post.resCnt}</span>명이
					<span>${post.resName}</span> 에서 식사하셨어요!</a></div>`;
		}
		post_text += `</div></div></div></div></div>`;
	}
	
	$("#post_div").html(post_text);
	
	//이벤트 리스너 함수 호출
	for (let i = 0; i < obj.numberOfElements; i++) {
		for (let j = 0; j < obj.content[i].fileLength; j++) {
			postFileIdList.push(obj.content[i].fileList[j].postFileId);
			const originFileObj = {
				postId: obj.content[i].postId,
				postFileId: obj.content[i].fileList[j].postFileId,
				postFileNm: $("#postFileNm" + obj.content[i].fileList[j].postFileId).val(),
				//업로드 파일 경로가 각각 다를때는 boardFilePath 속성도 추가
				//파일 상태값(수정되거나 삭제된 파일은 변경) - 파일의 상태 값을 표시함.
				postFileStatus: "N"
			};
			originFiles.push(originFileObj);
		}
		flagList.push(false);//게시글의 확장으로 처리할 정보 증가.
		$.updateBtnAtt(obj.content[i].postId);
		//$.updateBtn(elements, obj.numberOfElements, obj.content[i].postId);
		//$("#modify_content" + obj.content[i].postId).hide();
	}
	//이벤트를 줄때 모든 태그에 똑같이 이벤트를 다같이 줘야함. ajax로 태그를 새로 씌워서 이벤트가 없어지므로
	//선택자를 잡아 모든 태그에 이벤트를 따로 주도록 바꿈.
	for (let i = 0; i < $(".uploadFileSpace").length; i++) {
		updateBtnEvent(i, $(".uploadFileSpace").length, $($(".uploadFileSpace")[i]).attr("data-post-id"));
		$($(".uploadFileSpace")[i]).hide();
		$($(".changedFileSpace")[i]).hide();
		$("#modify_content" + $($(".uploadFileSpace")[i]).attr("data-post-id")).hide();
	}
	$.post_delete()
	$.like_button(loginUserId);
	$.comment_button();
	$.map_button();
	$.eatClick();
	
	page_num += 1;
	
}
/*
<div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
  <div class="carousel-indicators">
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
  </div>
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="..." class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="..." class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="..." class="d-block w-100" alt="...">
    </div>
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div>
 */
//포스트 삭제
$.post_delete = function(){
	//포스트 삭제
	$(".post_deleteButton").click(function(e) {
		const id = e.target.value;
		console.log("삭제버튼 클릭" + id);
		$("#delete_form" + e.target.value).submit();
	});
}


//eatclick---식사한 사람 띄워주는 모달 로직
$.eatClick = function(){
	$(".eatClick").click(function (e) {
		e.preventDefault();
	
		resName = e.target.id;
		console.log("타겟 아이디" + resName);
		$.ajax({
			url: '/user/eatFriend',
			type: 'get',
			data: {resName: resName},
			success: function (obj) {
				console.log(obj)
				htmlStr = "";
				for(let i = 0;i<obj.items.length;i++){
					htmlStr += `
						<div class="modal-dialog modal-dialog-centered modal-sm">
							<div class="modal-content" id="modal-content">
								<div class="modal-body" style="overflow-auto;">
									<div style="max-height: 30em;">
										<div style="width: 100%; height: 70px; text-align:center;">
											<a href="/social/otherUser?userId=${obj.items[i].userId}">
												<div style="width: 25%; margin-top: 10px; float: left;">
													<img class="img-fluid rounded-circle"
												 		src="/upload/${obj.items[i].profileimg.userProfileNm}" 
												 		style="width: 50px; height:50px;">
												</div>
												<div style="width: 65%; margin-top: 13px; float: left; font-weight: bold; color: black;">
													${obj.items[i].userNick}
												</div>
												<div style="width: 65%; color: #aaaaaa; font-size: x-small; float:left;">
			                                 		${obj.items[i].email}
			                                	</div>
											</a>
										</div>
									</div>
								</div>
							</div>
						</div>
			`
				$("#eatModal").html(htmlStr)
			}
			$("#eatModal").modal("show");
			},
			error: function (e) {
				console.log("실패실패")
				console.log(e);
			}
		});
	})
}



