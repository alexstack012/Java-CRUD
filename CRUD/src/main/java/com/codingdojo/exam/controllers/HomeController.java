package com.codingdojo.exam.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codingdojo.exam.models.Idea;
import com.codingdojo.exam.models.LoginUser;
import com.codingdojo.exam.models.User;
import com.codingdojo.exam.services.UserService;
import com.codingdojo.exam.services.IdeaService;


@Controller
public class HomeController {
	private UserService userServ;
	private IdeaService ideaServ;


	public HomeController(UserService userServ, IdeaService ideaServ) {
		this.userServ = userServ;
		this.ideaServ = ideaServ;
	}
	
	//#################################################################################################
	//########################      START OF LOGIN AND REG       ######################################
	//#################################################################################################

	@RequestMapping("/")
	public String index(HttpSession session, Model model) {
		model.addAttribute("newUser", new User()); 
		model.addAttribute("newLogin", new LoginUser());
		return "index.jsp";
	}

	@GetMapping("/dashboard")
	public String home(HttpSession session, Model model) {
		//check if "user_id" is not in session
		if(session.getAttribute("user_id") == null) {
			return "redirect:/";			
		} else {
			//This sends our User info to the JSP
			model.addAttribute("user", userServ.findOneUser((Long) session.getAttribute("user_id")));
			//This sends our "allIdeas" info to the jsp
			model.addAttribute("allIdeas", ideaServ.getAllIdeas());
			return "dashboard.jsp";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		//clears the session signing out the user then redirecting to login page
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser")User newUser, 
			BindingResult result, Model model, HttpSession session) {
		//first we call on our service method register to check if the email is in session
		userServ.register(newUser,  result);
		if(result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "index.jsp";
		}
		//setting our user_id into session
		session.setAttribute("user_id", newUser.getId());
		return "redirect:/dashboard";		
		}
	@PostMapping("/login")
	public String login (@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result,
			Model model, HttpSession session) {
		User user = userServ.login(newLogin, result);
		if(result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "index.jsp";
	}
		session.setAttribute("user_id", user.getId());
		return "redirect:/dashboard";
	}
	//#################################################################################################
	//########################      END OF LOGIN AND REG       ########################################
	//#################################################################################################
	
	
	//#################################################################################################
	//########################      START OF CREATE ROUTES       ######################################
	//#################################################################################################
	//get mapping for create
	@GetMapping("/newIdea")
	public String newIdea(HttpSession session, Model model) {
		User user = userServ.findOneUser((Long)session.getAttribute("user_id"));
		model.addAttribute("user", user);
		model.addAttribute("idea", new Idea());
		return "create.jsp";
	}
	//post mapping for create
	@PostMapping("/createIdea")
	public String createIdea(@Valid @ModelAttribute("idea")Idea idea, BindingResult result) {
		if(result.hasErrors()) {
			return"create.jsp";
		}
		ideaServ.createIdea(idea);
		return "redirect:/dashboard";
	}
	
	//#################################################################################################
	//########################      END OF CREATE ROUTES       ########################################
	//#################################################################################################
	
	//#################################################################################################
	//########################      START OF EDIT/UPDATE ROUTES       #################################
	//#################################################################################################
	//get mapping for edit
	@GetMapping("/editIdea/{id}")
	public String editIdea(@PathVariable("id") Long id, Model model, HttpSession session) {
		model.addAttribute("idea", ideaServ.getOneIdea(id)); //finding info for the idea by id
		model.addAttribute("user", userServ.findOneUser ((Long)session.getAttribute("user_id"))); //finding info from the user id tied to the idea
		return "edit.jsp";
	}
	
	//@RequestMapping(value="/update/{id}", method=RequestMethod.POST)
	@RequestMapping(value="/update/{id}", method=RequestMethod.POST)
	public String updatingIdea(@PathVariable("id")Long id,@Valid  @ModelAttribute("idea")Idea idea, BindingResult result) {
		if(result.hasErrors()) {
			return"edit.jsp";
		}
		ideaServ.updateIdea(idea);
		return "redirect:/dashboard";
	}
	
	
	//#################################################################################################
	//########################      START OF VIEW ROUTES       ########################################
	//#################################################################################################
	
	//get mapping view/id
	@GetMapping("/oneIdea/{id}")
	public String oneIdea(@PathVariable("id")Long id, Model model, HttpSession session) {
		model.addAttribute("idea", ideaServ.getOneIdea(id)); // getting one idea's info and sending it to the JSP
		model.addAttribute("user", userServ.findOneUser((Long) session.getAttribute("user_id"))); //getting the user in session and sending that to the JSP
		if(session.getAttribute("user_id") == null) {
			return "redirect:/";			
		} else {
			return "view.jsp";
		}
	}
	
	//#################################################################################################
	//########################      END OF VIEW ROUTE       ###########################################
	//#################################################################################################
	
	//#################################################################################################
	//########################      START OF DELETE ROUTE       #######################################
	//#################################################################################################
	
	//@RequestMapping(value="/<<PLACE OBJECT HERE>>/{id}/delete")
	@RequestMapping(value="/idea/{id}/delete")
	public String deleteIdea(@PathVariable("id")Long id, Model model) {
		this.ideaServ.deleteIdea(id);
		return "redirect:/dashboard";
	}
	
	//#################################################################################################
	//########################      END OF DELETE ROUTE       #########################################
	//#################################################################################################
	

}
