package com.springSecurity.stepsForSecurity.controller;

import com.springSecurity.stepsForSecurity.payload.BlogDTO;
import com.springSecurity.stepsForSecurity.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
private BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogDTO> createBlog(@RequestBody BlogDTO blogDTO){

        BlogDTO blog = blogService.createBlog(blogDTO);
        return  new ResponseEntity<>(blog, HttpStatus.CREATED);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<BlogDTO> getBlog(@PathVariable long id){

        BlogDTO blog = blogService.getBlogById(id);
        return  new ResponseEntity<>(blog, HttpStatus.CREATED);
    }



}
