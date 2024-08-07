package com.audition.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;

    public AuditionPost() {

    }

    public AuditionPost(final int userId, final int id, final String title, final String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }
}