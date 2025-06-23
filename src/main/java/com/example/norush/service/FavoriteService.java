package com.example.norush.service;

import static com.example.norush.exception.FavoriteExceptionType.*;
import static com.example.norush.exception.MemberExceptionType.USER_NOT_FOUND;

import com.example.norush.domain.Favorite;
import com.example.norush.domain.Member;
import com.example.norush.dto.FavoriteAddRequest;
import com.example.norush.dto.FavoriteResponse;
import com.example.norush.dto.FavoriteUpdateRequest;
import com.example.norush.exception.FavoriteException;
import com.example.norush.exception.MemberException;
import com.example.norush.repository.FavoriteRepository;
import com.example.norush.repository.MemberRepository;
import com.example.norush.util.FavoriteUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FavoriteResponse addFavorite(String memberId, FavoriteAddRequest request){
        validateMemberExistence(memberId); // 없으면 email로
        validateDuplicateFavorite(memberId,request.name());

        Member member = findMemberById(memberId); //

        Favorite newFavorite = Favorite.builder()
            .id(FavoriteUtil.generateFavoriteId())
            .userId(member.getId())
            .name(request.name())
            .type(request.type())
            .targetId(request.targetId())
            .memo(request.memo())
            .createdAt(LocalDateTime.now())
            .build();


        Favorite savedFavorite = favoriteRepository.save(newFavorite);
        return FavoriteResponse.from(savedFavorite);
    }
@Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(String memberId) {
        validateMemberExistence(memberId);

        Member member = findMemberById(memberId);

        return favoriteRepository.findByUserId(member.getId()).stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FavoriteResponse getFavoriteById(String favoriteId, String memberId){
        validateMemberExistence(memberId);

        Member member = findMemberById(memberId);

        Favorite favorite = findFavoriteByIdAndUserId(favoriteId,member.getId());

        return FavoriteResponse.from(favorite);
    }

    @Transactional
    public FavoriteResponse updateFavorite(String favoriteId, String memberId, FavoriteUpdateRequest request){
        validateMemberExistence(memberId);

        Member member = findMemberById(memberId);

        Favorite existingFavorite = findFavoriteByIdAndUserId(favoriteId, member.getId());

        existingFavorite.update(
                request.name(),
                request.type(),
                request.targetId(),
                request.memo()
        );

        Favorite updatedFavorite = favoriteRepository.save(existingFavorite);
        return FavoriteResponse.from(updatedFavorite);
    }

    @Transactional
    public void deleteFavorite(String favoriteId, String memberId) {
        validateMemberExistence(memberId);

        Member member = findMemberById(memberId);

        Favorite favoriteToDelete = findFavoriteByIdAndUserId(favoriteId, member.getId());
        favoriteRepository.delete(favoriteToDelete);
    }

    private void validateMemberExistence(String memberId) {
        if (!memberRepository.findById(memberId).isPresent()) {
            throw new MemberException(USER_NOT_FOUND);
        }
    }

    private void validateDuplicateFavorite(String memberId, String favoriteName) {
        if (favoriteRepository.findByUserIdAndName(memberId, favoriteName).isPresent()) {
            throw new FavoriteException(FAVORITE_ALREADY_EXISTS);
        }
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
    }

    private Favorite findFavoriteByIdAndUserId(String favoriteId, String userId) {
        return favoriteRepository.findByIdAndUserId(favoriteId, userId)
                .orElseThrow(() -> new FavoriteException(FAVORITE_NOT_FOUND));
    }
}