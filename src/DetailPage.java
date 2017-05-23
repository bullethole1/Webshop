import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


@SuppressWarnings("serial")
public class DetailPage extends HttpServlet{

	private Template tempDetail;
	String serverURLPath = "http://localhost:8080/";

	@Override
	public void init() throws ServletException {
		Velocity.init();

		try {
			// Load the template from disk; relative file reference
			this.tempDetail = Velocity.getTemplate("ShopTemplates/detailPage.html");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	

	// The doGet() runs once per HTTP GET request to this servlet.
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("text/html; charset=UTF-8");
		res.setStatus(HttpServletResponse.SC_OK);

		//Setting up a session by telling the server that its (true) that the session will be keeping track of what I would like it to.
		HttpSession session = req.getSession(true);

		VelocityContext context = new VelocityContext();

		String queryImp=req.getQueryString(); 
		queryImp = URLDecoder.decode(queryImp, "UTF-8");

		String idTmp = req.getParameter("id");
		String titleTmp = req.getParameter("title");
		String picUrlTmp = URLDecoder.decode(req.getParameter("picurl"), "UTF-8");//Because of "/" in the url
		String priceTmp = req.getParameter("price");
		String descTmp = req.getParameter("desc");
		//Writing to session for storage
		session.setAttribute("idAtt", idTmp);
		session.setAttribute("titleAtt", titleTmp);
		session.setAttribute("priceAtt", priceTmp);

		context.put("id", idTmp);
		context.put("title", titleTmp);
		context.put("picurl", picUrlTmp);
		context.put("price", priceTmp);
		context.put("desc", descTmp);
		context.put("viewCart", "viewCart.html");
		context.put("addToCart", "addToCart.html");
		System.out.println("Trying to use this in an ifStatement: " + session.getLastAccessedTime());
		session.setAttribute("item", queryImp);
		System.out.println("This is an object in our session: " + session.getAttribute("item"));

		this.tempDetail.merge(context, res.getWriter());


	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		doGet(req, res);
	}

}
