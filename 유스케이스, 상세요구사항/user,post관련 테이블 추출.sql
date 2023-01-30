use social2;

select * from t_mglg_post;

desc t_mglg_post;

insert into t_mglg_post(
post_id, 
user_id, 
post_content,  
rest_nm, 
post_rating, 
view_count,
post_date,
rest_rating)
values ((select ifnull(max(a.post_id), 0) + 1 from t_mglg_post a)
, 1, '작성된 글의 내용들입니다.', '놀부부대찌개', 2, 0, now(), 3);

use social2;

select * from t_mglg_post;

desc t_mglg_post;
-- 게시글 내용을 간략하게 테스트 하기위한 쿼리
insert into t_mglg_post(
post_id, 
user_id, 
post_content,  
rest_nm, 
post_rating, 
view_count,
view_count,
post_date,
rest_rating)
values ((select ifnull(max(a.post_id), 0) + 1 from t_mglg_post a)
, 1, '작성된 글의 내용들입니다. 두번째 작성 내용입니다.', '주다방', 3, 0, now(), 4);

desc t_mglg_user;

select * from t_mglg_user;

insert into t_mglg_user(
user_id,
user_name, 
password, 
first_name, 
last_name, 
phone,
email,
address,
bio,
regdate,
user_role,
user_nick)
values (
	(select ifnull(max(a.user_id), 0) + 1 from t_mglg_user a),
    '정재웅', '1234', '재웅', '정',
    '010-1234-1233', 'asdf1234@naver.com',
    '서울시 서초구', 'bio', now(), 'ADMIN', '2조 조장'
);

select * from t_mglg_user_seq;

update t_mglg_user_seq set next_val = 2;

delete from t_mglg_user where user_id=1;

DELETE FROM T_MGLG_POST WHERE POST_ID = 0;
