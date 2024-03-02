package personal.board.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import personal.board.domain.entity.Board;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
public class JbdcBoardRepository implements BoardRepository {
    private static final String INSERT_BOARD = "insert into board(name,password,title,content) values(?,?,?,?)";
    private static final String SELECT_BY_ID_QUERY = "select * from board where id = ?";
    private static final String DELETE_BY_ID_QUERY = "delete from board where id = ?";
    private final DataSource dataSource;

    @Override
    public Long save(Board board) {
        Long boardId = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(INSERT_BOARD, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setString(1, board.getUsername());
            ps.setString(2, board.getPassword());
            ps.setString(3, board.getTitle());
            ps.setString(4, board.getContent());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            String key = (rs.next()) ? rs.getString(1) : null;
            boardId = Long.parseLong(key);
            board.setId(boardId);
        } catch (SQLException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return boardId;
    }

    @Override
    public List<Board> findAll() {
        List<Board> boards = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "select id from board order by id desc ");
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long findId = Long.parseLong(rs.getString("id"));
                Board findBoard = findById(findId).orElseThrow(
                        () -> new RuntimeException("ERROR"));

                boards.add(findBoard);
            }
        } catch (SQLException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return boards;
    }

    @Override
    public Optional<Board> findById(Long id) {
        Board result = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_QUERY);
        ) {
            ps.setInt(1, id.intValue());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                String title = rs.getString("title");
                String content = rs.getString("content");
                result = new Board(name, title, password, content);
                result.setId(id);
            }
        } catch (SQLException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Board delete(Long id) {
        Board deletedBoard = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID_QUERY);
        ) {
            ps.setInt(1, id.intValue());

            deletedBoard = findById(id).orElseThrow(
                    () -> new NoSuchElementException("찾을 수 없습니다"));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return deletedBoard;
    }

    @Override
    public void update(Long id, Board board) {

    }

    @Override
    public void clear() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DELETE FROM board");
            statement.executeUpdate("ALTER TABLE board AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
    }
}
