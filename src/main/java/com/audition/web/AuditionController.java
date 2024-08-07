package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuditionController {

    @Autowired
    private transient AuditionService auditionService;

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(
        @RequestParam(value = "userId", required = false) final Integer userId) {
        List<AuditionPost> posts = auditionService.getPosts();
        if (userId != null) {
            posts = posts.stream().filter(p -> p.getUserId() == userId)
                .collect(Collectors.toList());
        }
        return posts;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPosts(@PathVariable("id") final String postId) {
        int id;

        // Input validation: Check if postId is a valid integer
        try {
            id = Integer.parseInt(postId);
            if (id <= 0) { // Check if id is positive
                throw new NumberFormatException("Id must be a positive Integer");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Invalid Post ID format. Must be a positive integer.", e);
        }

        final AuditionPost auditionPosts = auditionService.getPostById(postId);
        if (auditionPosts == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not Found");
        }

        return auditionPosts;
    }

    @GetMapping("/posts/{id}/comments")
    public AuditionPostComment getPostWithComments(@PathVariable("id") final String postId) {
        int id;

        // Input validation: Check if postId is a valid integer
        try {
            id = Integer.parseInt(postId);
            if (id <= 0) { // Check if id is positive
                throw new NumberFormatException("Id must be a positive Integer");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Invalid Post ID format. Must be a positive integer.", e);
        }

        final AuditionPostComment postComment = auditionService.getPostWithCommentsById(postId);
        if (postComment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not Found");
        }

        return postComment;
    }

    @GetMapping("/comments")
    public List<Comment> getComments(@RequestParam(value = "postId", required = true) final String postId) {
        int id;

        // Input validation: Check if postId is a valid integer
        try {
            id = Integer.parseInt(postId);
            if (id <= 0) { // Check if id is positive
                throw new NumberFormatException("Id must be a positive Integer");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Invalid Post ID format. Must be a positive integer.", e);
        }

        final List<Comment> comments = auditionService.getComments(postId);
        if (comments == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comments not Found");
        }

        return comments;
    }
}
