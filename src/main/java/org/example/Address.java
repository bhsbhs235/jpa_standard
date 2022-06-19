package org.example;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Address {
    private String city;
    private String street;

    private String zipcode;

    public String fullAddress(){
        return getCity() + "/" + getStreet() + "/" + getZipcode();
    }

    @Override // equals hashCode는 걍 무조건 넣어주자 이 담에 사이드이팩트 생기는 위험을 줄여준다 ( use getter ... )
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }

     /*
        객체 타입의 한계
        - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
        - 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다.
        - 자바 기본 타입에 값을 대입하면 값을 복사한다.
        - 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다
        - 객체의 공유 참조는 피할 수 없다
     */
    /*
                int a = 10;
                int b = a;
                a = 20;
                해도 b 값 안바뀜 값이 복사되서 들어가니깐

                대충
                Address a = new Address(10);
                Address b = a;
                a.setValue(20);
                b도 바뀜 클래스는 주소로 참조하기 때문에
                따라서 임베디드 타입도 같이 변경됨

                참고 Integer, String 도 클래스이며 공유참조가 된다 하지만 불변 객체로 값을 수정할 수 없게 하여 사이드 이팩트를 원천 차단한다.
             */
    /*
        불변 객체
        - 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
        - 값 타입은 불변 객체로 설계해야함
        - 불볍 객체 : 생성 시점 이후 절대 값을 변경할 수 없는 객체
        - 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨
        - Integer, String은 자바가 제공하는 대표적인 붋변 객체
     */
}
