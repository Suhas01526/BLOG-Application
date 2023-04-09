package com.springboot.blog.service;

import java.util.List;

import com.springboot.blog.payload.CommentDto;

public interface CommentService {

	// 1. create comment
	CommentDto createComment(Long postId, CommentDto commentDto);
	
	//2. get comments by post id
	List<CommentDto> getCommentByPostId(Long postId);
	
	// 3. get comment by id
	CommentDto getCommentById(Long postId, Long commentId);
	
	// 4. update comment
	CommentDto updateComment( Long postId,Long commentId,CommentDto commentDto);
	
	// 5. delete comment
	void deleteComment(Long postId, Long commentId);
	
}
