package com.newspeed.newspeed.domain.comments.controller;

import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.comments.dto.request.*;
import com.newspeed.newspeed.domain.comments.dto.response.*;
import com.newspeed.newspeed.domain.comments.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<CommentCreateResponseDto>> createComment(
            @SessionAttribute("userId") Long userId,
            @RequestParam Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto) {
        CommentCreateResponseDto commentResponseDto = commentService.createComment(userId, postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(SuccessCode.COMMENT_CREATE_SUCCESS, commentResponseDto, "/api/comments"));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<Void>> updateComment(
            @PathVariable Long commentId,
            @SessionAttribute("userId") Long userId,
            @RequestBody CommentUpdateRequestDto requestDto) {
        commentService.updateComment(userId, commentId, requestDto);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.COMMENT_UPDATE_SUCCESS, "/api/comments/" + commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComment(
            @PathVariable Long commentId,
            @SessionAttribute("userId") Long userId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.success(SuccessCode.COMMENT_DELETE_SUCCESS, "/api/comments/" + commentId));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponseDto<Void>> addCommentLike(
            @PathVariable Long commentId,
            @SessionAttribute("userId") Long userId) {
        commentService.addCommentLike(userId, commentId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.COMMENT_LIKE_SUCCESS, "/api/comments/" + commentId + "/like"));
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ApiResponseDto<Void>> removeCommentLike(
            @PathVariable Long commentId,
            @SessionAttribute("userId") Long userId) {
        commentService.removeCommentLike(userId, commentId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.COMMENT_UNLIKE_SUCCESS, "/api/comments/" + commentId + "/like"));
    }
}