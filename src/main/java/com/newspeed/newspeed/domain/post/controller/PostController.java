package com.newspeed.newspeed.domain.post.controller;

import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.dto.response.PostResponseDto;
import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.service.PostService;
import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
    * 게시글 작성
    * */
    @PostMapping
    public ResponseEntity<ApiResponseDto<PostResponseDto>> createPost(@Valid PostRequestDto requestDto,
                                                     @SessionAttribute("userId") Long userId) {
        PostResponseDto responseDto = postService.createPost(requestDto, userId);
        return ResponseEntity
                .status(SuccessCode.CREATE_POST.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.CREATE_POST, responseDto, "/api/posts"));
    }
    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> getPost(@PathVariable Long postId) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity
                .ok(ApiResponseDto.success(SuccessCode.GET_POST, responseDto, "/api/posts/" + postId));
    }
    /**
     * 뉴스피드 조회 (페이징)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<PostResponseDto>>> getNewsFeed(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<PostResponseDto> posts = postService.getNewsFeed(pageable, sort, startDate, endDate);
        return ResponseEntity
                .ok(ApiResponseDto.success(SuccessCode.GET_POSTS, posts, "/api/posts"));
    }

    /**
     * 게시글 수정 (작성자만 가능)
     */
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> updatePost(@PathVariable Long postId,
                                                                      @RequestBody PostRequestDto requestDto,
                                                                      @SessionAttribute("userId") Long userId) {
        PostResponseDto responseDto = postService.updatePost(postId, requestDto, userId);
        return ResponseEntity
                .ok(ApiResponseDto.success(SuccessCode.UPDATE_POST, responseDto, "/api/posts/" + postId));
    }

    /**
     * 게시글 삭제 (작성자만 가능)
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<Void>> deletePost(@PathVariable Long postId,
                                                           @SessionAttribute("userId") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity
                .status(SuccessCode.DELETE_POST.getHttpStatus())
                .body(ApiResponseDto.success(SuccessCode.DELETE_POST, "/api/posts/" + postId));
    }
    /**
     * 좋아요
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponseDto<Void>> toggleLike(@PathVariable Long postId,
                                                           @SessionAttribute("userId") Long userId) {
        postService.toggleLike(postId, userId);
        return ResponseEntity
                .ok(ApiResponseDto.success(SuccessCode.TOGGLE_LIKE, "/api/posts/" + postId + "/like"));
    }
}
