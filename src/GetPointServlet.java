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




@WebServlet("/getPoint")
public class GetPointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetPointServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		

		final String driverName = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://192.168.54.190:3306/jsonkadai13";
		final String id = "jsonkadai13";
		final String pass = "JsonKadai13";

		try {

			Class.forName(driverName);
			Connection connection = DriverManager.getConnection(url, id, pass);
			
			PreparedStatement st = connection.prepareStatement("select POINT from point where TENPO_ID=? AND USER_ID=?");
			String  mise = request.getParameter("TENPO_ID");
			String  namae = request.getParameter("USER_ID");
			st.setString(1, mise);
			st.setString(2, namae);
			ResultSet result = st.executeQuery();

			List<String[]> list = new ArrayList<>();
			
			if (result.next() == true) {
				String[] s = new String[1];
				s[0] = result.getString("POINT");
				list.add(s);
				
			}else {
				PreparedStatement st2 = connection
						.prepareStatement("insert into point (TENPO_ID,USER_ID,POINT) value(?,?,500)");
				st2.setString(1,mise);
				st2.setString(2,namae);
				
				int x = st2.executeUpdate();
				
				if(x == 1) {
					System.out.println("新規追加成功");
					st.setString(1, mise);
					st.setString(2, namae);
					result = st.executeQuery();
					if (result.next() == true) {
						String[]s = new String[1];
						s[0] = result.getString("POINT");
						list.add(s);
					}
				}else {
					System.out.println("新規追加失敗");
				}
			}
			
			request.setAttribute("list", list);
			RequestDispatcher rd =request.getRequestDispatcher("/WEB-INF/jsp/getPoint.jsp");
			rd.forward(request, response);
		}
		catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace(response.getWriter());
		}
		 catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace(response.getWriter());
		}
	}
}
//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

