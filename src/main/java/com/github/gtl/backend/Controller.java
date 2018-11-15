package com.github.gtl.backend;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.gtl.backend.importer.DictionaryImporter;
import com.github.gtl.backend.importer.TextImporter;
import com.github.gtl.backend.model.DictionaryEntry;
import com.github.gtl.backend.model.TextEntry;
import com.github.gtl.backend.repository.DictionaryEntryRepository;
import com.github.gtl.backend.repository.TextEntryRepository;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/1.0")
@CrossOrigin
public class Controller {
	private DictionaryEntryRepository dictRep;
	private TextEntryRepository textRep;
	private DictionaryImporter dictImp;
	private TextImporter textImp;

	public Controller(DictionaryEntryRepository dictRep, TextEntryRepository textRep, DictionaryImporter dictImp, TextImporter textImp) {
		this.dictRep = dictRep;
		this.textRep = textRep;
		this.dictImp = dictImp;
		this.textImp = textImp;
	}
	
	@GetMapping("dict")
	Flux<DictionaryEntry> dict(@RequestParam(required = false) String filter) {
		return dictRep.findAll().sort();
	}
	
	@GetMapping("text")
	Flux<TextEntry> text() {
		return textRep.findAll().sort().limitRequest(20);
	}
	
	@PostMapping("import/dict")
	@ResponseStatus(HttpStatus.CREATED)
	void importDict(@RequestBody ImportRequest request) throws IOException {
		dictImp.process(request.getPath());
	}
	
	@PostMapping("import/text")
	@ResponseStatus(HttpStatus.CREATED)
	void importText(@RequestBody ImportRequest request) throws IOException {
		textImp.process(request.getPath());
	}
}
