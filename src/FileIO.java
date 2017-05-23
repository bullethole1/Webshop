import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FileIO {


	public String makeTable() throws IOException {
		//itemsAll is all the table
		BufferedReader itemsAll = new BufferedReader(
				new InputStreamReader(new FileInputStream("ShopTemplates/items.csv"), "UTF-8"));
		StringBuilder html=new StringBuilder();
		//sets if we go to right or left cell in the loop, "a" for left, "b" for right
		String posFlag = "a";

		while(true) {
			//gets one line for each iteration
			String itemSingle = itemsAll.readLine();

			if(itemSingle == null) break;

			if(itemSingle.equals("")) continue;
			//for each loop split on semicolon, parts becomes an array named "parts" containing one field per item
			String[] parts = itemSingle.split(";");

			String idImp =parts[0];
			String titleImp =parts[1];
			String picUrlImp = parts[2];
			String priceImp = parts[3];
			String descImp = parts[4];

			//building the Query String, encoding each field separately to keep parameter characters
			String idTmp = "id="+ URLEncoder.encode(idImp, "UTF-8");
			String titleTmp = "&title=" +  URLEncoder.encode(titleImp, "UTF-8");
			String picUrlTmp = "&picurl=" + URLEncoder.encode(picUrlImp, "UTF-8");
			String priceTmp = "&price=" + URLEncoder.encode(priceImp, "UTF-8");
			String descTmp = "&desc=" +  URLEncoder.encode(descImp, "UTF-8");
			String urlTmp = idTmp + titleTmp + picUrlTmp + priceTmp + descTmp;

			//Adding the querystring to the url
			String urlImp = "detailPage.html?" + urlTmp;

			if (posFlag.contains("a")){
				html.append("<div class=\"leftbox\">");
				posFlag="b";
			}
			else {
				html.append("<div class=\"rightbox\">");
			}

			html.append("<h3>" + titleImp + "</h3>");
			html.append("<img src=\"/ShopTemplates/"+ picUrlImp + "\" width=\"90\" height=\"110\" alt=\"photo 1\" class=\"left\" />");
			html.append("<p><b>Price: SEK</b> <b>" + priceImp + "</b>");
			html.append("<p><b>Description: </b>" + descImp + "</p>");
			html.append("<p><b>Availability:</b> Usually ships within 24 hours</p>");

			html.append("<p class=\"readmore\"><a href=" + urlImp + "><b>Details</b></a></p>");//
			html.append("<div class=\"clear\"></div>");
			html.append("</div>");
		}
		html.append(" <div class=\"clear br\"></div>");
		itemsAll.close();
		return html.toString();

	}

	public String makeViewCartTable(HttpServletRequest req) throws IOException {

		HttpSession session = req.getSession(true);

		StringBuilder html=new StringBuilder();

		//getting the ordered items from the session
		String getOrderedItems = (String) session.getAttribute("orderedItems");
		ArrayList<String> myList = new ArrayList<String>();

		if(getOrderedItems == null){
			getOrderedItems = "No;items;in;your;cart";				
		}

		StringTokenizer stLine = new StringTokenizer(getOrderedItems, "\n"); 
		
		//loop the StringTokenizer object while there are more tokens
		while(stLine.hasMoreTokens()) { 
			//Creating an object to put in our ArrayList
			String a = new String(stLine.nextToken());
			//Adding to the ArrayList
			myList.add(a);
		} 
		
		html.append("<p><table border=\"1\" bordercolor=\"#E8E6E6\" style=\"background-color: #FFFFFF\" align=\"left\"><tr>");
		html.append("<th width=20%>Item</th>");
		html.append("<th width=>Title</th>");
		html.append("<th>Price</th>");
		html.append("<th>Qntty</th>");
		html.append("<th>Total</th></tr>");
		html.append("");

		for (String s : myList){

			System.out.println( s);
			String[] parts = s.split(";");

			String idTmp  =parts[0];
			String titleTmp = parts[1];
			String priceTmp = parts[2];
			String quantityTmp = parts[3];
			String priceTotalTmp = parts[4];

			html.append("<tr><td>" +idTmp+ "</td>");
			html.append("<td>" +titleTmp+ "</td>");
			html.append("<td>" + priceTmp + "</td>");
			html.append("<td>" +quantityTmp+ "</td>");
			html.append("<td>" +priceTotalTmp+ " SEK" + "</td>");
			html.append("</tr>");	
		}         
		html.append("</table>");
		html.append("<p></p>");
		html.append(" <div class=\"clear br\"></div>");

		return html.toString();

	}
}