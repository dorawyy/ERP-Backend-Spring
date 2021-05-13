package cat.itacademy.proyectoerp.service;

import java.util.List;
import java.util.UUID;

import cat.itacademy.proyectoerp.domain.Client;
import cat.itacademy.proyectoerp.dto.ClientDTO;

public interface IClientService {
	
	public ClientDTO createClient(Client client) throws Exception; //CREATE - Adds new client to the Database
	
	public ClientDTO createFastClient(ClientDTO client) throws Exception; // CREATE - Adds a new client without needing an username. Only for admin purposes.
	
	public List<Client> getAllClients(); // READ - full list of all clients
	
	public List<ClientDTO> getPageOfClients(int page, int amount); // READ - Get a page of X clients
	
	public Client findClientById(UUID id); //READ - finds the client by Id
		
	public ClientDTO updateClient(Client client) throws Exception; //UPDATE - Updates clients info
	
	public void deleteClient(UUID id); //DELETE - deletes client

	List<ClientDTO> listAllUsers();

	public boolean existsByDni(String dni);

	public boolean existsByUsername(String username);

}
