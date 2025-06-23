package com.example.norush.service;

import static com.example.norush.exception.MemoExceptionType.*;
import static com.example.norush.exception.MemberExceptionType.USER_NOT_FOUND;

import com.example.norush.domain.Memo;
import com.example.norush.domain.Member;
import com.example.norush.dto.MemoAddRequest;
import com.example.norush.dto.MemoResponse;
import com.example.norush.dto.MemoUpdateRequest;
import com.example.norush.exception.MemoException;
import com.example.norush.exception.MemberException;
import com.example.norush.repository.MemoRepository;
import com.example.norush.repository.MemberRepository;
import com.example.norush.util.MemoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MemoResponse addMemo(String memberId, MemoAddRequest request) {
        validateMemberExistence(memberId);

        Member member = findMemberById(memberId);

        Memo newMemo = Memo.builder()
                .id(MemoUtil.generateMemoId())
                .userId(member.getId())
                .title(request.title())
                .content(request.content())
                .timestamp(LocalDateTime.now())
                .build();

        Memo savedMemo = memoRepository.save(newMemo);
        return MemoResponse.from(savedMemo);
    }

    @Transactional(readOnly = true)
    public List<MemoResponse> getMemos(String memberId) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        return memoRepository.findByUserId(member.getId()).stream()
                .map(MemoResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemoResponse getMemoById(String memoId, String memberId) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        Memo memo = findMemoByIdAndUserId(memoId, member.getId());

        return MemoResponse.from(memo);
    }

    @Transactional
    public MemoResponse updateMemo(String memoId, String memberId, MemoUpdateRequest request) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        Memo existingMemo = findMemoByIdAndUserId(memoId, member.getId());

        existingMemo.update(
                request.title(),
                request.content()
        );

        Memo updatedMemo = memoRepository.save(existingMemo);
        return MemoResponse.from(updatedMemo);
    }

    @Transactional
    public void deleteMemo(String memoId, String memberId) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        Memo memoToDelete = findMemoByIdAndUserId(memoId, member.getId());

        memoRepository.delete(memoToDelete);
    }

    private void validateMemberExistence(String memberId) {
        if (!memberRepository.findById(memberId).isPresent()) {
            throw new MemberException(USER_NOT_FOUND);
        }
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
    }

    private Memo findMemoByIdAndUserId(String memoId, String userId) {
        return memoRepository.findByIdAndUserId(memoId, userId)
                .orElseThrow(() -> new MemoException(MEMO_NOT_FOUND));
    }
}
