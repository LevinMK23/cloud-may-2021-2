package netty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(callSuper = true)
@Data
public class FilesListResponse extends AbstractCommand {

    private List<String> files;

    public FilesListResponse(Path path) throws IOException {
        files = Files.list(path)
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());
    }
}
