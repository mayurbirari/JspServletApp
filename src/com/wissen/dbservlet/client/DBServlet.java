/**
 * @author wissen16(Mayur Biari)
 * Database Application in Servlet 
 */
package com.wissen.dbservlet.client;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.io.*;

@SuppressWarnings("serial")
public class DBServlet extends HttpServlet {

	String username = "root";
	String password = "wissen";
	Connection con;
	ResultSet rs;
	PreparedStatement stmt;
	Statement stmt1;
	String url = "jdbc:mysql://localhost:3306/google";
	PrintWriter pw1;

	/**
	 * this method to initialise all database connection classes 
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			// stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Sql Error" + e.getMessage());
		}
	}

	/**
	 * to handle post request for this page, like for insert ,update and delete
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		pw1 = resp.getWriter();
		resp.setContentType("text/html");
		if(con==null){pw1.write("connection problem");}
		try {
			// stmt = con.createStatement();
			if(req.getParameter("hidden").equals("insert")){
				String sql = "insert into account values(";
				sql += "'" + req.getParameter("fname") + "',";
				sql += "'" + req.getParameter("lname") + "',";
				sql += "'" + req.getParameter("lid") + "',";
				sql += "'" + req.getParameter("pwd") + "',";
				sql += "'" + req.getParameter("selq") + "',";
				sql += "'" + req.getParameter("ans") + "',";
				sql += "'" + req.getParameter("sece") + "',";
				sql += "'" + req.getParameter("country") + "')";
				stmt = con.prepareStatement(sql);
				stmt.executeUpdate();
				pw1.write("Account Created SuccessFully<br><b>");
				pw1.write(req.getParameter("fname"));
				}
			
				else if(req.getParameter("hidden").equals("update")){
					String sql = "update account set fname='";
					sql += req.getParameter("fname") +"',lname='";
					sql += req.getParameter("lname")+"',logname='";
					sql += req.getParameter("lid")+"',pwd='";
					sql += req.getParameter("pwd")+"' where fname='";
					sql += req.getParameter("uname")+"'";
					stmt = con.prepareStatement(sql);
					stmt.executeUpdate();
					pw1.write("Account Updated SuccessFully<br><b>");
					pw1.write(req.getParameter("fname"));
					}
			
				else if(req.getParameter("hidden").equals("delete")){
					String sql = "delete from account where fname='";
					sql += req.getParameter("dname")+"'";
					stmt = con.prepareStatement(sql);
					stmt.executeUpdate();
					pw1.write("Account deleted SuccessFully<br><b>");
					pw1.write(req.getParameter("dname"));
					}
			//imp -http://projects.allmarkedup.com/jquery_url_parser/
			//imp -resp.sendRedirect("Editgmail.html?fname="+req.getParameter("fname")+"&lname="+req.getParameter("lname")+"&logn="+req.getParameter("lid")+"&pwd="+req.getParameter("pwd"));
		} catch (Exception e) {
			pw1.write(e.getMessage());
		}
	}

	/**
	 * to handle get request for this servlet like to get sign in a user 
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		pw1 = resp.getWriter();
		resp.setContentType("text/html");
		try {
			stmt1 = con.createStatement();
			rs = stmt1.executeQuery("select * from account");
			int ok = 0;
			pw1.write("loginId: " + req.getParameter("lid"));
			pw1.write("password: " + req.getParameter("pwd"));
			String fname="";
			String lname="";
			String logname="";
			String pwd="";
			while (rs.next()) {
				 fname=String.valueOf(rs.getString(1));
				 lname=String.valueOf(rs.getString(2));
				 logname = String.valueOf(rs.getString(3));
				  pwd = String.valueOf(rs.getString(4));			
				 if(logname.equals(req.getParameter("lid"))) {
					if (pwd.equals(req.getParameter("pwd"))) {
						ok = 1;
						break;
					} else {
						ok = -1;break;
					}
				}
			}
			if (ok == 1) {
				pw1.write("<b>Login SuccessFully<br>");
				pw1.write(req.getParameter("lid"));
				
	            req.setAttribute("fname",fname);
	            req.setAttribute("lname",lname);
	            req.setAttribute("logn",logname);
	            req.setAttribute("pwd",pwd);
	          
	            RequestDispatcher requestdispather = req.getRequestDispatcher("Editgmail.jsp");
	            requestdispather.forward(req, resp);
				//resp.sendRedirect("Editgmail.html?fname="+fname+"&lname="+lname+"&logn="+req.getParameter("lid")+"&pwd="+req.getParameter("pwd"));
			} else if (ok == -1) {
				pw1.write("<br><b>Please Enter valid Passworequestdispather<br><b>");		
			} else {
				pw1.write("<br><b>Please Enter valid Login name<br><b>");				
			}
			rs.close();
		} catch (Exception e) {
			pw1.write(e.getMessage());
		}
	}
}