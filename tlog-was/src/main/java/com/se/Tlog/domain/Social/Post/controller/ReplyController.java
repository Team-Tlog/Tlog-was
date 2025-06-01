package com.se.Tlog.domain.Social.Post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Social.Post.application.ReplyService;
import com.se.Tlog.domain.Social.Post.controller.dto.CreateReplyReq;
import com.se.Tlog.domain.Social.Post.controller.dto.ReplyRes;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "SNS - 댓글")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/post/{postId}/reply")
    @Operation (
            summary = "게시물에 댓글 작성",
            description = "특정 게시물에 댓글을 작성합니다.<br>등록에 성공한 댓글 데이터를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<ReplyRes>> createReplyToPost(@PathVariable(name = "postId") String postId, @RequestBody CreateReplyReq request) {
        String newReplyId = replyService.attachReplyToPost(postId, request.author(), request.content());
        
        return ResponseEntity.ok(SuccessRes.from(
                replyService.getReply(newReplyId)));
    }

    @PostMapping("/reply/{replyId}/reply")
    @Operation (
            summary = "댓글에 대댓글 작성",
            description = "특정 댓글에 대댓글을 작성합니다.<br>등록에 성공한 댓글 데이터를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<ReplyRes>> createReplyToReply(@PathVariable(name = "replyId") String replyId, @RequestBody CreateReplyReq request) {
        String newReplyId = replyService.attachReplyToReply(replyId, request.author(), request.content());
        
        return ResponseEntity.ok(SuccessRes.from(
                replyService.getReply(newReplyId)));
    }

    @GetMapping("/post/{postId}/replys")
    @Operation (
            summary = "코스 리뷰(게시글)의 댓글 조회",
            description = "특정 코스 리뷰(게시글)의 댓글을 조회합니다."
                        + "<br>Paging 대신 <b>lastReplyId</b> 및 <b>size</b>를 사용하여 해당 댓글 이후부터 조회합니다."
                        + "<br><b>lastReplyId</b>가 <code>null</code>(기입X)이면 가장 첫 댓글부터 조회합니다."
                        + "<br>"
                        + "<br>- <b>lastReplyId</b>는 ReplyId 형식에 맞아야 합니다."
                        + "<br>- <b>size</b>는 1 이상이어야 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<ReplyRes>>> getReplyOfPost(
            @PathVariable(name = "postId") String postId,
            @RequestParam(required = false) String lastReplyId,
            @RequestParam int size) {
        return ResponseEntity.ok(SuccessRes.from(
                replyService.getReplyResOfPost(size, lastReplyId, postId)));
    }

    @GetMapping("/reply/{replyId}/replys")
    @Operation (
            summary = "댓글의 대댓글 조회",
            description = "특정 댓글의 대댓글을 조회합니다."
                        + "<br>Paging 대신 <b>lastReplyId</b> 및 <b>size</b>를 사용하여 해당 댓글 이후부터 조회합니다."
                        + "<br><b>lastReplyId</b>가 <code>null</code>(기입X)이면 가장 첫 댓글부터 조회합니다."
                        + "<br>"
                        + "<br>- <b>lastReplyId</b>는 ReplyId 형식에 맞아야 합니다."
                        + "<br>- <b>size</b>는 1 이상이어야 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<ReplyRes>>> getReplyOfReply(
            @PathVariable(name = "replyId") String replyId,
            @RequestParam(required = false) String lastReplyId,
            @RequestParam int size) {
        return ResponseEntity.ok(SuccessRes.from(
                replyService.getReplyResOfReply(size, lastReplyId, replyId)));
    }
}
