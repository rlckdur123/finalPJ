package com.muglang.muglangspace.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="T_MGLG_POST_FILE")
@SequenceGenerator(
		name="MglgPostFileSequenceGenerator",
		sequenceName="T_MGLG_POST_FILE_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@IdClass(MglgPostFileId.class)
public class MglgPostFile {
	@Id
	@ManyToOne
	@JoinColumn(name="POST_ID")
	private MglgPost mglgPost;
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="MglgPostFileSequenceGenerator"
	)
	private int postFileId;
	private String postFileNm;
	private String postFileOriginNm;
	private String postFilePath;
	private LocalDateTime postFileRegdate = LocalDateTime.now();
	private String postFileCate;
	@Transient
	private String postFileStatus;
	@Transient
	private String newFileNm;
	

	
}
