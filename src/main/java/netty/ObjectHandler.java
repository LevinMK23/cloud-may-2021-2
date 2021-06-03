package netty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import netty.client.FilesListRequest;

@Slf4j
public class ObjectHandler extends SimpleChannelInboundHandler<AbstractCommand> {

    private static final String serverRootDir = "serverDir";
    private Path serverPath;

    public ObjectHandler() {
        serverPath = Paths.get(serverRootDir);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractCommand message) throws Exception {
        if (message instanceof FilesListRequest) {
            log.debug("received: {}", message);
            ctx.writeAndFlush(getFiles());
        }
        if (message instanceof FileRequest) {
            log.debug("received: {}", message);
            ctx.writeAndFlush(getFileMessage((FileRequest) message));
        }
        if (message instanceof FileMessage) {
            log.debug("received fileMessage");
            saveFile((FileMessage) message);
        }
    }

    private void saveFile(FileMessage fileMessage) throws IOException {
        Files.write(
                serverPath.resolve(fileMessage.getName()),
                fileMessage.getBytes(),
                StandardOpenOption.CREATE
        );
    }

    private FileMessage getFileMessage(FileRequest request) throws IOException {
        return new FileMessage(serverPath.resolve(request.getName()));
    }

    private FilesListResponse getFiles() throws IOException {
        return new FilesListResponse(serverPath);
    }

}
