package com.myaws.myapp.service;

import java.util.ArrayList;

import com.myaws.myapp.domain.MemberVo;

// ���������� MEMBER ��ɿ��� ��� �� �޼ҵ带 ���� �ϴ� ��
public interface MemberService {

	public int memberInsert(MemberVo mv);
	
	public int memberIdCheck(String memberId);
	
	public MemberVo memberLoginCheck(String memberId);
	
	public ArrayList<MemberVo> memberSelectAll();
}
