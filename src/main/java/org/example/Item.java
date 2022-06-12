package org.example;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 없으면 단일 테이블 방식 ( 한 테이블(Item)에 Book, Movie 컬럼이 들어간다)
@DiscriminatorColumn(name = "D_TYPE") //( DTYPE 생성 - 없어도 운영상에 에러는 없지만 편의상 있는게 좋다 )
public class Item extends BaseEntity {
// abstract 추상 클래스로 하면 직접적으로 생성하는 것을 막을수 있다. 조회할 일 없는 테이블에 붙이면 된다.
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

}
/*
    JPA 장점
    Inheritance 전략만 바꿔줘도 알아서 테이블을 바꿔주고 JPA를 사용안했더라면 각 엔티티 값을 넣어주는 로직을 모두 수정해야 할 것이다(그 외에더 수정할거 많다). business 로직은 안바꿔도 된다.
 */
/*
    관계형 데이터베이스는 상속 관계란게 없다.
    모델링 기법이 객체 상속과 유사해서 여러가지 기법중 하나를 골라 쓴다.

    단일 테이블
    장점 :
        1. 성능이 좋다. (쿼리를 한번만 진행하니깐)
        2. 조회 쿼리가 단순함

    단점 :
        1. 자식 엔티티가 매핑한 컬럼은 모두 NULL 허용
        2. 단일 테이블에 모든 것을 저장하므로 테이블이 커질 수 있다 상황에 따라서 조회 성능이 오히려 느려질 수 있다(테이블이 매우 커지면).

    조인 전략
    장점 :
        1. 테이블 정규화
        2. 외래 키 참조 무결성 제약조건 활용가능
        3. 저장공간 효율화
    단점 :
        // 상대적으로 정석 전략임 단점이라 하기에는 치명적이지 않음
        1. 조회시 조인을 많이 사용, 성능 저하
        2. 조회 쿼리가 복잡함
        3. 데이터 저장시 INSERT SQL 2번 호출

    구현 클래스마다 테이블 전략
    쓰지말자 묻지도 따지지도 말고
 */