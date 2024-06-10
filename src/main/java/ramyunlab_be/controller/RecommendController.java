package ramyunlab_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ramyunlab_be.dto.RecommendDTO;
import ramyunlab_be.dto.ResDTO;
import ramyunlab_be.entity.RecommendEntity;
import ramyunlab_be.service.RecommendService;
import ramyunlab_be.vo.StatusCode;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces="application/json; charset=utf8")
@Tag(name = "Recommend", description = "공감 관련 API")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Operation(summary = "공감 추가", description = "토큰만 있으면 됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "공감 추가 성공"),
        @ApiResponse(responseCode = "400")
    })
    @PostMapping("/recommend/{rvIdx}")
    public ResponseEntity<ResDTO> recommend(@PathVariable Long rvIdx,
                                            @AuthenticationPrincipal String userIdx){
        RecommendEntity addRecommend = recommendService.create(rvIdx, userIdx);

        RecommendDTO responseRecommendDTO = RecommendDTO.builder()
            .recommendIdx(addRecommend.getRecommendIdx())
            .userIdx(addRecommend.getUser().getUserIdx())
            .recCreatedAt(addRecommend.getRecCreatedAt())
            .reviewIdx(addRecommend.getReview().getRvIdx())
            .build();

        return ResponseEntity.ok().body(ResDTO
            .builder()
            .statusCode(StatusCode.OK)
            .data(responseRecommendDTO)
            .message("공감 추가 성공")
            .build());
    }

    @DeleteMapping("/recommend/{recommendIdx}")
    public ResponseEntity<ResDTO> deleteRecommend(@PathVariable Long recommendIdx,
                                                   @AuthenticationPrincipal String userIdx){
        recommendService.delete(recommendIdx, userIdx);
        return ResponseEntity.ok().body(ResDTO
           .builder()
           .statusCode(StatusCode.OK)
           .message("공감 삭제 성공")
           .build());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResDTO> handleValidationException(ValidationException e) {
        return ResponseEntity
            .badRequest()
            .body(ResDTO.builder().statusCode(StatusCode.BAD_REQUEST).message(e.getMessage()).build());
    }
}
