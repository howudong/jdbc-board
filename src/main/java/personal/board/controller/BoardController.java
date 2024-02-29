package personal.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.board.domain.entity.Board;
import personal.board.repository.BoardRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @GetMapping("/add")
    public String addBoardForm(Model model) {
        model.addAttribute("board", new Board());
        return "addBoard";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Board board) {
        boardRepository.save(board);
        return "redirect:/";
    }

    @RequestMapping()
    public String boards(Model model) {
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "boards";
    }

    @GetMapping("/{id}")
    public String board(Model model, @PathVariable("id") Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("없음"));

        model.addAttribute("board", board);

        return "board";
    }
}
