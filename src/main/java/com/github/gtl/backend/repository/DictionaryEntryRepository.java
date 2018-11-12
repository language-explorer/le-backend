package com.github.gtl.backend.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.github.gtl.backend.model.DictionaryEntry;

public interface DictionaryEntryRepository extends ReactiveCassandraRepository<DictionaryEntry, String> {

}
