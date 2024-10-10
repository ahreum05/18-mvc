package exam3.mvc;

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


@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 요청정보와 데이터 처리 클래스를 쌍으로 저장할 Map 클래스
	// Object 
    private Map<String, Object> map = new HashMap<String, Object>();
    
    public Controller() {
        super();
        
    }
    // init() : 서블릿이 구동될 때, 제일 먼저 호출되는 함수
    // => 서블릿 설정을 주로 함
    
	// 서블릿이 구동될 때, 제일 먼저 호출되는 함수
    // => 서블릿 설정을 주로 함
    @Override
    public void init(ServletConfig config) throws ServletException {
    	// properties 파일의 경로 구하기
    	String realFolder = config.getServletContext().getRealPath("property");
    	String realPath = realFolder + "/" + "command.properties";
        System.out.println("realPath = " + realPath);
        
        // command.properties 파일 읽기
        FileInputStream fis = null;
        Properties properties = new Properties();
        try {
			fis = new FileInputStream(realPath);
			// 파일을 읽어옴
			properties.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        // command.properties 파일의 내용대로 객체 생성해서 Map에 저장하기 
        
        // properties.keySet().iterator() : 
        // => command.properties 파일의 = 문자 오른쪽 키값만 읽어옴
        // command와 해당 객체를 map 객체에 저장함
        Iterator<?> iterator = properties.keySet().iterator();
        
        while(iterator.hasNext()) {
        	// 키값 1개 읽어오기 ("=" 기준 왼쪽값)
        	String command = (String) iterator.next();
        	// 키값에 해당하는 클래스명 읽어오기 ("=" 기준 오른쪽값)
        	String className = properties.getProperty(command);
        	System.out.println("command = " + command);
        	System.out.println("className = " + className);
        	
        	// 클래스명으로 객체 생성
        	try {
				Class<?> commandClass = Class.forName(className);
			    // 객체 생성
				Object commandInstance = commandClass.newInstance();
				// Map 객체에 저장 (요청 정보, 모델 객체)
				map.put(command, commandInstance);
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
		String commmand = request.getParameter("command");
		// 2. Model 선택 (= 데이터 처리 클래스 선택)
		Action action = (Action)map.get(commmand);
		// 3. 데이터 처리 + view 처리 파일 저장
		String view = null;
		if(action != null) {
			try {
				view = action.process(request, response);
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
