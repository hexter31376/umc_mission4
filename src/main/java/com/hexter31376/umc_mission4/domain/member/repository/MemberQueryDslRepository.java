package com.hexter31376.umc_mission4.domain.member.repository;

import com.hexter31376.umc_mission4.domain.member.entity.Member;
import com.hexter31376.umc_mission4.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPADeleteClause;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public MemberQueryDslRepository(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Transactional
    public Member save(Member member) {
        if (member.getId() == null) {
            em.persist(member);
            return member;
        }
        return em.merge(member);
    }

    public Optional<Member> findById(Long id) {
        Member m = queryFactory.selectFrom(QMember.member).where(QMember.member.id.eq(id)).fetchOne();
        return Optional.ofNullable(m);
    }

    public Page<Member> findAll(Pageable pageable) {
        QMember m = QMember.member;
        List<Member> content = queryFactory.selectFrom(m)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(m.count()).from(m).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<Member> search(String email, com.hexter31376.umc_mission4.domain.member.enums.Status status, Pageable pageable) {
        QMember m = QMember.member;
        com.querydsl.core.BooleanBuilder where = new com.querydsl.core.BooleanBuilder();
        if (email != null && !email.isBlank()) where.and(m.email.containsIgnoreCase(email));
        if (status != null) where.and(m.status.eq(status));

        List<Member> content = queryFactory.selectFrom(m)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(m.count()).from(m).where(where).fetchOne();
        long totalCount = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Transactional
    public boolean deleteById(Long id) {
        QMember m = QMember.member;
        long affected = new JPADeleteClause(em, m).where(m.id.eq(id)).execute();
        return affected > 0L;
    }
}
