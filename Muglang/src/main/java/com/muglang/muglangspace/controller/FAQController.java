package com.muglang.muglangspace.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.muglang.muglangspace.dto.MglgBoardDTO;
import com.muglang.muglangspace.dto.MglgResponseDTO;
import com.muglang.muglangspace.entity.MglgBoard;
import com.muglang.muglangspace.service.mglgboard.MglgBoardService;

@RestController
@RequestMapping("/board")
public class FAQController {
	@Autowired
	private MglgBoardService mglgBoardService;

	// 어드민 FAQ로 이동
	@GetMapping("/adminFAQ")
	public ModelAndView adminFAQ() {
		List<MglgBoard> board = mglgBoardService.getFAQList();
		ModelAndView mv = new ModelAndView();

		mv.addObject("boardList", board);
		mv.setViewName("/admin/adminFAQ.html");
		return mv;
	}

	// FAQ로 이동
	@GetMapping("/userFAQ")
	public ModelAndView userFAQ() {
		List<MglgBoard> board = mglgBoardService.getFAQList();
		ModelAndView mv = new ModelAndView();

		mv.addObject("boardList", board);
		mv.setViewName("/board/userFAQ.html");
		return mv;
	}

	@GetMapping("getAdminFAQ")
	public ResponseEntity<?> getBoard(@RequestParam("boardId") int boardId) {
		MglgResponseDTO<MglgBoardDTO> response = new MglgResponseDTO<>();
		try {
			MglgBoard board = MglgBoard.builder().boardId(boardId).build();

			board = mglgBoardService.getBoard(board);
			MglgBoardDTO returnBoardDTO = MglgBoardDTO.builder()
					.boardId(boardId)
					.boardTitle(board.getBoardTitle())
					.boardContent(board.getBoardContent())
					.build();

			response.setItem(returnBoardDTO);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	

	@GetMapping("/deleteBoard")
	public void deleteBoard(@RequestParam("boardId") int boardId, HttpServletResponse response) throws IOException {
		mglgBoardService.deleteBoard(boardId);

		response.sendRedirect("/board/adminFAQ");
	}
	@PostMapping("/updateBoard")
	public void updateBoard(MglgBoardDTO boardDTO){
		MglgBoard board = MglgBoard.builder()
							.boardTitle(boardDTO.getBoardTitle())
							.boardContent(boardDTO.getBoardContent())
							.boardId(boardDTO.getBoardId())
							.build();
		mglgBoardService.updateBoard(board);
	

	}
	@PostMapping("/deleteBoard")
	public void deleteBoard(MglgBoardDTO boardDTO){
		MglgBoard board = MglgBoard.builder()
							.boardId(boardDTO.getBoardId())
							.build();
		mglgBoardService.deleteBoard(board);
	

	}
	@PostMapping("/insertBoard")
	public void insertBoard(MglgBoardDTO boardDTO)  {
		MglgBoard board = MglgBoard.builder()
							.boardTitle(boardDTO.getBoardTitle())
							.boardContent(boardDTO.getBoardContent())
							.build();
		mglgBoardService.insertBoard(board);
	

	}

}
