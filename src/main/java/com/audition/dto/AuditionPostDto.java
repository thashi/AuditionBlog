package com.audition.dto;

import com.audition.model.Comment;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditionPostDto {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<Comment> comments;
}
