package exam1.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/MessageController")
public class MessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public MessageController() {
        super();
        
    }

	//데이터 처리, view 처리
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		doRequest(request, response);
	}
    
	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String message = null;
		String result = null;
		// 1. 요청 정보 확인
		message = request.getParameter("message");
		// 2. 요청 작업 처리 (=데이터 처리)
		if(message == null) {
			result = "전달된 내용이 없습니다.";
		} else if (message.equals("host")) {
			result = "고길동 입니다.";
		} else if (message.equals("guest")) {
			result = "홍길동 입니다.";
		} else {
			result = "타입이 맞지 않습니다.";
		}
		// 3. 데이터 공유
		request.setAttribute("result", result);
		// 4. view 페이지 이동
		String view  = "messageView.jsp";
	    RequestDispatcher dispatcher = request.getRequestDispatcher(view);
	    dispatcher.forward(request, response);
	}

}
