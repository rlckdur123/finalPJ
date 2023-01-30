package com.muglang.muglangspace.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MglgRestaurantId implements Serializable{
	private int mglgPost;
	private String resName;
}
