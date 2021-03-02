package kr.co.emotion.domain.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 커뮤니티 관리 > 게시글 수정 Request
 *
 * @since 2021-02-19 @author 류성재
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {

    // 제목
    private String title;
    // 내용
    private String content;

}
