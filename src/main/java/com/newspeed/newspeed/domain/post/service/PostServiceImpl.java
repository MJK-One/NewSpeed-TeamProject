package com.newspeed.newspeed.domain.post.service;

import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.dto.response.PostResponseDto;
import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.User;
import com.newspeed.newspeed.domain.post.repository.PostRepository;

import com.newspeed.newspeed.domain.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    /**
     * @param dto
     * @param userId
     * @return
     */
    @Override
    public PostResponseDto createPost(PostRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = Post.createPost(user, dto.getContent(), dto.getImage());

        Post saved = postRepository.save(post);
        return PostResponseDto.from(saved);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findByPostId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
                //post not found 에러 추가해야됨
        return PostResponseDto.from(post);
    }

    /**
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<PostResponseDto> getNewsFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postRepository.findNewsFeed(pageable);
        List<PostResponseDto> dtos = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            dtos.add(PostResponseDto.from(post));
        }
        return dtos;
    }


    /**
     * @param id
     * @param dto
     * @param userId
     * @return
     */
    @Override
    public PostResponseDto updatePost(Long id, PostRequestDto dto, Long userId) {
        Post post = postRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없거나 수정 권한이 없습니다."));
        post.update(dto.getContent(), dto.getImage());
        return PostResponseDto.from(post);
    }

    /**
     * @param id
     * @param userId
     */
    @Override
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없거나 삭제 권한이 없습니다."));
        postRepository.delete(post);
    }
}
