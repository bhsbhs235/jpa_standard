package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Embedded
    private Period period;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE")),
    })
    private Address workAddress;

    /*
        임베디드 타입(내가 만든 커스텀 객체 Address, Period )의 장점
        - 재사용
        - 높은 응집도
        - 활용할 수 있는 메소드를 만들수 있음
        - 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함

        임베디드 타입과 테이블 매핑
        - 임베디드 타입은 엔티티의 값일 뿐이다.
        - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
        - 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능
        - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음
     */
}
