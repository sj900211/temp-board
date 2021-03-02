package kr.co.emotion.domain.community.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 커뮤니티 검색 관리
 *
 * @since 2021-02-19 @author 류성재
 */
@Data
public class CommunitySearch {

    private Integer page;
    private Integer cpp; // Count Per Page
    private String key;
    private String word;
    private String otarget; // order target
    private String otype; // order type
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sd; // start date
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ed; // end date

}
