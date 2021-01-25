package cat.itacademy.proyectoerp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.proyectoerp.dao.UserDao;
import cat.itacademy.proyectoerp.domain.*;
import cat.itacademy.proyectoerp.dto.UserDTO;
/**
 * Class Service for User entity
 * @author Rubén Rodríguez
 *
 */
@Transactional
@Service
public class UserService implements IUserService{
	
	@Autowired
	UserDao userDao;
	
	//We use ModelMapper for map User entity with the DTO.
	ModelMapper modelMapper = new ModelMapper();

	/**
	 * Method for search a entity by username.
	 * 
	 * @param username username to Search
	 * @return Optional<UserDTO>  a userDTO object
	 */
	public Optional<UserDTO> getByUsername(String username) {
		UserDTO userDTO = new UserDTO();

		//Verify if user Exist.
		User user = userDao.findByUsername(username);
		if (user == null) {
			userDTO.setSuccess("Failed");
			userDTO.setMessage("User don't exist");
			return Optional.of(userDTO);
		}
		
		userDTO =modelMapper.map(user,  UserDTO.class);
		userDTO.setMessage("User found");
		userDTO.setSuccess("Success");
		return Optional.of(userDTO);
		
	}
	
	/**
	 * Method for create a new user. If user 
	 * @param userDto  
	 * @return UserDTO
	 * 
	 */
	@Override
	public UserDTO registerNewUserAccount(UserDTO userDto) {
	    User user;
	    if (userDao.existsByUsername(userDto.getUsername())) {  
	        userDto.setSuccess("False");
	    	userDto.setMessage("User Exist");
	    	return userDto;
	    }
		user =modelMapper.map(userDto,  User.class);
		userDao.save(user);
	    userDto.setSuccess("True");
	    userDto.setMessage("User created");
	    return userDto;
	     
	    }
	 
	/**
	 * Method for list all users.
	 *@return List of all users. 
	 */
	@Override 
	public List<UserDTO> listAllUsers() {
		 
		List<UserDTO> listaUsers = new ArrayList<UserDTO>();
		 
		for (User user: userDao.findAll() ) {
			 listaUsers.add(modelMapper.map(user,  UserDTO.class));
			
		}
		 
		return listaUsers;
	 }

	/**
	 * Method for list all Employee users.
	 * @return list of all Employee users.
	 * 
	 */
	@Override
	public List<UserDTO> listAllEmployees() {
		 List<UserDTO> listaUsers = new ArrayList<UserDTO>();
		 
		 for (User user: userDao.findByUserType(UserType.EMPLOYEE) ) {
			 listaUsers.add(modelMapper.map(user,  UserDTO.class));
			
		}
		 
		 return listaUsers;
	}

	/**
	 * Method for list all Client users.
	 * @return list of all Cloient users.
	 * 
	 */
	@Override
	public List<UserDTO> listAllClients() {
		 List<UserDTO> listaUsers = new ArrayList<UserDTO>();
		 
		 for (User user: userDao.findByUserType(UserType.CLIENT) ) {
			 listaUsers.add(modelMapper.map(user,  UserDTO.class));
			
		}
		 
		 return listaUsers;
	}
	
	/**
	 * Method for delete user by id.
	 * @param id   id of user to delete
	 */
	@Override
	@Transactional
	public Optional<UserDTO> deleteUserById(Long id) {
		UserDTO userDto = new UserDTO();
		//Verify if id exist
		if (!userDao.existsById(id)) {
			 userDto = new UserDTO();
	    	 userDto.setSuccess("False");
	    	 userDto.setMessage("User Don't Exist");
	    	 return Optional.of(userDto);
	     }
	     
	     userDao.deleteById(id);
	     
	     userDto.setSuccess("True");
	     userDto.setMessage("User Deleted");
	     return Optional.of(userDto);
	}
	
	/**
	 * Method for modify data of User.
	 * 
	 * @param id   id of user to modify.
	 * @param userDto  user data to modify.
	 */
	@Transactional
	@Override
	public Optional<UserDTO> modifyUser(Long id, UserDTO userDto) {
	    
		//Verify if user id exist
		if (!userDao.existsById(id)) {  
	    	 userDto.setSuccess("False");
	    	 userDto.setMessage("User Don't Exist");
	    	 return Optional.of(userDto);
	     }
		//Verify if username already exist
		if (userDao.findByUsername(userDto.getUsername()) != null) {
			userDto.setSuccess("Failed");
			userDto.setMessage("User already exist");
			return Optional.of(userDto);
		}
	    
		
		User user = userDao.findById(id).get();
		
		/**
		 * We Verified what properties has received.
		 */
		if (userDto.getUsername() != null)
			user.setUsername(userDto.getUsername());
		if (userDto.getPassword() != null)
			user.setPassword(userDto.getPassword());
		if (userDto.getUser_type() != null)
			user.setUserType(userDto.getUser_type());
		
        userDao.save(user);
	    userDto.setSuccess("True");
	    userDto.setMessage("User modified");
	    return Optional.of(userDto);
	     
	    }
	
				   
}
