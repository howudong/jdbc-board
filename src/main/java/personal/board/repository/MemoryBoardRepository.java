package personal.board.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import personal.board.domain.entity.Board;

import java.util.*;

@Slf4j
@Repository
public class MemoryBoardRepository implements BoardRepository {
    private final Map<Long, Board> store = new HashMap<>();
    private Long sequence = 0L;

    @Override
    public Long save(Board board) {
        board.setId(sequence++);
        store.put(board.getId(), board);

        log.info("새로운 게시물 저장 : {}", board);
        return board.getId();
    }

    @Override
    public List<Board> findAll() {
        List<Board> boards = new ArrayList<>(store.values());
        return Collections.unmodifiableList(boards);
    }

    @Override
    public Optional<Board> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Board delete(Long id) {
        Board board = findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));

        store.remove(id);
        return board;
    }

    @Override
    public void update(Long id, Board board) {

    }

    @Override
    public void clear() {
        sequence = 0L;
        store.clear();
    }

    @Override
    public List<Board> findPortion(int offset, int size) {
        return null;
    }
}
