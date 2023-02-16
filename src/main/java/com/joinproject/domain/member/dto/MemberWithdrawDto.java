package com.joinproject.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class MemberWithdrawDto {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String checkPassword;

}
