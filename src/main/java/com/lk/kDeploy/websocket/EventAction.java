package com.lk.kDeploy.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.lk.kDeploy.base.dto.SocketMsgDTO;

/**
 * 
 *
 * @author: lk
 * @since: 2017年11月12日
 */
@FunctionalInterface
public interface EventAction {

	void onData(final SocketIOClient client, SocketMsgDTO socketMsg);
}
