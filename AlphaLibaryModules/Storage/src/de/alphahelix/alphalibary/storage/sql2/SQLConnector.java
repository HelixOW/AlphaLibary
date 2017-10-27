package de.alphahelix.alphalibary.storage.sql2;

import java.sql.Connection;

public interface SQLConnector {

    Connection connect();

    SQLConnectionHandler handler();

}
