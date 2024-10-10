package exam4.mvc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
@WebServlet("*.do")
public class ControllerURI extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Map<String, Object> map = new HashMap<String, Object>();   
 
    public ControllerURI() {
        super();
        
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	String realFolder = config.getServletContext().getRealPath("property");
    	String realPath = realFolder + "/commandURI.properties";
    	
    	FileInputStream fis = null;
    	Properties properties = new Properties();
    	try {
			fis = new FileInputStream(realPath);
		    properties.load(fis);
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis !=null) fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	Iterator<?> iterator = properties.keySet().iterator();
        while(iterator.hasNext()) {
        	String command = (String) iterator.next();
        	String className = properties.getProperty(command);
        	
        	try {
				Class<?> commandClass = Class.forName(className);
			    Object instance = commandClass.newInstance();
        	    // map에 저장 (요청정보, 모델 객체)
			    map.put(command, instance);
        	} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
        	
        }
        // map 객체에 저장된 내용 확인
        System.out.println(map);
    
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
	}
	
	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 요청 정보 확인
		// http://localhost:8080/18-mvc/exam4/message.do
		// request.getRequestURI() : /18-mvc/exam4/message.do
		String commandURI = request.getRequestURI();
		String contextPath = request.getContextPath();		
		int contextPathLength = request.getContextPath().length();
		System.out.println(" commandURI = " + commandURI);
		System.out.println(" contextPath = " + contextPath);
		System.out.println(" contextPathLength = " + contextPathLength);
		// http://localhost:8080/18-mvc/exam4/message.do 에서
		// 요청정보만 추출 => /exam4/message.do
	    String command = commandURI.substring(contextPathLength);
	    System.out.println(" command = " + command);
	    
		//2. 모델 객체 선택
		Action action = (Action) map.get(command);
		// 3. 데이터 처리 + view 처리 파일 선택
		String view = null;
		if(action != null) {
			try {
				view  = action.process(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		// 4. view 처리 파일로 이동
		if(view != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(view);
			dispatcher.forward(request, response);
			
		}
		
	}

}
