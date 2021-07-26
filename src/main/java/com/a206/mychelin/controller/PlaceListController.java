package com.a206.mychelin.controller;

import com.a206.mychelin.config.AuthConstants;
import com.a206.mychelin.domain.entity.PlaceList;
import com.a206.mychelin.domain.entity.PlaceListItem;
import com.a206.mychelin.service.PlaceListService;
import com.a206.mychelin.util.TokenUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/placelist")
@RequiredArgsConstructor
public class PlaceListController {

    final private PlaceListService placeListService;

    @PostMapping
    public ResponseEntity createPlaceList(@RequestHeader(AuthConstants.AUTH_HEADER) String myToken, @RequestBody PlaceList placeList){

//        String token = TokenUtils.getTokenFromHeader(myToken);
//        String useId = TokenUtils.getUserIdFromToken(token);

        return placeListService.createPlaceList(placeList);
    }

    @GetMapping("/searchid/{id}")
    public ResponseEntity searchPlaceListById(@PathVariable int id){
        return placeListService.searchPlaceListById(id);
    }

}
