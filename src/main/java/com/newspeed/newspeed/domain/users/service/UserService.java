package com.newspeed.newspeed.domain.users.service;

import com.newspeed.newspeed.common.config.PasswordEncoder;
import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.users.dto.request.UserLoginRequestDto;
import com.newspeed.newspeed.domain.users.dto.request.UserSignupRequestDto;
import com.newspeed.newspeed.domain.users.dto.request.UserWithdrawRequestDto;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     *
     * @param userSignupRequestDto 사용자 생성 요청 데이터를 담은 DTO
     */
    @Transactional
    public void signup(final UserSignupRequestDto userSignupRequestDto) {

        // 이메일 존재 여부 확인
        Optional<User> existingUserOpt = userRepository.findByEmail(userSignupRequestDto.email());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (existingUser.isDeleted()) {
                // 탈퇴한 사용자일 경우
                throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
            }

            // 이미 가입된 사용자일 경우
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userSignupRequestDto.password());

        // 빌더 패턴을 사용하여 사용자 객체 생성
        User user = User.builder()
                .name(userSignupRequestDto.name())
                .email(userSignupRequestDto.email())
                .password(encodedPassword)
                .build();  // 빌드 호출

        userRepository.save(user); // 로그인 한 건 아니므로 세션 저장은 X
    }

    @Transactional
    public User login(final UserLoginRequestDto requestDto) {

        // 사용자 조회
        User user = userRepository.findByEmail(requestDto.email())
                .filter(u -> u.getName().equals(requestDto.name()))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_REGISTERED));

        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return user;
    }

    @Transactional
    public void deleteUser(Long userId, UserWithdrawRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_REGISTERED));

        // 이미 탈퇴한 사용자 처리
        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.ALREADY_WITHDRAWN);
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // Soft Delete 처리
        user.markAsDeleted(); // user.setDeleted(true);
    }


}

