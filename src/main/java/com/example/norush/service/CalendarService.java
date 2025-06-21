package com.example.norush.service;

import static com.example.norush.exception.CalendarExceptionType.*;
import static com.example.norush.exception.MemberExceptionType.USER_NOT_FOUND;

import com.example.norush.domain.Calendar;
import com.example.norush.domain.Member;
import com.example.norush.dto.CalendarAddRequest;
import com.example.norush.dto.CalendarResponse;
import com.example.norush.dto.CalendarUpdateRequest;
import com.example.norush.exception.CalendarException;
import com.example.norush.exception.MemberException;
import com.example.norush.repository.CalendarRepository;
import com.example.norush.repository.MemberRepository;
import com.example.norush.util.CalendarUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CalendarResponse addCalendarEntry(String memberId, CalendarAddRequest request) {
        validateMemberExistence(memberId);

        Member member = findMemberById(memberId);

        Calendar newCalendarEntry = Calendar.builder()
                .id(CalendarUtil.generateCalendarId())
                .userId(member.getId())
                .year(request.year())
                .month(request.month())
                .day(request.day())
                .memo(request.memo())
                .build();

        Calendar savedCalendarEntry = calendarRepository.save(newCalendarEntry);
        return CalendarResponse.from(savedCalendarEntry);
    }

    @Transactional(readOnly = true)
    public List<CalendarResponse> getCalendarEntries(String memberId, Integer year, Integer month) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        List<Calendar> calendarEntries;
        if (year != null && month != null) {
            calendarEntries = calendarRepository.findByUserIdAndYearAndMonth(member.getId(), year, month);
        } else {
            calendarEntries = calendarRepository.findByUserId(member.getId());
        }

        return calendarEntries.stream()
                .map(CalendarResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalendarResponse getCalendarEntryById(String calendarId, String memberId) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        Calendar calendarEntry = findCalendarEntryByIdAndUserId(calendarId, member.getId());

        return CalendarResponse.from(calendarEntry);
    }

    @Transactional
    public CalendarResponse updateCalendarEntry(String calendarId, String memberId, CalendarUpdateRequest request) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        Calendar existingCalendarEntry = findCalendarEntryByIdAndUserId(calendarId, member.getId());

        existingCalendarEntry.update(
                request.year(),
                request.month(),
                request.day(),
                request.memo()
        );

        Calendar updatedCalendarEntry = calendarRepository.save(existingCalendarEntry);
        return CalendarResponse.from(updatedCalendarEntry);
    }

    @Transactional
    public void deleteCalendarEntry(String calendarId, String memberId) {
        validateMemberExistence(memberId);
        Member member = findMemberById(memberId);

        Calendar calendarEntryToDelete = findCalendarEntryByIdAndUserId(calendarId, member.getId());

        calendarRepository.delete(calendarEntryToDelete);
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

    private Calendar findCalendarEntryByIdAndUserId(String calendarId, String userId) {
        return calendarRepository.findByIdAndUserId(calendarId, userId)
                .orElseThrow(() -> new CalendarException(CALENDAR_NOT_FOUND));
    }
}
