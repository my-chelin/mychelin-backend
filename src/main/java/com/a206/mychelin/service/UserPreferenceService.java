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
        if (userPreferenceRepository.countByUserId(userId) == 0) {
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
                            .build());
        }
        Optional<UserPreference> optionalUserPreference = userPreferenceRepository.findUserPreferenceByUserId(userId);
        UserPreference userPreference = optionalUserPreference.get();
        System.out.println(userId);
        System.out.println(userPreferenceRequest.getChallenging());
        userPreference.update(userPreferenceRequest.getSweet(), userPreferenceRequest.getSalty(), userPreferenceRequest.getSour(),
                userPreferenceRequest.getOily(), userPreferenceRequest.getSpicy(), userPreferenceRequest.getChallenging(),
                userPreferenceRequest.getPlanning(), userPreferenceRequest.getSociable(), userPreferenceRequest.getSensitivity());

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
        userTaste.put("sour", Integer.valueOf(userPref.getSour()));
        userTaste.put("sweet", Integer.valueOf(userPref.getSweet()));
        userTaste.put("salty", Integer.valueOf(userPref.getSalty()));
        userTaste.put("oily", Integer.valueOf(userPref.getOily()));
        userTaste.put("spicy", Integer.valueOf(userPref.getSpicy()));
        return userTaste;
    }

    static Map<CharSequence, Integer> getSelectionStandardIntoMap(UserPreference userPref) {
        Map<CharSequence, Integer> userSelectionStandard = new HashMap<>();
        userSelectionStandard.put("planning", Integer.valueOf(userPref.getPlanning()));
        userSelectionStandard.put("challenging", Integer.valueOf(userPref.getChallenging()));
        userSelectionStandard.put("sociable", Integer.valueOf(userPref.getSociable()));
        userSelectionStandard.put("sensitivity", Integer.valueOf(userPref.getSensitivity()));
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

//        String userAsAnimal;
//        String userAsAction;
//        int challenging = userPref.get().getChallenging();
//        int planning = userPref.get().getPlanning();
//        int sociable = userPref.get().getSociable();
//        int sensitivity = userPref.get().getSensitivity();
//        if (challenging >= 50 && planning >= 50) {
//            userAsAnimal = "곰";
//        } else if (challenging >= 50) {
//            userAsAnimal = "호랑이";
//        } else if (planning >= 50) {
//            userAsAnimal = "고양이";
//        } else {
//            userAsAnimal = "다람쥐";
//        }
//
//        if (sociable >= 50 && sensitivity >= 50) {
//            userAsAction = "부지런한";
//        } else if (sociable >= 50) {
//            userAsAction = "사교적인";
//        } else if (sensitivity >= 50) {
//            userAsAction = "꼼꼼한";
//        } else {
//            userAsAction = "싱글벙글";
//        }

//        UserPreferenceDto.PlacePreferenceResponse placePreferenceResponse =
//                UserPreferenceDto.PlacePreferenceResponse.builder()
//                        .userAsAction(userAsAction)
//                        .userAsAnimal(userAsAnimal)
//                        .build();

//        Optional<UserPreference> userPref = userPreferenceRepository.findUserPreferenceByUserId(userId);
//        if (userPref.isPresent()) {
//            UserPreferenceDto.TastePreferenceResponse tastePreferenceResponse =
//                    UserPreferenceDto.TastePreferenceResponse.builder()
//                            .sweet(userPref.get().getSweet())
//                            .salty(userPref.get().getSalty())
//                            .sour(userPref.get().getSour())
//                            .oily(userPref.get().getOily())
//                            .spicy(userPref.get().getSpicy())
//                            .build();
//            linkedHashMap.put("tastePreference", tastePreferenceResponse);

            StringBuilder sb = new StringBuilder();
            boolean isChallenging = userPref.get().getChallenging() >= 50;
            boolean isPlanning = userPref.get().getPlanning() >= 50;
            boolean isSensitivity = userPref.get().getSensitivity() >= 50;
            boolean isSociable = userPref.get().getSociable() >= 50;

            String userAsAnimal = getUserAnimalType(isChallenging, isPlanning, isSensitivity, isSociable);

            if (isChallenging) {
                sb.append("최고의 맛을 쟁취하기 위해서라면 물리적 노력쯤이야! 그런 점을 마다하지 않고 찾아 나서는 당신에게는 탐험가의 기질이 있네요." +
                        " 도전하면서 새로운 취향을 찾아가는 재미를 한 번 맛본 이상 두 번 다시 돌아갈 수 없어요. 당신이 맛의 콜럼버스가 되어 개척해나간 식당들을 리스트로 만들어 팔로워들에게 소개해 보는 건 어떨까요?");
            } else {
                sb.append("근처의 맛집을 찾아가는 것도 귀찮아하는 당신…. 혹시 솔로는 아니신가요?\uD83D\uDE04\n" +
                        "안정적인 선택지에서 오는 안락함과 익숙함을 벗어나는 것을 조금은 두려워하는 듯 보여요. 하지만 또 반대로 생각하면 그런 당신이 좋아하는 가게라면 틀림없이 맛이 보장된 장소라는 이야기죠! " +
                        "그런 당신의 추천을 많은 사람이 기다리고 있어요! 그리고 때로는 검증된 장소로의 도전이라면 좋은 경험이 될지도 몰라요! 당신과 비슷한 사람들이 좋은 평가를 남긴 가게를 추천해드릴게요. 가까운 곳에 있다면 방문해보시는 건 어떨까요?");
            }

            if (isPlanning) {
                sb.append("정리정돈을 중시하고 계획과 질서가 잡혀 있는 것을 좋아하시는군요! 뚜렷한 목적의식을 갖고 체계적인 행동을 하는 성향이신 것 같아요. 가끔은 고집스럽다는 이야기를 들을 수도 있지만 " +
                        "예기치 못한 상황이 발생하는 것을 꺼리기에 합리적인 판단 끝에 결정을 내리는 신중한 성격이네요. 평소 철두철미하다는 얘기를 많이 들으시진 않나요? " +
                        "그런 당신의 맛집 탐색에 작은 도움을 줄 수 있는 몇 가지 장소들을 추천해 드릴게요! 시간을 조금이라도 더 절약할 수 있도록 말이죠.⏳");
            } else {
                sb.append(" 계획보다는 즉흥적으로 식당을 선택하는 당신은 창의적이고 새로운 걸 좋아하는 사람이군요! 낯선 장소에서도 '이 가게다!' 싶으면 들어가 보는 성격이라 만족도는 높고 후회는 적은 편이에요. " +
                        "임기응변에 강해서 주어진 환경에 빠르게 적응하는 모습을 볼 수 있어요. 호기심이 많고 상황 대처 능력이 뛰어나 여러 가능성을 고려하며 최고의 선택을 내리는 경향이 있어요. " +
                        "당신이 우연히 발견한 갯벌 속 진주 같은 가게를 추천해준다면 그 가게는 틀림없이 널리 알려야 할 맛집일 거예요.");
            }

            if (isSensitivity) {
                sb.append("자신의 몫은 제대로 챙기고 맡은 것도 꼼꼼하게 해내는 경향이 있어 보여요! 깐깐함과 꼼꼼함은 한 끗 차이겠죠! 당신의 식당 평가는 비슷한 성향의 분들께 좋은 참고가 될 거예요. " +
                        "깔끔하고 군더더기 없는 완벽한 가게를 찾기 위해 발품 팔아왔던 지난날을 떠올려 보아요…." +
                        " 나만 알고 있고 싶은 장소일 수도 있지만, 우리 함께 공유한다면 데이터가 모여 더 좋은 가게들에 대한 정보를 얻을 수 있을 거예요\uD83D\uDE09 ");
            } else {
                sb.append("무인도에 던져져도 먹고 잘 수만 있다면 그럴 수도 있지…. 하고 순응할 성향이에요! 화를 잘 내지 않고 받아들일 수 있는 태도 덕분에 불만을 제기하며 싸운 경험도 거의 없을 거예요. " +
                        "세상을 둥글고 조금은 느긋하게 살아가는 태도 덕분에 감정 기복도 적고 주변을 편안하게 만들어주는 사람이에요. 어떤 상대든지 기복 없이 맞춰줄 수 있기에 사람들이 주변에서 편안함을 느껴요. ");
            }

            if (isSociable) {
                sb.append("인싸의 삶을 즐기고 계신 당신…. 부럽습니다. 친화적인 성격으로 알고 지내는 여러 가게의 직원분들이 있을 것만 같아요. " +
                        "단골 가게의 직원뿐 아니라 처음 가는 가게더라도 비슷한 성향의 직원을 만나면 스스럼없이 이야기를 나눌 수 있는 인싸력이 돋보여요\uD83D\uDE0E  ");
            } else {
                sb.append("- 식사하러 또는 음료를 마시러 갔을 뿐인데 자주 시키는 메뉴를 기억하는 직원이라니\uD83D\uDE31 이런 관심은 조금 부담스럽게 느껴지지요. 말을 거는 직원이 부담스러워서 단골 가게를 아쉽게도 떠나보내야 했던 적이 있을 것 같아요. " +
                        "NPC라고 여겼던 직원에게 독립적인 존재로 인식되는 순간 당신의 대처를 통해 당신의 상황 판단 능력을 확인할 수 있어요. 적당한 비즈니스적 친절을 제공해서 여러분의 마음에 평안함이 오는 장소를 함께 공유해 보아요!");
            }

            UserPreferenceDto.PlacePreferenceResponse placePreferenceResponse =
                UserPreferenceDto.PlacePreferenceResponse.builder()
                        .challenging(isChallenging)
                        .planning(isPlanning)
                        .sociable(isSociable)
                        .sensitivity(isSensitivity)
                        .explanation(sb.toString())
                        .build();

            linkedHashmap.put("userNickname", curUser.get().getNickname());
            linkedHashmap.put("userAsAnimal", userAsAnimal);
            linkedHashmap.put("tastePreference", tastePreferenceResponse);
            linkedHashmap.put("placePreference", placePreferenceResponse);
            return Response.newResult(HttpStatus.OK, "취향을 알려드립니다.", linkedHashmap);
        }
        public static String getUserAnimalType(boolean isChallenging, boolean isPlanning, boolean isSensitivity, boolean isSociable){
            String userAsAnimal = "";
            if (isChallenging && !isPlanning) {
                if (isSensitivity && isSociable) {
                    userAsAnimal = "다재다능한 여우";
                } else if (isSensitivity) {
                    userAsAnimal = "모험을 좋아하는 복어";
                } else if (isSociable) {
                    userAsAnimal = "외출을 좋아하는 강아지";
                } else {
                    userAsAnimal = "매력적인 앵무새";
                }
            } else if (isChallenging) { // 모험 계획
                if (isSensitivity && isSociable) { // 외향 민감
                    userAsAnimal = "호기심 많은 꿀벌";
                } else if (isSensitivity) { // 내향 민감
                    userAsAnimal = "늠름한 토끼";
                } else if (isSociable) { //외향 무던
                    userAsAnimal = "붙임성 있는 펭귄";
                } else {
                    userAsAnimal = "생기발랄한 개미";
                }
            } else if (!isPlanning) { // 안정 즉흥
                if (isSociable && isSensitivity) {
                    userAsAnimal = "천진난만한 미어캣";
                } else if (isSensitivity) {
                    userAsAnimal = "긍정적인 고슴도치";
                } else if (isSociable) {
                    userAsAnimal = "낭만적인 치타";
                } else {
                    userAsAnimal = "온순한 나무늘보";
                }
            } else {
                if (isSociable && isSensitivity) {
                    userAsAnimal = "믿음직스러운 게코 도마뱀";
                } else if (isSociable) {
                    userAsAnimal = "상냥한 반달가슴곰";
                } else if (isSensitivity) {
                    userAsAnimal = "완벽을 추구하는 랫서팬더";
                } else {
                    userAsAnimal = "정의로운 거북이";
                }
            }
            return userAsAnimal;
        }
    }