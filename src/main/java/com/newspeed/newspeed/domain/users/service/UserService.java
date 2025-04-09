package com.newspeed.newspeed.domain.users.service;

import com.newspeed.newspeed.common.config.PasswordEncoder;
import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.domain.users.dto.request.UserLoginRequestDto;
import com.newspeed.newspeed.domain.users.dto.request.UserSignupRequestDto;
import com.newspeed.newspeed.domain.users.entity.User;
import com.newspeed.newspeed.domain.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void signup(final UserSignupRequestDto userSignupRequestDto, HttpServletRequest httpServletRequest) {

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userSignupRequestDto.password());

        // 이메일 중복 확인
        if (userRepository.findByEmail(userSignupRequestDto.email()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 빌더 패턴을 사용하여 사용자 객체 생성
        User user = User.builder()
                .name(userSignupRequestDto.name())
                .email(userSignupRequestDto.email())
                .password(encodedPassword)
                .build();  // 빌드 호출

        userRepository.save(user);

        HttpSession httpSession = httpServletRequest.getSession(true);  // 새로운 세션 생성
        userRepository.save(user); // 로그인 한 건 아니므로 세션 저장은 X
    }

    @Transactional
    public void login(final UserLoginRequestDto userLoginRequestDto, HttpServletRequest request) {

        // 사용자 조회
        User user = userRepository.findByEmail(userLoginRequestDto.email())
                .filter(u -> u.getName().equals(userLoginRequestDto.name()))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_REGISTERED));

        // 비밀번호 확인
        if (!passwordEncoder.matches(userLoginRequestDto.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
    }
}

