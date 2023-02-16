package com.joinproject.domain.member.dto;

import lombok.*;

import java.util.Optional;

@Data
@NoArgsConstructor
public class MemberUpdateDto {

    private Optional<String> name;
    private Optional<String> nickname;
    private Optional<Integer> age;

    @Builder
    public MemberUpdateDto(Optional<String> name, Optional<String> nickname, Optional<Integer> age) {
        this.name = name;
        this.nickname = nickname;
        this.age = age;
    }

}
