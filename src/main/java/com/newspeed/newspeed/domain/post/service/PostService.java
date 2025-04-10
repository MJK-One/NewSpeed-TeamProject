package com.newspeed.newspeed.domain.post.service;

import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.base.NotFoundException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.post.dto.request.PostLikeRequestDto;
import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.dto.response.PostResponseDto;
import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.entity.PostLike;
import com.newspeed.newspeed.domain.post.entity.User;
import com.newspeed.newspeed.domain.post.repository.PostLikeRepository;
import com.newspeed.newspeed.domain.post.repository.PostRepository;
import com.newspeed.newspeed.domain.post.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public PostResponseDto createPost(PostRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Post post = dto.toEntity(user);
        Post saved = postRepository.save(post);
        return PostResponseDto.from(saved);
    }

    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findByPostId(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
        return PostResponseDto.from(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getNewsFeed(Pageable pageable, String sort, LocalDate startDate, LocalDate endDate) {
        Sort sorting = "like".equalsIgnoreCase(sort)
                ? Sort.by(Sort.Direction.DESC, "likeCount")
                : Sort.by(Sort.Direction.DESC, "updatedAt");

        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.plusDays(1).atStartOfDay() : null;

        log.info("정렬 방식: {}", sort);
        log.info("조회 기간: {} ~ {}", start, end);

        Page<Post> postPage = postRepository.getNewsFeed(start, end, sorted);

        List<PostResponseDto> dtos = new ArrayList<>();
        for (Post post : postPage.getContent()) {
            dtos.add(new PostResponseDto(post));
        }

        return dtos;
    }

    public PostResponseDto updatePost(Long id, PostRequestDto dto, Long userId) {
        Post post = postRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_POST_ACCESS));
        return PostResponseDto.from(post);
    }

    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new  CustomException(ErrorCode.UNAUTHORIZED_POST_ACCESS));
        postRepository.delete(post);
    }

    @Transactional
    public void toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.SELF_LIKE_NOT_ALLOWED);
        }

        Optional<PostLike> like = postLikeRepository.findByUserIdAndPostId(userId, postId);

        if (like.isPresent()) {
            postLikeRepository.delete(like.get());
            post.decrementLikeCount();
        } else {
            PostLikeRequestDto postLikeRequest = new PostLikeRequestDto(userId, postId);
            PostLike postLike = postLikeRequest.toEntity(user, post);
            postLikeRepository.save(postLike);
            post.incrementLikeCount();
        }
    }
}
