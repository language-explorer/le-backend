package com.github.gtl.backend.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.util.StringUtils;

import lombok.Data;

@Data
@Table("text_entry")
public class TextEntry implements Comparable<TextEntry> {
	@PrimaryKey
	private String id;
	@Column
	private String original;
	@Column
	private String translation;
	
	@Override
	public int compareTo(TextEntry o) {
		return id.compareTo(o.getId());
	}

	public boolean matches(DictionaryEntry de) {
		return originalContains(de.getOriginal()) || translationContains(de.getTranslations());
	}

	private boolean translationContains(List<String> translations) {
		if (StringUtils.isEmpty(translation) || translations == null) {
			return false;
		}
		String t = translation.toLowerCase();
		return translations.stream().filter(tt -> containsTranslation(t, tt)).findFirst().isPresent();
	}

	private boolean containsTranslation(String text, String value) {
		if (value.startsWith("->")) {
			return false;
		}
		return text.contains(value.toLowerCase());
	}

	private boolean originalContains(String value) {
		return original.toLowerCase().contains(value.toLowerCase());
	}
}
