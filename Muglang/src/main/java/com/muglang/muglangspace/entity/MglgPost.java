package com.muglang.muglangspace.entity;



import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_POST")
@SequenceGenerator(
		name="MglgPostSequenceGenerator",
		sequenceName="T_MGLG_POST_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
public class MglgPost {
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="MglgPostSequenceGenerator"
	)
	private int postId;				//게시물 번호
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private MglgUser mglgUser;		//사용자 구분을 위한 아이디
	private int postRating;			//게시물 별점
	private String postContent;		//게시물 내용
	private String restNm;			//식당 이름
	private int restRating;			//식당 별점
	private String hashTag1;		//해시태그1
	private String hashTag2;		//해시태그2
	private String hashTag3;		//해시태그3
	private String hashTag4;		//해시태그4
	private String hashTag5;		//해시태그5
//	@Column
//	@ColumnDefault(LocalDateTime.now())
	private LocalDateTime postDate;	//게시한 날짜
	@Transient
	private Duration betweenDate;	//작성글 시간 계산을 위한 저장변수
	//테이블에 컬럼으로 구성할 필요가 없는 필드에 Transient Annotation 선언.
	@Transient
	private String searchKeyword;
	@Transient
	private int likeCnt;
	@Transient
	private int postLike;
}
