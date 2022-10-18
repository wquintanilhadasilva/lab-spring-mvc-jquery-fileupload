package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GreetingController {
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	
	@RequestMapping(value="/upload", headers=("content-type=multipart/*"), method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		String  fileName = file.getOriginalFilename();
		
		System.out.println(fileName);
		
		String tmpdir = System.getProperty("java.io.tmpdir");
        System.out.println("Temp file path: " + tmpdir);
        
        String tmpFile = tmpdir + "/" + fileName;
        
        System.out.println(tmpFile);
        
        try {
			file.transferTo( new File(tmpFile));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
		Map<String, String> map = new HashMap<>();
		map.put("nome", fileName);
		map.put("size", String.valueOf(file.getSize()));
		map.put("tempFileName", tmpFile);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writerWithDefaultPrettyPrinter()
		  .writeValueAsString(map);
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK); 

	}
	
	@RequestMapping(value="/delete/{file}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Void> delete(@PathVariable("file") String file) throws IOException {
		
		System.out.println(file);
		
		String tmpdir = System.getProperty("java.io.tmpdir");
        System.out.println("Temp file path: " + tmpdir);
        
        String tmpFile = tmpdir + "/" + file;
        
        System.out.println("Deletando o arquivo " + tmpFile);
        
        Files.delete(Path.of(tmpFile));
        
		return new ResponseEntity<Void>(HttpStatus.OK); // ("File uploaded successfully.");

	}
	
	@RequestMapping(value="/save", consumes = "application/json", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> saveForm(@RequestBody LinkedHashMap<String,Object> formulario) throws IOException {
		
		System.out.println(formulario);
		
		Map<String, String> map = new HashMap<>();
		map.put("mensagem", "Dados gravados com sucesso!");
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writerWithDefaultPrettyPrinter()
		  .writeValueAsString(map);
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);

	}

	
}
