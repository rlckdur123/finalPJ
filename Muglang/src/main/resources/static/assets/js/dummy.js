$.get_post_2 = function(obj){
	console.log(obj);
	totalPages_2 = obj.totalPages_2;
	let elements = page_num_2 * obj.pageable.pageSize;
	
	for (let post of obj.content) {
		let post_date = new Date(post.postDate);
		//날짜 데이터 밀리초
		post_date = now - post_date;
	
		let content_format = post.postContent;
		content_format = content_format.replaceAll("&lt;", "<");
		content_format = content_format.replaceAll("&gt;", ">");
		
		post_text_2 += `<div class="col-12 post">`;
		post_text_2 += `<input type="hidden" value="${post.betweenDate}">`;
		post_text_2 += `<input type="hidden" id="fileList${post.postId}" value="${post.fileLength}">`;
		post_text_2 += `<div class="card recent-sales">`
		post_text_2 += `<div class="card-body">`
		post_text_2 += `<div class="filter" style="margin-top: 15px;">`
		if (post_date / (1000 * 60 * 60 * 24 * 30 * 12) > 1) {
			post_text_2 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30 * 12))}년 전</a>`
		}
		else if (post_date / (1000 * 60 * 60 * 24 * 30) > 1) {
			post_text_2 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30))}달 전</a>`
		}
		else if (post_date / (1000 * 60 * 60 * 24) > 1) {
			post_text_2 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24))}일 전</a>`
		}
		else if (post_date / (1000 * 60 * 60) > 1) {
			post_text_2 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60))}시간 전</a>`
		}
		else {
			post_text_2 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60))}분전</a>`
		}
		post_text_2 += `<a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a>`
		post_text_2 += `<ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">`
	
		if(loginUserId == post.userId){
			post_text_2 += `<button type="button" class="updateBtn dropdown-item" id="updateButtonToggle${post.postId}" value="${post.postId}" style="text-align:center">게시글 수정</button>`
			post_text_2 += `<button type="button" class="dropdown-item post_deleteButton" id="deleteButton${post.postId}" value="${post.postId}" style="text-align:center">글 삭제</button>`
		}
		else {
			post_text_2 += `<form action="/user/reportUser?userId=${post.userId}" method="post">`
			post_text_2 += `<input type="submit" class="dropdown-item" value="유저 신고하기" style="text-align:center"></form>`
			post_text_2 += `<form action="/post/reportPost?postId=${post.postId}" method="post">`
			post_text_2 += `<input type="submit" class="dropdown-item" value="포스트 신고하기" style="text-align:center"></form>`
		}
		post_text_2 += `</ul></div>`
		post_text_2 += `<div class="card-title">`;
		post_text_2 += `<img class="img-fluid rounded-circle" src="/upload/${post.profile.userProfileNm}"
						style="width: 40px;">
						<a href="/social/otherUser?userId=${post.userId}" class="card-title">${post.userNick}</a>`;
		post_text_2 += `</div>`;
		post_text_2 += `<form id="updateForm${post.postId}" enctype="multipart/form-data">`;
		//<!-- 게시글 사진 부분 -->
		//<!-- imgArea는 반복문을 사용해 2차원 배열 처럼 사용되어 파일의 내용을 표시하게됨. -->
		post_text_2 += `<div class="activity" style="margin-bottom: 10px;" id="restImgBox${post.postId}">`;
		post_text_2 += `<div id="imgArea${post.postId}">`;
		post_text_2 += `<div id="carouselExampleIndicators" class="carousel carousel-dark slide" data-bs-ride="carousel">`;
		post_text_2 += `<div class="carousel-indicators">`;
		for(let i = 0; i < post.fileLength; i++) {
			if(i < 1) {
				post_text_2 +=  `<button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="${i}" class="active" aria-current="true" aria-label="Slide ${i+1}"></button>`;
			} else {
				post_text_2 +=  `<button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="${i}" aria-label="Slide ${i+1}"></button>`;
			}
		}
		post_text_2 += `</div>`; 
		post_text_2 += `<div class="carousel-inner">`; 
		if (loginUserId == post.userId) {
			for (let i = 0; i < post.fileLength; i++) {
				if(i < 1) {
					post_text_2 += `<div class="carousel-item active" data-bs-interval="4000" value="${post.fileList[i].postFileId}">`;
				} else {
					post_text_2 += `<div class="carousel-item" data-bs-interval="4000" value="${post.fileList[i].postFileId}">`;
				}
				//<!--<input type="text" th:id="'postFileNm' + ${post.postId}" value="">
				//<input type="text" th:id="'postFileId' + ${post.postId}" value="">-->
				//post_text_2 += `<div class="fileList${post.postId}" style="position: relative;">`;
				post_text_2 += `<input type="hidden" id="postFileId${post.fileList[i].postFileId}"
								class="postFileId${post.fileList[i].postId}" name="postFileId"
								value="${post.fileList[i].postFileId}">`;
				post_text_2 += `<input type="hidden"id="postFileNm${post.fileList[i].postFileId}"
								class="postFileNm" name="postFileNm"
								value="${post.fileList[i].postFileNm}">`;
				post_text_2 += `<input type="hidden" id="postId${post.fileList[i].postFileId}"
								class="postId${post.fileList[i].postId}" name="postId"
								value="${post.fileList[i].postId}">`;
				if (post.fileList[i].postFileCate == "img") {
					post_text_2 += `<input type="file" id="changedFile${post.fileList[i].postFileId}"
								name="changedFile${post.fileList[i].postFileId}"
								style="display: none;"
								onchange="fnGetChangedFileInfo(${post.fileList[i].postFileId}, ${i}, event)">`;
					post_text_2 += `<img id="img${post.fileList[i].postFileId}"
									src="/upload/${post.fileList[i].postFileNm}"
									style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
									class="d-block w-100"
									onclick="fnImgChange(${post.fileList[i].postFileId})">`;
					post_text_2 += `<input type="button" class="btnDel" id="btnFileDel${post.postId}" value="x"
								data-del-file="${post.fileList[i].postFileId}" style="width: 30px; height: 30px; position: absolute; right: 0px; bottom: 0px; 
								z-index: 999; background-color: rgba(255, 255, 255, 0.1); color: #f00; display: none; 
								onclick="fnPostImgDel(event)">`;
	
				} else {
					post_text_2 += `<img id="img${post.fileList[i].postFileId}"
									src="/upload/${post.fileList[i].postFileNm}"
									style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
									class="d-block w-100"
									onclick="fnImgChange(${post.fileList[i].postFileId})">`;
				}
				post_text_2 += `<p id="fileNm${post.fileList[i].postFileId}"
								style="display: none; font-size: 8px; cursor: pointer;"
								onclick="fnFileDown(${post.fileList[i].postId}, ${post.fileList[i].postFileId})">
								${post.fileList[i].postFileOriginNm}</p>`;
				//post_text_2 += `</div>`;
				post_text_2 += `</div>`;
			}
		} else {
			for (let i = 0; i < post.fileLength; i++) {
				if(i < 1) {
					post_text_2 += `<div class="carousel-item active">`;
				} else {
					post_text_2 += `<div class="carousel-item">`;
				}
				if (post.fileList[i].postFileCate == "img") {
					post_text_2 += `<img id="img${post.fileList[i].postFileId}" 
						 src="/upload/${post.fileList[i].postFileNm}"
					 	 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
						 class="fileImg d-block w-100" 
						 onclick="fnImgChange(${post.fileList[i].postFileId})">`;
				} else {
					post_text_2 += `<img id="img${post.fileList[i].postFileId}"
						 src="/assets/img/defaultFileImg.png"
						 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
						 class="fileImg d-block w-100" 
						 onclick="fnImgChange(${post.fileList[i].postFileId})">`;
				}
				post_text_2 += `</div>`;
			}
		}
		post_text_2 += `</div>`;
		post_text_2 += `<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
					    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
					    <span class="visually-hidden">Previous</span>
						</button>`;
		post_text_2 += `<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
						  <span class="carousel-control-next-icon" aria-hidden="true"></span>
						  <span class="visually-hidden">Next</span>
					  </button>`;
		post_text_2 += `</div>`;
		
		post_text_2 += `</div>`;
		post_text_2 += `</div>`;
		post_text_2 += `<div class="uploadFileSpace" data-post-id="${post.postId}">
						<input type="file" id="updateBtnAtt${post.postId}"
							class="updateBtnAtt" data-post-id="${post.postId}" name="uploadFiles"
							multiple="multiple">
						</div>`;
		post_text_2 += `<div class="changedFileSpace">
						<input type="file" id="changedFiles${post.postId}"
							name="changedFiles" value="" multiple="multiple">
						</div>`;
		post_text_2 += `<div id="postAttZone${post.postId}"
						data-placeholder="파일을 첨부하려면 파일선택 버튼을 누르세요."></div>`;
		post_text_2 += `<input type="hidden" name="postId" value="${post.postId}">`;
		post_text_2 += `<input type="hidden" name="originFiles" id="originFiles${post.postId}">`;
		post_text_2 += `<input type="hidden" id="userId" name="userId" value="${post.userId}">`;
		post_text_2 += `<input type="hidden" id="postContentIn${post.postId}" name="postContent" value="${post.postContent}">`;
		post_text_2 += `<input type="hidden" name="postDate" value="${post.postDate}"></form>`;
	
		post_text_2 += `<div class="activity">`
		post_text_2 += `<div id="postContent${post.postId}">${content_format}</div>`
		if (loginUserId == post.userId) {
			post_text_2 += `<div id="modify_content${post.postId}">
								<textarea id="contentIn${post.postId}" class="form-control" style="width: 100%; resize: none;"
									spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)"
								>${post.postContent}</textarea>
								<button type="button" id="fileRequest${post.postId}" value="${post.postId}"
									style="background:none; border:none;">
									<i class="ri-image-2-fill" style="font-size: 20px; color:#000069;"></i>
								</button>
							</div>`;
			post_text_2 += `<form id="delete_form${post.postId}" action="/post/deletePost" method="post">
								<input type="hidden" id="postId" name="postId" value="${post.postId}">
								<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">
								<input type="hidden" id="postDate" name="postDate"
									value="${post.postDate}">
								<input type="hidden" name="fileSize" id="fileSize"
									value="${post.fileLength}">
							</form>`
		}
		post_text_2 += `</div>`
		//<!--해시태그-->	
		post_text_2 += `<div class="activity">`
			if(post.hashTag1 != ""){
				post_text_2 += `<br><a href="/search/search?searchKeyword=${post.hashTag1}" style="color: blue;">&emsp;#<span>${post.hashTag1}</span></a>`
			}
			if(post.hashTag2 != ""){
				post_text_2 += `<a href="/search/search?searchKeyword=${post.hashTag2}" style="color: blue;">&emsp;#<span>${post.hashTag2}</span></a>`
			}
			if(post.hashTag3 != ""){
				post_text_2 += `<a href="/search/search?searchKeyword=${post.hashTag3}" style="color: blue;">&emsp;#<span>${post.hashTag3}</span></a>`
			}
			if(post.hashTag4 != ""){
				post_text_2 += `<a href="/search/search?searchKeyword=${post.hashTag4}" style="color: blue;">&emsp;#<span>${post.hashTag4}</span></a>`
			}
			if(post.hashTag5 != ""){
				post_text_2 += `<a href="/search/search?searchKeyword=${post.hashTag5}" style="color: blue;">&emsp;#<span>${post.hashTag5}</span></a>`
			}
		post_text_2 += `</div>`
		//<!--좋아요 댓글 지도-->	
		post_text_2 += `<div class="activity">`;
		if (post.postLike == "Y")
			post_text_2 += `<i class="ri-heart-3-line post_like" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:red; cursor: pointer;"></i>`
		else if (post.postLike == "N")
			post_text_2 += `<i class="ri-heart-3-line post_like" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		post_text_2 += `<i class="ri-message-3-line msg_icon" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		//식당 관련 내용을 적용하는 버튼	
		if (post.restaurant == "Y")
			post_text_2 += `<i class="ri-map-pin-2-line map_icon" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		post_text_2 += `<br>`
		post_text_2 += `<a>좋아요 <span id="likeCnt${post.postId}">${post.likeCnt}</span>개</a>`;
		post_text_2 += `</div>`
		/*
		//내부 서버로 옮기는 데이터를 모음. 추후에 이미지도 다룸. 
		post_text_2 += `<form class="data" action="/post/deletePost" method="post" id="delete_form${post.postId}">`
		post_text_2 += `<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">`
		post_text_2 += `<input type="hidden" id="postContentIn" name="postContent" value="${post.postContent}">`
		post_text_2 += `<input type="hidden" id="userId" name="userId" value="''+${post.userId}">`
		post_text_2 += `<input type="hidden" id="postId" name="postId" value="''+${post.postId}">`
		post_text_2 += `<input type="hidden" id="postDate" name="postDate" value="''+${post.postDate}">`
		post_text_2 += `</form>`
		*/
		//<!-- 친구 식사 했는지 확인 필드 -->
		if (post.resCnt != 0) {
			post_text_2 += `<div class="activity" style="text-align: center;"><hr>
				<a class="eatClick" id="${post.resName}"><span th:text="${post.userNick}"></span>님의 친구 <span>${post.resCnt}</span>명이
					<span>${post.resName}</span> 에서 식사하셨어요!</a></div>`;
		}
		post_text_2 += `</div></div></div></div></div>`;
	}
	
	$("#post_div_2").html(post_text_2);
	
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
	
	page_num_2 += 1;
	
}


$.get_post_3 = function(obj){
	console.log(obj);
	totalPages_3 = obj.totalPages_3;
	let elements = page_num_3 * obj.pageable.pageSize;
	
	for (let post of obj.content) {
		let post_date = new Date(post.postDate);
		//날짜 데이터 밀리초
		post_date = now - post_date;
	
		let content_format = post.postContent;
		content_format = content_format.replaceAll("&lt;", "<");
		content_format = content_format.replaceAll("&gt;", ">");
		
		post_text_3 += `<div class="col-12 post">`;
		post_text_3 += `<input type="hidden" value="${post.betweenDate}">`;
		post_text_3 += `<input type="hidden" id="fileList${post.postId}" value="${post.fileLength}">`;
		post_text_3 += `<div class="card recent-sales">`
		post_text_3 += `<div class="card-body">`
		post_text_3 += `<div class="filter" style="margin-top: 15px;">`
		if (post_date / (1000 * 60 * 60 * 24 * 30 * 12) > 1) {
			post_text_3 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30 * 12))}년 전</a>`
		}
		else if (post_date / (1000 * 60 * 60 * 24 * 30) > 1) {
			post_text_3 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24 * 30))}달 전</a>`
		}
		else if (post_date / (1000 * 60 * 60 * 24) > 1) {
			post_text_3 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60 * 24))}일 전</a>`
		}
		else if (post_date / (1000 * 60 * 60) > 1) {
			post_text_3 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60 * 60))}시간 전</a>`
		}
		else {
			post_text_3 += `<a style="margin-right: 20px;">${parseInt(post_date / (1000 * 60))}분전</a>`
		}
		post_text_3 += `<a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a>`
		post_text_3 += `<ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">`
	
		if(loginUserId == post.userId){
			post_text_3 += `<button type="button" class="updateBtn dropdown-item" id="updateButtonToggle${post.postId}" value="${post.postId}" style="text-align:center">게시글 수정</button>`
			post_text_3 += `<button type="button" class="dropdown-item post_deleteButton" id="deleteButton${post.postId}" value="${post.postId}" style="text-align:center">글 삭제</button>`
		}
		else {
			post_text_3 += `<form action="/user/reportUser?userId=${post.userId}" method="post">`
			post_text_3 += `<input type="submit" class="dropdown-item" value="유저 신고하기" style="text-align:center"></form>`
			post_text_3 += `<form action="/post/reportPost?postId=${post.postId}" method="post">`
			post_text_3 += `<input type="submit" class="dropdown-item" value="포스트 신고하기" style="text-align:center"></form>`
		}
		post_text_3 += `</ul></div>`
		post_text_3 += `<div class="card-title">`;
		post_text_3 += `<img class="img-fluid rounded-circle" src="/upload/${post.profile.userProfileNm}"
						style="width: 40px;">
						<a href="/social/otherUser?userId=${post.userId}" class="card-title">${post.userNick}</a>`;
		post_text_3 += `</div>`;
		post_text_3 += `<form id="updateForm${post.postId}" enctype="multipart/form-data">`;
		//<!-- 게시글 사진 부분 -->
		//<!-- imgArea는 반복문을 사용해 2차원 배열 처럼 사용되어 파일의 내용을 표시하게됨. -->
		post_text_3 += `<div class="activity" style="margin-bottom: 10px;" id="restImgBox${post.postId}">`;
		post_text_3 += `<div id="imgArea${post.postId}">`;
		post_text_3 += `<div id="carouselExampleIndicators" class="carousel carousel-dark slide" data-bs-ride="carousel">`;
		post_text_3 += `<div class="carousel-indicators">`;
		for(let i = 0; i < post.fileLength; i++) {
			if(i < 1) {
				post_text_3 +=  `<button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="${i}" class="active" aria-current="true" aria-label="Slide ${i+1}"></button>`;
			} else {
				post_text_3 +=  `<button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="${i}" aria-label="Slide ${i+1}"></button>`;
			}
		}
		post_text_3 += `</div>`; 
		post_text_3 += `<div class="carousel-inner">`; 
		if (loginUserId == post.userId) {
			for (let i = 0; i < post.fileLength; i++) {
				if(i < 1) {
					post_text_3 += `<div class="carousel-item active" data-bs-interval="4000" value="${post.fileList[i].postFileId}">`;
				} else {
					post_text_3 += `<div class="carousel-item" data-bs-interval="4000" value="${post.fileList[i].postFileId}">`;
				}
				//<!--<input type="text" th:id="'postFileNm' + ${post.postId}" value="">
				//<input type="text" th:id="'postFileId' + ${post.postId}" value="">-->
				//post_text_3 += `<div class="fileList${post.postId}" style="position: relative;">`;
				post_text_3 += `<input type="hidden" id="postFileId${post.fileList[i].postFileId}"
								class="postFileId${post.fileList[i].postId}" name="postFileId"
								value="${post.fileList[i].postFileId}">`;
				post_text_3 += `<input type="hidden"id="postFileNm${post.fileList[i].postFileId}"
								class="postFileNm" name="postFileNm"
								value="${post.fileList[i].postFileNm}">`;
				post_text_3 += `<input type="hidden" id="postId${post.fileList[i].postFileId}"
								class="postId${post.fileList[i].postId}" name="postId"
								value="${post.fileList[i].postId}">`;
				if (post.fileList[i].postFileCate == "img") {
					post_text_3 += `<input type="file" id="changedFile${post.fileList[i].postFileId}"
								name="changedFile${post.fileList[i].postFileId}"
								style="display: none;"
								onchange="fnGetChangedFileInfo(${post.fileList[i].postFileId}, ${i}, event)">`;
					post_text_3 += `<img id="img${post.fileList[i].postFileId}"
									src="/upload/${post.fileList[i].postFileNm}"
									style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
									class="d-block w-100"
									onclick="fnImgChange(${post.fileList[i].postFileId})">`;
					post_text_3 += `<input type="button" class="btnDel" id="btnFileDel${post.postId}" value="x"
								data-del-file="${post.fileList[i].postFileId}" style="width: 30px; height: 30px; position: absolute; right: 0px; bottom: 0px; 
								z-index: 999; background-color: rgba(255, 255, 255, 0.1); color: #f00; display: none; 
								onclick="fnPostImgDel(event)">`;
	
				} else {
					post_text_3 += `<img id="img${post.fileList[i].postFileId}"
									src="/upload/${post.fileList[i].postFileNm}"
									style="width: 100%; height: 100%; z-index: none; cursor: pointer;"
									class="d-block w-100"
									onclick="fnImgChange(${post.fileList[i].postFileId})">`;
				}
				post_text_3 += `<p id="fileNm${post.fileList[i].postFileId}"
								style="display: none; font-size: 8px; cursor: pointer;"
								onclick="fnFileDown(${post.fileList[i].postId}, ${post.fileList[i].postFileId})">
								${post.fileList[i].postFileOriginNm}</p>`;
				//post_text_3 += `</div>`;
				post_text_3 += `</div>`;
			}
		} else {
			for (let i = 0; i < post.fileLength; i++) {
				if(i < 1) {
					post_text_3 += `<div class="carousel-item active">`;
				} else {
					post_text_3 += `<div class="carousel-item">`;
				}
				if (post.fileList[i].postFileCate == "img") {
					post_text_3 += `<img id="img${post.fileList[i].postFileId}" 
						 src="/upload/${post.fileList[i].postFileNm}"
					 	 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
						 class="fileImg d-block w-100" 
						 onclick="fnImgChange(${post.fileList[i].postFileId})">`;
				} else {
					post_text_3 += `<img id="img${post.fileList[i].postFileId}"
						 src="/assets/img/defaultFileImg.png"
						 style="width: 100%; height: 100%; z-index: none; cursor: pointer;" 
						 class="fileImg d-block w-100" 
						 onclick="fnImgChange(${post.fileList[i].postFileId})">`;
				}
				post_text_3 += `</div>`;
			}
		}
		post_text_3 += `</div>`;
		post_text_3 += `<button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev">
					    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
					    <span class="visually-hidden">Previous</span>
						</button>`;
		post_text_3 += `<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next">
						  <span class="carousel-control-next-icon" aria-hidden="true"></span>
						  <span class="visually-hidden">Next</span>
					  </button>`;
		post_text_3 += `</div>`;
		
		post_text_3 += `</div>`;
		post_text_3 += `</div>`;
		post_text_3 += `<div class="uploadFileSpace" data-post-id="${post.postId}">
						<input type="file" id="updateBtnAtt${post.postId}"
							class="updateBtnAtt" data-post-id="${post.postId}" name="uploadFiles"
							multiple="multiple">
						</div>`;
		post_text_3 += `<div class="changedFileSpace">
						<input type="file" id="changedFiles${post.postId}"
							name="changedFiles" value="" multiple="multiple">
						</div>`;
		post_text_3 += `<div id="postAttZone${post.postId}"
						data-placeholder="파일을 첨부하려면 파일선택 버튼을 누르세요."></div>`;
		post_text_3 += `<input type="hidden" name="postId" value="${post.postId}">`;
		post_text_3 += `<input type="hidden" name="originFiles" id="originFiles${post.postId}">`;
		post_text_3 += `<input type="hidden" id="userId" name="userId" value="${post.userId}">`;
		post_text_3 += `<input type="hidden" id="postContentIn${post.postId}" name="postContent" value="${post.postContent}">`;
		post_text_3 += `<input type="hidden" name="postDate" value="${post.postDate}"></form>`;
	
		post_text_3 += `<div class="activity">`
		post_text_3 += `<div id="postContent${post.postId}">${content_format}</div>`
		if (loginUserId == post.userId) {
			post_text_3 += `<div id="modify_content${post.postId}">
								<textarea id="contentIn${post.postId}" class="form-control" style="width: 100%; resize: none;"
									spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)"
								>${post.postContent}</textarea>
								<button type="button" id="fileRequest${post.postId}" value="${post.postId}"
									style="background:none; border:none;">
									<i class="ri-image-2-fill" style="font-size: 20px; color:#000069;"></i>
								</button>
							</div>`;
			post_text_3 += `<form id="delete_form${post.postId}" action="/post/deletePost" method="post">
								<input type="hidden" id="postId" name="postId" value="${post.postId}">
								<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">
								<input type="hidden" id="postDate" name="postDate"
									value="${post.postDate}">
								<input type="hidden" name="fileSize" id="fileSize"
									value="${post.fileLength}">
							</form>`
		}
		post_text_3 += `</div>`
		//<!--해시태그-->	
		post_text_3 += `<div class="activity">`
			if(post.hashTag1 != ""){
				post_text_3 += `<br><a href="/search/search?searchKeyword=${post.hashTag1}" style="color: blue;">&emsp;#<span>${post.hashTag1}</span></a>`
			}
			if(post.hashTag2 != ""){
				post_text_3 += `<a href="/search/search?searchKeyword=${post.hashTag2}" style="color: blue;">&emsp;#<span>${post.hashTag2}</span></a>`
			}
			if(post.hashTag3 != ""){
				post_text_3 += `<a href="/search/search?searchKeyword=${post.hashTag3}" style="color: blue;">&emsp;#<span>${post.hashTag3}</span></a>`
			}
			if(post.hashTag4 != ""){
				post_text_3 += `<a href="/search/search?searchKeyword=${post.hashTag4}" style="color: blue;">&emsp;#<span>${post.hashTag4}</span></a>`
			}
			if(post.hashTag5 != ""){
				post_text_3 += `<a href="/search/search?searchKeyword=${post.hashTag5}" style="color: blue;">&emsp;#<span>${post.hashTag5}</span></a>`
			}
		post_text_3 += `</div>`
		//<!--좋아요 댓글 지도-->	
		post_text_3 += `<div class="activity">`;
		if (post.postLike == "Y")
			post_text_3 += `<i class="ri-heart-3-line post_like" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:red; cursor: pointer;"></i>`
		else if (post.postLike == "N")
			post_text_3 += `<i class="ri-heart-3-line post_like" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		post_text_3 += `<i class="ri-message-3-line msg_icon" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		//식당 관련 내용을 적용하는 버튼	
		if (post.restaurant == "Y")
			post_text_3 += `<i class="ri-map-pin-2-line map_icon" id="${post.postId}" style="font-size: 30px; margin-right: 5px; color:black; cursor: pointer;"></i>`
		post_text_3 += `<br>`
		post_text_3 += `<a>좋아요 <span id="likeCnt${post.postId}">${post.likeCnt}</span>개</a>`;
		post_text_3 += `</div>`
		/*
		//내부 서버로 옮기는 데이터를 모음. 추후에 이미지도 다룸. 
		post_text_3 += `<form class="data" action="/post/deletePost" method="post" id="delete_form${post.postId}">`
		post_text_3 += `<input type="hidden" id="restNmIn" name="restNm" value="${post.restNm}">`
		post_text_3 += `<input type="hidden" id="postContentIn" name="postContent" value="${post.postContent}">`
		post_text_3 += `<input type="hidden" id="userId" name="userId" value="''+${post.userId}">`
		post_text_3 += `<input type="hidden" id="postId" name="postId" value="''+${post.postId}">`
		post_text_3 += `<input type="hidden" id="postDate" name="postDate" value="''+${post.postDate}">`
		post_text_3 += `</form>`
		*/
		//<!-- 친구 식사 했는지 확인 필드 -->
		if (post.resCnt != 0) {
			post_text_3 += `<div class="activity" style="text-align: center;"><hr>
				<a class="eatClick" id="${post.resName}"><span th:text="${post.userNick}"></span>님의 친구 <span>${post.resCnt}</span>명이
					<span>${post.resName}</span> 에서 식사하셨어요!</a></div>`;
		}
		post_text_3 += `</div></div></div></div></div>`;
	}
	
	$("#post_div_3").html(post_text_3);
	
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
	
	page_num_3 += 1;
	
}