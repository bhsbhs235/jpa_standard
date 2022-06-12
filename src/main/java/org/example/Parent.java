package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }

    /*
        영속성 전이(CASCADE)
        - 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
        - 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함만 제공한다

        주의
        참조하는 곳이 하나일 때 사용해야함, 특정 엔티티가 개인 소유할 때 사함 cascade로 지웠는데 다른데는 반영안됨 뒤죽박죽됨

        CascadeType.ALL + orphanRemoval=true
        - 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
        - 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음

        라이프 사이클을 어디서 관리할건지 어떻게 관리할건지가 중요
     */


}
