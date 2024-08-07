package com.audition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuditionControllerTests {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void testGetPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetPostsWithParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?userId=1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetPostsWithId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetPostWithComments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/1/comments"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetComments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comments?postId=1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}