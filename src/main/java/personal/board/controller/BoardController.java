package personal.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.board.domain.entity.Board;
import personal.board.repository.BoardRepository;

@Controller
@Slf4j
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @GetMapping("/add")
    public String board(Model model) {
        model.addAttribute("board", new Board());
        return "board";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Board board) {
        boardRepository.save(board);
        return "redirect:/";
    }
}
