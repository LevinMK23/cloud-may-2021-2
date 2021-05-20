package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {

    private ByteBuffer buffer;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public Server() throws Exception {
        buffer = ByteBuffer.allocate(100);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8189));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (serverSocketChannel.isOpen()) {

            selector.select(); // block

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                keyIterator.remove();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder s = new StringBuilder();
        int r;
        while (true) {
            r = channel.read(buffer);
            if (r == -1) {
                channel.close();
                return;
            }
            if (r == 0) {
                break;
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                s.append((char) buffer.get());
            }
            buffer.clear();
         }

        String message = s.toString();

        channel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = serverSocketChannel.accept();
        channel.write(ByteBuffer.wrap("Welcome to server!".getBytes(StandardCharsets.UTF_8)));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, "Hello world");
    }
}
