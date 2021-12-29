package bgu.spl.net.srv.bidi;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionsImpl;

import java.sql.Connection;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private Connections<T> connections;
    private int myConnectionId;

    @Override
    public void start(int connectionId, Connections<T> connections) {
        this.connections = connections;
        this.myConnectionId = connectionId;
    }

    @Override
    public void process(T message) {
        connections.send(myConnectionId, message );
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}