package com.myaws.myapp.persistance;

import java.util.ArrayList;
import com.myaws.myapp.domain.CommentVo;

public interface CommentMapper {

	// ¸ðµç ´ñ±Û ÃßÃâ
	public ArrayList<CommentVo> commentSelectAll(int bidx, int block);
	
	// ´ñ±Û »ý¼º
	public int commentInsert(CommentVo cv);
	
	// ´ñ±Û »èÁ¦
	public int commentDelete(CommentVo cv);
	
	// ¸ðµç ´ñ±Û °¹¼ö
	public int commentTotalCnt(int bidx);
}
