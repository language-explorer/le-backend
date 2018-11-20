package com.github.gtl.backend.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.domain.Pageable;

import com.github.gtl.backend.model.DictionaryEntry;

import reactor.core.publisher.Flux;

public interface DictionaryEntryRepository extends ReactiveCassandraRepository<DictionaryEntry, String> {
}
