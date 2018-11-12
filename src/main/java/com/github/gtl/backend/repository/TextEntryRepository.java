package com.github.gtl.backend.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.github.gtl.backend.model.TextEntry;

public interface TextEntryRepository extends ReactiveCassandraRepository<TextEntry, String> {

}
