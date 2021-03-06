package org.example;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sound.midi.Soundbank;
import java.nio.channels.FileChannel;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Set;

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
            /*
            Member member = new Member();
            member.setName("Hyoseong");

            em.persist(member);

            em.flush();
            em.clear();

            //Member findMember = em.find(Member.class, member.getId()); // 즉시 쿼리를 조회함
            Member findMember = em.getReference(Member.class, member.getId()); // 가짜 객체(프록시 객체)를 조회, 값이 없을 때 조회함
            /*
                프록시 특징
                - 실제 클래스를 상속 받아서 만들어짐
                - 실제 클래스와 겉모양이 같다
                - 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨(이론상)

                *중요
                - 프록시 객체는 처음 사용할 때 한번만 초기화
                - 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화되면 프록시 객체(가짜 객체)를 통해서 실제 엔티티에 접근 가능
                - 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함 (instance of 사용 , == 실패)
                - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환 ( 이 말은 == 해도 true 같은 타입이라는뜻 프록시 객체 X)
                  JPA 메커니즘이 영속성 컨텍스트에 있는 것들은 같게 해줘야함
                - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하는 문제 발생 ( LazyInitializationException )
             */
            /*
            System.out.println(findMember.getClass()); // HibernateProxy 객체
            System.out.println(findMember.getId());
            System.out.println(findMember.getName());

            //em.detach(findMember);
            em.clear();

            //findMember.getName(); // LazyInitializationException
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(findMember)); // false
            findMember.getName();
            // 또는 Hibernate.initialize(findMember);
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(findMember)); // true

            tx.commit();
            */
            /*
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("hyoseong");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());
            // List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
            // select * from member, select * from team 두번 나감 매핑 되어있는 만큼 select를 조회
            // List<Member> members = em.createQuery("select m from Member m join fetch m.team", Memer.class).getResultList();

            System.out.println(m.getTeam().getClass()); // 프록시 객체

            m.getTeam().getName(); // 실제 Team을 사용하는 시점에 조회
            */
            /*
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            //em.remove(findParent);
            findParent.getChildList().remove(0); // orphanRemoval = true 일 때, 동작함
            */

            /*User user = new User();
            user.setUsername("hello");
            user.setAddress(new Address("city", "street", "zipcode"));
            user.setPeriod(new Period());

            em.persist(user);*/

            /*
            Address address = new Address("city", "street", "zipcode");
            User user = new User();
            user.setUsername("hello");
            user.setHomeAddress(address);
            user.setPeriod(new Period());

            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
            User user2 = new User();
            user2.setUsername("hello");
            //user2.setAddress(address);
            user2.setHomeAddress(copyAddress); //복사해서 쓰자
            user2.setPeriod(new Period());

            em.persist(user);

            // user.getAddress().setCity("newCity"); // user 만 바꾸려고 했는데 user2 까지 바뀜 Setter 를 없애자
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
                int a = 10;
                int b = 10;
                a == b true

                Address address1 = new Address("city");
                Address address2 = new Address("city");

                address1 == address2 false
                address1.equals(address2) false

                equals @overide해서 사용
             */
            /*em.persist(user2);
            */

            /*
            User user = new User();
            user.setUsername("user");
            user.setHomeAddress(new Address("city", "street", "zipcode"));

            user.getFavoriteFoods().add("피자");
            user.getFavoriteFoods().add("족발");
            user.getFavoriteFoods().add("치킨");

            //user.getAddressList().add(new Address("city2", "street2", "zipcode2"));
            //user.getAddressList().add(new Address("city3", "street3", "zipcode3"));
            user.getAddressList().add(new AddressEntity("city2", "street2", "zipcode2"));
            user.getAddressList().add(new AddressEntity("city3", "street3", "zipcode3"));
            em.persist(user);

            em.flush();
            em.clear();

            User u = em.find(User.class, user.getId());

            List<Address> addressList = u.getAddressList();

            addressList.forEach(System.out::println); // 지연 로딩이라 사용해줄 때 조회함

            Set<String> favoriteFoods = u.getFavoriteFoods();

            favoriteFoods.forEach(System.out::println); // 지연 로딩이라 사용해줄 때 조회함

            // u.getHomeAddress().setCity("newCity") // 아주 위험한 방법

            Address a = u.getHomeAddress();
            u.setHomeAddress(new Address("new City", a.getStreet(), a.getZipcode())); // 교체하는 느낌으로 가야함 딱 그 값만 바꾸려고 set을 두면 사이드 이팩트가 너무 큼

            // 치킨 -> 떡볶이
            u.getFavoriteFoods().remove("치킨");
            u.getFavoriteFoods().add("떡볶이"); // 처럼 업데이트 한단 느낌보단 갈아 끼움

            u.getAddressList().remove(new Address("city2", "street2", "zipcode2")); // equals가 구현이 안되어 있으면 해당 값 타입을 못찾아 지워지지 않는다.
            u.getAddressList().add(new Address("aaci다ty", "aastreet", "aazipcode")); // 같이 업데이트 한단 느낌보단 갈아 끼움

            Address(값 타입)을 엔티티(AddressEntity)로 관리하면 식별자가 있어 업데이트 할 때도 유리하다.
            */

            /*
            em.createQuery("select m from Member m where m.name like '%kim%'",
                    Member.class).getResultList();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();
            */

            /*
            TypedQuery<Member> query = em.createQuery("select m from Member m where m.name = :name", Member.class);
            List<Member> result = query.getResultList();

            Member result2 = query.getSingleResult(); // 결과가 하나 일 때
            // 결과가 없으면 NoResultException
            // 둘 이상이면 NonUniqueResultException
            */

            /*Member result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", "member1")
                    .getSingleResult();*/

            //List<Team> result = em.createQuery("select m.team from Member m ", Team.class).getResultList(); 보다는 아래를 지향
            /*List<Team> result = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();*/

            // em.createQuery("select u.homeAddress from User u ").getResultList(); 임베디드 타입도 가능

            /*List resultList = em.createQuery("select m.name, m.age from Member m ").getResultList();

            Object o = resultList.get(0);
            Object[] result = (Object[]) o;
            System.out.println("name = " + result[0]);
            System.out.println("age = " + result[1]);*/

            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Team team2 = new Team();
            team.setName("team2");
            em.persist(team2);

            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            member.setTeam(team);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setAge(20);
            member2.setTeam(team);

            Member member3 = new Member();
            member3.setName("member3");
            member3.setAge(30);
            member3.setTeam(team2);

            em.persist(member);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

            /*List<MemberDTO> resultList = em.createQuery("select new org.example.MemberDTO(m.name, m.age) from Member m", MemberDTO.class).getResultList();
            MemberDTO memberDTO = resultList.get(0);
            System.out.println(" name = " + memberDTO.getName());
            System.out.println(" age = " + memberDTO.getAge());*/

            /*
                DTO로 바로 조회
                패키지 명을 포함한 전체 클래스 명입력
                순서와 타입이 일치하는 "생성자" 필요

             */

            /*for(int i=0; i< 100; i++) {
                Member member = new Member();
                member.setName("member" + i);
                member.setAge(10+i);
                em.persist(member);
            }

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0) // 조회 시작 위치
                    .setMaxResults(10) // offset
                    .getResultList();
            for(Member member1 : result){
                System.out.println("member1 = " + member1);
            }*/

           /* String query = "select m from Member m inner join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            for(Member tmp : result){
                System.out.println("member = " + tmp.getName() + ", Team = " + tmp.getTeam().getName());
            }*/

            /*String query = "select function('group_concat', m.name) From Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for(String s: result){
                System.out.println(s);
            }*/

            //String query = "select m.team from Member m";
            // 해당 JPQL문은 묵시적 INNER JOIN이 발생하기 때문에 나중에 성능이 저하 될 수 있다.
            //List<Team> result = em.createQuery(query, Team.class).getResultList();

           /* String query = "select m.name from Team t join t.members m";
            List<Team> result = em.createQuery(query, Team.class).getResultList();*/

            // *중요* 걍 묵시적 조인 쓰지말고 명시적으로 조인 해줘라!!

            /*
               경로 탐색 (예 m.team) 을 사용한 묵시적 조인 시 주의사항
               - 항상 내부 조인 Inner join
               - 컬렉션 (Many 다 인 부분) 은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야함
                String query = "select t.members.name from Team t "; // 못함 size밖에 못구함
                String query = "select t.members.name from Team t join t.members m";
                List<Team> result = em.createQuery(query, Team.class).getResultList();

               - 경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM 절에 영향을 줌
             */

            /*
                JPA 는 WHERE HAVING 절에서만 서브 쿼리 사용 가능
                FROM 절의 서브 쿼리는 불가능(조인으로 풀 수 있으면 풀어서 해결)

                서브 쿼리 예
                select m from Member m
                where m.age > ( select avg(m2.age) from Member m2 )

                서브 쿼리 지원 함수
                - (NOT) EXISTS : 서브쿼리에 결과가 존재하면 참
                - ALL : 모두 만족하면 참
                - ANY, SOME : 조건을 하나라도 만족하면 참
                - (NOT) IN : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참

                teamA 소속인 회원
                select m from Member m
                where exists ( select t from m.team t where t.name = "teamA" )

                전체 상품 각각의 재고보다 주문량이 많은 주문들
                select o from Order o
                where o.orderAmount > ALL ( select p.stockAmount from Product p )

                어떤 팀이든 팀에 소속된 회원
               select m from Member m
                where m.team = ANY ( select t from Team t )
             */


            /*
                JPQL
                - 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
                - SQL을 추상화해서 특정 데이터베이스 SQL에 의존 X
                - JPQL을 한마디로 정의하면 객체 지향 SQL

                - JPQL은 객체지향 쿼리 언어다. 따라서 테이블을 대상으로 쿼리 하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다.
                - JPQL은 SQL을 추상화해서 특정데이터베이스 SQL에 의존하지 않는다.
                - JPQL은 결국 SQL로 변환된다.
             */
            /*
                Criteria
                - 문자가 아 자바코드로 JPQL을 작성할 수 있음
                - JPQL 빌더 역할
                - JPA 공식 기능
                - 단점 : 너무 복잡하고 실용성이 없다
                - Criteria 대신에 QueryDSL사용 권장
             */
            /*
                QueryDSL 소개
                - 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
                - JPQL 빌더 역할
                - 컴파일 시점에 문법 오류를 찾을 수 있음
                - 동적쿼리 작성 편리함
                - 단순하고 쉬움
             */

            /*String query = "select m from Member m";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for(Member tmp : result){
                System.out.println("member = " + tmp.getName() + ", Team = " + tmp.getTeam().getName());
                // LAZY 방식
                // member1 team1 을 가져옴
                // member2 team1 은 캐쉬에서 가져옴
                // member3 team2 를 다시 가져옴
            }

            String query2 = "select m from Member m join fetch m.team";
            fetch 조인으로 즉시 다 가져온(즉시 로딩)
             */

             /*String query = "select t from Team t join fetch t.members";
             List<Team> result = em.createQuery(query, Team.class).getResultList();

             for(Team t : result){
                 System.out.println("team  = " + team.getName() + " members size = " + team.getMembers().size());
                 for(Member m : t.getMembers()){
                     System.out.println("member = " + m.getName());
                 }
                 // team1 2번
                 // team2 1번 표출됨
                 // WHY? inner join하면 member 수만큼 로우가 생기기 때문 N:1 1 부분을 JOIN하면 데이터가 뻥튀기 될 수 있다
             }*/

             /*
              페치 조인 주의 사항
              - 페치 조인 대상에는 별칭을 줄 수 없다.
              - 둘 이상의 컬렉션은 페치 조인 할 수 없다
              - 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다. ( 위에 처럼 데이터 뻥튀기 돼기 때문에 )

              select t from Team t join fetch t.members m where m.age > 10 이런 문법 최대한 안쓰도록 하자!

              - 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화
              - 엔티티에 직접 적용하는 글로벌 로딩전략보다 우선함 (@OneToMany(fetch = FetchType.LAZY)
              - 실무에서 글로벌 로딩 전략은 모두 지연 로딩
              - 최적화가 필요한 곳은 페치 조인 적용
               */

            //em.createNamedQuery("Member.findByName", Member.class).setParameter("name", "member1").getResultList();

            // 자동 FLUSH 됨
            int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();
            System.out.println("resultCount = " + resultCount);

            System.out.println("member.getAge() = " + member.getAge());
            System.out.println("member2.getAge() = " + member2.getAge());
            System.out.println("member3.getAge() = " + member3.getAge());

            em.clear();
            // 1. flush
            // 2. update 쿼리 (DB에만 반영된다)
            // 3. 영속성 컨텍스트에 있는 그대로 들고 오기 때문에 member age 값은 초기 값이다
            // 4. 따라서 clear해주고 다시 조회를 해야한다

            String query = "select m from Member m";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for(Member tmp : result){
                System.out.println("member.getAge() = " + tmp.getAge());
            }

            /*
                벌크 연산 주의
                - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
                 따라서 1. 벌크 연산을 가장 먼저 실행 또는 2. 벌크 연산 수행 후 영속성 컨테스트 초기화
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
