package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

        EntityManager em = emf.createEntityManager(); // 쉽게 말하면 jdbc connection 하나 받았다고 생각하면됨

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");*/

            Member member = em.find(Member.class, 2L);
            /*System.out.println(member.getId() + member.getName());
            em.remove(member);*/
            member.setName("hyoseong"); //이렇게만 해도 update문이 실행됨
            /* 중요
            transaction commit 시점에 달라진 것을 확인한 후 update문을 실행시켜주고 커밋함
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
