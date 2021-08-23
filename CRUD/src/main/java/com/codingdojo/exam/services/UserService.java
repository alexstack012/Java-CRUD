package com.codingdojo.exam.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.exam.models.LoginUser;
import com.codingdojo.exam.models.User;
import com.codingdojo.exam.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepo;
    
	public User findOneUser(Long id) {
		Optional<User> user = userRepo.findById(id);
		if(user.isPresent()) {
		return user.get();
		} else {
			return null;
		}
	}
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
//Registration method
public User register(User newUser, BindingResult result) {
	//checks to see if an e-mail is already in the DB
	if(userRepo.findByEmail(newUser.getEmail()).isPresent()) {
		result.rejectValue("email", "Unique", "this email is already in use");
	}
	//checks to see if the password matches the confirm password
	if(!newUser.getPassword().equals(newUser.getConfirm())) {
		result.rejectValue("confirm", "matches", "the confirmation password must match password");
	}
	if(result.hasErrors()) {
		return null;
	}else {
		//hashes and salts PW saves it then saves new user
        String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashed);
        return userRepo.save(newUser);
	}
}


		//Login Method
public User login(LoginUser newLogin, BindingResult result) {
	if(result.hasErrors()) {
		return null;
	}
	Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
	//if the email exists in the DB, checks to see if the passwords match
	User user = potentialUser.get();
	if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
		result.rejectValue("password", "matches","invalid password");
	}
	if(result.hasErrors()) {
		return null;
	}
	else {
		return user;
	}
}
}
