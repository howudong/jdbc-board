package personal.board;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
@Component
public class TestData {
    private final DataSource dataSource;

    @Bean
    public void createBoardTable() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
        ) {
            statement.execute("DROP TABLE IF EXISTS board");
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS board(" +
                            "id int not null auto_increment," +
                            "name varchar(20) not null," +
                            "password varchar(20) not null," +
                            "title varchar(20) not null," +
                            "content varchar(100) null," +
                            "primary key(id))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
