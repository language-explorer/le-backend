package com.github.gtl.backend.importer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.utils.UUIDs;
import com.github.gtl.backend.model.DictionaryEntry;
import com.github.gtl.backend.repository.DictionaryEntryRepository;

@Component
public class DictionaryImporter {
	private DictionaryEntryRepository dictRep;

	public DictionaryImporter(DictionaryEntryRepository dictRep) {
		this.dictRep = dictRep;
	}

	public void process(String file) throws IOException {
		try (FileReader in = new FileReader(file)) {
			List<String> lines = IOUtils.readLines(in);
			lines.stream()
				.map(line -> toDictEntry(line))
				.forEach(de -> 
					dictRep.save(de).subscribe(d -> System.out.println(d))
				);
		}
	}

	private DictionaryEntry toDictEntry(String line) {
		String[] split = line.split("\\|");
		String w = split[0];
		List<String> t = toTranslations(split.length == 1 ? null : split[1]);
		DictionaryEntry de = new DictionaryEntry();
		de.setOriginal(w);
		de.setTranslations(t);
		de.setId(UUIDs.random().toString());
		return de;
	}

	private List<String> toTranslations(String t) {
		if (t == null) {
			return new ArrayList<>();
		}
		return Arrays.asList(t.split("#"));
	}
}
