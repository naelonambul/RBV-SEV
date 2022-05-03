package com.rbc.red.api.service;

import com.rbc.red.api.dto.BookDto;
import com.rbc.red.api.dto.BookSearchCondition;
import com.rbc.red.api.entity.*;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.exeption.NotExistBookException;
import com.rbc.red.api.exeption.NotExistCategoryException;
import com.rbc.red.api.exeption.NotExistGroupAssetException;
import com.rbc.red.api.exeption.NotExistUserTeamException;
import com.rbc.red.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AssetRepository assetRepository;
    private final GroupRepository groupRepository;
    /**
     * 가계부 입력
     */
    @Transactional
    public void addBook(Book book){ bookRepository.saveAndFlush(book);}
    @Transactional
    public void addBook(Team team, Book book){
        team.addBook(book);
    }
    /**
     * 가계부 읽어오기
     */
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new NotExistBookException("Fail to Find Book"));
    }
    public Book findByTeamById(Team team, Long bookId){
        return team.getBooks().stream()
                .filter(m->m.getId() == bookId)
                .findFirst()
                .orElseThrow(() -> new NotExistBookException("Fail to Find Book"));
    }

    /**
     * 가계부 QueryDsl
     */
    public Page<BookDto> searchBookComplex(Team team, BookSearchCondition condition, Pageable pageable){
        return bookRepository.searchPageComplex(team.getId(), condition, pageable);
    }
    /**
     * 가계부 수정
     */
    @Transactional
    public void changeBook(Book book, Category category, Asset asset, Asset transfer, long price, String memo, String filePath, Team team, User user) {
        book.changeBook(
                category,
                asset,
                transfer,
                price,
                memo,
                filePath,
                team,
                user
        );
    }
    /**
     * 가계부 다 읽어오기
     */
    public List<Book> findByAll() {return bookRepository.findAll();}
    /**
     * 가계부 삭제하기
     */
    public void deleteById(Book book) {
        book.setNull();
        bookRepository.delete(book);
    }
    @Transactional
    public Boolean deleteBook(Team team, Long bookId){
        return team.getBooks().removeIf(m -> m.getId() == bookId);
    }

    /**
     * 카테고리 입력
     */
    @Transactional
    public void saveCategory(Category category){
        categoryRepository.saveAndFlush(category);
    }

    @Transactional
    public void addCategory(Team team, Category category) {
        team.addCategory(category);
    }
    /**
     * 카테고리 읽어오기
     */
    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotExistCategoryException("Fail to Find Category"));
    }
    public Category findTeamCategoryById(Team team, Long category_id){
        return team.getCategories().stream()
                .filter(m->m.getId() == category_id)
                .findFirst()
                .orElseThrow(() -> new NotExistCategoryException("Fail to Find Category"));
    }
    /**
     * 카테고리 수정
     */
    @Transactional
    public void changeCategory(Category category, String name, AssetType type) {
        category.changeCategory(
                name,
                type
        );
    }
    /**
     * 카테고리 다 읽어오기
     */
    public List<Category> findCatgoryAll(){
        return categoryRepository.findAll();
    }
    /**
     * 카테고리 삭제
     */
    public void deleteCategory(Long categoryId){
        categoryRepository.deleteById(categoryId);
    }
    @Transactional
    public void deleteCategory(Team team,Long categoryId){
        team.getCategories().removeIf(m->m.getId() == categoryId);
    }


    /**
     * 그룹 입력
     */
    @Transactional
    public void saveGroup(Group group) { groupRepository.saveAndFlush(group);}
    @Transactional
    public void addGroup(Team team, Group group){
        team.addGroup(group);
    }
    /**
     * 그룹 읽어오기
     */
    public Group findGroupById(Long groupId){
        return groupRepository.findById(groupId).orElseThrow(() -> new NotExistGroupAssetException("Fail to Find Group"));
    }
    public Group findTeamGroup(Team team, Long group_id){
        return team.getGroups().stream()
                .filter(m->m.getId() == group_id)
                .findFirst()
                .orElseThrow(() -> new NotExistGroupAssetException("Fail to Find Group"));
    }
    /**
     * 그룹 이름 수정
     */
    @Transactional
    public Group changeGroupName(Group group, String name){
        group.changeName(name);
        return group;
    }
    /**
     * 그룹 리스트 읽어오기
     */
    public List<Group> findGroups(Long teamId) {return groupRepository.findAll();}
    /**
     * 그룹 삭제
     */
    public void deleteGroup(Long groupId){ groupRepository.deleteById(groupId);}
    @Transactional
    public Boolean deleteGroup(Team team, Long groupId){
        Group group = team.getGroups().stream()
                .filter(m -> m.getId() == groupId)
                .findFirst()
                .orElseThrow(() -> new NotExistGroupAssetException("Fail to Find Group"));
        List<Long> assets = group.getAssets().stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());
        for (Long assetId : assets) {
            deleteAsset(group, assetId);
        }
        return team.getGroups().removeIf(m -> m.getId() == groupId);
    }

    /**
     * 자산 입력
     */
    @Transactional
    public void saveAsset(Asset asset){
        assetRepository.saveAndFlush(asset);
    }
    @Transactional
    public void addAsset(Group group, String name, long mBalance, long pBalance, LocalDate setDay, LocalDate payDay, Boolean autoPay) {
        group.addAsset(new Asset(
                name,
                mBalance,
                pBalance,
                setDay,
                payDay,
                autoPay)
        );
    }
    /**
     * 자산 읽어오기
     */
    public Asset findAssetById(Long assetId) {
        return assetRepository.findById(assetId).orElseThrow(() -> new NotExistGroupAssetException("Fail to Find Asset"));
    }
    public Asset findGroupAssetById(Group group, Long assetId){
        return group.getAssets().stream()
                .filter(m -> m.getId() == assetId)
                .findFirst()
                .orElseThrow(() -> new NotExistGroupAssetException("Fail to Find Asset"));
    }

    /**
     * 자산 수정
     */
    @Transactional
    public void changeAsset(Asset asset, String name, long mBalance, long pBalance, LocalDate setDay, LocalDate payDay, Boolean autoPay) {
        asset.changeAsset(
                name,
                mBalance,
                pBalance,
                setDay,
                payDay,
                autoPay
        );
    }
    /**
     * 자산 리스트 읽어오기
     */
    public List<Asset> findAssets(){
        return assetRepository.findAll();
    }
    /**
     * 자산 삭제
     */
    public void deleteAsset(Long assetId){
        assetRepository.deleteById(assetId);
    }
    @Transactional
    public Boolean deleteAsset(Group group, Long assetId){
        return group.getAssets().removeIf(m -> m.getId() == assetId);
    }
}
