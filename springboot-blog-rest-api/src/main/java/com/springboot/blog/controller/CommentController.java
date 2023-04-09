package com.springboot.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}

	// create comment
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") Long postId,
			@Valid @RequestBody CommentDto commentDto) {

		return new ResponseEntity<CommentDto>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
	}

	// get all comment by post id
	@GetMapping("/posts/{postId}/comments")
	public List<CommentDto> getCommentBypostId(@PathVariable(value = "postId") Long postId) {
		return commentService.getCommentByPostId(postId);
	}

	@GetMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
			@PathVariable(value = "id") Long commentId) {
		return new ResponseEntity<CommentDto>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
	}

	// update comment
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") long postId,
			@PathVariable(value = "commentId") long commentId, @Valid @RequestBody CommentDto commentDto) {
		CommentDto updateComment = commentService.updateComment(postId, commentId, commentDto);
		return new ResponseEntity<CommentDto>(updateComment, HttpStatus.OK);
	}

	// delete comment
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
			@PathVariable(value = "commentId") long commentId) {
		commentService.deleteComment(postId, commentId);
		return new ResponseEntity<String>(" Comment Deleted Successfully", HttpStatus.OK);

	}
}
