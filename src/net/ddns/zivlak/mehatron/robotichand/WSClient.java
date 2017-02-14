package net.ddns.zivlak.mehatron.robotichand;

import java.net.URI;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WSClient {

	private WebSocketClient m_webSocketClient;
	private List<IConnectHandler> m_connectHandlers;
	private List<IMessageHandler> m_messageHandlers;

	protected WSClient(URI url) {

		m_webSocketClient = new WebSocketClient(url) {

			@Override
			public void onOpen(ServerHandshake handshake) {
				for(IConnectHandler connectHandler : m_connectHandlers)
					connectHandler.onConnect();
			}
			
			@Override
			public void onMessage(String msg) {
				for(IMessageHandler messageHandler : m_messageHandlers)
					messageHandler.onMessageRecived(msg);
			}
			
			@Override
			public void onError(Exception ex) {
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
			}
		};
	}

	public void open() {
		m_webSocketClient.connect();
	}

	public void close() {
		m_webSocketClient.close();
	}

	public void send(String message) {
		m_webSocketClient.send(message);
	}

	public void addOnConnectHandler(IConnectHandler handler) {
		m_connectHandlers.add(handler);
	}

	public void addOnMessageHandler(IMessageHandler handler) {
		m_messageHandlers.add(handler);
	}

	/*
	 * Singleton
	 */

	private static WSClient s_wsClient = null;		// Singleton

	public static WSClient connect(URI url) {

		if(s_wsClient != null)
			s_wsClient.close();
		s_wsClient = new WSClient(url);

		s_wsClient.open();

		return s_wsClient;
	}

	public static void disconnect() {
		s_wsClient.close();
		s_wsClient = null;
	}

	public static WSClient getInstance() {
		return s_wsClient;
	}
}
