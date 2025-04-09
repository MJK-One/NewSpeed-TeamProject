package com.newspeed.newspeed.domain.post.service;


import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.dto.response.PostResponseDto;
import com.newspeed.newspeed.domain.post.entity.Post;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto dto, Long userId);
    PostResponseDto getPost(Long id);
    List<PostResponseDto> getNewsFeed(int page, int size);
    PostResponseDto updatePost(Long id,PostRequestDto dto, Long userId);
    void deletePost(Long id, Long userId);
}
