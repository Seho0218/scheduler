package com.attendance.scheduler.notification.repository;

import com.attendance.scheduler.notification.dto.Condition;
import com.attendance.scheduler.notification.dto.NoticeDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.attendance.scheduler.notification.domain.notice.QNoticeEntity.noticeEntity;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    public final JPAQueryFactory queryFactory;


    public Page<NoticeDTO> pageNoticeList(Condition condition, Pageable pageable){
        List<NoticeDTO> content = queryFactory
                .select(Projections.fields(NoticeDTO.class,
                        noticeEntity.id,
                        noticeEntity.title,
                        noticeEntity.type,
                        noticeEntity.content,
                        noticeEntity.author,
                        noticeEntity.creationTimestamp,
                        noticeEntity.modifiedDate))
                .from(noticeEntity)
                .where(
                        titleEq(condition.getTitleContent()),
                        contentEq(condition.getTitleContent())
                )
                .orderBy(noticeEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> counts = queryFactory
                .select(noticeEntity.count())
                .from(noticeEntity)
                .where(
                        titleEq(condition.getTitleContent()),
                        contentEq(condition.getTitleContent())
                );


        return PageableExecutionUtils.getPage(content, pageable, counts::fetchOne);
    }

    private BooleanExpression titleEq(String title){
        return hasText(title) ? noticeEntity.title.eq(title) : null;
    }

    private BooleanExpression contentEq(String content){
        return hasText(content) ? noticeEntity.content.eq(content) : null;
    }

    public NoticeDTO findPostById(Long id) {
        return queryFactory
                .select(Projections.fields(NoticeDTO.class,
                        noticeEntity.id,
                        noticeEntity.title,
                        noticeEntity.type,
                        noticeEntity.content,
                        noticeEntity.author,
                        noticeEntity.creationTimestamp,
                        noticeEntity.modifiedDate))
                .from(noticeEntity)
                .where(noticeEntity.id.eq(id))
                .fetchOne();
    }
}