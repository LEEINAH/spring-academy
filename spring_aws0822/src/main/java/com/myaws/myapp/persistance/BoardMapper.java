package com.myaws.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;

public interface BoardMapper {

	// 모든 게시글 추출
	public ArrayList<BoardVo> boardSelectAll(HashMap<String,Object> hm);
	
	// 게시글 총 개수 추출
	public int boardTotalCount(SearchCriteria scri);
	
	// 게시글 생성
	public int boardInsert(BoardVo bv);
	
	// 게시글 생성 후 null값이 들어가있는 Originbidx에 maxbidx값을 넣어주는 Update 메서드
	public int boardOriginbidxUpdate(int bidx);
	
	// 특정 게시글 추출
	public BoardVo boardSelectOne(int bidx);
	
	// 조회수 업데이트
	public int boardViewCntUpdate(int bidx);
	
	// 추천수 업데이트
	public int boardRecomUpdate(BoardVo bv);
	
	// 게시글 삭제
	public int boardDelete(HashMap<String,Object> hm);
	
	// 게시글 수정
	public int boardUpdate(BoardVo bv);
	
	// 게시글 답변 deqth, level 업데이트
	public int boardReplyUpdate(BoardVo bv);
	
	// 게시글 답변 생성
	public int boardReplyInsert(BoardVo bv);
}
