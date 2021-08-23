package com.codingdojo.exam.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingdojo.exam.models.Idea;
import com.codingdojo.exam.repositories.IdeaRepository;


@Service
public class IdeaService {

	@Autowired
	private IdeaRepository ideaRepo;
	
	//create
	public Idea createIdea(Idea idea) {
		return ideaRepo.save(idea);
	}
	//get one idea
	public Idea getOneIdea(Long id) {
		Optional<Idea> optionalIdea = this.ideaRepo.findById(id);
		if(optionalIdea.isPresent()) {
		return optionalIdea.get();
		} else {
			return null;
		}
	}
	//get all idea
	public List<Idea> getAllIdeas(){
		return ideaRepo.findAll();
	}
	//update a idea
	public Idea updateIdea(Idea idea) {
		return ideaRepo.save(idea);
	}
	//delete a idea
	public void deleteIdea(Long id) {
		this.ideaRepo.deleteById(id);
	}
}

