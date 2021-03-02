package kr.co.emotion.controller;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.emotion.domain.community.dto.request.BoardCreateRequest;
import kr.co.emotion.domain.community.dto.request.BoardUpdateRequest;
import kr.co.emotion.domain.community.vo.CommunitySearch;
import kr.co.emotion.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.co.emotion.common.config.URIConfig.uriCommunityBoard;
import static kr.co.emotion.common.config.URIConfig.uriCommunityBoardId;

/**
 * 커뮤니티 관리 Controller
 *
 * @since 2020-08-10 @author 류성재
 */
@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService service;

    // .______     ______        ___      .______       _______
    // |   _  \   /  __  \      /   \     |   _  \     |       \
    // |  |_)  | |  |  |  |    /  ^  \    |  |_)  |    |  .--.  |
    // |   _  <  |  |  |  |   /  /_\  \   |      /     |  |  |  |
    // |  |_)  | |  `--'  |  /  _____  \  |  |\  \----.|  '--'  |
    // |______/   \______/  /__/     \__\ | _| `._____||_______/

    /**
     * 게시글 조회 - Page
     */
    @GetMapping(uriCommunityBoard)
    public ResponseEntity<JsonNode> getBoardPage(CommunitySearch search) {
        return service.getBoardPage(search);
    }

    /**
     * 게시글 조회
     */
    @GetMapping(uriCommunityBoardId)
    public ResponseEntity<JsonNode> getBoardPage(@PathVariable Long id) {
        return service.getBoard(id);
    }

    /**
     * 게시글 등록
     */
    @PostMapping(uriCommunityBoard)
    public ResponseEntity<JsonNode> createBoard(@RequestBody BoardCreateRequest dto) {
        return service.createBoard(dto);
    }

    /**
     * 게시글 수정
     */
    @PutMapping(uriCommunityBoardId)
    public ResponseEntity<JsonNode> updateBoard(@PathVariable Long id, @RequestBody BoardUpdateRequest dto) {
        return service.updateBoard(id, dto);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping(uriCommunityBoardId)
    public ResponseEntity<JsonNode> updateBoard(@PathVariable Long id) {
        return service.removeBoard(id);
    }

}
