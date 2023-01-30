package com.muglang.muglangspace.service.mglgcomment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.muglang.muglangspace.common.CamelHashMap;
import com.muglang.muglangspace.entity.MglgComment;
import com.muglang.muglangspace.entity.MglgCommentId;
import com.muglang.muglangspace.repository.MglgCommentRepository;
import com.muglang.muglangspace.service.mglgcomment.MglgCommentService;

@Service
public class MglgCommentServiceImpl implements MglgCommentService{
		@Autowired
		private MglgCommentRepository mglgCommentRepository;

		@Override
		public MglgComment getComment(MglgComment comment) {
			// TODO Auto-generated method stub
			System.out.println(comment.getCommentId());
			MglgCommentId commentIds = new MglgCommentId();
			commentIds.setCommentId(comment.getCommentId());
			commentIds.setMglgPost(comment.getMglgPost().getPostId());
			return mglgCommentRepository.findById(commentIds).get();
		}

		@Override
		public Page<MglgComment> getPageCommentList(Pageable pageable, int postId) {
			// TODO Auto-generated method stub
			return mglgCommentRepository.getCommentList(pageable, postId);
		}
		
		@Override
		public Page<CamelHashMap> getPageCommentCamelList(Pageable pageable, int postId) {
			// TODO Auto-generated method stub
			return mglgCommentRepository.getCommentCamelList(pageable, postId);
		}
		
		@Override
		public void deleteComment(int commentId,int postId) {
			mglgCommentRepository.deleteComment(commentId,postId);
		}
		
		@Override
		public void updateComment(int commentId, int postId, String commentContent) {
			// TODO Auto-generated method stub
			mglgCommentRepository.updateComment(commentId, postId, commentContent);
		}

		@Override
		public void insertComment(int userId, int postId, String commentContent) {
			mglgCommentRepository.insertComment(userId, postId, commentContent);
			
		}

		@Override
		public String reportComment(int postId, int commentId,int postUserId, int userId) {
			if(postUserId == userId) {
				return "self";
			}
			//동일 신고가 존재하면 리턴시킴
			if(0 != mglgCommentRepository.reportCommentCheck(postId,commentId,userId)) {
				return "fail";
			}
			
			
			//신고하는 로직
			mglgCommentRepository.reportComment(postId,commentId,postUserId,userId);
			return "success";

		}

		@Override
		public void insertComment(MglgComment comment) {
			// TODO Auto-generated method stub
			mglgCommentRepository.save(comment);
			
			mglgCommentRepository.flush();
		}


//		@Override
//		public void insertComment(MglgComment comment) {
//			// TODO Auto-generated method stub
//			mglgCommentRepository.save(comment);
//		}

//		@Override
//		public void updateComment(MglgComment comment) {
//			// TODO Auto-generated method stub
//			mglgCommentRepository.updateComment(comment);
//		}


}
