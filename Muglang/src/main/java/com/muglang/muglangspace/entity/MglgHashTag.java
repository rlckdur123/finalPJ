package com.muglang.muglangspace.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name="T_MGLG_HASH_TAG")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
public class MglgHashTag {
	@Id
	private String hashNm;
	private int hashCnt;


}
