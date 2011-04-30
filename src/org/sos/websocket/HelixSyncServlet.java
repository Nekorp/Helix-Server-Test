package org.sos.websocket;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.sos.websocket.helix.server.GameCycle;

/**
 * Helix game sync test speed.
 */
public class HelixSyncServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
       
    
    public HelixSyncServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getNamedDispatcher("default").forward(request, response);
	}
	protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new GameCycle();
	}

}
