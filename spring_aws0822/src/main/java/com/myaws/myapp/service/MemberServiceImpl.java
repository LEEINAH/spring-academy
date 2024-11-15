package com.myaws.myapp.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myaws.myapp.domain.MemberVo;
import com.myaws.myapp.persistance.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{
	
	private MemberMapper mm;

	@Autowired
	public MemberServiceImpl(SqlSession sqlSession) {
	  	this.mm = sqlSession.getMapper(MemberMapper.class); // 실행 파일이 있는지 확인하기 위해 class를 붙임
												             // mybatis를 쓰기 위해 mapper와 연동
	}
	
	@Override
	public int memberInsert(MemberVo mv) {
		
		int value = mm.memberInsert(mv);
		
		return value;
	}
	
	public int memberIdCheck(String memberId) {
		
		int value = mm.memberIdCheck(memberId);
		
		return value;
	}

	public MemberVo memberLoginCheck(String memberId) {
		
		MemberVo mv = mm.memberLoginCheck(memberId);

		return mv;
	}

	@Override
	public ArrayList<MemberVo> memberSelectAll() {
		
		ArrayList<MemberVo> alist = mm.memberSelectAll();
		
		return alist;
	}
	
	
	
}
