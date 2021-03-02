package kr.co.emotion.domain.community.repository;

import kr.co.emotion.domain.community.entity.Board;
import kr.co.emotion.domain.community.vo.CommunitySearch;
import org.springframework.data.domain.Page;

/**
 * 커뮤니티 관리 > 게시글 Repository Custom
 *
 * @since 2021-02-19 @author 류성재
 */
public interface BoardRepositoryCustom {

    /**
     * Data 조회 - Page
     */
    Page<Board> getPage(CommunitySearch search);

}
