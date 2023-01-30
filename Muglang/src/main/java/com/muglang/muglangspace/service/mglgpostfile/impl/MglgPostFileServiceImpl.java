package com.muglang.muglangspace.service.mglgpostfile.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.entity.MglgPost;
import com.muglang.muglangspace.entity.MglgPostFile;
import com.muglang.muglangspace.entity.MglgPostFileId;
import com.muglang.muglangspace.repository.MglgPostFileRepository;
import com.muglang.muglangspace.service.mglgpostfile.MglgPostFileService;

@Service
public class MglgPostFileServiceImpl implements MglgPostFileService{
	@Autowired
	private MglgPostFileRepository mglgPostFileRepository;
	//해당 게시글의 모든 파일들을 불러오는 메소드
	@Override
	public List<MglgPostFile> getPostFileList(int postId) {
		// TODO Auto-generated method stub
		return mglgPostFileRepository.findAllByMglgPost(postId);
	}
	
	//해당 게시글의 파일 1개를 로드하는 메소드.
	@Override
	public MglgPostFile getPostFile(MglgPost mglgPost, MglgPostFile mglgPostFile) {
		MglgPostFileId targetId = new MglgPostFileId(mglgPost.getPostId(), mglgPostFile.getPostFileId());
		Optional<MglgPostFile> targetFile = mglgPostFileRepository.findById(targetId);
		// TODO Auto-generated method stub
		return targetFile.get();
	}

	//해당 게시글의 파일 1개씩 DB에 저장하여 입력한다. controller 에서 반복문을 통해 여러개의 파일을 입력하게됨.
	@Override
	public void insertPostFile(MglgPostFile fileList) {
		// TODO Auto-generated method stub
		mglgPostFileRepository.save(fileList);
		
		mglgPostFileRepository.flush();
	}

	//해당 게시글의 파일 정보를 수정한다. 수정 작업을 한 최종 결과물을 가공하여 최종적으로 DB에 반영하는 과정이다.
	@Override
	public void updatePostFileList(List<MglgPostFile> uploadFileList) {
		// TODO Auto-generated method stub
		for(int i = 0; i < uploadFileList.size(); i++) {
			if(uploadFileList.get(i).getPostFileStatus().equals("U")) {
				mglgPostFileRepository.save(uploadFileList.get(i));
			} else if(uploadFileList.get(i).getPostFileStatus().equals("D")) {
				mglgPostFileRepository.delete(uploadFileList.get(i));
			} else if(uploadFileList.get(i).getPostFileStatus().equals("I")) {
				//추가한 파일들은 postId를 갖고있지만, postFileId는 없는 상태로 새로 추가할 때는 이것을 1증가하면서 늘린다.
				
				mglgPostFileRepository.save(uploadFileList.get(i));
			}
		}
		
		mglgPostFileRepository.flush();
	}
	//포스트의 아이디에 해당하는 모든 파일리스트를 찾아서 지운다.
	@Override
	public void deletePostFileList(int postId) {
		// TODO Auto-generated method stub
		mglgPostFileRepository.deleteAllByPostId(postId);
		
		mglgPostFileRepository.flush();
	}

}
