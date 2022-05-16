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

    @ManyToOne
    @JoinColumn(name = "team_id") // N:1 일 때, N인 객체 테이블 모델에서 외래키가 있는 객체가 연관간계 주인이다
    private Team team;

    // 양방향 연관관계 양쪽값 설정 메소드
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

}
