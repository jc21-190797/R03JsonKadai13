import java.io.IOException;
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

//import tool.Page;


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

		//RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/getPoint.jsp");
		//rd.forward(request, response);

		final String driverName = "com.mysql.jdbc.Driver";
		final String url = "mysql:thin:@192.168.54.190:3306/jsonkadai13";
		final String id = "jsonkadi13";
		final String pass = "JsonKadai13";

		try {

			Class.forName(driverName);
			java.sql.Connection connection = DriverManager.getConnection(url, id, pass);
			//InitialContext ic = new InitialContext();
			//DataSource ds = (DataSource) ic.lookup("java:/comp/env/mysql/book");
			//Connection con = ds.getConnection();

			PreparedStatement st = connection.prepareStatement("select point from point_table where shopcode=? AND userid=?");
			String  mise = request.getParameter("shopcode");
			String  namae = request.getParameter("userid");
			st.setString(1, mise);
			st.setString(2, namae);
			ResultSet result = st.executeQuery();

			List<String[]> list = new ArrayList<>();
			
			if (result.next() == true) {
				String[] s = new String[1];
				s[0] = result.getString("point");
				list.add(s);
				
			}else {
				PreparedStatement st2 = connection
						.prepareStatement("insert into point (shopcode,userid,point_table) value(?,?,500)");
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
						s[0] = result.getString("point");
						list.add(s);
					}
				}else {
					System.out.println("新規追加失敗");
				}
			}
			// String list =request.getParameter("list");
			request.setAttribute("list", list);
			RequestDispatcher rd =request.getRequestDispatcher("/WEB-INF/JSP/getPoint.jsp");
			rd.forward(request, response);
		}
		catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		 catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}


