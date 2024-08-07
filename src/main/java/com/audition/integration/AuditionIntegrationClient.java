package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.Comment;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuditionIntegrationClient {


    @Autowired
    AuditionLogger logger;

    @Autowired
    private transient RestTemplate restTemplate;

    //    @Value("${audition.base.url}")
    private transient String baseUrl = "https://jsonplaceholder.typicode.com/";

    private transient String unknownError = "Unknown Error: ";

    public List<AuditionPost> getPosts() {
        final String url = baseUrl + "posts";

        try {
            // Make a GET request to fetch the posts
            final AuditionPost[] postArray = restTemplate.getForObject(url, AuditionPost[].class);

            return Arrays.asList(postArray);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find any Posts", "Resource Not Fount", 404, e);
            } else {
                final String errorMessage = "An error occured while fetching the posts " + e.getMessage();
                throw new SystemException(unknownError + errorMessage, e);
            }
        }
    }

    public AuditionPost getPostById(final String id) {
        final String url = baseUrl + "posts/" + id;
        try {
            return restTemplate.getForObject(url, AuditionPost.class);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found",
                    404, e);
            } else {
                final String errorMessage =
                    "An error occured while fetching the post with id " + id + ": " + e.getMessage();
                throw new SystemException(unknownError + errorMessage, e);
            }
        }
    }

    public AuditionPostComment getPostWithCommentsById(final String postId) {
        final String url = baseUrl + "posts/" + postId + "/comments";
        try {
            final AuditionPostComment postComment = new AuditionPostComment();
            // use getPostById method
            final AuditionPost post = getPostById(postId);

            final Comment[] comments = restTemplate.getForObject(url, Comment[].class);
            if (post != null && comments != null) {
                postComment.setPost(post);
                postComment.setComments(Arrays.asList(comments));
            }

            return postComment;
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + postId, "Resource Not Found", 404, e);
            } else {
                final String errorMessage =
                    "An error occurred while fetching the post with id " + postId + ": " + e.getMessage();
                throw new SystemException(unknownError + errorMessage, e);
            }
        }
    }

    public List<Comment> getComments(final String postId) {
        final String url = baseUrl + "comments?postId=" + postId;
        try {
            final Comment[] comments = restTemplate.getForObject(url, Comment[].class);

            return Arrays.asList(comments);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find any Comments", "Resource Not Fount", 404, e);
            } else {
                final String errorMessage = "An error occured while fetching the comments " + e.getMessage();
                throw new SystemException(unknownError + errorMessage, e);
            }
        }
    }
    // The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.
}
