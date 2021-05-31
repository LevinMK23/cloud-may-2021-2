package netty.client;

import netty.AbstractCommand;
import netty.Message;

@FunctionalInterface
public interface Callback {
    void call(AbstractCommand message);
}
