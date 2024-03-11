package personal.board.repository;

import personal.board.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Long save(Board board);

    List<Board> findAll();

    List<Board> findPortion(int offset, int size);

    Optional<Board> findById(Long id);

    Board delete(Long id);

    void update(Long id, Board board);

    void clear();
}
