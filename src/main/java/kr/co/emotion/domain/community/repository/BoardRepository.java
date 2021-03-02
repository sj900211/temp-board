package kr.co.emotion.domain.community.repository;

import kr.co.emotion.domain.community.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 커뮤니티 관리 > 게시글 Repository
 *
 * @since 2021-02-19 @author 류성재
 */
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
