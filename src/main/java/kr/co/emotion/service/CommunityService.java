package kr.co.emotion.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.emotion.domain.community.dto.request.BoardCreateRequest;
import kr.co.emotion.domain.community.dto.request.BoardUpdateRequest;
import kr.co.emotion.domain.community.dto.response.BoardListResponse;
import kr.co.emotion.domain.community.dto.response.BoardResponse;
import kr.co.emotion.domain.community.entity.Board;
import kr.co.emotion.domain.community.service.BoardService;
import kr.co.emotion.domain.community.vo.CommunitySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * 커뮤니티 관리 Service
 *
 * @since 2021-02-19 @author 류성재
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final ObjectMapper objectMapper;
    private final BoardService boardService;

    // .______     ______        ___      .______       _______
    // |   _  \   /  __  \      /   \     |   _  \     |       \
    // |  |_)  | |  |  |  |    /  ^  \    |  |_)  |    |  .--.  |
    // |   _  <  |  |  |  |   /  /_\  \   |      /     |  |  |  |
    // |  |_)  | |  `--'  |  /  _____  \  |  |\  \----.|  '--'  |
    // |______/   \______/  /__/     \__\ | _| `._____||_______/

    /**
     * 게시글 조회 - Page
     */
    public ResponseEntity<JsonNode> getBoardPage(CommunitySearch search) {
        HashMap<String, Object> map = new HashMap<>();

        Page<BoardListResponse> page = boardService
                .getPage(search)
                .map(entity -> {
                    BoardListResponse data = new BoardListResponse();

                    data.setId(entity.getId());
                    data.setTitle(entity.getTitle());
                    data.setHit(entity.getHit());
                    data.setCreateDt(entity.getCreateDt());

                    return data;
                });

        map.put("page", page);

        return ResponseEntity.ok()
                .body(objectMapper.valueToTree(map));
    }

    /**
     * 게시글 조회
     */
    public ResponseEntity<JsonNode> getBoard(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        Board entity = boardService.get(id);

        entity.addHit();

        BoardResponse data = new BoardResponse();

        data.setId(entity.getId());
        data.setTitle(entity.getTitle());
        data.setContent(entity.getContent());
        data.setHit(entity.getHit());
        data.setCreateDt(entity.getCreateDt());

        map.put("data", data);

        return ResponseEntity.ok()
                .body(objectMapper.valueToTree(map));
    }

    /**
     * 게시글 생성
     */
    @Transactional
    public ResponseEntity<JsonNode> createBoard(BoardCreateRequest dto) {
        HashMap<String, Object> map = new HashMap<>();

        long id = boardService.create(Board.createEntity(
                dto.getTitle(),
                dto.getContent()
        ));

        map.put("id", id);

        return ResponseEntity.ok()
                .body(objectMapper.valueToTree(map));
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public ResponseEntity<JsonNode> updateBoard(Long id, BoardUpdateRequest dto) {
        Board entity = boardService.get(id);

        entity.updateEntity(dto.getTitle(), dto.getContent());

        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public ResponseEntity<JsonNode> removeBoard(Long id) {
        Board entity = boardService.get(id);

        entity.removeEntity();

        return ResponseEntity.ok().build();
    }

}
