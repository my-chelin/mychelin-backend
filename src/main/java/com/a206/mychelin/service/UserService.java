package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.Follow;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.entity.UserEmailCheck;
import com.a206.mychelin.domain.entity.UserPreference;
import com.a206.mychelin.domain.repository.*;
import com.a206.mychelin.util.CosineSimilarity;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 이메일 보내기 위해 선언, JavaMailSender를 구현한 Bean 객체 주입
    private final JavaMailSender javaMailSender;
    private final UserEmailCheckRepository userEmailCheckRepository;

    private User getUser(HttpServletRequest request) {
        String id = TokenToId.check(request);
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get();
    }

    public String getIdByNickname(String nickname) {
        return userRepository.findUserByNickname(nickname).get().getId();
    }

    @Transactional
    public ResponseEntity<Response> updateInfo(@RequestBody UserDto.UpdateRequest requestDTO, HttpServletRequest request) {
        String userId = TokenToId.check(request);
        Optional<User> checkUser = userRepository.findUserByNickname(requestDTO.getNickname());
        if (checkUser.isPresent() && !checkUser.get().getId().equals(userId)) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.", null);
        }
        if (requestDTO.getNickname().length() > 8) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "닉네임은 8자리 이하만 가능합니다.", null);
        }
        if (requestDTO.getPhoneNumber().length() > 11) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "폰번호는 11자리 이하만 가능합니다.", null);
        }
        Optional<User> user = userRepository.findUserById(userId);
        user.get().updateInfo(requestDTO.getNickname(), requestDTO.getBio(), requestDTO.getPhoneNumber());
        return Response.newResult(HttpStatus.OK, "정보가 업데이트 되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> changePassword(UserDto.PasswordChangeRequest passwordChangeRequest, HttpServletRequest request) {
        User user = getUser(request);
        if (user == null) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다.", null);
        }
        if (!passwordEncoder.matches(passwordChangeRequest.getPassword(), user.getPassword())) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", null);
        }
        user.changePassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        return Response.newResult(HttpStatus.OK, "비밀번호가 변경되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> signUp(UserDto.UserSaveRequest userSaveRequest) {
        String id = userSaveRequest.getId();
        String nickname = userSaveRequest.getNickname();
        if (userRepository.findUserById(id).isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.", null);
        }
        if (userRepository.findUserByNickname(nickname).isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.", null);
        }
        userSaveRequest.setPassword(passwordEncoder.encode(userSaveRequest.getPassword()));
        User user = User.builder()
                .id(id)
                .password(userSaveRequest.getPassword())
                .nickname(nickname)
                .phoneNumber(userSaveRequest.getPhoneNumber())
                .build();
        userRepository.save(user);
        return Response.newResult(HttpStatus.OK, "회원가입이 완료되었습니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> deleteUser(HttpServletRequest request) {
        User user = getUser(request);
        if (!user.isWithdraw()) {
            user.userWithdraw();
            return Response.newResult(HttpStatus.OK, "탈퇴가 완료되었습니다.", null);
        }
        return Response.newResult(HttpStatus.BAD_REQUEST, "유효하지 않은 접근입니다.", null);
    }

    @Transactional
    public ResponseEntity<Response> getProfile(String userId, HttpServletRequest request) {
        UserDto.UserProfileResponse.UserProfileResponseBuilder userProfileResponseBuilder;
        Optional<User> tempUser = userRepository.findUserById(userId);
        if (!tempUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.", null);
        }
        if (tempUser.get().isWithdraw()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다.", null);
        }
        User user = tempUser.get();
        int follow = followRepository.countByUserIdAndAccept(user.getId(), true); // 사용자가 신청한 팔로우 리스트이므로 팔로잉
        int follower = followRepository.countByFollowingIdAndAccept(user.getId(), true); // 사용자를 팔로우 하겠다고 담은 사람 리스트이므로 팔로워.
        long like = postLikeRepository.getLikes(user.getId());
        userProfileResponseBuilder = UserDto.UserProfileResponse.builder()
                .nickname(user.getNickname())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .follow(follow)
                .follower(follower)
                .like(like);
        User loginUser = getUser(request);
        UserDto.UserProfileResponse userProfileResponse;
        if (loginUser.getId().equals(user.getId())) {
            userProfileResponseBuilder = userProfileResponseBuilder.phoneNumber(user.getPhoneNumber());
        } else {
            if (followRepository.countByUserIdAndFollowingIdAndAccept(loginUser.getId(), user.getId(), true) > 0) {
                userProfileResponseBuilder = userProfileResponseBuilder.isFollowing(2);
            } else if (followRepository.countByUserIdAndFollowingIdAndAccept(loginUser.getId(), user.getId(), false) > 0) {
                userProfileResponseBuilder = userProfileResponseBuilder.isFollowing(1);
            } else {
                userProfileResponseBuilder = userProfileResponseBuilder.isFollowing(0);
            }
        }
        userProfileResponse = userProfileResponseBuilder.build();

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("userProfile", userProfileResponse);

        Optional<UserPreference> userPref = userPreferenceRepository.findUserPreferenceByUserId(userId);
        if (userPref.isPresent()) {
            UserPreferenceDto.TastePreferenceResponse tastePreferenceResponse =
                    UserPreferenceDto.TastePreferenceResponse.builder()
                            .sweet(userPref.get().getSweet())
                            .salty(userPref.get().getSalty())
                            .sour(userPref.get().getSour())
                            .oily(userPref.get().getOily())
                            .spicy(userPref.get().getSpicy())
                            .build();
            linkedHashMap.put("tastePreference", tastePreferenceResponse);

            StringBuilder sb = new StringBuilder();
            boolean challenging = userPref.get().getChallenging() >= 50;
            boolean planning = userPref.get().getPlanning() >= 50;
            boolean sensitivity = userPref.get().getSensitivity() >= 50;
            boolean sociable = userPref.get().getSociable() >= 50;

            String userAsAnimal = UserPreferenceService.getUserAnimalType(challenging, planning, sensitivity, sociable);

            linkedHashMap.put("userAsAnimal", userAsAnimal);

        }
        return Response.newResult(HttpStatus.OK, "회원정보를 출력합니다.", linkedHashMap);
    }

    public ResponseEntity<Response> checkEmail(EmailRequest emailRequest) {
        Optional<User> user = userRepository.findUserById(emailRequest.getEmail());
        if (user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.", null);
        }
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        StringBuffer token = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    token.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    // A-Z
                    token.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    // 0-9
                    token.append((rnd.nextInt(10)));
                    break;
            }
        }

        emailMessage.setTo(emailRequest.getEmail());
        emailMessage.setSubject("가입 인증 메일입니다.");
        emailMessage.setText("안녕하세요😄 Mychelin 입니다.\n\n" +
                "가입 인증 토큰은 : " + token + " 입니다.\n\n" +
                "감사합니다.");
        javaMailSender.send(emailMessage);

        Optional<UserEmailCheck> userEmailCheckOptional = userEmailCheckRepository.findByUserId(emailRequest.getEmail());
        UserEmailCheck userEmailCheck;
        if (userEmailCheckOptional.isPresent()) { // 여러번 요청할 때
            userEmailCheck = userEmailCheckOptional.get();
            userEmailCheck.changeToken(token.toString());
        } else { // 처음 요청할 때
            userEmailCheck = UserEmailCheck.builder()
                    .userId(emailRequest.getEmail())
                    .token(token.toString())
                    .build();
        }
        userEmailCheckRepository.save(userEmailCheck);
        return Response.newResult(HttpStatus.OK, emailRequest.getEmail() + "로 인증 번호를 전송하였습니다.", null);
    }

    public ResponseEntity<Response> checkEmailToken(EmailTokenRequest emailTokenRequest) {
        Optional<User> user = userRepository.findUserById(emailTokenRequest.getEmail());
        if (user.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.", null);
        }
            /*
                1. token DB에 저장되어 있는지 확인
                2. 없다면, 없다고 리턴
                3. 있다면, 들어온 토큰과 비교
                4. 다르다면, 다르다고 리턴
                5. 같다면 회원 가입 가능한 이메일임을 알려주기
             */

        Optional<UserEmailCheck> userEmailCheck = userEmailCheckRepository.findByUserId(emailTokenRequest.getEmail());
        if (!userEmailCheck.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "인증 번호 요청이 되지 않은 이메일입니다.", null);
        }
        if (!userEmailCheck.get().getToken().equals(emailTokenRequest.getToken())) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "인증 번호가 일치하지 않습니다.", null);
        }
        return Response.newResult(HttpStatus.OK, "인증되었습니다.", null);
    }

    public ResponseEntity<Response> saveUserProfileImage(ImageRequest image, HttpServletRequest request) {
        User user = getUser(request);
        user.userImageUpdate(image.getImage());
        userRepository.save(user);
        return Response.newResult(HttpStatus.OK, "프로필 이미지 저장에 성공했습니다.", null);
    }

    public ResponseEntity<Response> searchUserByNickname(String nickname) {
        List<User> userList = userRepository.findUsersByNicknameContains(nickname);
        ArrayList<UserDto.UserSearchResponse> arr = new ArrayList<>();
        for (User u : userList) {
            arr.add(UserDto.UserSearchResponse.builder()
                    .nickname(u.getNickname())
                    .profileImage(u.getProfileImage())
                    .bio(u.getBio())
                    .build());
        }
        if (userList.size() > 0) {
            return Response.newResult(HttpStatus.OK, "사용자 검색을 완료하였습니다.", arr);
        }
        return Response.newResult(HttpStatus.OK, "일치하는 사용자가 없습니다.", null);
    }

    public ResponseEntity getUserRecommendationByPlacePreference(HttpServletRequest httpServletRequest) {
        String userId = TokenToId.check(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 사용해주세요.", null);
        }
        Optional<User> curUser = userRepository.findUserById(userId);
        if (!curUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자입니다.", null);
        }
        Optional<UserPreference> userPreference = userPreferenceRepository.findUserPreferenceByUserId(userId);
        if (!userPreference.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "취향 설문을 먼저 진행해주세요.", null);
        }
        Map<CharSequence, Integer> curUserPlace = UserPreferenceService.getSelectionStandardIntoMap(userPreference.get());

        List<User> users = userRepository.findAll();
        double maxSimilarity = -1.0;
        double secondMaxSimilarity = -1.0;
        String mostSimilarUserId = "null";
        String secondMostSimilarUserId = "null";

        for (User item : users) {
            String compareUserId = item.getId();
            if (userId.equals(compareUserId)) {
                continue;
            }
            Optional<Follow> checkFollow = followRepository.findFollowByUserIdAndFollowingId(userId, compareUserId);
            if (checkFollow.isPresent()) {
                continue;
            }
            userPreferenceRepository.findUserPreferenceByUserId(compareUserId);
            CosineSimilarity cs = new CosineSimilarity();
            Optional<UserPreference> compareUserPref = userPreferenceRepository.findUserPreferenceByUserId(item.getId());
            if (!compareUserPref.isPresent()) {
                continue;
            }
            Map<CharSequence, Integer> compareUserPlace = UserPreferenceService.getSelectionStandardIntoMap(compareUserPref.get());
            double comparison = cs.cosineSimilarity(curUserPlace, compareUserPlace);

            if (maxSimilarity < comparison) {
                maxSimilarity = comparison;
                mostSimilarUserId = item.getId();
            } else if (secondMaxSimilarity < comparison) {
                secondMaxSimilarity = comparison;
                secondMostSimilarUserId = item.getId();
            }
        }

        ArrayList<UserDto.UserSearchResponse> arr = new ArrayList<>();
        Optional<User> firstSimilarUser = userRepository.findUserById(mostSimilarUserId);
        arr.add(UserDto.UserSearchResponse.builder()
                .nickname(firstSimilarUser.get().getNickname())
                .profileImage(firstSimilarUser.get().getProfileImage())
                .bio(firstSimilarUser.get().getBio())
                .build());
        Optional<User> secondSimilarUser = userRepository.findUserById(secondMostSimilarUserId);
        arr.add(UserDto.UserSearchResponse.builder()
                .nickname(secondSimilarUser.get().getNickname())
                .profileImage(secondSimilarUser.get().getProfileImage())
                .bio(secondSimilarUser.get().getBio())
                .build());

        if (arr.size() > 0) {
            return Response.newResult(HttpStatus.OK, "사용자 검색을 완료하였습니다.", arr);
        }
        return Response.newResult(HttpStatus.OK, "새로운 사용자의 가입을 기다려주세요.", null);

    }
}