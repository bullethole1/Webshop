
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

@SuppressWarnings("serial")
public class CheckOut extends HttpServlet {

	private Template tempDetail;

	@Override
	public void init() throws ServletException {
		Velocity.init();

		try {
			this.tempDetail = Velocity.getTemplate("ShopTemplates/CheckOut.html");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("text/html; charset=UTF-8");
		res.setStatus(HttpServletResponse.SC_OK);

		HttpSession session = req.getSession(true);
		String sessionId = session.getId();
		System.out.println(sessionId);

		// This is sort of a hashmap on steroids:
		VelocityContext context = new VelocityContext();

		if(req.getParameter("first_name").isEmpty()||req.getParameter("last_name").isEmpty()
				||req.getParameter("address1").isEmpty()||req.getParameter("city").isEmpty()
				||req.getParameter("zip").isEmpty()||!req.getParameter("email").contains("@")){
			session.setAttribute("wrongInput", "<img src=\"/ShopTemplates/images/Alert-Icon-.png\" " +
					"width=\"100\"height=\"100\" alt=\"photo 1\" /><br><b>Please fill out form correctly</b>");
			//A way to redirect and telling the customer that he needs to fill out the form correctly
			//TODO
			//needs some work!

			res.sendRedirect(res.encodeRedirectURL("/viewCart.html"));
		}
		/*if(!req.getParameter("email").contains("@")){
			session.setAttribute("wrongInput", "<img src=\"/ShopTemplates/images/Alert-Icon-.png\" " +
					"width=\"100\"height=\"100\" alt=\"photo 1\" /><br><b>Please fill out form correctly</b>");
			res.sendRedirect(res.encodeRedirectURL("/viewCart.html"));
		}
		 */

		else{

			FileIO getTable1 = new FileIO();
			String itemTable1 = getTable1.makeViewCartTable(req);

			context.put("allTotal", session.getAttribute("allTotalPrice")+" SEK");

			if(session.getAttribute("allTotalPrice")==null){
				context.put("allTotal", "Cart is empty");
			}

			context.put("table1", itemTable1);
			context.put("first_name", req.getParameter("first_name"));
			context.put("last_name", req.getParameter("last_name"));
			context.put("address1", req.getParameter("address1"));
			context.put("address2", req.getParameter("address2"));
			context.put("city", req.getParameter("city"));
			context.put("country", req.getParameter("country"));
			context.put("state", req.getParameter("state"));
			context.put("zip", req.getParameter("zip"));
			context.put("home_phone", req.getParameter("home_phone"));
			context.put("work_phone", req.getParameter("work_phone"));
			context.put("cell_phone", req.getParameter("cell_phone"));
			context.put("email", req.getParameter("email"));

			this.tempDetail.merge(context, res.getWriter());
			session.invalidate();
		}

	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		doGet(req, res);
	}
}
