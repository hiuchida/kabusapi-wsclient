package com.github.hiuchida.kabusapi.wsclient;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.github.hiuchida.kabusapi.client_ex.pushapi.BoardBean;
import com.github.hiuchida.kabusapi.client_ex.pushapi.BoardBeanFactory;

@ClientEndpoint
public class MainWSClient {
	/**
	 * PUSH API(WebSocket)のURI。
	 */
	public static final String WEBSOCKET_URI = "ws://localhost:18080/kabusapi/websocket";

	public static void main(String[] args) throws DeploymentException, IOException {
		new MainWSClient().execute();
	}

	/**
	 * メインスレッド。
	 */
	private Thread mainThread;

	/**
	 * WebSocketセッション。
	 */
	private Session session;

	public MainWSClient() throws DeploymentException, IOException {
		this.mainThread = Thread.currentThread();

		URI uri = URI.create(WEBSOCKET_URI);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		this.session = container.connectToServer(this, uri);
	}

	public void execute() {

		// 別途、RegisterApi.registerPut()を呼び出して銘柄登録しないと、onMessage()に受信しない。

		// シャットダウンハンドラ
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					session.close();
				} catch (IOException e) {
					e.printStackTrace(System.out);
					System.out.flush();
				}
				mainThread.interrupt();
				try {
					mainThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace(System.out);
					System.out.flush();
				}
				System.out.println("ShutdownHook: Done");
				System.out.flush();
			}
		});

		// イベントループ
		while (session.isOpen()) {
			try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace(System.out);
				System.out.flush();
			}
		}

		System.out.println("MainThread: Done");
		System.out.flush();
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen: uri=" + session.getRequestURI() + ", id=\'" + session.getId() + "\'");
		System.out.flush();
	}

	@OnMessage
	public void onMessage(String message) {
		try {
			System.out.println("onMessge: " + message);
			System.out.flush();
			BoardBean json = BoardBeanFactory.parseJson(message);
			System.out.println("    json:");
			System.out.println(json.toString());
			System.out.flush();
		} catch (Throwable t) {
			t.printStackTrace(System.out);
			System.out.flush();
		}
	}

	@OnError
	public void onError(Throwable th) {
		System.out.println("onError： " + th.getMessage());
		th.printStackTrace(System.out);
		System.out.flush();
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose: uri=" + session.getRequestURI() + ", id=\'" + session.getId() + "\'");
		System.out.flush();
		mainThread.interrupt();
	}

}
