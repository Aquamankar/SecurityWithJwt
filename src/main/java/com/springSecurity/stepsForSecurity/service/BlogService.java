package com.springSecurity.stepsForSecurity.service;

import com.springSecurity.stepsForSecurity.payload.BlogDTO;

import java.util.List;

public interface BlogService {
    BlogDTO createBlog(BlogDTO blogDTO);
    BlogDTO getBlogById(Long id);
    List<BlogDTO> getAllBlogs();
    BlogDTO updateBlog(Long id, BlogDTO blogDTO);
    void deleteBlog(Long id);
}