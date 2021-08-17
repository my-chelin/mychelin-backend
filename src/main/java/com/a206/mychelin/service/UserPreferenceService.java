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
    public ResponseEntity saveUserPreference(UserPreferenceDto.UserPreferenceRequest userPreferenceRequest, HttpServletRequest httpServletRequest){
        String userId = TokenToId.check(httpServletRequest);
        if(userId == null) {
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

    public String findMostSimilarUserByTaste(String userId){
        Optional<User> user = userRepository.findUserById(userId);
        if(!user.isPresent()){
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
        for(User item : users) {
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
        userSelectionStandard.put("sociable", userPref.getSociable);
        userSelectionStandard.put("sensitivity", userPref.getSensitivity);
        return userSelectionStandard;
    }
}