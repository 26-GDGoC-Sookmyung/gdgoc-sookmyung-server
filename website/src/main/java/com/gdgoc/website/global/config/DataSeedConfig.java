package com.gdgoc.website.global.config;

import com.gdgoc.website.member.entity.Member;
import com.gdgoc.website.member.entity.MemberRole;
import com.gdgoc.website.member.entity.MemberTagType;
import com.gdgoc.website.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeedConfig implements ApplicationRunner {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (memberRepository.count() == 0) {
            log.info("DB에 멤버 데이터가 없습니다. 6기 멤버 초기 데이터 세팅을 시작합니다...");

            List<Member> initialMembers = List.of(
                createMember("조예인", MemberRole.ORGANIZER, null, List.of(MemberTagType.AI_ML, MemberTagType.DEV_REL)),
                createMember("이연재", MemberRole.TEAM_MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-leeyeonjae.jpg", List.of(MemberTagType.APP_WEB, MemberTagType.FRONTEND)),
                createMember("김혜나", MemberRole.TEAM_MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-kimhyena.jpg", List.of(MemberTagType.AI_ML, MemberTagType.CLOUD)),
                createMember("최서영", MemberRole.TEAM_MEMBER, null, List.of(MemberTagType.AI_ML, MemberTagType.DEV_REL)),
                createMember("최윤서", MemberRole.TEAM_MEMBER, null, List.of(MemberTagType.AI_ML, MemberTagType.DEV_REL)),
                createMember("제유진", MemberRole.TEAM_MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("이하늘", MemberRole.TEAM_MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("이경은", MemberRole.TEAM_MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-leekyungeun.jpeg", List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("최우진", MemberRole.TEAM_MEMBER, null, List.of(MemberTagType.FLUTTER, MemberTagType.FRONTEND)),
                createMember("김민영", MemberRole.TEAM_MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-kimminyoung.jpeg", List.of(MemberTagType.AI_ML, MemberTagType.DEV_REL)),
                createMember("곽해림", MemberRole.TEAM_MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-kwakhaerim.jpeg", List.of(MemberTagType.AI_ML, MemberTagType.DEV_REL)),
                createMember("오현서", MemberRole.TEAM_MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-ohhyunseo.jpeg", List.of(MemberTagType.AI_ML, MemberTagType.FRONTEND)),
                createMember("유가영", MemberRole.MEMBER, null, List.of(MemberTagType.REACT, MemberTagType.FRONTEND)),
                createMember("한지민", MemberRole.MEMBER, null, List.of(MemberTagType.REACT, MemberTagType.FRONTEND)),
                createMember("송유민", MemberRole.MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-songyumin.jpg", List.of(MemberTagType.APP_WEB, MemberTagType.DEV_REL)),
                createMember("권도연", MemberRole.MEMBER, null, List.of(MemberTagType.REACT, MemberTagType.FRONTEND)),
                createMember("송서현", MemberRole.MEMBER, null, List.of(MemberTagType.FLUTTER, MemberTagType.FRONTEND)),
                createMember("홍서영", MemberRole.MEMBER, null, List.of(MemberTagType.FLUTTER, MemberTagType.FRONTEND)),
                createMember("전하은", MemberRole.MEMBER, null, List.of(MemberTagType.FLUTTER, MemberTagType.FRONTEND)),
                createMember("오정민", MemberRole.MEMBER, null, List.of(MemberTagType.REACT, MemberTagType.FRONTEND)),
                createMember("이채연", MemberRole.MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-leechaeyeon.jpeg", List.of(MemberTagType.AI_ML, MemberTagType.BACKEND)),
                createMember("현소윤", MemberRole.MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-hyunsoyun.jpg", List.of(MemberTagType.AI_ML, MemberTagType.BACKEND)),
                createMember("김찬란", MemberRole.MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-kimchanran.jpeg", List.of(MemberTagType.AI_ML, MemberTagType.CLOUD)),
                createMember("이채영", MemberRole.MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-leechaeyoung.png", List.of(MemberTagType.AI_ML, MemberTagType.DEV_REL)),
                createMember("정은서", MemberRole.MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("송하은", MemberRole.MEMBER, null, List.of(MemberTagType.FLUTTER, MemberTagType.FRONTEND)),
                createMember("안채원", MemberRole.MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("이주원", MemberRole.MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("권재희", MemberRole.MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("이은영", MemberRole.MEMBER, null, List.of(MemberTagType.SPRING, MemberTagType.BACKEND)),
                createMember("이기주", MemberRole.MEMBER, "https://pub-523ba1326e0a4220b9fe7c241f5198b6.r2.dev/members/6-leekiju.jpeg", List.of(MemberTagType.BACKEND, MemberTagType.CLOUD))
            );

            memberRepository.saveAll(initialMembers);
            log.info("✅ 초기 멤버 {}명 세팅 완료!", initialMembers.size());
        } else {
            log.info("✅ 이미 멤버 데이터가 존재하여 초기화 스크립트를 건너뜁니다.");
        }
    }

    private Member createMember(String name, MemberRole role, String profileImageUrl, List<MemberTagType> tags) {
        return Member.builder()
                .name(name)
                .role(role)
                .profileImageUrl(profileImageUrl)
                .generation(6)
                .tags(tags)
                .build();
    }
}