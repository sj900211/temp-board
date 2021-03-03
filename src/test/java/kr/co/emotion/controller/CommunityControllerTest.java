package kr.co.emotion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.emotion.domain.community.dto.request.BoardCreateRequest;
import kr.co.emotion.domain.community.dto.request.BoardUpdateRequest;
import kr.co.emotion.domain.community.entity.Board;
import kr.co.emotion.domain.community.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static kr.co.emotion.common.config.URIConfig.uriCommunityBoard;
import static kr.co.emotion.common.config.URIConfig.uriCommunityBoardId;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 커뮤니티 관리 Controller Test
 *
 * @since 2021-02-19 @author 류성재
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommunityControllerTest {

    /**
     * ObjectMapper
     * RequestBody 내용을 설정하기 위한 Class
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * MockMvc
     * 서버에 배포하지 않고 Spring MVC 동작을 재현할 수 있는 Class
     * Test 기능의 핵심
     */
    private MockMvc mockMvc;

    /**
     * RestDocumentationResultHandler
     * 문서를 출력하기 위한 Class
     */
    private RestDocumentationResultHandler document;

    /**
     * 각 Test 실행 전에 실행되는 메서드
     */
    @BeforeEach
    public void beforeEach(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.document = document(
                "{class-name}/{method-name}", // 문서 경로 설정
                preprocessRequest( // Request 설정
                        modifyUris()
                                .scheme("https")
                                .host("rest.emotion.co.kr"), // 문서에 노출되는 도메인 설정
                        prettyPrint() // 정리해서 출력
                ),
                preprocessResponse(prettyPrint()) // Response 설정. 정리해서 출력
        );

        this.mockMvc = MockMvcBuilders // MockMvc 공통 설정. 문서 출력 설정
                .webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document)
                .build();
    }

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("게시글 조회 - Page")
    public void getBoardPage() throws Exception {
        for (int i = 0; i < 15; i++) {
            boardService.create(Board.createEntity("test title " + i, "test content " + i));
        }

        mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get(uriCommunityBoard)
                        .param("page", "0")
                        .param("cpp", "5")
                        .param("key", "title")
                        .param("word", "test")
                        .param("otarget", "title")
                        .param("otype", "desc")
                        .param("sd", "2021-01-01")
                        .param("ed", "2021-12-31")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andDo(print())
                .andDo(document.document(
                        RequestDocumentation.requestParameters(
                                // RequestDocumentation.parameterWithName("파라미터 이름").description("설명")
                                RequestDocumentation.parameterWithName("page").description("조회할 페이지 번호 [0 부터 시작]"),
                                RequestDocumentation.parameterWithName("cpp").description("조회할 페이지 데이터 수"),
                                RequestDocumentation.parameterWithName("key").description("자연어 검색 대상").optional(),
                                RequestDocumentation.parameterWithName("word").description("자연어 검색 문자").optional(),
                                RequestDocumentation.parameterWithName("otarget").description("정렬 대상").optional(),
                                RequestDocumentation.parameterWithName("otype").description("정렬 유형").optional(),
                                RequestDocumentation.parameterWithName("sd").description("등록일 기간 조회 시작일").optional(),
                                RequestDocumentation.parameterWithName("ed").description("등록일 기간 조회 종료일").optional()
                        ),
                        PayloadDocumentation.responseFields(
                                // PayloadDocumentation.fieldWithPath("경로").type(유형).description("설명")
                                // PayloadDocumentation.fieldWithPath("경로.경로").type(유형).description("설명") -> {}
                                // PayloadDocumentation.fieldWithPath("경로[].경로").type(유형).description("설명") -> []
                                PayloadDocumentation.fieldWithPath("page").type(OBJECT).description("페이지 반환 객체"),
                                PayloadDocumentation.fieldWithPath("page.content").type(ARRAY).description("페이지 데이터 목록"),
                                PayloadDocumentation.fieldWithPath("page.content[].id").type(NUMBER).description("게시글 일련 번호"),
                                PayloadDocumentation.fieldWithPath("page.content[].title").type(STRING).description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("page.content[].hit").type(NUMBER).description("게시글 조회수"),
                                PayloadDocumentation.fieldWithPath("page.content[].createDt").type(STRING).attributes(Attributes.key("format").value("yyyy-MM-dd HH:mm:ss")).description("게시글 등록일"),
                                PayloadDocumentation.fieldWithPath("page.pageable").type(OBJECT).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.sort").type(OBJECT).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.sort.sorted").type(BOOLEAN).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.sort.unsorted").type(BOOLEAN).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.sort.empty").type(BOOLEAN).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.offset").type(NUMBER).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.pageSize").type(NUMBER).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.pageNumber").type(NUMBER).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.unpaged").type(BOOLEAN).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.pageable.paged").type(BOOLEAN).description("페이징 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.totalElements").type(NUMBER).description("총 데이터 수"),
                                PayloadDocumentation.fieldWithPath("page.last").type(BOOLEAN).description("마지막 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("page.totalPages").type(NUMBER).description("총 페이지 수"),
                                PayloadDocumentation.fieldWithPath("page.size").type(NUMBER).description("요청한 페이지 데이터 수"),
                                PayloadDocumentation.fieldWithPath("page.number").type(NUMBER).description("요청한 페이지 번호"),
                                PayloadDocumentation.fieldWithPath("page.sort").type(OBJECT).description("정렬 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.sort.sorted").type(BOOLEAN).description("정렬 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.sort.unsorted").type(BOOLEAN).description("정렬 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.sort.empty").type(BOOLEAN).description("정렬 관련 데이터 객체"),
                                PayloadDocumentation.fieldWithPath("page.numberOfElements").type(NUMBER).description("요청한 페이지의 데이터 수"),
                                PayloadDocumentation.fieldWithPath("page.first").type(BOOLEAN).description("첫 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("page.empty").type(BOOLEAN).description("데이터가 비어있는지 여부")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 조회")
    public void getBoard() throws Exception {
        Long id = boardService.create(Board.createEntity("test title", "test content"));

        Board entity = boardService.get(id);

        entity.addHit();

        mockMvc.perform(
                RestDocumentationRequestBuilders.get(uriCommunityBoardId, id)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andDo(print())
                .andDo(document.document(
                        RequestDocumentation.pathParameters(
                                // RequestDocumentation.parameterWithName("파라미터 이름").description("설명")
                                RequestDocumentation.parameterWithName("id").description("게시글 일련 번호")
                        ),
                        PayloadDocumentation.responseFields(
                                // PayloadDocumentation.fieldWithPath("경로").type(유형).description("설명")
                                // PayloadDocumentation.fieldWithPath("경로.경로").type(유형).description("설명") -> {}
                                // PayloadDocumentation.fieldWithPath("경로[].경로").type(유형).description("설명") -> []
                                PayloadDocumentation.fieldWithPath("data").type(OBJECT).description("반환 객체"),
                                PayloadDocumentation.fieldWithPath("data.id").type(NUMBER).description("게시글 일련 번호"),
                                PayloadDocumentation.fieldWithPath("data.title").type(STRING).description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("data.content").type(STRING).description("게시글 내용"),
                                PayloadDocumentation.fieldWithPath("data.hit").type(NUMBER).description("게시글 조회수"),
                                PayloadDocumentation.fieldWithPath("data.createDt").type(STRING).attributes(Attributes.key("format").value("yyyy-MM-dd HH:mm:ss")).description("게시글 등록일")
                        )
                ))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("게시글 등록")
    public void createBoard() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.post(uriCommunityBoard)
                        .content(
                                objectMapper.writeValueAsString(BoardCreateRequest
                                        .builder()
                                        .title("test title")
                                        .content("test content")
                                        .build())
                        )
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andDo(print())
                .andDo(document.document(
                        PayloadDocumentation.requestFields(
                                // PayloadDocumentation.fieldWithPath("경로").type(유형).description("설명")
                                // PayloadDocumentation.fieldWithPath("경로.경로").type(유형).description("설명") -> {}
                                // PayloadDocumentation.fieldWithPath("경로[].경로").type(유형).description("설명") -> []
                                PayloadDocumentation.fieldWithPath("title").type(STRING).description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("content").type(STRING).description("게시글 내용")
                        )
                ))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("게시글 수정")
    public void updateBoard() throws Exception {
        Long id = boardService.create(Board.createEntity("test title", "test content"));

        mockMvc.perform(
                RestDocumentationRequestBuilders.put(uriCommunityBoardId, id)
                        .content(
                                objectMapper.writeValueAsString(BoardUpdateRequest
                                        .builder()
                                        .title("update title")
                                        .content("update content")
                                        .build())
                        )
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andDo(print())
                .andDo(document.document(
                        RequestDocumentation.pathParameters(
                                // RequestDocumentation.parameterWithName("파라미터 이름").description("설명")
                                RequestDocumentation.parameterWithName("id").description("게시글 일련 번호")
                        ),
                        PayloadDocumentation.requestFields(
                                // PayloadDocumentation.fieldWithPath("경로").type(유형).description("설명")
                                // PayloadDocumentation.fieldWithPath("경로.경로").type(유형).description("설명") -> {}
                                // PayloadDocumentation.fieldWithPath("경로[].경로").type(유형).description("설명") -> []
                                PayloadDocumentation.fieldWithPath("title").type(STRING).description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("content").type(STRING).description("게시글 내용")
                        )
                ))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("게시글 삭제")
    public void deleteBoard() throws Exception {
        Long id = boardService.create(Board.createEntity("test title", "test content"));

        mockMvc.perform(
                RestDocumentationRequestBuilders.delete(uriCommunityBoardId, id)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andDo(print())
                .andDo(document.document(RequestDocumentation.pathParameters(
                        // RequestDocumentation.parameterWithName("파라미터 이름").description("설명")
                        RequestDocumentation.parameterWithName("id").description("게시글 일련 번호")
                )))
                .andExpect(status().isOk());
    }

}
