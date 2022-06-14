package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Address homeAddress;

    /*
    식별자가 없다
    @ElementCollection(fetch = FetchType.LAZY) // default FetchType.LAZY
    @CollectionTable(name = "FAVORITE_FOOD",
            joinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY) // default FetchType.LAZY
    @CollectionTable(name = "ADDRESS",
            joinColumns = @JoinColumn(name = "USER_ID")
    )
    private List<Address> addressList = new ArrayList<>();
    */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private List<AddressEntity> addressList = new ArrayList<>();

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
    /*
        값 타입 컬렉션 대안
        - 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
        - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
        - 영속성 전이(cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
        AddressEntity
     */
    /*
     값 타입 컬렉션(List, Set)
        - 값 타입을 하나 이상 저장할 때 사용
        - @ElementCollection, @CollectionTable사용
        - 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다 ( ADDRESS, FAVORITE_FOOD 테이블을 별도로 만듦 )
        - 컬렉션을 저장하기 위한 별도의 테이블이 필요함
        - 생명주기가 User객체에 달려있다 ( 따로 엔티티가 아니라 User가 persist되면 컬렉션 값들도 적용됨 )
     */
    /*
        값 타입 컬렉션의 제약사항
        - 값 타입은 엔티티와 다르게 식별자 개념이 없다
        - 값은 변경하면 추적이 어렵다
        - 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
            - 그래서 웬만하면 값 타입 컬렉션 사용하지 말자!!!!

        - 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야 함 null 입력X, 중복 저장X
     */

    /*
        엔티티 타입의 특징
        - 식별자가 있다 ( Id )
        - 생명 주기 관리
        - 공유

        값 타입의 특징
        - 식별자가 없다
        - 생명 주기를 엔티티에 의존
        - 공유하지 않는 것이 안전(복사해서 사용) - copyAddress
        - 불변 객체로 만드는 것이 안전 (no setter)

        값 타인은 정말 값 타입이라 판단될 때만 사용
        엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안됨
        식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것은 값 타입이 아닌 엔티티
     */
}
