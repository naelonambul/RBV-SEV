package com.rbc.red.api.controller.team;

import com.rbc.red.api.dto.BookDto;
import com.rbc.red.api.dto.BookSearchCondition;
import com.rbc.red.api.entity.*;
import com.rbc.red.api.entity.user.User;
import com.rbc.red.api.service.BookService;
import com.rbc.red.api.service.TeamService;
import com.rbc.red.api.service.UserService;
import com.rbc.red.api.service.UserTeamService;
import com.rbc.red.common.ApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserTeamService userTeamService;
    private final TeamService teamService;
    private final UserService userService;

    /**
     * 가계부
     */
    @GetMapping("/api/v1/teams/{teamId}/book")
    public ApiResponse getBookV1(@PathVariable("teamId") Long team_id,
                                 BookSearchCondition condition,
                                 Pageable pageable){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Page<BookDto> bookDtos = bookService.searchBookComplex(team, condition, pageable);

        return ApiResponse.success("Books", bookDtos) ;
    }

    @PostMapping("/api/v1/teams/{teamId}/book")
    public ApiResponse saveBookV1(@PathVariable("teamId") Long teamId,
                                  @RequestBody @Valid PostBookRequest request){
        User user = getUser();
        Team team = userTeamService.findUserTeam(user, teamId).getTeam();
        Category category = bookService.findTeamCategoryById(team, request.getCategory_id());
        Group assetGroup = bookService.findTeamGroup(team, request.getAsset_group_id());
        Asset asset = bookService.findGroupAssetById(assetGroup, request.getAsset_id());
        Group transferGroup = bookService.findTeamGroup(team, request.getTransfer_group_id());
        Asset transfer = bookService.findGroupAssetById(transferGroup, request.getTransfer_id());

        bookService.addBook(team, new Book(
                category,
                asset,
                transfer,
                request.getPrice(),
                request.getLocalDateTime(),
                request.getMemo(),
                request.getFilePath(),
                team,
                user
        ));
        return ApiResponse.success("Book", "Book inserted.") ;
    }
    @PutMapping("/api/v1/teams/{teamId}/book/{id}")
    public ApiResponse updateBookV1(@PathVariable("teamId") Long team_id,
                                    @PathVariable("id") Long id,
                                    @RequestBody @Valid PutBookRequest request){
        User user = getUser();
        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Category category = bookService.findTeamCategoryById(team, request.getCategory_id());
        Group assetGroup = bookService.findTeamGroup(team, request.getAsset_group_id());
        Asset asset = bookService.findGroupAssetById(assetGroup, request.getAsset_id());
        Group transferGroup = bookService.findTeamGroup(team, request.getTransfer_group_id());
        Asset transfer = bookService.findGroupAssetById(transferGroup, request.getTransfer_id());
        Book book = bookService.findByTeamById(team, id);
        bookService.changeBook(
                book,
                category,
                asset,
                transfer,
                request.getPrice(),
                request.getMemo(),
                request.getFilePath(),
                team,
                user
        );

        return ApiResponse.success("Book", "Book changed.") ;
    }
    @DeleteMapping("/api/v1/teams/{teamId}/book/{id}")
    public ApiResponse deleteBookV1(@PathVariable("teamId") Long team_id,
                                    @PathVariable("id") Long id){
        User user = getUser();
        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Book book = bookService.findByTeamById(team, id);
        Boolean result = bookService.deleteBook(team, book.getId());

        if(result)
            return ApiResponse.success("Book", "Book deleted.") ;
        else
            return ApiResponse.success("Book", "Book not deleted.");
    }
    /**
     * 그룹
     */
    @GetMapping("/api/v1/teams/{teamId}/groups")
    public ApiResponse getGroupsV1(@PathVariable("teamId") Long team_id){
        User user = getUser();
        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        List<GetGroupsResponse> result = team.getGroups().stream()
                .map(o -> new GetGroupsResponse(o))
                .collect(Collectors.toList());

        return ApiResponse.success("group", result);
    }
    @PostMapping("/api/v1/teams/{teamId}/groups")
    public ApiResponse saveGroupV1(@PathVariable("teamId") Long team_id,
                                   @RequestBody @Valid PostGroupsRequest request){
        User user = getUser();
        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        bookService.addGroup(team, new Group(request.name, request.groupType));

        return ApiResponse.success("group", team.getGroups()) ;
    }
    @PutMapping("/api/v1/teams/{teamId}/groups/{id}")
    public ApiResponse updateGroupV1(@PathVariable("teamId") Long team_id,
                                     @PathVariable("id") Long id,
                                     @RequestBody @Valid PutGroupsRequest request){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Group group = bookService.findTeamGroup(team, id);
        bookService.changeGroupName(group, request.getName());
        return ApiResponse.success("group", "group updated.") ;
    }
    @DeleteMapping("/api/v1/teams/{teamId}/groups/{id}")
    public ApiResponse deleteGroupV1(@PathVariable("teamId") Long team_id,
                                     @PathVariable("id") Long id){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Boolean Boolean = bookService.deleteGroup(team, id);
        if(Boolean)
            return ApiResponse.success("group", "group deleted") ;
        else
            return ApiResponse.success("group", "group not deleted") ;
    }

    /**
     * 자산
     */
    @GetMapping("/api/v1/teams/{teamId}/groups/{groupId}/assets")
    public ApiResponse getAssetsV1(@PathVariable("teamId") Long team_id,
                                   @PathVariable("groupId") Long groupId){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Group group = bookService.findTeamGroup(team, groupId);
        List<GetAssetsResponse> result = group.getAssets().stream()
                .map(o -> new GetAssetsResponse(o))
                .collect(Collectors.toList());

        return ApiResponse.success("Asset", result) ;
    }
    @PostMapping("/api/v1/teams/{teamId}/groups/{groupId}/assets")
    public ApiResponse saveAssetV1(@PathVariable("teamId") Long team_id,
                                   @PathVariable("groupId") Long groupId,
                                   @RequestBody @Valid PostAssetsRequest request){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Group group = bookService.findTeamGroup(team, groupId);
        bookService.addAsset(
                group,
                request.getName(),
                request.getMBalance(),
                request.getPBalance(),
                request.getSetDay(),
                request.getPayDay(),
                request.getAutoPay())
        ;

        return ApiResponse.success("Asset", "Asset inserted.") ;
    }
    @PutMapping("/api/v1/teams/{teamId}/groups/{groupId}/assets/{id}")
    public ApiResponse updateAssetV1(@PathVariable("teamId") Long team_id,
                                     @PathVariable("groupId") Long groupId,
                                     @PathVariable("id") Long id,
                                     @RequestBody @Valid PutAssetsRequest request){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Group group = bookService.findTeamGroup(team, groupId);
        Asset asset = bookService.findGroupAssetById(group, id);
        bookService.changeAsset(
                asset,
                request.getName(),
                request.getMBalance(),
                request.getPBalance(),
                request.getSetDay(),
                request.getPayDay(),
                request.getAutoPay()
        );

        return ApiResponse.success("Asset", "Assert updated.") ;
    }
    @DeleteMapping("/api/v1/teams/{teamId}/groups/{groupId}/assets/{id}")
    public ApiResponse deleteAssetV1(@PathVariable("teamId") Long team_id,
                                     @PathVariable("groupId") Long groupId,
                                     @PathVariable("id") Long id){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Group group = bookService.findTeamGroup(team, groupId);
        Asset asset = bookService.findGroupAssetById(group, id);
        // todo 연결된 Group & Book 정리
        Boolean result = bookService.deleteAsset(group, id);
        if(result)
            return ApiResponse.success("Asset", "Asset removed.") ;
        else
            return ApiResponse.success("Asset", "Asset not removed.") ;
    }
    /**
     * 카테고리
     */
    @GetMapping("/api/v1/teams/{teamId}/categories")
    public ApiResponse getCategoryV1(@PathVariable("teamId") Long team_id){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        List<GetCatogoryResponse> result = team.getCategories().stream()
                .map(o -> new GetCatogoryResponse(o))
                .collect(Collectors.toList());
        return ApiResponse.success("category", result) ;
    }
    @PostMapping("/api/v1/teams/{teamId}/categories")
    public ApiResponse saveCategoryV1(@PathVariable("teamId") Long team_id,
                                      @RequestBody @Valid PostCategoryRequest request){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        bookService.addCategory(team, new Category(
                request.getName(),
                request.getType()
        ));

        return ApiResponse.success("category", "Category inserted.") ;
    }
    @PutMapping("/api/v1/teams/{teamId}/categories/{id}")
    public ApiResponse updateCategoryV1(@PathVariable("teamId") Long team_id,
                                        @PathVariable("id") Long id,
                                        @RequestBody @Valid PutCategoryRequest request){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        Category category = bookService.findTeamCategoryById(team, id);
        bookService.changeCategory(
                category,
                request.getName(),
                request.getType()
        );

        return ApiResponse.success("category", "Category updated.") ;
    }
    @DeleteMapping("/api/v1/teams/{teamId}/categories/{id}")
    public ApiResponse deleteCategoryV1(@PathVariable("teamId") Long team_id,
                                        @PathVariable("id") Long id){
        User user = getUser();

        Team team = userTeamService.findUserTeam(user, team_id).getTeam();
        // todo 연결된 Group & Book 정리
        bookService.deleteCategory(team, id);
        return ApiResponse.success("category", "message") ;
    }

    //인증
    private User getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        User user = userService.getUser(principal.getUsername());
        return user;
    }
    
    @Data
    static class GetBookResponse{
        private Long category_id;
        private Long asset_id;
        private Long transfer_id;
        private long price;
        private LocalDateTime dateTime;
        private String memo;
        private String filePath;
        private Long team_id;
        private Long user_id;

        public GetBookResponse(Book book) {
            this.category_id = book.getCategory().getId();
            this.asset_id = book.getAsset().getId();
            this.transfer_id = book.getTransfer().getId();
            this.price = book.getPrice();
            this.dateTime = book.getDateTime();
            this.memo = book.getMemo();
            this.filePath = book.getFilePath();
            this.team_id = book.getTeam().getId();
            this.user_id = book.getUser().getUserSeq();
        }
    }

    @Data
    static class PutBookRequest{
        private Long category_id;
        private Long asset_group_id;
        private Long asset_id;
        private Long transfer_group_id;
        private Long transfer_id;
        private long price;
        private String memo;
        private String filePath;
    }
    @Data
    static class PostBookRequest{
        private Long category_id;
        private Long asset_group_id;
        private Long asset_id;
        private Long transfer_group_id;
        private Long transfer_id;
        private long price;
        private String memo;
        private String filePath;
        private LocalDateTime localDateTime;
    }
    @Data
    static class PutCategoryRequest{
        private String name;
        private AssetType type;
    }

    @Data
    static class PostCategoryRequest{
        private Long id;
        private String name;
        private AssetType type;
    }

    @Data
    static class GetCatogoryResponse {
        private Long id;
        private String name;
        private AssetType type;

        public GetCatogoryResponse(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.type = category.getType();
        }
    }
    @Data
    static class PostAssetsRequest{
        private String name;
        private long pBalance;
        private long mBalance;
        private LocalDate setDay;
        private LocalDate payDay;
        private Boolean autoPay;
    }
    @Data
    static class PutAssetsRequest{
        private String name;
        private long pBalance;
        private long mBalance;
        private LocalDate setDay;
        private LocalDate payDay;
        private Boolean autoPay;
    }

    @Data
    static class GetAssetsResponse{
        private Long id;
        private String name;
        private long pBalance;
        private long mBalance;
        private LocalDate setDay;
        private LocalDate payDay;
        private Boolean autoPay;

        public GetAssetsResponse(Asset asset) {
            this.id = asset.getId();
            this.name = asset.getName();
            this.pBalance = asset.getPBalance();
            this.mBalance = asset.getMBalance();
            this.setDay = asset.getSetDay();
            this.payDay = asset.getPayDay();
            this.autoPay = asset.getAutoPay();
        }
    }
    @Data
    static class PutGroupsRequest {
        private String name;
    }

    @Data
    static class PostGroupsRequest {
        private String name;
        private GroupType groupType;
    }


    @Data
    static class GetGroupsResponse{
        private Long id;
        private String name;

        public GetGroupsResponse(Group group) {
            this.id = group.getId();
            this.name = group.getName();
        }
    }
}
