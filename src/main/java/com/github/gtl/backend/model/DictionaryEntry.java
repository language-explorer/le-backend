package com.github.gtl.backend.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Data
@Table("dictionary_entry")
public class DictionaryEntry implements Comparable<DictionaryEntry> {
	@PrimaryKey
	private String id;
	@Column
	private String original;
	@Column
	private List<String> translations;

	@Override
	public int compareTo(DictionaryEntry o) {
		return original.compareTo(o.getOriginal());
	}
}
