package com.github.gtl.backend.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.utils.UUIDs;
import com.github.gtl.backend.model.TextEntry;
import com.github.gtl.backend.repository.TextEntryRepository;

@Component
public class TextImporter {
	private TextEntryRepository textRep;

	public TextImporter(TextEntryRepository textRep) {
		this.textRep = textRep;
	}
	
	public void process(String file) throws IOException {
		File f = new File(file);
		String fileName = f.getName();
		try (FileReader in = new FileReader(f)) {
			List<String> lines = IOUtils.readLines(in);
			Map<String, String> translations = new HashMap<>();
            lines.forEach(line -> {
                    if (line.startsWith("[")) {
                            int p = line.indexOf(']');
                            String key = line.substring(1,  p);
                            String value = line.substring(p + 1);
                            translations.put(key, value);
                    }
            });
            AtomicLong counter = new AtomicLong();
            lines.forEach(line -> {
                if (!line.startsWith("[")) {
                        StringBuilder b = new StringBuilder();
                        while (true) {
                                if (!line.contains("[")) {
                                        break;
                                }
                                int p = line.indexOf('[');
                                int e = line.indexOf(']', p);
                                String k = line.substring(p + 1, e);
                                line = line.substring(0, p) + line.substring(e + 1);
                                b.append(translations.get(k));
                        }
                        TextEntry te = new TextEntry();
                        te.setOriginal(line);
                        te.setTranslation(b.toString());
                        te.setId(fileName + "-" + String.format("%07d", counter.incrementAndGet()));
                        textRep.save(te).subscribe(t ->System.out.println(t));
                }
        });
		}
	}

	private TextEntry toDictEntry(String line) {
		String[] split = line.split("\\|");
		String w = split[0];
		String t = split.length == 1 ? null : split[1];
		TextEntry te = new TextEntry();
		te.setOriginal(w);
		te.setTranslation(t);
		te.setId(UUIDs.random().toString());
		return te;
	}
}
