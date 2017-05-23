
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
public class AddToCart extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType("text/html; charset=UTF-8");
		res.setStatus(HttpServletResponse.SC_OK);

		HttpSession session = req.getSession(true);

		VelocityContext context = new VelocityContext(); 

		//get the quantity field from the html form
		String a= req.getParameter("quantity");
		int i;
		//check if empty or not a number
		if(a != null){
			i = Integer.parseInt(a);
			//write the quantity to the session
			session.setAttribute("quantity", i);
		}

		//get the data of the items from the session
		String idTmp = (String) session.getAttribute("idAtt");
		String titleTmp = (String) session.getAttribute("titleAtt");
		String priceTmp = (String) session.getAttribute("priceAtt");
		int quantityTmp = (int) session.getAttribute("quantity");

		int itemPrice = quantityTmp * Integer.valueOf(priceTmp);

		//set the total price of all in totalPrice
		String allTotalPriceTmp = (String) session.getAttribute("allTotalPrice");

		//if attribute is empty, null is returned. change to 0
		if (allTotalPriceTmp==null){
			allTotalPriceTmp=("0");
		}

		int addUpItems = Integer.parseInt(allTotalPriceTmp) + itemPrice;
		String addUpItemsText = ""+addUpItems;


		if (allTotalPriceTmp == "0"){
			session.setAttribute("allTotalPrice",""+itemPrice);
		}
		else {
			session.setAttribute("allTotalPrice",addUpItemsText);
		}


		//put what is ordered into a attribute called orderedItems.
		String buildorderedItemsTmp = (String) session.getAttribute("orderedItems");
		String buildorderedItems =
				(idTmp + ";" + titleTmp  + ";" + priceTmp   + ";" + String.valueOf(quantityTmp)  + ";" + String.valueOf(itemPrice) + "\n");

		if (buildorderedItemsTmp == null){
			session.setAttribute("orderedItems",buildorderedItems);	
		}
		else {
			session.setAttribute("orderedItems", buildorderedItemsTmp + buildorderedItems);
		}

		//load ViewCart
		ViewCart vc = new ViewCart();
		vc.init();
		vc.doPost(req, res);


	}
}