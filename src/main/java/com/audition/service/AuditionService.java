package com.audition.service;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.Comment;
import java.util.List;

public interface AuditionService {

    List<AuditionPost> getPosts();

    AuditionPost getPostById(String postId);

    AuditionPostComment getPostWithCommentsById(String postId);

    List<Comment> getComments(String postId);
}
