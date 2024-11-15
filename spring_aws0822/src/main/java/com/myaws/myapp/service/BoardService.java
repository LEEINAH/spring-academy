package com.myaws.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

public interface BoardService {

	// 모든 게시글 추출
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri);
	
	// 게시글 총 개수 추출
	public int boardTotalCount(SearchCriteria scri);
	
	// 게시글 작성
	public int boardInsert(BoardVo bv);
	
	// 특정 게시글 추출
	public BoardVo boardSelectOne(int bidx);
	
	// 조회수 업데이트
	public int boardViewCntUpdate(int bidx);
	
	// 추천수 업데이트
	public int boardRecomUpdate(int bidx);
	
	// 게시글 삭제
	public int boardDelete(int bidx, int midx, String password);
	
	// 게시글 수정
	public int boardUpdate(BoardVo bv);
	
	// 게시글 답변 생성
	public int boardReply(BoardVo bv);
}
