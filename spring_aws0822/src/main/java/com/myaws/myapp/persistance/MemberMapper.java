package com.myaws.myapp.persistance;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.myaws.myapp.domain.MemberVo;

// MEMBER ��ɿ��� ����� mybatis�� �޼ҵ� ���� �ϴ� ��
public interface MemberMapper {

	public int memberInsert(MemberVo mv); 
	
	public int memberIdCheck(String memberId);
	
	public MemberVo memberLoginCheck(String memberId);
	
	public ArrayList<MemberVo> memberSelectAll();
}
