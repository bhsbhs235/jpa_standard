package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.temporal.TemporalAccessor;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /* 중요
        EntityMangerFactory 애플리케이션 로딩시 한번만 만들어줘야함
        트랜잭션단위는 EntityManager로 관리
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        /*
            중요: EntityManager는 쓰레드간에 공유 X
            JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
         */
        EntityManager em = emf.createEntityManager(); // 쉽게 말하면 jdbc connection 하나 받았다고 생각하면됨

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
           실무에서는 setter 잘 안쓰고 생성자 빌더로 함
            */

            //Member member = em.find(Member.class, 2L);
            /*System.out.println(member.getId() + member.getName());
            em.remove(member);*/

            /* 중요
            transaction commit 시점에 달라진 것을 확인한 후 update문을 실행시켜주고 커밋함
             */
            //member.setName("hyoseong"); //이렇게만 해도 update문이 실행됨

            /*
                JPQL "객체"를 중심으로 쿼리문을 작성한다
                hibernate dialect만 변경해주면(Oracle 등등) 자동으로 그 문법에 맞게 쿼리가 변경되서 날림 즉, dialect만 변경해주면 됨.
             */
            /*List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5) //limit
                    .setMaxResults(10) //limit
                    .getResultList();*/

            /*
                중요

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");

            team.getMembers().add(member); // 연관관계가 주인이 아닌 객체에서는 add해도 적용안된다.
            member.setTeam(team); // member에서 지정을 해줘야 한다.!!

            em.persist(member);
             */

            /*
                중요

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member);

            team.getMembers().add(member); // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자
            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId()); // flush로 비우지 않으면 memebers는 null이다 1차 캐시에 있는 상황에는 양쪽에 넣어주지 않으면 적용안됨
            List<Member> members = findTeam.getMembers();

            for(Member m : members){

            }
             */

            /*
                일대다 단방향 단점
                - 엔티티가 관리하는 외래 키가 다른 테이블에 있음 ( 관계 테이블상 "다" 쪽에 외래키가 있음 )
                - 연관관계 관리를 위해 추가로 UPDATE SQL을 실행 ( 외래키가 반대쪽에 있기 때문에 )

                따라서 일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하자
             */
            /*
                일대일 관계
                    - 주 테이블에 외래키 (Member)
                        -  주 객체가 대상 객체의 참조를 가지는 것 처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾음
                        - 객체지향 개발자 선호
                        - JPA 매핑 편리
                        - 장점 : 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
                        - 단점 : 값이 없으면 외래 키에 null 허용

                    - 대상 테이블에 외래 키 (Locker)
                        - 대상 테이블에 외래 키가 존재
                        - 장점 : 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
                        - 단점 : 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨
             */
            /*
                다대다 관계
                연결 테이블을 엔티티 추가
                @ManyToMany -> @OneToMany, @ManyToOne
             */

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
