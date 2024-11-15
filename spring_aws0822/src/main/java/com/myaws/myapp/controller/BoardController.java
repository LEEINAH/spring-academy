package com.myaws.myapp.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myaws.myapp.domain.BoardVo;
import com.myaws.myapp.domain.PageMaker;
import com.myaws.myapp.domain.SearchCriteria;
import com.myaws.myapp.service.BoardService;
import com.myaws.myapp.util.MediaUtils;
import com.myaws.myapp.util.UploadFileUtiles;
import com.myaws.myapp.util.UserIp;


@Controller
@RequestMapping(value="/board/")
public class BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired(required=false)
	private BoardService boardService;
	
	@Autowired(required=false)
	private UserIp userIp;
	
	@Autowired(required=false)
	private PageMaker pm;
	
	@Resource(name="uploadPath")
	private String uploadPath;
		
	@RequestMapping(value="boardList.aws", method=RequestMethod.GET)
	public String boardList(SearchCriteria scri, Model model) {
			
		int cnt = boardService.boardTotalCount(scri);
		
		pm.setScri(scri);
		pm.setTotalCount(cnt);
			
		ArrayList<BoardVo> blist = boardService.boardSelectAll(scri);
			
		model.addAttribute("blist", blist);
		model.addAttribute("pm", pm);
		
		String path = "WEB-INF/board/boardList";
		return path; 
	}
		
	@RequestMapping(value="boardWrite.aws", method=RequestMethod.GET)
	public String boardWrite() {
		
		String path = "WEB-INF/board/boardWrite";
		return path; 
	}
	
	@RequestMapping(value="boardWriteAction.aws", method=RequestMethod.POST)
	public String boardWriteAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile attachfile,
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		MultipartFile file = attachfile;
		String uploadedFileName = "";
		
		if (! file.getOriginalFilename().equals("")) {	
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			// 업로드 파일 이름 추출
		}
		
		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx); // 회원번호를 숫자형으로 추출
		
		String ip = userIp.getUserIp(request); // ip 추출
		
		// bv에 추출한 3개의 값을 담는다
		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int value = boardService.boardInsert(bv);

		String path = "";
		if (value == 2) { 
			path = "redirect:/board/boardList.aws"; 
		} else { 
			rttr.addFlashAttribute("msg", "입력이 잘못 되었습니다.");
			path = "redirect:/board/boardWrite.aws"; 
		}	
		
		return path; 
	}
	
	@RequestMapping(value="boardContents.aws", method=RequestMethod.GET)
	public String boardContents(@RequestParam("bidx") int bidx, Model model) {
		
		boardService.boardViewCntUpdate(bidx);
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		model.addAttribute("bv", bv);
		
		String path = "WEB-INF/board/boardContents";
		return path; 
	}
	
	@RequestMapping(value="/displayFile.aws", method=RequestMethod.GET)
	public ResponseEntity<byte[]> displayFile(
			@RequestParam("fileName") String fileName,
			@RequestParam(value="down", defaultValue="0") int down
			) {
		
		ResponseEntity<byte[]> entity = null;
		InputStream in = null;
		
		try{
			
			String formatName = fileName.substring(fileName.lastIndexOf(".")+1); // 확장자를 꺼낸다
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();		
			 
			in = new FileInputStream(uploadPath+fileName);
			
			
			if(mType != null){
				
				if (down==1) {
					fileName = fileName.substring(fileName.indexOf("_")+1);
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.add("Content-Disposition", "attachment; filename=\""+
							new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");	
					
				}else {
					headers.setContentType(mType);	
				}
				
			}else{
				
				fileName = fileName.substring(fileName.indexOf("_")+1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\""+
						new String(fileName.getBytes("UTF-8"),"ISO-8859-1")+"\"");				
			}
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),
					headers,
					HttpStatus.CREATED);
			
		}catch(Exception e){
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return entity;
	}
	
	@ResponseBody
	@RequestMapping(value="boardRecom.aws", method=RequestMethod.GET)
	public JSONObject boardRecom(@RequestParam("bidx") int bidx) {
		
		int value = boardService.boardRecomUpdate(bidx);		
		
		JSONObject js = new JSONObject();
		js.put("recom", value);

		return js; 
	}
	
	@RequestMapping(value="boardDelete.aws", method=RequestMethod.GET)
	public String boardDelete(@RequestParam("bidx") int bidx, Model model) {
		
		model.addAttribute("bidx", bidx);
		
		String path = "WEB-INF/board/boardDelete";
		return path; 
	}
	
	@RequestMapping(value="boardDeleteAction.aws", method=RequestMethod.POST)
	public String boardDeleteAction(
			@RequestParam("bidx") int bidx,
			@RequestParam("password") String password,
			HttpSession session,
			RedirectAttributes rttr) {

		BoardVo bv = boardService.boardSelectOne(bidx);
		int midx = Integer.parseInt(session.getAttribute("midx").toString());
		int value = boardService.boardDelete(bidx, midx, password);

	      
	    String path = "";
	    if(bv.getMidx()==midx) {
	       if(value==1) {
	          path = "redirect:/board/boardList.aws";
	       }else {
	          rttr.addFlashAttribute("msg", "비밀번호가 틀렸습니다.");
	          path = "redirect:/board/boardDelete.aws?bidx="+bidx;
	       }
	    }else {
	       rttr.addFlashAttribute("msg", "자신의 게시글만 삭제 할 수 있습니다.");
	       path = "redirect:/board/boardDelete.aws?bidx="+bidx;
	    }

	    return path;
	}
	
	@RequestMapping(value="boardModify.aws", method=RequestMethod.GET)
	public String boardModify(@RequestParam("bidx") int bidx, Model model) {
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		model.addAttribute("bv", bv);
		
		String path = "WEB-INF/board/boardModify";
		return path; 
	}	
	
	@RequestMapping(value="boardModifyAction.aws", method=RequestMethod.POST)
	public String boardModifyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile attachfile,
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		MultipartFile file = attachfile;
		String uploadedFileName = "";
		
		if (! file.getOriginalFilename().equals("")) {	
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			// 업로드 파일 이름 추출
		}

		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx); // 회원번호를 숫자형으로 추출
		String ip = userIp.getUserIp(request); // ip 추출
		
		// bv에 추출한 3개의 값을 담는다
		bv.setUploadedFilename(uploadedFileName);
		bv.setIp(ip);

		int value = boardService.boardUpdate(bv);

		String path = "";

		if(bv.getMidx() == midx_int) {
			if(value == 1) {
				path = "redirect:/board/boardContents.aws?bidx=" + bv.getBidx();
			}else {
		        rttr.addFlashAttribute("msg", "비밀번호가 틀렸습니다.");
		        path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
		    }
		}else {
		    rttr.addFlashAttribute("msg", "자신의 게시글만 수정 할 수 있습니다.");
		    path = "redirect:/board/boardModify.aws?bidx=" + bv.getBidx();
		}
				
		return path; 
	}
	
	@RequestMapping(value="boardReply.aws", method=RequestMethod.GET)
	public String boardReply(@RequestParam("bidx") int bidx, Model model) {
		
		BoardVo bv = boardService.boardSelectOne(bidx);
		model.addAttribute("bv", bv);
		
		String path = "WEB-INF/board/boardReply";
		return path; 
	}	
	
	@RequestMapping(value="boardReplyAction.aws", method=RequestMethod.POST)
	public String boardReplyAction(
			BoardVo bv,
			@RequestParam("attachfile") MultipartFile attachfile,
			HttpServletRequest request,
			RedirectAttributes rttr
			) throws Exception {
		
		MultipartFile file = attachfile;
		String uploadedFileName = "";
		
		if (! file.getOriginalFilename().equals("")) {	
			uploadedFileName = UploadFileUtiles.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());
			// 업로드 파일 이름 추출
		}
		
		String midx = request.getSession().getAttribute("midx").toString();
		int midx_int = Integer.parseInt(midx); // 회원번호를 숫자형으로 추출
		String ip = userIp.getUserIp(request); // ip 추출
		
		// bv에 추출한 3개의 값을 담는다
		bv.setUploadedFilename(uploadedFileName);
		bv.setMidx(midx_int);
		bv.setIp(ip);
		
		int maxBidx = boardService.boardReply(bv);
		
		String path = "";
		if (maxBidx != 0) { 
			path = "redirect:/board/boardContents.aws?bidx=" + maxBidx; 
		} else { 
			rttr.addFlashAttribute("msg", "입력이 잘못 되었습니다.");
			path = "redirect:/board/boardReply.aws?bidx=" + bv.getBidx(); 
		}	
		
		return path; 
	}	
}
