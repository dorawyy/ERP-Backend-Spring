package cat.itacademy.proyectoerp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.proyectoerp.domain.Order;
import cat.itacademy.proyectoerp.domain.OrderStatus;
import cat.itacademy.proyectoerp.dto.MessageDTO;
import cat.itacademy.proyectoerp.service.OrderServiceImpl;

@RestController
@RequestMapping("/api")
public class OderFilterContoller {
	
	@Autowired
	OrderServiceImpl orderService;

	@GetMapping("/orders/client/{clientId}")
	public ResponseEntity<MessageDTO> getOrdersByClientId(@PathVariable(name = "clientId") UUID id) {
		MessageDTO output;
		try {
			List<Order> orders = orderService.findOrdersByClient(id);
			if (orders.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			output = new MessageDTO("true", "orders successfully retrieved.", orders);
			return ResponseEntity.status(HttpStatus.OK).body(output);		
		} catch (Exception e) {
			output = new MessageDTO("False", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(output);
		}
	}

	@GetMapping("/orders/employee/{employeeId}")
	public ResponseEntity<MessageDTO> getOrdersByEmployeeId(@PathVariable(name = "employeeId") UUID id) {
		MessageDTO output;
		try {
			List<Order> orders = orderService.findOrdersByEmployeeId(id);
			List<Order> ordersUnassigned = orderService.findOrdersByStatus(OrderStatus.UNASSIGNED);
			orders.addAll(ordersUnassigned);
			
			if (orders.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			output = new MessageDTO("true", "orders successfully retrieved.", orders);
			return ResponseEntity.status(HttpStatus.OK).body(output);		
		} catch (Exception e) {
			output = new MessageDTO("False", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(output);
		}
	}
}
