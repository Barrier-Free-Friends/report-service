package org.bf.reportservice.infrastructure.persistence;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bf.reportservice.domain.entity.ImageTag;
import org.bf.reportservice.domain.entity.QReport;
import org.bf.reportservice.domain.entity.QReportImage;
import org.bf.reportservice.domain.entity.ReportStatus;
import org.bf.reportservice.presentation.dto.ReportDetailResponse;
import org.bf.reportservice.presentation.dto.ReportSearchRequest;
import org.bf.reportservice.presentation.dto.ReportSearchResponse;
import org.bf.reportservice.presentation.dto.ReportSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepositoryImpl implements ReportQueryRepository {

    private final JPAQueryFactory queryFactory;

    private static final QReport report = QReport.report;
    private static final QReportImage image = QReportImage.reportImage;

    @Override
    public Page<ReportSummaryResponse> findReports(Pageable pageable) {
        // 삭제된 제보글 제외한 목록 조회
        List<OrderSpecifier<?>> orders = toOrderSpecifiers(pageable.getSort());

        List<ReportSummaryResponse> content = queryFactory
                .select(Projections.constructor(
                        ReportSummaryResponse.class,
                        report.id,
                        report.title,
                        report.tag,
                        report.createdAt
                ))
                .from(report)
                .where(report.reportStatus.ne(ReportStatus.DELETED))
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(report.id.count())
                .from(report)
                .where(report.reportStatus.ne(ReportStatus.DELETED))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Optional<ReportDetailResponse> findReportDetail(UUID reportId) {

        // 제보 기본 필드 먼저 조회 (images 제외)
        Tuple base = queryFactory
                .select(
                        report.id,
                        report.userId,
                        report.title,
                        report.content,
                        report.tag,
                        report.createdAt
                )
                .from(report)
                .where(
                        report.id.eq(reportId),
                        report.reportStatus.ne(ReportStatus.DELETED)
                )
                .fetchOne();

        if (base == null) {
            return Optional.empty();
        }

        // images 별도 조회
        List<ReportDetailResponse.ReportImageDto> images = queryFactory
                .select(Projections.constructor(
                        ReportDetailResponse.ReportImageDto.class,
                        image.fileUrl,
                        image.latitude,
                        image.longitude,
                        image.address
                ))
                .from(image)
                .where(image.report.id.eq(reportId))
                .orderBy(image.id.asc())
                .fetch();

        // 최종 응답
        ReportDetailResponse response = new ReportDetailResponse(
                base.get(report.id),
                base.get(report.userId),
                base.get(report.title),
                base.get(report.content),
                base.get(report.tag),
                base.get(report.createdAt),
                images
        );

        return Optional.of(response);
    }

    /**
     * 제보글 검색
     * - 삭제된 제보글 제외
     * - keyword(title/content), tag, 기간 조건 적용
     */
    @Override
    public Page<ReportSearchResponse> searchReports(ReportSearchRequest request, Pageable pageable) {

        List<ReportSearchResponse> content = queryFactory
                .select(Projections.constructor(
                        ReportSearchResponse.class,
                        report.id,
                        report.title,
                        report.tag,
                        report.createdAt
                ))
                .from(report)
                .where(
                        report.reportStatus.ne(ReportStatus.DELETED),
                        keywordContains(request.keyword()),
                        tagEq(request.tag()),
                        createdAfter(request.from()),
                        createdBefore(request.to())
                )
                .orderBy(report.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(report.count())
                .from(report)
                .where(
                        report.reportStatus.ne(ReportStatus.DELETED),
                        keywordContains(request.keyword()),
                        tagEq(request.tag()),
                        createdAfter(request.from()),
                        createdBefore(request.to())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private List<OrderSpecifier<?>> toOrderSpecifiers(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return List.of(new OrderSpecifier<>(Order.DESC, report.createdAt));
        }

        List<OrderSpecifier<?>> list = new ArrayList<>();
        for (Sort.Order o : sort) {
            Order direction = o.isAscending() ? Order.ASC : Order.DESC;

            switch (o.getProperty()) {
                case "createdAt" -> list.add(new OrderSpecifier<>(direction, report.createdAt));
                case "updatedAt" -> list.add(new OrderSpecifier<>(direction, report.updatedAt));
                default -> list.add(new OrderSpecifier<>(Order.DESC, report.createdAt));
            }
        }
        return list;
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        return report.title.containsIgnoreCase(keyword)
                .or(report.content.containsIgnoreCase(keyword));
    }

    private BooleanExpression tagEq(ImageTag tag) {
        return tag == null ? null : report.tag.eq(tag);
    }

    private BooleanExpression createdAfter(LocalDateTime from) {
        return from == null ? null : report.createdAt.goe(from);
    }

    private BooleanExpression createdBefore(LocalDateTime to) {
        return to == null ? null : report.createdAt.loe(to);
    }

}
