package com.newspeed.newspeed.domain.comments.service;

import com.newspeed.newspeed.domain.comments.dto.request.*;
import com.newspeed.newspeed.domain.comments.dto.response.CommentResponseDto;
import com.newspeed.newspeed.domain.comments.entity.*;
import com.newspeed.newspeed.domain.comments.repository.*;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    // 댓글 전체 조회
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.findCommentResponseDtoByPostId(postId);
    }

    @Transactional
    // 댓글 생성
    public Comment createComment(Long userId, Long postId, CommentRequestDto requestDto) {
            // 1. 사용자 인증 및 게시글 존재 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

            // 2. 댓글 생성
            Comment comment = new Comment(user, post, requestDto.getCommentText());
            comment.setCommentLikes(0); // 좋아요 개수 0 초기화

            // 3. 댓글 저장
            return commentRepository.save(comment);
    }


    @Transactional
    // 댓글 수정
    public void updateComment(Long userId, Long commentId, UpdateCommentRequestDto requestDto) {
        // 1. 사용자 인증 및 댓글 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 댓글 작성자인지 확인
        if (!isCommentOwner(userId, comment)) {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다.");
        }

        // 3. 댓글 내용 수정
        comment.setCommentText(requestDto.getCommentText());

        // 4. 변경 사항 저장
        commentRepository.save(comment);
    }

    @Transactional
    // 댓글 삭제
    public void deleteComment(Long userId, Long commentId) {
        // 1. 사용자 인증 및 댓글 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. 게시글 작성자 또는 댓글 작성자인지 확인
        if (!isPostOwner(userId, comment.getPost().getId()) && !isCommentOwner(userId, comment)) {
            throw new IllegalArgumentException("게시글 작성자 및 댓글 작성자가 아닙니다.");
        }

        // 3. 댓글 삭제
        commentRepository.delete(comment);
    }

    @Transactional
    // 댓글 좋아요
    public void addCommentLike(Long userId, Long commentId) {
        // 1. 사용자 인증 및 댓글 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (commentLikeRepository.existsByComment_IdAndUser_Id(commentId, userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 댓글입니다.");
        }

        // 2. 좋아요
        CommentLike commentLike = new CommentLike(user, comment);

        // 3. 좋아요 저장
        commentLikeRepository.save(commentLike);

        // 4. Comment 엔티티의 commentLikes 값 증가
        comment.setCommentLikes(comment.getCommentLikes() + 1);
    }

    @Transactional
    // 댓글 좋아요 취소
    public void removeCommentLike(Long userId, Long commentId) {
        // 1. 사용자 인증 및 댓글 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 2. 댓글 좋아요 유무 확인
        if (!commentLikeRepository.existsByComment_IdAndUser_Id(userId, commentId)) {
            throw new IllegalStateException("좋아요를 누르지 않은 댓글입니다.");
        }

        // 3. 좋아요 삭제
        commentLikeRepository.deleteByComment_IdAndUser_Id(userId, commentId);

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
