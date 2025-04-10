package com.newspeed.newspeed.domain.comments.controller;

import com.newspeed.newspeed.domain.comments.dto.request.CommentRequestDto;
import com.newspeed.newspeed.domain.comments.dto.request.UpdateCommentRequestDto;
import com.newspeed.newspeed.domain.comments.dto.response.CommentResponseDto;
import com.newspeed.newspeed.domain.comments.entity.Comment;
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
    public ResponseEntity<?> createComment(
            @RequestParam Long userId,
            @RequestParam Long postId,
            @Valid @RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.createComment(userId, postId, requestDto);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestParam("userId") Long userId,
            @RequestBody UpdateCommentRequestDto requestDto) {
        commentService.updateComment(userId, commentId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @RequestParam("userId") Long userId) {
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> addCommentLike(
            @PathVariable Long commentId,
            @RequestParam("userId") Long userId) {
        commentService.addCommentLike(userId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<?> removeCommentLike(
            @PathVariable Long commentId,
            @RequestParam("userId") Long userId) {
        commentService.removeCommentLike(userId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
