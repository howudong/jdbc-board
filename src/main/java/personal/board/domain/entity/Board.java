package personal.board.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Board {
    private Long id;
    private String username;
    private String title;
    private String password;
    private String content;

    public Board(String username, String title, String password, String content) {
        this.username = username;
        this.title = title;
        this.password = password;
        this.content = content;
    }
}

