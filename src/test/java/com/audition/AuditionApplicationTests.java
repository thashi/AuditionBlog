package com.audition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.Comment;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuditionApplicationTests {

    @Mock
    private transient RestTemplate restTemplate;

    @InjectMocks
    private transient AuditionIntegrationClient auditionIntegrationClient;

    private transient String baseUrl = "https://jsonplaceholder.typicode.com/";
    private transient String postUrl = baseUrl + "posts/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPostsSuccess() {
        final AuditionPost[] posts = {
            new AuditionPost()
        };
        final String url = baseUrl + "posts";
        when(restTemplate.getForObject(url, AuditionPost[].class)).thenReturn(posts);

        final List<AuditionPost> result = auditionIntegrationClient.getPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restTemplate, times(1)).getForObject(url, AuditionPost[].class);
    }

    @Test
    void testGetPostsNotFound() {
        final String url = baseUrl + "posts";
        when(restTemplate.getForObject(url, AuditionPost[].class)).thenThrow(
            new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPosts();
        });

        assertEquals("Cannot find any Posts", exception.getMessage());
    }

    @Test
    void testGetPostByIdSuccess() {
        final String id = "1";
        final String url = postUrl + id;
        final AuditionPost auditionPost = new AuditionPost(1, 1,
            "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");
        when(restTemplate.getForObject(url, AuditionPost.class)).thenReturn(auditionPost);

        final AuditionPost result = auditionIntegrationClient.getPostById(id);
        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(url, AuditionPost.class);
    }

    @Test
    void testGetPostByIdNotFound() {
        final String id = "1";
        final String url = postUrl + id;
        when(restTemplate.getForObject(url, AuditionPost.class))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById(id);
        });

        assertEquals("Cannot find a Post with id 1", exception.getMessage());
    }

    @Test
    void testGetPostWithCommentsByIdSuccess() {
        final String postId = "1";
        final String pUrl = postUrl + postId;
        final String commentUrl = postUrl + postId + "/comments";
        final AuditionPost post = new AuditionPost();
        final Comment[] comments = {
            new Comment()
        };

        when(restTemplate.getForObject(pUrl, AuditionPost.class)).thenReturn(post);
        when(restTemplate.getForObject(commentUrl, Comment[].class)).thenReturn(comments);

        final AuditionPostComment result = auditionIntegrationClient.getPostWithCommentsById(postId);

        assertNotNull(result);
        assertNotNull(result.getPost());
        assertNotNull(result.getComments());
        verify(restTemplate, times(1)).getForObject(pUrl, AuditionPost.class);
        verify(restTemplate, times(1)).getForObject(commentUrl, Comment[].class);
    }

    @Test
    void testGetPostWithCommentsByIdNotFound() {
        final String postId = "1";
        final String url = postUrl + postId + "/comments";
        lenient().when(restTemplate.getForObject(url, Comment[].class))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostWithCommentsById(postId);
        });

        assertEquals("Cannot find a Post with id 1", exception.getMessage());
    }

    @Test
    void testGetCommentsSuccess() {
        final String postId = "1";
        final String url = baseUrl + "comments?postId=" + postId;
        final Comment[] comments = {
            new Comment()
        };
        when(restTemplate.getForObject(url, Comment[].class)).thenReturn(comments);
        final List<Comment> result = auditionIntegrationClient.getComments(postId);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restTemplate, times(1)).getForObject(url, Comment[].class);
    }

    @Test
    void testGetCommentsNotFound() {
        final String postId = "1";
        final String url = baseUrl + "comments?postId=" + postId;
        when(restTemplate.getForObject(url, Comment[].class))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getComments(postId);
        });

        assertEquals("Cannot find any Comments", exception.getMessage());
    }

}
