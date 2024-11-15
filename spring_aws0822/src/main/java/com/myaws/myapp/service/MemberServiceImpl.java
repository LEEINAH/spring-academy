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
	  	this.mm = sqlSession.getMapper(MemberMapper.class); // ���� ������ �ִ��� Ȯ���ϱ� ���� class�� ����
												             // mybatis�� ���� ���� mapper�� ����
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
