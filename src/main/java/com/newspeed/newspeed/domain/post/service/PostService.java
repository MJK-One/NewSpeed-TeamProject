package com.newspeed.newspeed.domain.post.service;


import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.dto.response.PostResponseDto;
import com.newspeed.newspeed.domain.post.entity.Post;

import java.time.LocalDate;
import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto dto, Long userId);
    PostResponseDto getPost(Long id);
    List<PostResponseDto> getNewsFeed(int page, int size, String sort,
                                      LocalDate startDate, LocalDate endDate);
    PostResponseDto updatePost(Long id,PostRequestDto dto, Long userId);
    void deletePost(Long id, Long userId);

    //좋아요 기능
    void toggleLike(Long postId, Long userId);
}
