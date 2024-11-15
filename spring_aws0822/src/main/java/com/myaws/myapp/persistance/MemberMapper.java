package com.myaws.myapp.persistance;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.myaws.myapp.domain.MemberVo;

// MEMBER 기능에서 사용할 mybatis용 메소드 선언 하는 곳
public interface MemberMapper {

	public int memberInsert(MemberVo mv); 
	
	public int memberIdCheck(String memberId);
	
	public MemberVo memberLoginCheck(String memberId);
	
	public ArrayList<MemberVo> memberSelectAll();
}
