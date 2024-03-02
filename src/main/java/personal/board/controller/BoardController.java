package personal.board.controller;

import jakarta.annotation.PostConstruct;
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
@RequestMapping("boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @PostConstruct
    public void createTestData() {
        for (int i = 0; i < 50; i++) {
            boardRepository.save(new Board("member" + i, "title" + i, "pwd" + i, "content" + i));
        }
    }

    @GetMapping("/write")
    public String boardForm(Model model) {
        model.addAttribute("board", new Board());
        return "addBoard";
    }

    @PostMapping("/write")
    public String addBoard(@ModelAttribute Board board) {
        boardRepository.save(board);
        return "redirect:/";
    }

    @GetMapping
    public String boards(Model model,
                         @RequestParam(defaultValue = "0", name = "offset") Integer offset,
                         @RequestParam(defaultValue = "5", name = "size") Integer size) {
        List<Board> boards = boardRepository.findPortion(offset, size);
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


    @PostMapping("/{id}")
    public String deleteBoard(@PathVariable("id") Long id) {
        boardRepository.delete(id);
        return "redirect:/boards";
    }
}
