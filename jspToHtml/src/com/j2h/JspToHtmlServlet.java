package com.j2h;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class JspToHtmlServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("*******************in************");

		ServletContext sc = getServletContext();// 为你的应用的上下文路径。
		// String fileName = req.getParameter("fileName");// 你要访问的jsp
		// 则你访问这个servlet时加参数.如http://localhost/test/toHtml?fileName=index
		String url = "/jsp/j2h.jsp";// 你要生成的页面的文件名。扩展名为jsp
		String name = sc.getRealPath("/") + "j2h.html";// 这是生成的html文件名
		System.out.println("out file: " + name);
		RequestDispatcher rd = sc.getRequestDispatcher(url);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		final ServletOutputStream stream = new ServletOutputStream() {
			public void write(byte[] data, int offset, int length) {
				os.write(data, offset, length);
			}

			public void write(int b) throws IOException {
				os.write(b);
			}
		};

		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));

		HttpServletResponse reponse = new HttpServletResponseWrapper(resp) {
			public ServletOutputStream getOutputStream() {
				return stream;
			}

			public PrintWriter getWriter() {
				return pw;
			}
		};

		rd.include(req, reponse);
		pw.flush();

		FileOutputStream fos = new FileOutputStream(name); // 把jsp输出的内容写到xxx.html
		os.writeTo(fos);
		fos.close();

		PrintWriter out = resp.getWriter();
		out.print("SUCCESS");
	}
}
