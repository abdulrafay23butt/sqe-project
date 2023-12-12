package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.models.Product;
import com.jtspringproject.JtSpringProject.models.User;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jtspringproject.JtSpringProject.services.userService;
import com.jtspringproject.JtSpringProject.services.productService;


@Controller
public class UserController{

	@Autowired
	private userService userService;

	@Autowired
	private productService productService;

	private User hey;
	@GetMapping("/register")
	public String registerUser()
	{
		return "register";
	}

	@GetMapping("/buy")
	public String buy()
	{
		return "buy";
	}


	@GetMapping("/")
	public String userlogin(Model model) {

		return "userLogin";
	}
	@RequestMapping(value = "userloginvalidate", method = RequestMethod.POST)
	public ModelAndView userlogin( @RequestParam("username") String username, @RequestParam("password") String pass,Model model,HttpServletResponse res) {

		System.out.println(pass);
		User u = this.userService.checkLogin(username, pass);
		hey = u;
		System.out.println(u.getUsername());

		if(u.getUsername()!=null && u.getRole().equals("ROLE_ADMIN"))
		{
			ModelAndView mView = new ModelAndView("userLogin");
			mView.addObject("message", "Please go to admin login page to login as an admin");
			return mView;
		}

		if(u.getUsername()!=null && u.getUsername().equals(username)) {

			res.addCookie(new Cookie("username", u.getUsername()));
			ModelAndView mView  = new ModelAndView("index");
			mView.addObject("user", u);
			List<Product> products = this.productService.getProducts();

			if (products.isEmpty()) {
				mView.addObject("msg", "No products are available");
			} else {
				mView.addObject("products", products);
			}
			return mView;

		}else {
			ModelAndView mView = new ModelAndView("userLogin");
			mView.addObject("message", "Please enter correct email and password");
			return mView;
		}

	}

	@GetMapping("logout")
	public String userLogout(Model model){
		return "userLogin";
	}

	@GetMapping("/user/products")
	public ModelAndView getproduct() {

		ModelAndView mView = new ModelAndView("uproduct");

		List<Product> products = this.productService.getProducts();

		if(products.isEmpty()) {
			mView.addObject("msg","No products are available");
		}else {
			mView.addObject("products",products);
		}

		return mView;
	}
	@RequestMapping(value = "newuserregister", method = RequestMethod.POST)
	public String newUseRegister(@ModelAttribute User user)
	{

		System.out.println(user.getEmail());
		user.setRole("ROLE_NORMAL");
		this.userService.addUser(user);

		return "redirect:/";
	}



	//for Learning purpose of model
	@GetMapping("/test")
	public String Test(Model model)
	{
		System.out.println("test page");
		model.addAttribute("author","jay gajera");
		model.addAttribute("id",40);

		List<String> friends = new ArrayList<String>();
		model.addAttribute("f",friends);
		friends.add("xyz");
		friends.add("abc");

		return "test";
	}

	// for learning purpose of model and view ( how data is pass to view)

	@GetMapping("/test2")
	public ModelAndView Test2()
	{
		System.out.println("test page");
		//create modelandview object
		ModelAndView mv=new ModelAndView();
		mv.addObject("name","jay gajera 17");
		mv.addObject("id",40);
		mv.setViewName("test2");

		List<Integer> list=new ArrayList<Integer>();
		list.add(10);
		list.add(25);
		mv.addObject("marks",list);
		return mv;


	}

	@GetMapping("cartproduct")
	public String cartproduct()
	{
		return "cartproduct";
	}
//	@GetMapping("carts")
//	public ModelAndView  getCartDetail()
//	{
//		ModelAndView mv= new ModelAndView();
//		List<Cart>carts = cartService.getCarts();
//	}


	@GetMapping("profileDisplay")
	public ModelAndView profileDisplay()
	{
		ModelAndView mv = new ModelAndView("updateProfile");
		mv.addObject("userid",hey.getId());
		mv.addObject("username",hey.getUsername());
		mv.addObject("email",hey.getEmail());
		mv.addObject("password",hey.getPassword());
		mv.addObject("address",hey.getAddress());
		return mv;
	}


	@RequestMapping(value = "updateuser", method = RequestMethod.POST)
	public ModelAndView profileUpdate(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("email") String email, @RequestParam("address") String address, Model model)
	{
		if(hey!=null)
		{
			hey.setUsername(username.trim());
			hey.setPassword(password);
			hey.setEmail(email);
			hey.setAddress(address);

			userService.updateUser(hey);
		}
		ModelAndView mv= new ModelAndView("updateProfile");
//		mv.addObject("userid",yo.getId());
		mv.addObject("username",hey.getUsername());
		mv.addObject("email",hey.getEmail());
		mv.addObject("password",hey.getPassword());
		mv.addObject("address",hey.getAddress());
		mv.addObject("success","Profile has been updated successfully");
		return mv;
	}


}
