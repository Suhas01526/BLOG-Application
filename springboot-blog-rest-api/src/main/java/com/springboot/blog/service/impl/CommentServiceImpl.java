package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper modelMapper;

	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
		super();
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.modelMapper=modelMapper;
	}

	@Override
	public CommentDto createComment(Long postId, CommentDto commentDto) {
		// create comment object
		Comment comment = mapToEntity(commentDto);
		// find post by using postid
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
		// set post to comment object
		comment.setPost(post);
		// save comment object in DB
		Comment newComment = commentRepository.save(comment);

		return mapToDto(newComment);
	}

	@Override
	public List<CommentDto> getCommentByPostId(Long postId) {

		// retrive comment by postid
		List<Comment> comments = commentRepository.findByPostId(postId);

		// convert list comment into list of comment dto
		return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
	}

	@Override
	public CommentDto getCommentById(Long postId, Long commentId) {
		// retrieve post by using id
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
		// retrieve comment by using comment id
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		// check the given comment id is assign to post or no
		if (!(comment.getPost().getId() == post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "comment does not belongs to post");

		}
		return null;
	}

	@Override
	public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
		// 1. find post by using id
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		// 2. find comment by using comment id
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "commentId", commentId));

		// 3. check given comment id is assign to post or not
		if (!(comment.getPost().getId() == post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, " comment does not belong to post");
		}

		// updating value

		comment.setName(commentDto.getName());
		comment.setEmail(commentDto.getEmail());
		comment.setBody(commentDto.getBody());

		// save updated post to DB

		Comment updatedComment = commentRepository.save(comment);
		return mapToDto(updatedComment);

	}

	@Override
	public void deleteComment(Long postId, Long commentId) {

		// 1. find post by using id
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		// 2. find comment by using comment id
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "commentId", commentId));

		// 3. check given comment id is assign to post or not
		if (!(comment.getPost().getId() == post.getId())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, " comment does not belong to post");
		}

		//delete comment by using repo delete method
		
		commentRepository.delete(comment);
	}

	// create comment to commentDto
	private CommentDto mapToDto(Comment comment) {
		CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
//		commentDto.setId(comment.getId());
//		commentDto.setName(comment.getName());
//		commentDto.setEmail(comment.getEmail());
//		commentDto.setBody(comment.getBody());
		return commentDto;

	}

	// commentDto to comment
	private Comment mapToEntity(CommentDto commentDto) {
		Comment comment = modelMapper.map(commentDto, Comment.class);
//		comment.setId(commentDto.getId());
//		comment.setName(commentDto.getName());
//		comment.setEmail(commentDto.getEmail());
//		comment.setBody(commentDto.getBody());
		return comment;
	}

}
