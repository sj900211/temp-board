package kr.co.emotion.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * QueryDSL Config
 *
 * @since 2021-02-19 @author 류성재
 */
@Configuration
public class QueryDslConfig {

    // EntityManager 영속성 컨텍스트 설정
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory Bean 등록
     */
    @Bean
    public JPAQueryFactory jPAQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
