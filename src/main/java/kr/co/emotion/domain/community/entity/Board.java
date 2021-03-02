package kr.co.emotion.domain.community.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 커뮤니티 관리 > 게시글 관리
 *
 * @since 2021-02-19 @author 류성재
 */
@Entity
@Table(name = "TB_CMNT_BOARD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 50
    )
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private Integer hit;

    private Boolean useFlag;

    private Boolean delFlag;

    @CreationTimestamp
    private LocalDateTime createDt;

    @UpdateTimestamp
    private LocalDateTime updateDt;

    /**
     * 생성자
     */
    private Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 생성 메서드
     */
    public static Board createEntity(String title, String content) {
        return new Board(title, content);
    }

    /**
     * 수정 메서드
     */
    public void updateEntity(String title, String content) {
        this.title = title;
        this.content = content;
        this.updateDt = LocalDateTime.now();
    }

    /**
     * 조회수 증가 메서드
     */
    public void addHit() {
        this.hit++;
    }

    /**
     * 논리 삭제
     */
    public void removeEntity() {
        this.useFlag = false;
        this.delFlag = true;
        this.updateDt = LocalDateTime.now();
    }

    /**
     * 등록 로직에서 기본값 설정
     */
    @PrePersist
    private void prePersist() {
        this.hit = 0;
        this.useFlag = true;
        this.delFlag = false;
    }

}
