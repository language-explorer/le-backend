create keyspace IF NOT EXISTS gtl WITH REPLICATION = { 
   'class' : 'SimpleStrategy', 
   'replication_factor' : 1 
  };

create table IF NOT EXISTS gtl.dictionary_entry (
	id text,
	original text,
	translations list<text>,
	PRIMARY KEY (id)
);

create table IF NOT EXISTS gtl.text_entry (
	id text,
	original text,
	translation text, 
	PRIMARY KEY (id)
);
