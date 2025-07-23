package com.selfpro.selfprodemo.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {

	// DTO 역할

	@Size(max = 25, min = 3, message = "IDは3文字以上、25文字以下で入力してください。")
	@NotEmpty(message = "IDは必須項目です。")
	private String memberLoginId;

	@Size(min = 5, max = 16, message = "パスワードは5文字以上、16文字以下で入力してください。")
	@NotEmpty(message = "パスワードは必須項目です。")
	private String memberPassword1;

	@Size(min = 5, max = 16, message = "パスワードは5文字以上、16文字以下で入力してください。")
	@NotEmpty(message = "確認用パスワードは必須項目です。")
	private String memberPassword2;

}
