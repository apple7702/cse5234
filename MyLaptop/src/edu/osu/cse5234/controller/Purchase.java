package edu.osu.cse5234.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.osu.cse5234.business.view.Inventory;
import edu.osu.cse5234.model.*;
import edu.osu.cse5234.util.ServiceLocator;


@Controller
@RequestMapping("/purchase")
public class Purchase {

	@RequestMapping(method = RequestMethod.GET)
	public String viewOrderEntryForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Order order = new Order();

		Inventory in=ServiceLocator.getInventoryService().getAvailableInventory();
		order.setItems(in.getList());
		
		request.setAttribute("order", order);
		
		request.setAttribute("message", request.getSession().getAttribute("message"));

		return "OrderEntryForm";
	}


	@RequestMapping(path = "/submitItems", method = RequestMethod.POST)
	public String submitItems(@ModelAttribute("order") Order order, HttpServletRequest request) {
		boolean isValid=ServiceLocator.getOrderProcessingService().validateItemAvailability(order);
		request.getSession().setAttribute("message", "");
		if(isValid) {
			
			request.getSession().setAttribute("order", order);
			return "redirect:/purchase/paymentEntry";
			
		}else {
			request.getSession().setAttribute("message", "Please resubmit item quantities.");
			return "redirect:/purchase";
			
		}
//		request.getSession().setAttribute("order", order);
//		return "redirect:/purchase/paymentEntry";
	}

	@RequestMapping(path = "/paymentEntry", method = RequestMethod.GET)
	public String viewPaymentEntryPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Order order = (Order)request.getSession().getAttribute("order");
		PaymentInfo payment = new PaymentInfo();
//		payment.setHolderName("");
//		payment.setccNumber("");
//		payment.setcvvCode("");
//		payment.setexpDate("");
		request.setAttribute("payment", payment);
		
		return "PaymentEntryForm";
	}

	@RequestMapping(path = "/submitPayment", method = RequestMethod.POST)
	public String submitPayment(@ModelAttribute("payment") PaymentInfo payment, HttpServletRequest request) {
		request.getSession().setAttribute("payment", payment);
		return "redirect:/purchase/shippingEntry";
	}

	@RequestMapping(path = "/shippingEntry", method = RequestMethod.GET)
	public String viewShippingEntryPage(HttpServletRequest request, HttpServletResponse response) {
		ShippingInfo shipping = new ShippingInfo();

//		shipping.setName("");
//		shipping.setAddLine1("");
//		shipping.setAddLine2("");
//		shipping.setCity("");
//		shipping.setState("");
//		shipping.setZip("");
		request.setAttribute("shipping", shipping);
		return "ShippingEntryForm";
	}
	
	@RequestMapping(path = "/submitShipping", method = RequestMethod.POST)
	public String submitShipping(@ModelAttribute("shippingInfo") ShippingInfo shippingInfo,
			HttpServletRequest request) {
		request.getSession().setAttribute("shippingInfo", shippingInfo);
		return "redirect:/purchase/viewOrder";
	}

	@RequestMapping(path = "/viewOrder", method = RequestMethod.GET)
	public String viewOrder(HttpServletRequest request, HttpServletResponse response) {
		Order order = (Order)request.getSession().getAttribute("order");
		request.setAttribute("vieworder", order);
		return "ViewOrder";
	}

	@RequestMapping(path = "/confirmOrder", method = RequestMethod.POST)
	public String confirmOrder(@ModelAttribute("order") Order order, HttpServletRequest request) {
		String confirmCode=ServiceLocator.getOrderProcessingService().processOrder(order);
		request.getSession().setAttribute("confirmCode", confirmCode);
		return "redirect:/purchase/viewConfirmation";
	}

	@RequestMapping(path = "/viewConfirmation", method = RequestMethod.GET)
	public String viewConfirmation(HttpServletRequest request, HttpServletResponse response) {
		// display order detail.
		request.setAttribute("confirmedorder", request.getSession().getAttribute("order"));
		request.setAttribute("payment", request.getSession().getAttribute("payment"));	
		request.setAttribute("shipping", request.getSession().getAttribute("shippingInfo"));	
		request.setAttribute("confirmCode", request.getSession().getAttribute("confirmCode"));
		return "Confirmation";
	}
}
