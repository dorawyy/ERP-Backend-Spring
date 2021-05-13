package cat.itacademy.proyectoerp;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import cat.itacademy.proyectoerp.exceptions.ArgumentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cat.itacademy.proyectoerp.domain.Address;
import cat.itacademy.proyectoerp.domain.Client;
import cat.itacademy.proyectoerp.domain.Employee;
import cat.itacademy.proyectoerp.domain.Order;
import cat.itacademy.proyectoerp.domain.OrderDetail;
import cat.itacademy.proyectoerp.domain.OrderStatus;
import cat.itacademy.proyectoerp.domain.PaymentMethod;
import cat.itacademy.proyectoerp.domain.Product;
import cat.itacademy.proyectoerp.domain.User;
import cat.itacademy.proyectoerp.domain.UserType;
import cat.itacademy.proyectoerp.repository.UserRepository;
import cat.itacademy.proyectoerp.service.AddressServiceImpl;
import cat.itacademy.proyectoerp.service.ClientServiceImpl;
import cat.itacademy.proyectoerp.service.EmployeeServiceImpl;
import cat.itacademy.proyectoerp.service.OrderDetailServiceImpl;
import cat.itacademy.proyectoerp.service.OrderServiceImpl;
import cat.itacademy.proyectoerp.service.ProductServiceImpl;
import cat.itacademy.proyectoerp.service.UserServiceImpl;

/**
 * This class establishes that when the application is started, the run method
 * creates the following users, products, clients and orders in the database.
 * 
 */

@Component
public class Runner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	@Autowired
	ProductServiceImpl productService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	ClientServiceImpl clientService;

	@Autowired
	OrderServiceImpl orderService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmployeeServiceImpl employeeService;
	
	@Autowired
	AddressServiceImpl addressService;
	
	@Autowired
	OrderDetailServiceImpl orderDetailService;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
		//checks if there is already an admin created, if not, it creates one.
		if(!userRepository.existsByUsername("admin@erp.com")){
			userRepository.save(new User("admin@erp.com", passwordEncoder.encode("ReW9a0&+TP"), UserType.ADMIN));
		}
		
		//checks if there is already an employee created, if not, it creates one
		if(!userRepository.existsByUsername("employee@erp.com")) {
			User userEmployee = new User("employee@erp.com", "ReW9a0&+TP", UserType.EMPLOYEE);
			userService.registerNewUserAccount(userEmployee);
			Employee employee = new Employee(18000.00, "C1234567Z", 667999997, LocalDate.now(),null, userEmployee);
			employeeService.createEmployee(employee);
		}

		Address address1 = null;
		Address address2 = null;
		Address address3 = null;
		Address address4 = null;
		Address addressExample = null;

		try{
			List<Address> adresses =  addressService.findAllAddresses();
		}catch(ArgumentNotFoundException ex){
			address1 = new Address("Calle Maldivas", "1 C", "Barcelona", "Spain", "08016");
			addressService.createAddress(address1);
			address2 = new Address("Calle Azores", "1 C", "Barcelona", "Spain", "08016");
			addressService.createAddress(address2);
			address3 = new Address("Calle Canarias", "1 C", "Barcelona", "Spain", "08016");
			addressService.createAddress(address3);
			address4 = new Address("Calle Bahamas", "1 C", "Barcelona", "Spain", "08016");
			addressService.createAddress(address4);

			addressExample = new Address("Calle Ejemplo", "1 A", "Barcelona", "Spain", "08018");
			addressService.createAddress(addressExample);
		}

		//checks if there is already a client created, if not, it creates one
		if(!userRepository.existsByUsername("client@erp.com")) {
			User userClient = new User("client@erp.com", "ReW9a0&+TP", UserType.CLIENT);
			userService.registerNewUserAccount(userClient);
			Client client = new Client("L1234567Z", "url image","Random Name", addressExample, userClient);
			clientService.createClient(client);
		}
		
		//X is the variable of the amount of clients do you want to have. 
        //that excludes two separate clients that are made to create two test orders.
		int x = 5;
		
		
		//Checks if there is less data than the asked about and adds the amount left.
		if(userService.listAllUsers().size() < x+1) {
			
			//Subtracts the amount of users already created
			x = x - userService.listAllUsers().size()+1;
			
			logger.info("initializing " + x+2 +" users type client, " + x+2 + " clients, 2 products and 2 orders");
			
			//Initialize the amount of users and clients marked by x
			for (int i = 0;i < x; i++) {
				Random rand = new Random();
				String random = String.valueOf(rand.nextInt(10000000)); //beware of unhandled exception!
				String dni = "L"+random+"Z";
				String mail = "userclient"+i+"@example.com";
				User userClient = new User(mail, "ReW9a0&+TP", UserType.CLIENT);
				userService.registerNewUserAccount(userClient);
				addressExample = new Address ("Otra Calle Ejemplo", "1 A", "Barcelona", "Spain", "08018");
				addressService.createAddress(addressExample);
				Client client = new Client(dni, "url image","Random Name", addressExample, userClient);
				clientService.createClient(client);
			}
						
			//Initialize two users for the clients for the orders
			User userClientOne = new User("userclientone@example.com", "ReW9a0&+TP", UserType.CLIENT);
			userService.registerNewUserAccount(userClientOne);

			User userClientTwo = new User("userclienttwo@example.com", "ReW9a0&+ET", UserType.CLIENT);
			userService.registerNewUserAccount(userClientTwo);
						
			// Initialize two Clients for the orders
			Client clientOne = new Client("L1554567Z", "url image","Random Name", address1, address2, userClientOne);
			clientService.createClient(clientOne);

			Client clientTwo = new Client("B7654321C", "url image","Random Name", address3, address3, userClientTwo);
			clientService.createClient(clientTwo);
			
			
			//Initialize two users for the employees for the orders
			User userEmployeeOne = new User("employee1@company.com", "ReW9a0&+TP", UserType.EMPLOYEE);
			userService.registerNewUserAccount(userEmployeeOne);

			User userEmployeeTwo = new User("employee2@company.com", "ReW9a0&+ET", UserType.EMPLOYEE);
			userService.registerNewUserAccount(userEmployeeTwo);
			
			
			// Initialize two Employees for the orders
			Employee employeeOne = new Employee(24000.00,"A1234567Z",667999999, LocalDate.now(),null,userEmployeeOne);
			employeeService.createEmployee(employeeOne);

			Employee employeeTwo = new Employee(14000.00,"B1234567Z",667999998, LocalDate.now(),null, userEmployeeTwo);

			employeeService.createEmployee(employeeTwo);
			

			// Initialize 3 products
			Product productOne = new Product("ejemplo 1", 100, "url image", "Bebidas", 150.00, 21.00, 100, 200);
			productService.createProduct(productOne);

			Product productTwo = new Product("ejemplo 2", 200, "url image", "Comidas", 250.00, 21.00, 175, 200);
			productService.createProduct(productTwo);
			
			Product productThree = new Product("ejemplo 3", 50, "url image", "Souvenirs", 350.00, 21.00, 250, 100);
			productService.createProduct(productThree);

			
			// Initialize 3 orders, we need to create lines of orderDetails with products
			
			//Order1
			Order orderOne = new Order(employeeOne.getId(), clientOne.getId(), LocalDateTime.now(),
					OrderStatus.COMPLETED, PaymentMethod.CREDIT_CARD, address1, address1, 550.00);
						
			OrderDetail orderDetail1 = new OrderDetail (productOne, orderOne, 2, 300.00);
			OrderDetail orderDetail2 = new OrderDetail (productTwo, orderOne, 1, 250.00);
			
			orderDetailService.createOrderDetail(orderDetail1);
			orderDetailService.createOrderDetail(orderDetail2);
			
			orderOne.addOrderDetail(orderDetail1);
			orderOne.addOrderDetail(orderDetail2);			
			orderService.createOrder(orderOne);	
			
			//Order2
			Order orderTwo = new Order(employeeOne.getId(), clientOne.getId(), LocalDateTime.now(),
					OrderStatus.IN_DELIVERY, PaymentMethod.CASH, address2, address1, 1200.00);
			
			OrderDetail orderDetail3 = new OrderDetail (productTwo, orderTwo, 2, 500.00);
			OrderDetail orderDetail4 = new OrderDetail (productThree, orderTwo, 2, 700.00);
			
			orderDetailService.createOrderDetail(orderDetail3);
			orderDetailService.createOrderDetail(orderDetail4);
			
			orderTwo.addOrderDetail(orderDetail3);
			orderTwo.addOrderDetail(orderDetail4);	
			
			orderService.createOrder(orderTwo);
			
			//Order3
			Order orderThree = new Order(employeeTwo.getId(), 
					clientTwo.getId(), LocalDateTime.now(), OrderStatus.PENDING_DELIVERY,
					PaymentMethod.PAYPAL, address3, address3, 1200.00);
			
			OrderDetail orderDetail5 = new OrderDetail (productOne, orderThree, 1, 150.00);
			OrderDetail orderDetail6 = new OrderDetail (productThree, orderThree, 3, 1050.00);
			
			orderTwo.addOrderDetail(orderDetail5);
			orderTwo.addOrderDetail(orderDetail6);	
			
			orderThree.addOrderDetail(orderDetail5);
			orderThree.addOrderDetail(orderDetail6);	
			
			orderService.createOrder(orderThree);
			 
		}

	}

}
