package com.rbc.red.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rbc.red.api.dto.BookDto;
import com.rbc.red.api.dto.BookSearchCondition;
import com.rbc.red.api.dto.QBookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.rbc.red.api.entity.QAsset.asset;
import static com.rbc.red.api.entity.QBook.book;
import static com.rbc.red.api.entity.QCategory.category;
import static com.rbc.red.api.entity.QTeam.*;
import static com.rbc.red.api.entity.user.QUser.*;
import static org.springframework.util.StringUtils.hasText;

public class BookRepositoryImpl implements BookRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public BookRepositoryImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em);}
    @Override
    public Page<BookDto> searchPageComplex(Long teamId, BookSearchCondition condition, Pageable pageable) {
        List<BookDto> content = queryFactory
                .select(new QBookDto(
                        book.id,
                        team.name.as("teamName"),
                        user.username.as("username"),
                        category.name.as("categoryName"),
                        asset.name.as("assetName"),
                        book.price,
                        book.memo,
                        book.filePath,
                        book.dateTime
                        ))
                .from(book)
                .leftJoin(book.user, user)
                .leftJoin(book.team, team)
                .leftJoin(book.category, category)
                .leftJoin(book.asset, asset)
                .leftJoin(book.transfer, asset)
                .where(
                        teamIdEq(teamId),
                        categoryIdEq(condition.getCategoryId()),
                        assetIdEq(condition.getAssetId()),
                        priceGoe(condition.getPriceGoe()),
                        priceLoe(condition.getPriceLoe()),
                        dataTimeAfter(condition.getDataTimeAfter()),
                        dataTimeBefore(condition.getDataTimeBefore())
                        )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(book.count())
                .from(book)
                .leftJoin(book.user, user)
                .leftJoin(book.team, team)
                .leftJoin(book.category, category)
                .leftJoin(book.asset, asset)
                .where(
                        teamIdEq(teamId),
                        categoryIdEq(condition.getCategoryId()),
                        assetIdEq(condition.getAssetId()),
                        priceGoe(condition.getPriceGoe()),
                        priceLoe(condition.getPriceLoe()),
                        dataTimeAfter(condition.getDataTimeAfter()),
                        dataTimeBefore(condition.getDataTimeBefore())
                );


        return PageableExecutionUtils.getPage(content, pageable,countQuery::fetchOne);
    }

    private BooleanExpression userNameEq(String username){
        return hasText(username) ? user.username.eq(username) : null;
    }
    private BooleanExpression teamIdEq(Long teamId){
        return teamId != null? team.id.eq(teamId) : null;
    }
    private BooleanExpression categoryIdEq(Long categoryId){
        return categoryId != null ? category.id.eq(categoryId) : null;
    }
    private BooleanExpression assetIdEq(Long assetId){
        return assetId != null ? asset.id.eq(assetId) : null;
    }
//    private BooleanExpression transferId(Long transferId){}
    private BooleanExpression priceGoe(Long priceGoe){
        return priceGoe != null ? book.price.goe(priceGoe) : null;
    }
    private BooleanExpression priceLoe(Long priceLoe){
        return priceLoe != null ? book.price.loe(priceLoe) : null;
    }
    private BooleanExpression dataTimeAfter(LocalDateTime dateTimeAfter){
        return dateTimeAfter != null ? book.dateTime.after(dateTimeAfter) : null;
    }
    private BooleanExpression dataTimeBefore(LocalDateTime dataTimeBefore){
        return dataTimeBefore != null ? book.dateTime.before(dataTimeBefore) : null;
    }
}
