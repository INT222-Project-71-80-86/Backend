package int222.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("/get/id/{oid}")
	public Orders getOrderById(@PathVariable int oid, @RequestAttribute String username) {
		return orderService.getOrderById(oid);
	}
	
	@GetMapping("/all")
	public Page<Orders> getOrderPaging(@RequestParam(defaultValue = "0") int pageNo, 
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "oid") String sortBy){
		return orderService.getAllOrders(pageNo, size, sortBy);
	}
	
	@GetMapping("/get/username")
	public Page<Orders> getOrderByUsername( @RequestAttribute String username, @RequestParam(defaultValue = "0") int pageNo, 
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "oid") String sortBy){
		return orderService.getOrdersByUsername(username, pageNo, size, sortBy);
	}
	
	@PostMapping("/save")
	public Orders addOrders(@RequestBody Orders order, @RequestAttribute String username) {
		return orderService.addOrder(order, username);
	}
	
	@DeleteMapping("/cancel/{oid}")
	public Orders calcelOrder(@PathVariable int oid, @RequestAttribute String username) {
		return orderService.cancelOrder(oid, username);
	}

}
