package com.newspeed.newspeed.domain.post.controller;

import com.newspeed.newspeed.domain.post.dto.request.PostRequestDto;
import com.newspeed.newspeed.domain.post.dto.response.PostResponseDto;
import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,
                                                      @SessionAttribute("userId") Long userId) {
        PostResponseDto responseDto = postService.createPost(requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }
    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 뉴스피드 조회 (페이징)
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getNewsFeed(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "latest") String sort,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<PostResponseDto> posts = postService.getNewsFeed(page, size, sort, startDate, endDate);
        return ResponseEntity.ok(posts);
    }
    /**
     * 게시글 수정 (작성자만 가능)
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                      @RequestBody PostRequestDto requestDto,
                                                      @SessionAttribute("userId") Long userId) {
        PostResponseDto responseDto = postService.updatePost(postId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 게시글 삭제 (작성자만 가능)
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @SessionAttribute("userId") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
    /**
     * 좋아요
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId,
                                           @SessionAttribute("userId") Long userId) {
        postService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }
}
