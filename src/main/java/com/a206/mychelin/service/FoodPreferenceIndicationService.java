package com.a206.mychelin.service;

import java.util.HashMap;
import java.util.Map;

public class FoodPreferenceIndicationService {
    int[] choices;


    public Map<String, Double> getResult(Object[] tastes, Object[] choices) {
        Map<String, Double> map = new HashMap<>();
        int[] res = new int[5];
        // sour 10, sweet 10, salty 12, oily 12, spicy 6 * 2
        for (int i = 0; i < 2; i++) {
            res[i] = 10 - (((char)tastes[2 * i] - 97) * 3) - (((char)tastes[2 * i + 1] - 97) * 2);
        }
        for (int i = 3; i < 4; i++) {
            res[i] = 12 - (((char)tastes[2 * i] - 97) * 3) - (((char)tastes[3] - 97) * 3);
        }
        res[4] = 6 - (((char)tastes[8] - 97) * 2);
//        res[0] = 10 - (((char)tastes[0] - 97) * 3) - (((char)tastes[1] - 97) * 2);
//        res[1] = 10 - (((char)tastes[2] - 97) * 3) - (((char)tastes[3] - 97) * 2);
//        res[2] = 10 - (((char)tastes[4] - 97) * 3) - (((char)tastes[3] - 97) * 2);
        return map;
    }
}