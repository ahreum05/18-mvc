package exam2.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exam2.message.Action;
import exam2.message.MessageGuestAction;
import exam2.message.MessageHostAction;
import exam2.message.MessageMismatchAction;
import exam2.message.MessageNullAction;


@WebServlet("/MessageController2")
public class MessageController2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public MessageController2() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doRequest(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doRequest(request, response);
	}

    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 요청정보 확인
    	String message = null;
    	message = request.getParameter("message");
    	
    	// 2. 요청 작업 처리
    	// => model 선택 : 데이터 처리 클래스 선택
        // => 1) 데이터 처리 2) 데이터 공유 3) view 처리 파일명 리턴
    	Action action = null;
    	if(message == null) {
    	   action = new MessageNullAction();
    	} else if(message.equals("host")) {
    		action = new MessageHostAction();  
    	} else if(message.equals("guest")) {
    		action = new MessageGuestAction();
    	} else { 
    	    action = new MessageMismatchAction();
        }	
    	// 3. 데이터 처리 및 view 처리 파일명 저장
        // => 1) 데이터 처리 
    	
    	String view = null;
    	if(action !=null ) {
    		try {
				view = action.process(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    		
    	// 4. view 처리 파일도 이동
    	if(view != null) {
    		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
    		dispatcher.forward(request, response);
    	}
	}
}
