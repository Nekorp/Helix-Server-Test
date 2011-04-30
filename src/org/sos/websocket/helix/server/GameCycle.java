package org.sos.websocket.helix.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jetty.websocket.WebSocket;

public class GameCycle implements Runnable, WebSocket {

	private final static Set<GameCycle> users = new HashSet<GameCycle>();
	
	private final static GameCycle theGame = new GameCycle();
	
	private boolean alive;
	
	private static long frameNumber = 0;
	
	@Override
	public void run() {
		System.out.println("inicio ciclo infinito");
		while(GameCycle.theGame.alive) {
			//System.out.println("vuelta");
			for (GameCycle user : GameCycle.users) {
				try {
					//System.out.println("msg");
					user.getOutbound().sendMessage(GameCycle.frameNumber + "");
				} catch (IOException e) {
					GameCycle.theGame.alive = false;
					e.printStackTrace();
				}
			}
			GameCycle.frameNumber++;
			try {
				//System.out.println("sleep");
				Thread.sleep(17);
			} catch (InterruptedException e) {
				GameCycle.theGame.alive = false;
				e.printStackTrace();
			}
		}
		GameCycle.frameNumber = 0;
		System.out.println("fin ciclo infinito");
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}	
	
	//lo del web socket
	private Outbound outbound;

	public void onConnect(Outbound outbound) {
		System.out.println("conectando");
		this.outbound = outbound;
		GameCycle.users.add(this);
		if (!GameCycle.theGame.isAlive()) {
			System.out.println("arrancando");
			GameCycle.theGame.setAlive(true);
			new Thread(theGame).start();
		}
	}

	/*
	 * @see org.eclipse.jetty.websocket.WebSocket#onMessage(byte, byte[],
	 * int, int)
	 */
	public void onMessage(byte frame, byte[] data, int offset, int length) {
	}

	/*
	 * @see org.eclipse.jetty.websocket.WebSocket#onMessage(byte,
	 * java.lang.String)
	 */
	public void onMessage(byte frame, String data) {
		System.out.println("idc:" + data);
	}

	/*
	 * @see org.eclipse.jetty.websocket.WebSocket#onDisconnect()
	 */
	public void onDisconnect() {
		System.out.println("desconectando");
		GameCycle.users.remove(this);
		if (GameCycle.users.size() == 0) {
			GameCycle.theGame.alive = false;
		}
	}
	
	/*
	 * 
	 * @see org.eclipse.jetty.websocket.WebSocket#onFragment(boolean, byte,
	 * byte[], int, int)
	 */
	public void onFragment(boolean more, byte opcode, byte[] data,
			int offset, int length) {

	}

	public Outbound getOutbound() {
		return outbound;
	}
	
}
