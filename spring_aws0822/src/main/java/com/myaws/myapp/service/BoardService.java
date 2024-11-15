package com.myaws.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

public interface BoardService {

	// ��� �Խñ� ����
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri);
	
	// �Խñ� �� ���� ����
	public int boardTotalCount(SearchCriteria scri);
	
	// �Խñ� �ۼ�
	public int boardInsert(BoardVo bv);
	
	// Ư�� �Խñ� ����
	public BoardVo boardSelectOne(int bidx);
	
	// ��ȸ�� ������Ʈ
	public int boardViewCntUpdate(int bidx);
	
	// ��õ�� ������Ʈ
	public int boardRecomUpdate(int bidx);
	
	// �Խñ� ����
	public int boardDelete(int bidx, int midx, String password);
	
	// �Խñ� ����
	public int boardUpdate(BoardVo bv);
	
	// �Խñ� �亯 ����
	public int boardReply(BoardVo bv);
}
