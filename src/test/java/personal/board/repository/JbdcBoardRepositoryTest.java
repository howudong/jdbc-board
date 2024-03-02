package personal.board.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import personal.board.domain.entity.Board;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JbdcBoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @AfterEach
    void clear() {
        boardRepository.clear();
    }

    @Test
    @DisplayName("저장한 게시글과 같은 객체면 성공한다.")
    void 저장_ID_테스트() {
        //given
        for (int i = 1; i <= 10; i++) {
            //given
            Board board = new Board("test" + i, "pwd" + i, "hong" + i, "a");
            //when
            Long savedId = boardRepository.save(board);
            //then
            assertThat(savedId).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("저정한 게시글과 조회한 게시글이 같은 객체면 성공한다.")
    void 멤버_조회_테스트() {
        //given
        Board board = new Board("test", "pwd", "hong", "a");
        //when
        Long savedId = boardRepository.save(board);
        Board findBoard = boardRepository.findById(savedId).get();
        //then
        assertThat(findBoard).isEqualTo(board);
    }

    @Test
    @DisplayName("해당 게시글 ID를 삭제하고 조회시 없으면 성공한다.")
    void 멤버_삭제_테스트() {
        //given
        for (int i = 0; i < 12; i++) {
            Board board = new Board("m" + i, "l" + i, "p" + i, "a");
            boardRepository.save(board);
        }
        //when
        Board deletedBoard = boardRepository.delete(3L);
        Optional<Board> result = boardRepository.findById(deletedBoard.getId());
        //then
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("데이터베이스에 저장한 수와 같으면 성공한다.")
    void 멤버_객체_개수_조회() {
        //given
        int memberCount = 12;
        for (int i = 0; i < 12; i++) {
            Board board = new Board("m" + i, "l" + i, "p" + i, "a");
            boardRepository.save(board);
        }
        //when
        int memberSize = boardRepository.findAll().size();
        //then
        assertThat(memberSize).isEqualTo(memberCount);
    }

    @Test
    @DisplayName("저장되지 않은 게시글을 삭제할시 예외가 발생한다.")
    void 멤버_예외_발생_테스트() {
        assertThatThrownBy(
                () -> boardRepository.delete(200L)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("페이지 쿼리만큼 뜨면 성공")
    void JbdcBoardRepositoryTest() {
        //given
        for (int i = 0; i < 12; i++) {
            Board board = new Board("m" + i, "l" + i, "p" + i, "a");
            boardRepository.save(board);
        }
        //when
        int size = 3;
        int offset = 1;
        List<Board> portion = boardRepository.findPortion(1, size);
        List<Long> boards = portion.stream().map(Board::getId).toList();
        //then
        assertThat(boards).hasSize(size);
        assertThat(boards).containsExactly(9L, 8L, 7L);
    }

}