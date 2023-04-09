package com.springboot.blog.controller;



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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;

import jakarta.validation.Valid;


	@RestController
	@RequestMapping("/api/posts")
	public class PostController {
	       
		private PostService postService;

		public PostController(PostService postService) {
			super();
			this.postService = postService;
		}
		
		// create post method
		@PreAuthorize("hasRole('ADMIN')")
		@PostMapping
		public ResponseEntity<PostDto>createPost(@Valid @RequestBody PostDto postDto){
			
		return new ResponseEntity<PostDto>(postService.createPost(postDto), HttpStatus.CREATED);
		}
		
		//create get all method
		  @GetMapping
		  public PostResponse getAllPost(
				  @RequestParam(value="pageNumber", defaultValue=AppConstants.DEFAULT_PAGE_NUMBER, required=false) int pageNumber,
				  @RequestParam(value="pageSize", defaultValue=AppConstants.DEFAULT_PAGE_SIZE, required=false) int pageSize,
				  @RequestParam(value="sortBy", defaultValue=AppConstants.DEFAULT_SORT_BY, required=false) String sortBy,
				  @RequestParam(value="sortDir",defaultValue=AppConstants.DEFAULT_SORT_DIRECTION, required= false) String sortDir
				  ){
			  return postService.getAllPosts(pageNumber, pageSize, sortBy , sortDir);
		  }
		  
		  // Create get by id method
		  @GetMapping("/{id}")
		  public ResponseEntity<PostDto>getPostById(@PathVariable(name="id") long id){
			  
			  return ResponseEntity.ok(postService.getPostById(id));
		  }
		  
		  // update post
		  @PreAuthorize("hasRole('ADMIN')")
		  @PutMapping("{id}")
		  public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(value="id") long id){
		  
		  PostDto postResponse=postService.updatePost(postDto, id);
		  return new ResponseEntity<PostDto>(postResponse,HttpStatus.OK);
	}
		  // delete post api
		  @PreAuthorize("hasRole('ADMIN')")
		  @DeleteMapping("{id}")
		  public ResponseEntity<String>deletePost(@PathVariable(value="id")long id){
			  postService.deletePost(id);
			  return new ResponseEntity<String>("Post Entity Deleted Successfully", HttpStatus.ACCEPTED);
		  }
	}
	



