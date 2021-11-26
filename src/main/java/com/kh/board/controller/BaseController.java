package com.kh.board.controller;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
	// 메세지 보여주고 원래 위치로 이동
	protected String msgAndBack(HttpServletRequest req, String msg) {
		req.setAttribute("historyBack", true);
		req.setAttribute("msg", msg);
		return "common/redirect";
	}
	// 메세지 보여주고 이동하려던 위치로 이동
	protected String msgAndReplace(HttpServletRequest req, String msg, String redirectUrl) {
		req.setAttribute("redirectUrl", redirectUrl);
		req.setAttribute("msg", msg);
		return "common/redirect";
	}
}
