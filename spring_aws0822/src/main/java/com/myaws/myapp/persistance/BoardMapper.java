package com.myaws.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

public interface BoardMapper {

	// ��� �Խñ� ����
	public ArrayList<BoardVo> boardSelectAll(HashMap<String,Object> hm);
	
	// �Խñ� �� ���� ����
	public int boardTotalCount(SearchCriteria scri);
	
	// �Խñ� ����
	public int boardInsert(BoardVo bv);
	
	// �Խñ� ���� �� null���� ���ִ� Originbidx�� maxbidx���� �־��ִ� Update �޼���
	public int boardOriginbidxUpdate(int bidx);
	
	// Ư�� �Խñ� ����
	public BoardVo boardSelectOne(int bidx);
	
	// ��ȸ�� ������Ʈ
	public int boardViewCntUpdate(int bidx);
	
	// ��õ�� ������Ʈ
	public int boardRecomUpdate(BoardVo bv);
	
	// �Խñ� ����
	public int boardDelete(HashMap<String,Object> hm);
	
	// �Խñ� ����
	public int boardUpdate(BoardVo bv);
	
	// �Խñ� �亯 deqth, level ������Ʈ
	public int boardReplyUpdate(BoardVo bv);
	
	// �Խñ� �亯 ����
	public int boardReplyInsert(BoardVo bv);
}
