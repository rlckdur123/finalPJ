package com.muglang.muglangspace.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MGLG_RESTAURANT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Data
@IdClass(MglgRestaurantId.class)
public class MglgRestaurant {
	@Id
	@OneToOne
	@JoinColumn(name="POST_ID")
	private MglgPost mglgPost;	
	@Id
	@Column
	@ColumnDefault("''")
	private String resName;
	
	private String resAddress;			
	private String resRoadAddress;
	private String resPhone;
	private String resCategory;
}
