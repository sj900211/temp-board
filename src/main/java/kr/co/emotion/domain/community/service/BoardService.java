package kr.co.emotion.domain.community.service;

import kr.co.emotion.domain.community.entity.Board;
import kr.co.emotion.domain.community.repository.BoardRepository;
import kr.co.emotion.domain.community.vo.CommunitySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * 커뮤니티 관리 > 게시글 Service
 *
 * @since 2021-02-19 @author 류성재
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository repository;

    /**
     * 생성 - Single
     */
    @Transactional
    public Long create(Board entity) {
        return repository.save(entity).getId();
    }

    /**
     * Data 조회 by ID - Single
     */
    public Board get(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Data 조회 - Page
     */
    public Page<Board> getPage(CommunitySearch search) {
        return repository.getPage(search);
    }

}
