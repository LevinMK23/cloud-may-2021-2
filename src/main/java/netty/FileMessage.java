package netty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileMessage extends AbstractCommand {
    private String name;
    private byte[] bytes;

    public FileMessage(Path path) throws IOException {
        name = path.getFileName().toString();
        bytes = Files.readAllBytes(path);
    }
}
