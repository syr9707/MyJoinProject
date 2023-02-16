package com.joinproject.domain.member.dto;

import com.joinproject.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDto {

    private String name;
    private String nickname;
    private String username;
    private Integer age;

    @Builder
    public MemberInfoDto(Member member) {
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
        this.age = member.getAge();;
    }

}
