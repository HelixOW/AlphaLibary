package io.github.alphahelixdev.alpary.utilities.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ServerResult implements Serializable {
	private final int playercount, maximumPlayers;
	private final String motd;
}
