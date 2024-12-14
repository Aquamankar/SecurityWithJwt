package com.springSecurity.stepsForSecurity.service.impl;


import com.springSecurity.stepsForSecurity.entity.Blog;
import com.springSecurity.stepsForSecurity.entity.Comment;
import com.springSecurity.stepsForSecurity.payload.BlogDTO;
import com.springSecurity.stepsForSecurity.repositoty.BlogRepository;
import com.springSecurity.stepsForSecurity.repositoty.CommentRepository;
import com.springSecurity.stepsForSecurity.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public BlogDTO createBlog(BlogDTO blogDTO) {
        Blog blog = mapToEntity(blogDTO);
      blog.setCreatedAt(LocalDateTime.now());

        Blog savedBlog = blogRepository.save(blog);
        return mapToDTO(savedBlog);
    }

    @Override
    public BlogDTO getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        return mapToDTO(blog);
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO blogDTO) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        blog.setName(blogDTO.getName());
        blog.setDescription(blogDTO.getDescription());
        blog.setContent(blogDTO.getContent());
        blog.setAuthorName(blogDTO.getAuthorName());
        blog.setPublished(blogDTO.getPublished());
        blog.setTags(blogDTO.getTags());
        blog.setUpdatedAt(LocalDateTime.now());
        Blog updatedBlog = blogRepository.save(blog);
        return mapToDTO(updatedBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        blogRepository.delete(blog);
    }

    private BlogDTO mapToDTO(Blog blog) {

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setName(blog.getName());
        blogDTO.setContent(blog.getContent());
        blogDTO.setDescription(blog.getDescription());
        blogDTO.setCreatedAt(blog.getCreatedAt());
        blogDTO.setAuthorName(blog.getAuthorName());
        blogDTO.setUpdatedAt(blog.getUpdatedAt());
        blogDTO.setCategories(blog.getCategories());

        blogDTO.setPublished(blog.getPublished());
        blogDTO.setViews(blog.getViews());
        blogDTO.setTags(blog.getTags());

// Map comment IDs from the list of comments
        List<Long> commentIds = blog.getComments().stream()
                .map(Comment::getId) // Use method reference for cleaner code
                .collect(Collectors.toList());

        blogDTO.setCommentsIds(commentIds); // Set the mapped IDs
        return blogDTO;

    }

    private Blog mapToEntity(BlogDTO blogDTO) {
        Blog blog = new Blog();
        blog.setName(blogDTO.getName());
        blog.setDescription(blogDTO.getDescription());
        blog.setContent(blogDTO.getContent());
        blog.setAuthorName(blogDTO.getAuthorName());
        blog.setPublished(blogDTO.getPublished());
        blog.setTags(blogDTO.getTags());
        blog.setViews(blogDTO.getViews());
        blog.setCategories(blogDTO.getCategories());

        // Populate comments if IDs are present
        List<Long> commentsIds = blogDTO.getCommentsIds();
        if (commentsIds != null && !commentsIds.isEmpty()) {
            List<Comment> comments = commentsIds.stream()
                    .map(commentId -> commentRepository.findById(commentId)
                            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId)))
                    .collect(Collectors.toList());
            blog.setComments(comments);
        } else {
            blog.setComments(Collections.emptyList()); // Set an empty list if no comments are provided
        }

        return blog;
    }
}