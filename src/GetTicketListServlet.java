import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetPointServlet
 */
@WebServlet("/getTicketList")
public class GetTicketListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTicketListServlet() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/getTicketList.jsp");
		rd.forward(request, response);
		
		final String driverName = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://192.168.54.190:3306/jsonkadai13";
		final String id = "jsonkadai13";
		final String pass = "JsonKadai13";

		try {

			Class.forName(driverName);
			Connection connection = DriverManager.getConnection(url, id, pass);
			
			PreparedStatement st = connection.prepareStatement("select TICKET_ID,TICKET_NAME,POINT from ticket where TENPO_ID=? AND TICKET_ID=?");
			//select���ύX����
			
			String  ticketid = request.getParameter("TICKET_ID");
			String  namae = request.getParameter("TICKET_NAME");
			String  point = request.getParameter("POINT");
			st.setString(1, ticketid);
			st.setString(2, namae);
			st.setString(3, point);
			ResultSet result = st.executeQuery();

			List<String[]> list = new ArrayList<>();
			
			if (result.next() == true) {
				String[]s = new String[3];
				s[0] = result.getString("TICKET_ID");
				s[1] = result.getString("TICKET_NAME");
				s[2] = result.getString("POINT");
				list.add(s);
				
			}else {
				PreparedStatement st2 = connection
						.prepareStatement("insert into ticket (TICKET_ID,TICKET_NAME,POINT) value(?,?,?)");
				st2.setString(1,ticketid);
				st2.setString(2,namae);
				st2.setString(3,point);
				int x = st2.executeUpdate();
				
				if(x == 1) {
					System.out.println("�V�K�ǉ�����");
					st.setString(1,ticketid);
					st.setString(2,namae);
					st.setString(3,point);
					result = st.executeQuery();
					if (result.next() == true) {
						String[]s = new String[3];
						s[0] = result.getString("TICKET_ID");
						s[1] = result.getString("TICKET_NAME");
						s[2] = result.getString("POINT");
						list.add(s);
					}
				}else {
					System.out.println("�V�K�ǉ����s");
				}
			}
			
			request.setAttribute("list", list);
			RequestDispatcher gt =request.getRequestDispatcher("/WEB-INF/jsp/getTicketList.jsp");
			gt.forward(request, response);
		}
		catch (ClassNotFoundException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace(response.getWriter());
		}
		 catch (SQLException e) {
			// TODO �����������ꂽ catch �u���b�N
			 e.printStackTrace(response.getWriter());
		}
	}
}
		
	
