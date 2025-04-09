package com.newspeed.newspeed.domain.profiles.service;

import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import com.newspeed.newspeed.domain.profiles.dto.*;
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
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    //타인 프로필 조회
    public OtherProfileResponseDto findProfileById(Long userId, Long currentUserId) {
        User user = userRepository.findUserByIdOrElseThrow(userId);

        //친구, 게시글 수
        int friendCount = friendRepository.countById(userId);
        int postCount = postRepository.countByUserId(userId);

        //게시글 검색 후 Dto 리스트로 변경
        List<ProfilePostDto> profilePostList = convertPostToDtoList(userId);

        //친구 상태
        String friendStatus;
        if(currentUserId.equals(userId)){
            friendStatus = "ME";
        } else {
            friendStatus = friendRepository.findStatusById(userId);
        }

        //유저Dto 생성
        OtherProfileUserDto otherProfileUserDto = new OtherProfileUserDto(user.getId(), user.getName(), friendStatus, friendCount, postCount);

        return new OtherProfileResponseDto(otherProfileUserDto, profilePostList);
    }

    //내 프로필 조회
    public MyProfileResponseDto findMyProfileById(Long userId) {
        User user = userRepository.findUserByIdOrElseThrow(userId);

        //친구, 게시글 수
        int friendCount = friendRepository.countById(userId);
        int postCount = postRepository.countByUserId(userId);

        //게시글 검색 후 Dto 리스트로 변경
        List<ProfilePostDto> profilePostList = convertPostToDtoList(userId);

        //유저Dto 생성
        MyProfileUserDto myProfileUserDto = new MyProfileUserDto(user.getId(), user.getName(), user.getEmail(), friendCount, postCount);

        return new MyProfileResponseDto(myProfileUserDto, profilePostList);
    }

    //유저 아이디로 게시글들 찾은 후 ProfilePostDto로 변환
    public List<ProfilePostDto> convertPostToDtoList(Long userId){
        List<Post> postList = postRepository.findByUserId(userId);
        return postList.stream()
                .map(post -> {
                    int commentCount = commentRepository.countById(post.getId());
                    return ProfilePostDto.toDto(post, commentCount);
                })
                .toList();
    }

    //프로필 수정
    @Transactional
    public ApiResponseDto<Void> updateProfile(ProfileRequestDto requestDto, Long userId) {
        User user = userRepository.findUserByIdOrElseThrow(userId);

        //이름과 이메일 업데이트
        user.updateInfo(requestDto.getUserName(), requestDto.getEmail());

        //비번 업데이트
        if(requestDto.getNewPassword() != null) { //새 비밀번호를 지정하려고 한다면 비밀번호 체크
            if(passwordEncoder.matches(requestDto.getPreviousPassword(), user.getPassword())){ //기존 비밀번호가 같으면
                user.updatePassword(requestDto.getNewPassword()); //업데이트 로직
            } else {
                return ApiResponseDto.fail(ErrorCode.INVALID_PASSWORD, "/api/profiles");
            }
        }
        return ApiResponseDto.success(SuccessCode.GENERAL_SUCCESS, "/api/profiles");
    }
}
