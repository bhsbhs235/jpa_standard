package org.example;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
// @Table(name="USER") 테이블 지정
/*@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 1
)*/
public class Member {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*
    GenerationType.Identity
    기본키 생성을 데이터베이스에 위임 Mysql은 AUTO_INCREMENT

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MEMBER_SEQ_GENERATOR")
     */
    private Long id;

    /* @Column(name = "user_name") 컬럼지정 insertable, updatable 등록,변경 가능 여부, nullable null값 허용, length, columnDefinition 컬럼정보를 직접 줄 수 있다.
     */
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING) // Enum타입 매핑 EnumType.ORDINAL, EnumType.STRING
    private RoleType roleType;

    @Temporal(TemporalType.DATE) // 날짜 매핑
    private Date createDate;
    //private localDate createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    // private localDateTime updateDate;

    @Lob // 큰 텍스트 clob blob longtext 등
    private String description;

    @Transient // 특정 필드를 컬럼에서 제외
    private int temp;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 프로시로 조회, 굳이 Team정보를 조회할 경우가 별로 없을 때 ( 가급적이면 지연 로딩으로 쓰자 필요없을 때도 예상하지 못한 sql이 발생할 수도 있회)
    @JoinColumn(name = "team_id") // N:1 일 때, N인 객체 테이블 모델에서 외래키가 있는 객체가 연관간계 주인이다 name = 외래키 컬럼이름
    private Team team;

    // 양방향 연관관계 양쪽값 설정 메소드
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }


    /*
        지연로딩 즉시로딩
        - 가급적 지연 로딩만 사용 (실무에선 걍 무조건 지연 로딩을 사용)
        - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
        - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다
        - @ManyToOne, @OneToOne은 기본이 즉시로딩 -> LAZY로 설정
        - @OneToMany, @ManyToMany는 기본이 지연 로딩
     */

}
