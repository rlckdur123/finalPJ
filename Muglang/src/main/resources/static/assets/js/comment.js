
//게시글에서 댓글버튼 이벤트 리스너
$.comment_button = function() {

	text_comment = "";
	$(".msg_icon").click(function(e) {
		//여기도 댓글 아이콘 아이디를 포스트 id로 잡았음
		//let comment_page_num = 0;
		postId = Number(e.target.id);
		console.log("대상 댓글 포스트 번호: " + postId);
		$.comment_list(postId);
		//확장 스크롤은 처음 이벤트를 생성하여 설정할 때 결정.
		/*
		comment_page_num = 1;
		replyScroll(thisCommentTotalPages, thisCommentTotalElements, postId);
		*/
	});

}

//댓글 리스트 불러오는 함수
$.comment_list = function(postId) {
	$.ajax({
		url: '/comment/commentList',
		type: 'get',
		data: {
			postId: postId,
		},
		success: function(obj) {
			text_comment += `<div class="modal-header">`
			text_comment += `<textarea id="insert_text${postId}" style="width:80%; height: 30px; border:none; outline: none; overflow:hidden; resize: none;" spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)" placeholder="댓글을 작성해주세요"></textarea>`
			text_comment += `<button class="btn" id="insert_btn${postId}">작성</button></div>`
			text_comment += `<div class="modal-body overflow-auto" id="modal-body">`
			text_comment += `<div id="comment${postId}" class="comments" style="max-height: 30em;">`
			//받아온 객체의 내용을(댓글) 숫자만큼 반복
			for (let comment of obj.content) {
				let comment_date = new Date(comment.commentDate);
				//날짜 데이터 밀리초
				comment_date = now - comment_date;

				text_comment += `<div class="comment_info" style="margin-bottom: 30px;  position:relative;">`
				text_comment += `<div class="filter" style="margin-top: 15px; position: absolute; top:-12px; right:10px;">`
				//댓글에 시간 표시하는 로직.
				if (comment_date / (1000 * 60 * 60 * 24 * 30 * 12) > 1) {
					text_comment += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60 * 24 * 30 * 12))}년 전</a>`;
				}
				else if (comment_date / (1000 * 60 * 60 * 24 * 30) > 1) {
					text_comment += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60 * 24 * 30))}달 전</a>`;
				}
				else if (comment_date / (1000 * 60 * 60 * 24) > 1) {
					text_comment += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60 * 24))}일 전</a>`;
				}
				else if (comment_date / (1000 * 60 * 60) > 1) {
					text_comment += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60))}시간 전</a>`;
				}
				else {
					text_comment += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60))}분전</a>`;
				}
				text_comment += `<a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a>`;
				text_comment += `<ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">`;
				if (loginUserId == comment.userId) {	//현재 로그인했던 유저와 해당 댓글의 id를 비교
					text_comment += `<li><a class="dropdown-item comment_update" id="${comment.commentId}"></i>&emsp;수정</a></li>`
					text_comment += `<li><a class="dropdown-item comment_delete" id="${comment.commentId}" href="#"></i>&emsp;삭제</a></li>`
				} else {

					text_comment += `				<form action="/comment/reportComment" method="post">
													<li>
														<input type="hidden" name="postId" value="${postId}">
														<input type="hidden" name="postUserId" value="${comment.userId}">
														<input type="hidden" name="commentId" value="${comment.commentId}">
														<input type="submit" class="dropdown-item" value="코멘트 신고하기">
													</li>
													</form>`
				}
				text_comment += `</ul>`
				text_comment += `</div>`
				//프사 넣는부분
				text_comment += `<img class="img-fluid rounded-circle" src="/upload/${comment.profile.userProfileNm}" style="width: 40px;">`
				text_comment += `<span style="margin-left: 10px;"><a href="#" style="color: black; cursor:pointer;">${comment.mglgUser.userName}</a></span>`
				text_comment += `<div style="word-break: keep-all;">${comment.commentContent}`
				text_comment += `<br><textarea id="update_text${comment.commentId}" style="width:100%; overflow:hidden; resize: none; display:none;" spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)">${comment.commentContent}</textarea>`
				text_comment += `<button class="btn cancle_comment" id="cancle_comment${comment.commentId}" style="float:right; display:none;">취소</button>`
				text_comment += `<button class="btn update_comment" id="update_comment${comment.commentId}" style="float:right; display:none;">수정</button></div></div>`
			}
			text_comment += `</div></div>`;
			$("#comment-content").html(text_comment);

			//댓글 페이징을 위한 페이징 넘버를 최초 댓글 열때 저장.
			thisCommentTotalPages = Number(obj.totalPages);
			thisCommentTotalElements = Number(obj.totalElements);
			console.log(thisCommentTotalPages);
			console.log(thisCommentTotalElements);
			//댓글 무한 스크롤 발생 이벤트 시작
			
			$('#msgModal').modal("show");
			//이벤트 넣기
			$.comment_insert();
			$.comment_update();
			$.comment_delete();
			
			comment_page_num = 1;
			//스크롤을 하는 조건의 높이
			/*$('.modal-body').height() - */
			//comHeight = 0;
			comHeight = 224//$('.modal-body').innerHeight() - ($('.comment_info').height() * 4);	//200씩 뜸
			console.log(comHeight);
			$('.modal-body').scroll(function(e) {
				if(!commentScrollYn) {
					//modal-body = 480, comment_info = 256
					//스크롤 발생 필요한 높이 224
					//댓글 창을 끝까지 내릴 때쯤 이벤트가 발생.
					console.log($('.modal-body').scrollTop());
					if ($('.modal-body').scrollTop() > comHeight) {
						commentScrollYn = true;
						comHeight += $('.modal-body').height() + ($('.comment_info').height() * 4);
						console.log("모달 확장 스크롤 안쪽 게시글 내용 크기 : " + comHeight);
						replyScroll(thisCommentTotalPages, thisCommentTotalElements, postId);
						console.log("모달 확장 스크롤 더 내리기 : " + comment_page_num);
						setTimeout(function(){
							commentScrollYn = false;
						}, 500);
					}

				}
			});
		},
		error: function(e) {
			console.log("에러에러");
		}
	});
}

//댓글 작성 함수
$.comment_insert = function() {
	let text_comment = "";
	$("#insert_btn" + postId).click(function(e) {
		console.log($("#insert_text" + postId).val())
		$.ajax({
			url: '/comment/insertComment',
			type: 'post',
			data: {
				userId: loginUserId,
				postId: postId,
				commentContent: $("#insert_text" + postId).val()
			},
			success: function() {
				$.comment_list(postId);
			},
			error: function(e) {
				console.log("에러에러");
			}
		});
	});
}

//댓글 삭제 함수
$.comment_delete = function() {
	$(".comment_delete").click(function(e) {
		let commentId = e.target.id;
		let text_comment = "";

		$.ajax({
			url: '/comment/deleteComment',
			type: 'post',
			data: {
				commentId: commentId,
				postId: postId
			},
			success: function() {
				$.comment_list(postId, comment_page_num);
			},
			error: function(e) {
				console.log("에러에러");
			}
		});
	});
}

//댓글 수정 함수
$.comment_update = function() {
	text_comment = "";
	$(".comment_update").click(function(e) {
		commentId = e.target.id;

		$("#update_text" + commentId).css("display", "inline-block");
		$("#update_comment" + commentId).css("display", "inline-block");
		$("#cancle_comment" + commentId).css("display", "inline-block");
	});

	$(".update_comment").click(function(e) {
		commentContent = $("#update_text" + commentId).val();
		$.ajax({
			url: '/comment/updateComment',
			type: 'put',
			data: {
				commentId: commentId,
				postId: postId,
				commentContent: commentContent
			},
			success: function() {
				$.comment_list(postId);
			},
			error: function(e) {
				console.log("에러에러");
			}
		});
	})

	$(".cancle_comment").click(function(e) {
		$("#update_text" + commentId).css("display", "none");
		$("#update_comment" + commentId).css("display", "none");
		$("#cancle_comment" + commentId).css("display", "none");
	})
}



//댓글 부분 무한 스크롤 - 부분
//아이디comment 태그의 스크롤에 다차서 내려가면, 다시 ajax비동기 처리로 댓글 목록 불러옴.
function replyScroll(thisCommentTotalPages, thisCommentTotalElements, postId) {
	let comment_text_scroll = "";
	/*
	$('.modal-body').scroll(function(e) {
		if(!commentScrollYn) {
			//let comHeight = $('.modal-body').height() - $('.comment_info').height() * 4;
			//console.log("안쪽 게시글 내용 크기 : " + comHeight);
			//댓글 창을 끝까지 내릴 때쯤 이벤트가 발생.
			if ($('.modal-body').scrollTop() > $('.modal-body').height() - $('.comment_info').height() * 4) {
				commentScrollYn = true;*/
				//해당 댓글의 댓글 페이지수를 확인. 내용이 남아있을경우 추가 스크롤 활성화
				if (comment_page_num < thisCommentTotalPages) {
					$.ajax({
						url: '/comment/commentList',
						type: 'post',
						data: {
							page_num: comment_page_num,
							postId: postId
						},
						success: function(obj) {
							//숨김처리후 다시 재출력
							//$('#msgModal').modal("hide");
							//한번 더 스크롤나오면 다음페이지를 데이터로 넘기고, 다시 스크롤을 늘려 다음 댓글을 갱신하여 표시.
							//comment_page_num++;
	
							for (let comment of obj.content) {
								let comment_date = new Date(comment.commentDate);
								//console.log(comment);
								//날짜 데이터 밀리초
								comment_date = now - comment_date;
	
								//console.log(comment_date);
	
								comment_text_scroll += `<div class="comment_info" style="margin-bottom: 30px; position:relative;">`;
	
								comment_text_scroll += `<div class="filter" style="margin-top: 15px; position: absolute; top:-12px; right:10px;">`;
								//댓글에 시간 표시하는 로직.
								if (comment_date / (1000 * 60 * 60 * 24 * 30 * 12) > 1) {
									comment_text_scroll += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60 * 24 * 30 * 12))}년 전</a>`;
								}
								else if (comment_date / (1000 * 60 * 60 * 24 * 30) > 1) {
									comment_text_scroll += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60 * 24 * 30))}달 전</a>`;
								}
								else if (comment_date / (1000 * 60 * 60 * 24) > 1) {
									comment_text_scroll += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60 * 24))}일 전</a>`;
								}
								else if (comment_date / (1000 * 60 * 60) > 1) {
									comment_text_scroll += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60 * 60))}시간 전</a>`;
								}
								else {
									comment_text_scroll += `<a style="margin-right: 20px;">${parseInt(comment_date / (1000 * 60))}분전</a>`;
								}
								comment_text_scroll += `<a class="icon" href="#" data-bs-toggle="dropdown"><i class="bi bi-three-dots"></i></a>`;
								comment_text_scroll += `<ul class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">`;
								if (loginUserId == comment.mglgUser.userId) {	//현재 로그인했던 유저와 해당 댓글의 id를 비교
									comment_text_scroll += `<li><a class="dropdown-item comment_update" id="${comment.commentId}"></i>&emsp;수정</a></li>`
									comment_text_scroll += `<li><a class="dropdown-item comment_delete" id="${comment.commentId}" href="#"></i>&emsp;삭제</a></li>`
								} else {
					
									comment_text_scroll += `				<form action="/comment/reportComment" method="post">
																	<li>
																		<input type="hidden" name="postId" value="${postId}">
																		<input type="hidden" name="postUserId" value="${comment.mglgUser.userId}">
																		<input type="hidden" name="commentId" value="${comment.commentId}">
																		<input type="submit" class="dropdown-item" value="코멘트 신고하기">
																	</li>
																	</form>`
								}
								comment_text_scroll += `</ul>`
								comment_text_scroll += `</div>`
								comment_text_scroll += `<img class="img-fluid rounded-circle" src="/upload/${comment.profile.userProfileNm}" style="width: 40px;">`;
								comment_text_scroll += `<span style="margin-left: 10px;"><a href="#" style="color: black; cursor:pointer;">${comment.mglgUser.userName}</a></span>`;
								comment_text_scroll += `<div style="word-break: keep-all;">${comment.commentContent}`;
								comment_text_scroll += `<br><textarea id="update_text${comment.commentId}" style="width:100%; overflow:hidden; resize: none; display:none;" spellcheck="false" onkeydown="resize(this)" onkeyup="resize(this)">${comment.commentContent}</textarea>`;
								comment_text_scroll += `<button class="btn cancle_comment" id="cancle_comment${comment.commentId}" style="float:right; display:none;">취소</button>`;
								comment_text_scroll += `<button class="btn update_comment" id="update_comment${comment.commentId}" style="float:right; display:none;">수정</button></div></div>`;
	
							}
							//댓글을 화면에 뿌리는 상위 태그는 id가 comment인 div태그 따라서 그 자식 태그에 append한다.
							//$("#comment_scroll_div").html(comment_text_scroll);
							$("#comment" + postId).append(comment_text_scroll);
							//$('#msgModal').modal("show");
							//이벤트 리스너 함수 호출
							//이벤트 넣기
							$.comment_insert();
							$.comment_update();
							$.comment_delete();

							comment_page_num += 1;
							
							commentScrollYn = false;
						},
						error: function(e) {
							console.log("댓글 무한 로딩 에러");
							console.log(e);
						}
					});
				}
			//}
						
	//}
	//});
}