package com.meta.jpacore;

import com.meta.jpacore.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EntityTest {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("memo");
        em = emf.createEntityManager();
    }

    @Test
    @DisplayName("EntityTransaction 성공 테스트")
    void test1() {
        EntityTransaction et = em.getTransaction(); // EntityManager에서 트랜잭션을 가져옴

        et.begin(); // 트랜잭션 시작

        try {
            Memo memo = new Memo(); // 저장할 메모 엔터티 객체 생성
            memo.setId(2L);
            memo.setUsername("Meta2");
            memo.setContents("영속성 컨텍스트와 트랜잭션 이해하기. (디버거 보는중)");

            em.persist(memo); // EM이 memo 객체를 영속성 컨텍스트에 저장.

            et.commit(); // 오류가 발생하지 않고 정상이라면, Commit(쿼리 수행)을 호출
        } catch (Exception e) {
            e.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }


    @Test
    @DisplayName("EntityTransaction 실패 테스트")
    void test2() {
        EntityTransaction et = em.getTransaction(); // EntityManager에서 트랜잭션을 가져옴

        et.begin(); // 트랜잭션 시작

        try {
            Memo memo = new Memo(); // 저장할 메모 엔터티 객체 생성

            memo.setUsername("Meta2");
            memo.setContents("영속성 컨텍스트와 트랜잭션 이해하기.");

            em.persist(memo); // EM이 memo 객체를 영속성 컨텍스트에 저장.

            et.commit(); // 오류가 발생하지 않고 정상이라면, Commit(쿼리 수행)을 호출
        } catch (Exception e) {
            System.out.println("식별자 값을 넣어주지 않아 오류가 발생했습니다.");
            e.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }


    @Test
    @DisplayName("1차 캐시 : Entity 저장")
    void test3() {
        EntityTransaction et = em.getTransaction(); // EntityManager에서 트랜잭션을 가져옴

        et.begin(); // 트랜잭션 시작

        // Ctrl + Alt + T -> TryCatchFinally
        try {
            // 저장할 엔터티 객체 생성
            Memo memo = new Memo();
            memo.setId(10L);
            memo.setUsername("김메타10");
            memo.setContents("1차 캐시 Entity 저장");

            // EM이 memo 객체를 영속성 컨텍스트에 관리
            em.persist(memo);

            // 트랜잭션 커밋
            et.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("1차 캐시 : 조회시 캐시 저장소에 해당하는 Id가 존재하지 않는 경우")
    void test4() {
        try {
            // 저장할 엔터티 객체 생성
            Memo memo = em.find(Memo.class, 10L);
            System.out.println("memo.getId() = " + memo.getId());
            System.out.println("memo.getUsername() = " + memo.getUsername());
            System.out.println("memo.getContents() = " + memo.getContents());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }

    @Test
    @DisplayName("1차 캐시 : 조회시 캐시 저장소에 해당하는 Id가 존재하는 경우")
    void test5() {
        try {
            // 저장할 엔터티 객체 조회
            Memo memo1 = em.find(Memo.class, 10L);
            Memo memo2 = em.find(Memo.class, 10L);
            // 다른 메모리를 사용하는 변수를 지정하더라도 위 2개의 엔터티는 같은 객체를 바라봄.

            System.out.println("memo.getId() = " + memo2.getId());
            System.out.println("memo.getUsername() = " + memo2.getUsername());
            System.out.println("memo.getContents() = " + memo2.getContents());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }


    @Test
    @DisplayName("객체 동일성 보장 확인")
    void test6() {
        EntityTransaction et = em.getTransaction(); // EntityManager에서 트랜잭션을 가져옴

        et.begin(); // 트랜잭션 시작

        // Ctrl + Alt + T -> TryCatchFinally
        try {
            // 저장할 엔터티 객체 생성
            Memo memo3 = new Memo();
            memo3.setId(23L);
            memo3.setUsername("김메타23");
            memo3.setContents("객체 동일성 보장");

            // EM이 memo 객체를 영속성 컨텍스트에 관리
            em.persist(memo3);
            Memo memo0 = em.find(Memo.class, 23L);

            Memo memo1 = em.find(Memo.class, 10L);

            System.out.println(memo3 == memo0); // true
            System.out.println(memo1 == memo0); // false

            // 트랜잭션 커밋
            et.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        emf.close();
    }
}
