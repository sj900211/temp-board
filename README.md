# Test
> ## Class Annotation
>> ``` java
>> @ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
>> @SpringBootTest
>> @AutoConfigureRestDocs
>> @AutoConfigureMockMvc
>> @Transactional
>> @TestInstance(TestInstance.Lifecycle.PER_CLASS)
>> ```
> ## Common & Docs
>> ``` java
>> /**
>>     * ObjectMapper
>>     * RequestBody 내용을 설정하기 위한 Class
>>     */
>> @Autowired
>> private ObjectMapper objectMapper;
>> 
>> /**
>>     * EntityManager 영속성 컨텍스트
>>     */
>> @Autowired
>> private EntityManager entityManager;
>> 
>> /**
>>     * MockMvc
>>     * 서버에 배포하지 않고 Spring MVC 동작을 재현할 수 있는 Class
>>     * Test 기능의 핵심
>>     */
>> private MockMvc mockMvc;
>> 
>> /**
>>     * RestDocumentationResultHandler
>>     * 문서를 출력하기 위한 Class
>>     */
>> private RestDocumentationResultHandler document;
>> 
>> /**
>>     * 각 Test 실행 전에 실행되는 메서드
>>     */
>> @BeforeEach
>> public void beforeEach(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
>>     this.document = document(
>>             "{class-name}/{method-name}", // 문서 경로 설정
>>             preprocessRequest( // Request 설정
>>                     modifyUris()
>>                             .scheme("https")
>>                             .host("rest.emotion.co.kr"), // 문서에 노출되는 도메인 설정
>>                     prettyPrint() // 정리해서 출력
>>             ),
>>             preprocessResponse(prettyPrint()) // Response 설정. 정리해서 출력
>>     );
>> 
>>     this.mockMvc = MockMvcBuilders // MockMvc 공통 설정. 문서 출력 설정
>>             .webAppContextSetup(webApplicationContext)
>>             .addFilter(new CharacterEncodingFilter("UTF-8", true))
>>             .apply(documentationConfiguration(restDocumentation))
>>             .alwaysDo(document)
>>             .build();
>> }
>> 
>> @Autowired
>> private BoardService boardService;
>> ```

# Docs
> ``` adoc
> ifndef::snippets[]
> :snippets: ../../../target/generated-snippets
> endif::[]
> :toc: left
> :toclevels: 3
> :sectlinks:
> ```

# Deploy
> ## Portainer
> http://10.80.1.212:9000
> ## Port
> 최재필 책임 리더님 > 30100
> 최규필 책임 리더님 > 30200
> 석원석 책임 리더님 > 30300
> 오지훈 리더님 > 30400
> 이소라 리더님 > 30500
> 이소정님 > 30600
> 정주희님 > 30700
> 송지오님 > 30800
> 조한신님 > 30900
