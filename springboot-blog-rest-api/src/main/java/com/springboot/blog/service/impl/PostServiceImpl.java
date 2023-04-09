package com.springboot.blog.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
@Service
public class PostServiceImpl implements PostService {

	private PostRepository postRepository;
	
	private ModelMapper modelMapper;
	
	
	public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
		super();
		this.postRepository = postRepository;
		this.modelMapper=modelMapper;
	}


	@Override
	public PostDto createPost(PostDto postDto) {
		
		// create post class object
		Post post=mapToEntity(postDto);
		/*
		//convert PostDto to Post
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		*/
		//save Post to DB
		Post newPost=postRepository.save(post);
		
		//create PostDto class object
		
		PostDto postResponse=mapToDto(newPost);
		/*
		//convert Post to PostDto
		postResponse.setId(newPost.getId());
		postResponse.setTitle(newPost.getTitle());
		postResponse.setDescription(newPost.getDescription());
		postResponse.setContent(newPost.getContent());
		*/
		//return the PostDto object
		return postResponse;
		
	}


	@Override
	public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort=null;
		
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=sort.by(sortBy).ascending();

		}
		else if(sortDir.equalsIgnoreCase("desc")) {
			sort=sort.by(sortBy).descending();
		}
		else {
			sort=sort.by(sortBy).ascending();
		}
		//create pageable object
		Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
		// pass pageable object in findAll
	    Page<Post> page=postRepository.findAll(pageable);
		//transfer data  from page<post> to list<post>
		List<Post>listOfPosts=page.getContent();
		
		List<PostDto> content=listOfPosts.stream().map(post->mapToDto(post)).collect(Collectors.toList());
		
		PostResponse postResponse= new PostResponse();
		
		postResponse.setContent(content);
		postResponse.setPageNo(page.getNumber());
		postResponse.setPageSize(page.getSize());
		postResponse.setTotalElements(page.getTotalElements());
		postResponse.setTotalPages(page.getTotalPages());
		postResponse.setLast(page.isLast());
		return postResponse;
	}
 
	// converting entity to Dto
	private PostDto mapToDto(Post post) {
		
		// creating PostDto object
          PostDto postDto=modelMapper.map(post, PostDto.class);
          
//        //convert fields  
//          postDto.setId(post.getId());
//          postDto.setTitle(post.getTitle());
//          postDto.setDescription(post.getDescription());
//		  postDto.setContent(post.getContent());
//		
		  // return PostDto object
		  return postDto;
}
	//converting Dto to entity
	private Post mapToEntity(PostDto postDto) {
		
		// create post class object
				Post post=modelMapper.map(postDto, Post.class);
				
//				//convert PostDto to Post
//				post.setTitle(postDto.getTitle());
//				post.setDescription(postDto.getDescription());
//				post.setContent(postDto.getContent());
//		
		return post;
	}


	@Override
	public PostDto getPostById(long id) {
		Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
		
		
		
		return mapToDto(post);
	}


	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		// find the post by using id
		Post post=postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
		// update value of variable using getter and setter method
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		// save updated post to database
		Post updatedPost=postRepository.save(post);
		
		return mapToDto(updatedPost) ;
	}


	@Override
	public void deletePost(long id) {
		// find the post by using id
		Post post=postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));

		//delete post from database
		postRepository.delete(post);
	}
	
}