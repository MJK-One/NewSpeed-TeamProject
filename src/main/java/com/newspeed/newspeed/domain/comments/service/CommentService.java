package com.newspeed.newspeed.domain.comments.service;

import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.comments.dto.request.*;
import com.newspeed.newspeed.domain.comments.dto.response.*;
import com.newspeed.newspeed.domain.comments.entity.*;
import com.newspeed.newspeed.domain.comments.repository.*;
import com.newspeed.newspeed.domain.post.entity.Post;
import com.newspeed.newspeed.domain.post.repository.PostRepository;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    // 댓글 전체 조회
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.findCommentResponseDtoByPostId(postId);
    }

    @Transactional
    public CommentCreateResponseDto createComment(Long userId, Long postId, CommentCreateRequestDto requestDto) {
        // 1. 사용자 인증 및 게시글 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 2. 댓글 생성
        Comment comment = new Comment(user, post, requestDto.getCommentText());
        comment.setCommentLikes(0); // 좋아요 개수 0 초기화

        // 3. 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // 4. CommentCreateResponseDto 생성 및 반환
        return new CommentCreateResponseDto(
                savedComment.getId(),
                savedComment.getUser().getUserId(),
                savedComment.getPost().getId(),
                savedComment.getCommentText(),
                savedComment.getCommentLikes()
        );
    }


    @Transactional
    // 댓글 수정
    public void updateComment(Long userId, Long commentId, CommentUpdateRequestDto requestDto) {
        // 1. 사용자 인증 및 댓글 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 2. 댓글 작성자인지 확인
        if (!isCommentOwner(userId, comment)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        // 3. 댓글 내용 수정
        comment.setCommentText(requestDto.getCommentText());
    }

    @Transactional
    // 댓글 삭제
    public void deleteComment(Long userId, Long commentId) {
        // 1. 사용자 인증 및 댓글 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 2. 게시글 작성자 또는 댓글 작성자인지 확인
        if (!isPostOwner(userId, comment.getPost().getId()) && !isCommentOwner(userId, comment)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        // 3. 댓글 삭제
        commentRepository.delete(comment);
    }

    @Transactional
    // 댓글 좋아요
    public void addCommentLike(Long userId, Long commentId) {
        // 1. 사용자 인증 및 댓글 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 2. 댓글 좋아요 유무 확인
        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new CustomException(ErrorCode.COMMENT_ALREADY_LIKED);
        }

        // 3. 좋아요
        CommentLike commentLike = new CommentLike(user, comment);

        // 4. 좋아요 저장
        commentLikeRepository.save(commentLike);

        // 5. Comment 엔티티의 commentLikes 값 증가
        comment.setCommentLikes(comment.getCommentLikes() + 1);
    }

    @Transactional
    // 댓글 좋아요 취소
    public void removeCommentLike(Long userId, Long commentId) {
        // 1. 사용자 인증 및 댓글 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        // 2. 댓글 좋아요 유무 확인
        if (!commentLikeRepository.existsByCommentIdAndUserId(userId, commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_LIKED);
        }

        // 3. 좋아요 삭제
        commentLikeRepository.deleteByCommentIdAndUserId(userId, commentId);

        // 4. Comment 엔티티의 commentLikes 값 감소
        comment.setCommentLikes(comment.getCommentLikes() - 1);
    }

    // 게시글 작성자인지 확인하는 메서드
    private boolean isPostOwner(Long userId, Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return post.getUser().getUserId().equals(userId);
        }
        return false;
    }

    // 댓글 작성자인지 확인하는 메서드
    private boolean isCommentOwner(Long userId, Comment comment) {
        return comment.getUser().getUserId().equals(userId);
    }
}