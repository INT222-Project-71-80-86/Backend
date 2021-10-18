package int222.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Orders;
import int222.project.repositories.OrderJpaRepository;
import int222.project.repositories.OrderdetailJpaRepository;
import int222.project.repositories.ProdColorJpaRepository;
import int222.project.repositories.ProductJpaRepository;
import int222.project.repositories.UserJpaRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderJpaRepository orderRepo;
	@Autowired
	private OrderdetailJpaRepository orderdetailRepo;
	@Autowired
	private ProductJpaRepository prodRepo;
	@Autowired
	private ProdColorJpaRepository prodColorRepo;
	@Autowired
	private UserJpaRepository userRepo;
	
	public Orders getOrderById(int orderId) {
		Orders order = orderRepo.findById(orderId).orElseThrow(() -> new DataRelatedException(ERROR_CODE.ORDER_DOESNT_FOUND
				, "Order id "+orderId+" doesn't found in database."));
		return order;
	}
	
	public Page<Orders> getAllOrders(int pageNo, int size, String sortBy){
		Page<Orders> orders = orderRepo.findAll( PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return orders;
	}
	
	public Page<Orders> getOrdersByUsername(String username, int pageNo, int size, String sortBy){
		Page<Orders> orders = orderRepo.findByUsername(username, PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return orders;
	}
	
	public Orders addOrder(Orders order) {
		orderRepo.save(order);
		return null;
	}
	
	

}
