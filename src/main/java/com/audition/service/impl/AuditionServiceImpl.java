package com.audition.service.impl;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditionServiceImpl implements AuditionService {

    @Autowired
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @Override
    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    @Override
    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    @Override
    public AuditionPostComment getPostWithCommentsById(final String postId) {
        return auditionIntegrationClient.getPostWithCommentsById(postId);
    }

    @Override
    public List<Comment> getComments(final String postId) {
        return auditionIntegrationClient.getComments(postId);
    }
}
