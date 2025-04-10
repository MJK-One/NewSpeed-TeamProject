package com.newspeed.newspeed.domain.profiles.service;

import com.newspeed.newspeed.common.config.PasswordEncoder;
import com.newspeed.newspeed.common.exception.base.NotFoundException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.comments.entity.Post;
import com.newspeed.newspeed.domain.comments.repository.CommentRepository;
import com.newspeed.newspeed.domain.comments.repository.PostRepository;
import com.newspeed.newspeed.domain.friends.repository.FriendshipRepository;
import com.newspeed.newspeed.domain.profiles.dto.request.ProfileUpdateRequestDto;
import com.newspeed.newspeed.domain.profiles.dto.response.*;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final PasswordEncoder passwordEncoder;

    //타인 프로필 조회
    public OtherProfileResponseDto findProfileById(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //친구, 게시글 수
        int friendCount = friendshipRepository.countFriendsByUserId(userId);
        int postCount = postRepository.countByUserId(userId);

        //게시글 검색 후 Dto 리스트로 변경
        List<ProfilePostDto> profilePostList = convertPostToDtoList(userId);

        //친구 상태
        String friendStatus;
        if(currentUserId.equals(userId)){
            friendStatus = "ME";
        } else {
            friendStatus = friendshipRepository.findStatusByUserIdAndProfileId(currentUserId, userId);
            if(friendStatus == null) {
                friendStatus = "SEND REQUEST";
            }
        }

        //유저Dto 생성
        OtherProfileUserDto otherProfileUserDto = OtherProfileUserDto.builder()
                .id(user.getUserId())
                .name(user.getName())
                .friendStatus(friendStatus)
                .postCount(postCount)
                .friendCount(friendCount)
                .build();

        return new OtherProfileResponseDto(otherProfileUserDto, profilePostList);
    }

    //내 프로필 조회
    public MyProfileResponseDto findMyProfileById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //친구, 게시글 수
        int friendCount = friendshipRepository.countFriendsByUserId(userId);
        int postCount = postRepository.countByUserId(userId);

        //게시글 검색 후 Dto 리스트로 변경
        List<ProfilePostDto> profilePostList = convertPostToDtoList(userId);

        //유저Dto 생성
        MyProfileUserDto myProfileUserDto = new MyProfileUserDto(user.getUserId(), user.getName(), user.getEmail(), friendCount, postCount);

        return new MyProfileResponseDto(myProfileUserDto, profilePostList);
    }

    //유저 아이디로 게시글들 찾은 후 ProfilePostDto로 변환
    public List<ProfilePostDto> convertPostToDtoList(Long userId){
        List<Post> postList = postRepository.findByUserId(userId);
        return postList.stream()
                .map(post -> {
                    int commentCount = commentRepository.countByPostId(post.getId());
                    return ProfilePostDto.toDto(post, commentCount);
                })
                .toList();
    }

    //프로필 수정
    @Transactional
    public boolean updateProfile(ProfileUpdateRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //이름과 이메일 업데이트
        user.updateInfo(requestDto.getName(), requestDto.getEmail());

        //비번 업데이트
        if(requestDto.getNewPassword() != null) { //새 비밀번호를 지정하려고 한다면 비밀번호 체크
            //기존 비밀번호가 같으면
            if (passwordEncoder.matches(requestDto.getPreviousPassword(), user.getPassword())) {
                user.updatePassword(requestDto.getNewPassword()); //업데이트 로직
            } else {
                return false;
            }
        }
        return true;
    }
}