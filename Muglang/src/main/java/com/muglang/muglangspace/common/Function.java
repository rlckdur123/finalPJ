package com.muglang.muglangspace.common;

import java.util.List;

import com.muglang.muglangspace.dto.MglgPostDTO;
import com.muglang.muglangspace.entity.MglgPost;

public interface Function<T, R> {
	public R entityToDTO(T entity); 
}
