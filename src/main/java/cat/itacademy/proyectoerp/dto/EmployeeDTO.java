package cat.itacademy.proyectoerp.dto;

import java.io.Serializable;
import java.util.UUID;

public class EmployeeDTO implements Serializable{
	private UUID employee_id;
	private Double total;
	
	public UUID getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(UUID employee_id) {
		this.employee_id = employee_id;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
}
