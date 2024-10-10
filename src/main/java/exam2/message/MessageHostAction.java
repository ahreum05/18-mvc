package exam2.message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageHostAction implements Action  {
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
    // 1. 데이터 처리
	String result = "홍길동 입니다.";
	// 2. 데이터 공유
	request.setAttribute("result", result);
	// 3. view 처리 파일명 리턴
	return "/messageView.jsp";
	}
}


