package com.claim.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.claim.entity.Point;
import com.claim.entity.UploadFiles;
import com.claim.entity.User;
import com.claim.repository.PointRepository;
import com.claim.repository.UploadFilesRepository;
import com.claim.repository.UserRepository;
import com.claim.utils.SheetRead;

@CrossOrigin
@RestController
public class UserController 
{
	
	// inject reference to where repository is located
	// same concept as = new UserRepository();
	@Autowired 
	private UserRepository userRepository;
	@Autowired 
	private PointRepository pointRepository;
	@Autowired
	private UploadFilesRepository uploadFilesRepository;
	@Autowired
	private SheetRead sheetRead;
	
	// post request goes to request body
	// expects on object not entire fields
	
	@RequestMapping(value="/submitUserDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> submitUserDetails(@RequestBody User user)
	{
		Optional<User> existingUser = this.userRepository.findByEmail(user.getEmail());
		if(!existingUser.isPresent()) {
			System.out.println("SAVING USER");
			return new ResponseEntity<>(this.userRepository.save(user), HttpStatus.OK);
		}
		return new ResponseEntity<>(existingUser.get(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/findUserByEmail", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<User> findUser(String email)
	{
	
		return new ResponseEntity<>(this.userRepository.findByEmail(email).get(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	private ResponseEntity<Optional<User>> login(@RequestBody User user)
	{
		System.out.println("loginstart");

		System.out.println("EMAIL=" + user.getEmail() + "PASS=" + user.getPassword());

		System.out.println("FOUND EMAIL");
		Optional<User> user2 = this.userRepository.findEmail(user.getEmail());
		System.out.println("USERFOUND");
		//Optional<User> user2 = this.userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
		if(user2.isPresent())
		{
			if(user2.get().getPassword().equals(user.getPassword()))
			{
				System.out.println("LOGGED IN");
				return new ResponseEntity<>(user2, HttpStatus.OK);
			}
			else
			{
				System.out.println("FAIL1");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			System.out.println("FAIL2");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@RequestMapping(value="/getAllUsers", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	private ResponseEntity<List<User>> getAllUsers()
	{
		List<User> Users = this.userRepository.findAll();
		System.out.println("USERS=" + Users.size());
		
		return new ResponseEntity<List<User>>(Users, HttpStatus.OK);
	}
	
	@PutMapping(value="/user/{userId}/upload", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public  ResponseEntity<List<UploadFiles>> uploadFile(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			try {
				sheetRead.importSheet(file, userId);
				return new ResponseEntity<List<UploadFiles>>(uploadFilesRepository.findByUser(user.get()), HttpStatus.OK);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/user/{userId}/uploaded-files")
	public  ResponseEntity<List<UploadFiles>> uploadedFiles(@PathVariable Long userId){
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			return new ResponseEntity<List<UploadFiles>>(uploadFilesRepository.findByUser(user.get()), HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/user/{userId}/delete-file/{fileId}")
	@Transactional
	public  ResponseEntity<List<UploadFiles>> deleteFile(@PathVariable Long userId, @PathVariable Long fileId){
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			Optional<UploadFiles> file = uploadFilesRepository.findById(fileId);
			if(file.isPresent()) {
				pointRepository.deleteByFile(file.get());
				uploadFilesRepository.deleteById(fileId);
			}
			return new ResponseEntity<List<UploadFiles>>(uploadFilesRepository.findByUser(user.get()), HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/file-points/{fileId}")
	public  ResponseEntity<List<Point>> deleteFile( @PathVariable Long fileId){
		Optional<UploadFiles> file = uploadFilesRepository.findById(fileId);
		if(file.isPresent()) {
			return new ResponseEntity<List<Point>>(pointRepository.findAllByFile(file.get()), HttpStatus.OK);
		}
		return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
	}
	
}
