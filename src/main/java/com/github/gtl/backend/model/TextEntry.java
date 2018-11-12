package com.github.gtl.backend.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
}
