package com.se.Tlog.domain.Tbti.controller;

import com.se.Tlog.domain.Tbti.dto.TbtiQuestionReq;
import com.se.Tlog.domain.Tbti.service.TbtiService;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/tbti")
@RequiredArgsConstructor
public class TbtiController {

    final TbtiService tbtiService;

    @GetMapping
    public ResponseEntity<?> getAllTbtiQuestion(
            @RequestParam(name = "categories", required = false) String traitCategory,
            @PageableDefault(size = 10) Pageable pageable
    ){
        if (traitCategory == null || traitCategory.isEmpty())
            return ResponseEntity.ok().body(SuccessRes.from(tbtiService.getAllTbtiQuestion(pageable)));
        return ResponseEntity.ok().body(SuccessRes.from(tbtiService.getAllTbtiQuestionByTraitCategory(traitCategory, pageable)));
    }

    @PostMapping
    public ResponseEntity<?> createTbtiQuestion(@RequestBody TbtiQuestionReq tbtiQuestionReq){
        tbtiService.createTbtiQuestion(tbtiQuestionReq);

        return ResponseEntity.ok()
                .body(SuccessRes.from(SuccessType.OK));
    }
    @DeleteMapping("/{tbtiQuestionId}")
    public ResponseEntity<?> deleteTbtiQuestion(@PathVariable(name = "tbtiQuestionId") UUID tbtiQuestionId) {
        tbtiService.deleteTbtiQuestion(tbtiQuestionId);
        return ResponseEntity.ok()
                .body(SuccessRes.from(SuccessType.OK));
    }



}
