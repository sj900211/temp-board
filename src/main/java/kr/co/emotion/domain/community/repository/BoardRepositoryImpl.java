package kr.co.emotion.domain.community.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.emotion.domain.community.entity.Board;
import kr.co.emotion.domain.community.vo.CommunitySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static kr.co.emotion.domain.community.entity.QBoard.board;

/**
 * 커뮤니티 관리 > 게시글 Repository Impl
 *
 * @since 2021-02-19 @author 류성재
 */
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Data 조회 - Page
     */
    @Override
    public Page<Board> getPage(CommunitySearch search) {
        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .where(board.delFlag.isFalse(), board.useFlag.isTrue());

        // 등록 날짜 시간 기간 조회 - by 시작
        LocalDate startD = search.getSd();

        if (!ObjectUtils.isEmpty(startD)) {
            query.where(board.createDt.goe(startD.atStartOfDay()));
        }

        // 등록 날짜 시간 기간 조회 - by 종료
        LocalDate endD = search.getEd();

        if (!ObjectUtils.isEmpty(endD)) {
            query.where(board.createDt.loe(endD.atStartOfDay()));
        }

        // 자연어 검색
        String word = search.getWord();

        if (!StringUtils.isEmpty(word)) { // 검색어가 있을 경우
            String key = search.getKey();

            // 제목으로 검색
            if (key.equalsIgnoreCase("title")) {
                query.where(board.title.containsIgnoreCase(word));
            }

            // 내용으로 검색
            if (key.equalsIgnoreCase("content")) {
                query.where(board.content.containsIgnoreCase(word));
            }

            // 전체 검색
            if (StringUtils.isEmpty(key)) {
                query.where(board.title.containsIgnoreCase(word)
                        .or(board.content.containsIgnoreCase(word)));
            }
        }

        // 정렬
        String otarget = search.getOtarget();
        String otype = search.getOtype();
        String orderTarget = StringUtils.isEmpty(otarget) ? "" : otarget;
        String orderType = StringUtils.isEmpty(otype) ? Order.DESC.name() : otype;

        if (orderTarget.equalsIgnoreCase("title")) { // 제목으로 정렬
            query.orderBy(new OrderSpecifier<>(orderType.contentEquals("desc") ? Order.DESC : Order.ASC, board.title));
        }

        if (orderTarget.equalsIgnoreCase("create")) { // 등록 날짜 시간 기준으로 정렬
            query.orderBy(new OrderSpecifier<>(orderType.contentEquals("desc") ? Order.DESC : Order.ASC, board.createDt));
        }

        query.orderBy(board.id.desc()); // 기본 정렬 조건 추가

        // 페이징 객체 생성
        PageRequest pageRequest = PageRequest.of(search.getPage(), search.getCpp());

        // 페이징 처리
        query.offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize());

        // 쿼리 실행
        QueryResults<Board> result = query.fetchResults();

        // 페이지 객체로 반환
        return new PageImpl<>(result.getResults(), pageRequest, result.getTotal());
    }

}
