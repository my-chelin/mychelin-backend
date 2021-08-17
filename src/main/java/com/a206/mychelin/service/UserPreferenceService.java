package com.a206.mychelin.service;

import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.entity.UserPreference;
import com.a206.mychelin.domain.repository.UserPreferenceRepository;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.util.CosineSimilarity;
import com.a206.mychelin.util.TokenToId;
import com.a206.mychelin.web.dto.Response;
import com.a206.mychelin.web.dto.UserPreferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserPreferenceService {
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    @Transactional
    public ResponseEntity<Response> saveUserPreference(UserPreferenceDto.UserPreferenceRequest userPreferenceRequest, HttpServletRequest httpServletRequest) {
        String userId = TokenToId.check(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요.", null);
        }
        userPreferenceRepository.save(
                UserPreference.builder()
                        .userId(userId)
                        .sweet(userPreferenceRequest.getSweet())
                        .salty(userPreferenceRequest.getSalty())
                        .sour(userPreferenceRequest.getSour())
                        .spicy(userPreferenceRequest.getSpicy())
                        .oily(userPreferenceRequest.getOily())
                        .challenging(userPreferenceRequest.getChallenging())
                        .planning(userPreferenceRequest.getPlanning())
                        .sociable(userPreferenceRequest.getSociable())
                        .sensitivity(userPreferenceRequest.getSensitivity())
                        .build()
        );
        return Response.newResult(HttpStatus.OK, "사용자의 취향을 저장하였습니다.", null);
    }

    public String findMostSimilarUserByTaste(String userId) {
        Optional<User> user = userRepository.findUserById(userId);
        if (!user.isPresent()) {
            return "curUserNotValid";
        }
        Optional<UserPreference> curUserPref = userPreferenceRepository.findUserPreferenceByUserId(user.get().getId());
        if (!curUserPref.isPresent()) {
            return "BAD_REQUEST"; //Response.newResult(HttpStatus.BAD_REQUEST, "취향 설문을 먼저 진행해주세요.", null);
        }
        Map<CharSequence, Integer> curUserTaste = getTasteIntoMap(curUserPref.get());

        List<User> users = userRepository.findAll();
        double maxSimilarity = -1.0;
        String mostSimilarUserId = "null";
        for (User item : users) {
            String compareUserId = item.getId();
            if (userId.equals(compareUserId)) {
                continue;
            }
            userPreferenceRepository.findUserPreferenceByUserId(compareUserId);
            CosineSimilarity cs = new CosineSimilarity();
            Optional<UserPreference> compareUserPref = userPreferenceRepository.findUserPreferenceByUserId(item.getId());
            if (!compareUserPref.isPresent()) {
                continue;
            }
            Map<CharSequence, Integer> compareUserTaste = getTasteIntoMap(compareUserPref.get());
            double comparison = cs.cosineSimilarity(curUserTaste, compareUserTaste);
            if (maxSimilarity < comparison) {
                maxSimilarity = comparison;
                mostSimilarUserId = item.getId();
            }
        }
        if (mostSimilarUserId.equals("null")) {
            return "NO_CONTENT"; //Response.newResult(HttpStatus.NO_CONTENT, "서비스 이용을 위한 데이터를 모으는 중입니다. 조금만 기다려주세요.", null);
        }

        return mostSimilarUserId;
    }

    private static Map<CharSequence, Integer> getTasteIntoMap(UserPreference userPref) {
        Map<CharSequence, Integer> userTaste = new HashMap<>();
        userTaste.put("sour", userPref.getSour());
        userTaste.put("sweet", userPref.getSweet());
        userTaste.put("salty", userPref.getSalty());
        userTaste.put("oily", userPref.getOily());
        userTaste.put("spicy", userPref.getSpicy());
        return userTaste;
    }

    private static Map<CharSequence, Integer> getSelectionStandardIntoMap(UserPreference userPref) {
        Map<CharSequence, Integer> userSelectionStandard = new HashMap<>();
        userSelectionStandard.put("planning", userPref.getPlanning());
        userSelectionStandard.put("challenging", userPref.getChallenging());
        userSelectionStandard.put("sociable", userPref.getSociable());
        userSelectionStandard.put("sensitivity", userPref.getSensitivity());
        return userSelectionStandard;
    }

    public ResponseEntity<Response> getPreference(HttpServletRequest httpServletRequest) {
        String userId = TokenToId.check(httpServletRequest);
        if (userId == null) {
            return Response.newResult(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요", null);
        }
        Optional<User> curUser = userRepository.findUserById(userId);
        if (!curUser.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자입니다.", null);
        }

        Optional<UserPreference> userPref = userPreferenceRepository.findUserPreferenceByUserId(userId);
        if (!userPref.isPresent()) {
            return Response.newResult(HttpStatus.BAD_REQUEST, "취향 설문을 먼저 진행해주세요", null);
        }

        HashMap<String, Object> linkedHashmap = new LinkedHashMap<>();

        UserPreferenceDto.TastePreferenceResponse tastePreferenceResponse =
                UserPreferenceDto.TastePreferenceResponse.builder()
                        .sour(userPref.get().getSour())
                        .sweet(userPref.get().getSweet())
                        .salty(userPref.get().getSalty())
                        .oily(userPref.get().getOily())
                        .spicy(userPref.get().getSpicy())
                        .build();

        String userAsAnimal;
        String userAsAction;
        int challenging = userPref.get().getChallenging();
        int planning = userPref.get().getPlanning();
        int sociable = userPref.get().getSociable();
        int sensitivity = userPref.get().getSensitivity();
        if (challenging >= 50 && planning >= 50) {
            userAsAnimal = "곰";
        } else if (challenging >= 50) {
            userAsAnimal = "호랑이";
        } else if (planning >= 50) {
            userAsAnimal = "고양이";
        } else {
            userAsAnimal = "다람쥐";
        }

        if(sociable >= 50 && sensitivity >= 50) {
            userAsAction = "부지런한";
        } else if (sociable >= 50) {
            userAsAction = "사교적인";
        } else if (sensitivity >= 50) {
            userAsAction = "꼼꼼한";
        } else {
            userAsAction = "싱글벙글";
        }

        UserPreferenceDto.PlacePreferenceResponse placePreferenceResponse =
                UserPreferenceDto.PlacePreferenceResponse.builder()
                        .userAsAction(userAsAction)
                        .userAsAnimal(userAsAnimal)
                        .build();

        linkedHashmap.put("userNickname", curUser.get().getNickname());
        linkedHashmap.put("tastePreference", tastePreferenceResponse);
        linkedHashmap.put("placePreference", placePreferenceResponse);
        return Response.newResult(HttpStatus.OK, "취향을 알려드립니다.", linkedHashmap);
    }
}