
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


@SuppressWarnings("serial")
public class ViewCart extends HttpServlet{

	private Template tempDetail;

	@Override
	public void init() throws ServletException {
		Velocity.init();


		try {
			this.tempDetail = Velocity.getTemplate("ShopTemplates/viewCart.html");
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

		VelocityContext context = new VelocityContext(); 

		if(session.getAttribute("wrongInput")!=null){
			context.put("wrongIn", session.getAttribute("wrongInput"));
			session.setAttribute("wrongInput", null);
		}

		FileIO getTable1 = new FileIO();
		String itemTable1 = getTable1.makeViewCartTable(req);

		context.put("table1", itemTable1);
		this.tempDetail.merge(context, res.getWriter());

		}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		doGet(req, res);

	}
}