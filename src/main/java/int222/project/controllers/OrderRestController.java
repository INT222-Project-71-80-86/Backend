package int222.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Orders;
import int222.project.services.OrderService;

@RequestMapping(path = "/api/order")
@RestController
public class OrderRestController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/get/{oid}")
	public Orders getOrderById(@PathVariable int oid) {
		return orderService.getOrderById(oid);
	}
	
	@GetMapping("/all")
	public Page<Orders> getOrderPaging(@RequestParam(defaultValue = "0") int pageNo, 
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "oid") String sortBy){
		return orderService.getAllOrders(pageNo, size, sortBy);
	}
	
	

}
