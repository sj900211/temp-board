package kr.co.emotion.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 커뮤니티 관리 > 게시글 Response
 *
 * @since 2021-02-19 @author 류성재
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private Integer hit;
    private LocalDateTime createDt;

}
