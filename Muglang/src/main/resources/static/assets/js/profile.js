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
	$("#updateProfile").click(function(e) {
		e.preventDefault();
		$("#profileBtnAtt").click();
	});

	$("#defaultProfileImg").click(function(e) {
		e.preventDefault();
		if (confirm("프로필 이미지를 초기화하시겠습니까?")) {
			$.ajax({
				url: '/user/changeDefaultImg',
				type: 'get',
				success: function(obj) {
					console.log(obj);
					location.reload();
				},
				error: function(e) {
					console.log(e);
				}
			});
			alert("기본이미지로 변경했습니다.");
		} else {  // 취소 버튼을 눌렀을 때
			alert("취소 버튼을 누르셨습니다.");
		}
	});









$("#profileBtnAtt").on("change", function(e) {
	const profileFiles = e.target.files;
	profileArr = Array.prototype.slice.call(profileFiles)


	let img = document.createElement("img");
	img.setAttribute("style", "width: 100%; height: 100%; z-index: none;");

	profileArr.forEach(function(f) {
		var reader = new FileReader();
		reader.readAsDataURL(f)
		reader.onload = function(e) {

			if (f.name.toLowerCase().match(/(.*?)\.(jpg|jpeg|png|gif|svg|bmp)$/)) {
				//이미지 파일 미리보기 처리
				img.src = e.target.result;
			} else {
				//일반 파일 미리보기 처리
				alert("이미지 파일만 등록할 수 있습니다.")
				return;
			}

		}
		$("#defaultBox").remove();
		$("#profileImgBox").append(img);

	})

})


})

