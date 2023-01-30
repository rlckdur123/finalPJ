use social2;

use studydb;

select * from t_board_file;

select * from t_mglg_post_file where post_id=94;

select * from t_mglg_post order by POST_ID desc;


desc t_mglg_post;

select
        mglgpost0_.post_id as post_id1_4_,
        mglgpost0_.between_date as between_2_4_,
        mglgpost0_.hash_tag1 as hash_tag3_4_,
        mglgpost0_.hash_tag2 as hash_tag4_4_,
        mglgpost0_.hash_tag3 as hash_tag5_4_,
        mglgpost0_.hash_tag4 as hash_tag6_4_,
        mglgpost0_.hash_tag5 as hash_tag7_4_,
        mglgpost0_.user_id as user_id13_4_,
        mglgpost0_.post_content as post_con8_4_,
        mglgpost0_.post_date as post_dat9_4_,
        mglgpost0_.post_rating as post_ra10_4_,
        mglgpost0_.rest_nm as rest_nm11_4_,
        mglgpost0_.rest_rating as rest_ra12_4_ 
    from
        t_mglg_post mglgpost0_ 
        order by post_id desc
        limit 5
        ;


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
, 2, '최고의 맛을 보여준거 같습니다.', '애슐리 강남점', 4, 0, now(), 4);



use social2;

select * from t_mglg_post;

select * from t_mglg_post_file where post_id = 98;

desc t_mglg_post;
-- 게시글 내용을 간략하게 테스트 하기위한 쿼리
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
, 4, '나만이 아는 맛집 공유해요.', '공복돈까스', 3, 0, now(), 4);

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
reg_date,
user_role,
user_nick,
user_sns_id)
values (
	(select ifnull(max(a.user_id), 0) + 1 from t_mglg_user a),
    '강동윤', '12345234', '동윤', '강',
    '010-1258-1323', 'asdf1238@naver.com',
    '경기도 성남시 수정구', 'bios', now(), 'USER', 'DOGI', 'kakao_2323654729'
);

select * from t_mglg_user_seq;

select * from t_mglg_post_seq;

select * from t_mglg_user_profile;

SELECT @@AUTOCOMMIT;

commit;

select * from t_mglg_user_relation;
insert into t_mglg_user_relation value(4, 2, now());
insert into t_mglg_user_relation value(2, 3, now());

update t_mglg_user_seq set next_val = 2;

update t_mglg_post_seq set next_val = 3;

delete from t_mglg_user where user_id=1;

DELETE FROM T_MGLG_POST WHERE POST_ID = 0;

select * from t_mglg_comment where post_id= 2;

select * from t_mglg_post_file where post_id=94;

SELECT A.* FROM T_MGLG_USER A
 WHERE A.USER_NAME = searchKeyword AND
 A.USER_ID IN (SELECT FOLLOWER_ID FROM t_mglg_user_relation B WHERE B.USER_ID = userId);
 
 
 SELECT * FROM T_MGLG_POST P 
             INNER JOIN T_MGLG_USER U
              ON P.USER_ID = U.USER_ID 
            WHERE P.POST_CONTENT LIKE CONCAT('%', '', '%')
            ORDER BY P.POST_DATE DESC;
            

 