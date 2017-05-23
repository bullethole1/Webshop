
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

@SuppressWarnings("serial")
public class StoreFront extends HttpServlet { 

	private Template template;

	@Override
	public void init() throws ServletException {
		Velocity.init();

		try {
			// Load the template from disk; relative file reference
			this.template = Velocity.getTemplate("ShopTemplates/index.html");

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}
	// The doGet() runs once per HTTP GET request to this servlet.
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("text/html; charset=UTF-8");
		res.setStatus(HttpServletResponse.SC_OK);

		HttpSession session = req.getSession(true);
		//Taking care if the IO, outputs and calc for ViewCart & CheckOut
		FileIO getTable1 = new FileIO();
		String itemTable1 = getTable1.makeTable();

		// This is sort of a hashmap on steroids:
		VelocityContext context = new VelocityContext();

		context.put("table1", itemTable1);
		// Tell the template to fill itself out and output itself to the browser
		this.template.merge(context, res.getWriter());

	}
/***
 * 
 * @param args
 * @throws Exception
 */
	public static void main(String[] args) throws Exception{

		ServletContextHandler html, servlets;

		servlets = new ServletContextHandler(ServletContextHandler.SESSIONS);

		servlets.addServlet(StoreFront.class, "/");
		servlets.addServlet(DetailPage.class, "/detailPage.html");
		servlets.addServlet(ViewCart.class, "/viewCart.html");
		servlets.addServlet(AddToCart.class, "/addToCart.html");
		servlets.addServlet(CheckOut.class, "/checkOut.html");

		html = new ServletContextHandler(ServletContextHandler.SESSIONS);
		ServletHolder holder = html.addServlet(DefaultServlet.class, "/ShopTemplates/*");
		holder.setInitParameter("resourceBase", "./");
		//Borrowed this solution from A.Q
		ContextHandlerCollection collection = new ContextHandlerCollection();
		// First, try the static stuff
		collection.addHandler(html);
		// Then ask the greedy servlet
		collection.addHandler(servlets);

		Server server = new Server(8080);
		server.setHandler(collection);
		server.start();
		server.join();
	}
}
