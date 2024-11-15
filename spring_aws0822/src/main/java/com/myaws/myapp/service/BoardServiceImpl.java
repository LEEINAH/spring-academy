package com.myaws.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.persistance.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{
	
	private BoardMapper bm;

	@Autowired
	public BoardServiceImpl(SqlSession sqlSession) {
	  	this.bm = sqlSession.getMapper(BoardMapper.class); // 실행 파일이 있는지 확인하기 위해 class를 붙임
	  														// mybatis를 쓰기 위해 mapper와 연동
	}
	
	@Override
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri) {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("startPageNum", (scri.getPage()-1)*scri.getPerPageNum());
		hm.put("searchType", scri.getSearchType());
		hm.put("keyword", scri.getKeyword());
		hm.put("perPageNum", scri.getPerPageNum());		
		
		ArrayList<BoardVo> blist = bm.boardSelectAll(hm);
		
		return blist;
	}

	@Override
	public int boardTotalCount(SearchCriteria scri) {
		
		int cnt = bm.boardTotalCount(scri);
		return cnt;
	}

	@Override
	@Transactional
	public int boardInsert(BoardVo bv) {
	
		int value = bm.boardInsert(bv);
		int maxBidx = bv.getBidx();
		int value2 = bm.boardOriginbidxUpdate(maxBidx);
		
		return value+value2;
	}

	@Override
	public BoardVo boardSelectOne(int bidx) {
		
		BoardVo bv = bm.boardSelectOne(bidx);
		return bv;
	}

	@Override
	public int boardViewCntUpdate(int bidx) {
		
		int cnt = bm.boardViewCntUpdate(bidx);
		return cnt;
	}

	@Override
	public int boardRecomUpdate(int bidx) {
		
		BoardVo bv = new BoardVo();
		bv.setBidx(bidx);
		bm.boardRecomUpdate(bv);
		int recom = bv.getRecom();
		return recom;
	}

	@Override
	public int boardDelete(int bidx, int midx, String password) {
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("bidx", bidx);
		hm.put("midx", midx);
		hm.put("password", password);
		
		int value = bm.boardDelete(hm);
		return value;
	}

	@Override
	public int boardUpdate(BoardVo bv) {

		int value = bm.boardUpdate(bv);		
		return value;
	}

	@Transactional // update, insert 둘 중 하나라도 실행 실패하면 원복
	@Override
	public int boardReply(BoardVo bv) {
		// 업데이트 하고 입력하기
		bm.boardReplyUpdate(bv);
		bm.boardReplyInsert(bv);
		
		int maxBidx = bv.getBidx();
		
		return maxBidx;
	}

}
