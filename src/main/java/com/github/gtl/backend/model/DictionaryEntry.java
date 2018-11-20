package com.github.gtl.backend.model;

import java.text.CollationKey;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Table("dictionary_entry")
public class DictionaryEntry implements Comparable<DictionaryEntry> {
	private static final String BCI_RULES = "=´;'`';'-' < a,A < b,B < c,C < d,D < e,E < ε,ꜫ < f,F " +
		    "< g,G < h,H < i,I < j,J < k,K < l,L " +
		    "< m,M < n,N < ɲ,Ɲ < o,O < ɔ,Ↄ < p,P < q,Q < r,R " +
		    "< s,S < t,T < u,U < v,V < w,W < x,X " +
		    "< y,Y < z,Z";

	private static Collator bciCollator;
	
	static {
		try {
			bciCollator = new RuleBasedCollator(BCI_RULES);
			bciCollator.setStrength(Collator.TERTIARY);
		} catch (ParseException e) {
			bciCollator = Collator.getInstance();
		}
	}
	
	@PrimaryKey
	private String id;
	@Column
	private String original;
	@Column
	private List<String> translations;
	
	@JsonIgnore
	@Transient
	private CollationKey collationKey;
	
	public CollationKey getCollationKey() {
		if (collationKey == null) {
			collationKey = bciCollator.getCollationKey(original);
		}
		return collationKey;
	}

	@Override
	public int compareTo(DictionaryEntry o) {
		return getCollationKey().compareTo(o.getCollationKey());
	}

	public boolean contains(String filter) {
		if (StringUtils.isEmpty(filter)) {
			return true;
		}
		String f = filter.toLowerCase();
		return originalContains(f) || translationsContain(f);
	}

	private boolean translationsContain(String f) {
		if (translations == null) {
			return false;
		}
		return translations.stream().filter(t -> t.toLowerCase().contains(f)).findFirst().isPresent();
	}

	private boolean originalContains(String f) {
		return original.toLowerCase().contains(f);
	}
}
