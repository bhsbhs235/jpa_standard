package org.example;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team") // Member 클래스의 team 변수명 mappedBy로 주인이랑 매핑해줘야함
    private List<Member> members = new ArrayList<>();

    // 양방향 연관관계 양쪽값 설정 메소드
    public void addMember(Member member){
        member.setTeam(this);
        members.add(member);
    }
}
