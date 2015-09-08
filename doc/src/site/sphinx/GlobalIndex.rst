CROSSDATA Global Index
*******************


Introduction
============

Crossdata Global Index have been added as a new feature since version 0.4.0. This document introduces the key concepts
of this feature and provides some examples in order to help the developer to understand the idea behind the Crossdata Global 
Index.

This new function allows to index some values of a table in a search-specialized cluster, and then, when we make a query 
that filter by the indexed values, Crossdata get the PKs from the search-specialized cluster and then make a reduced query
to the original database. This allow us to use powerful text-search funtions like Elastic Search Fuzzy in multiple datastores. 


Declaration
===========
  

    CREATE GLOBAL INDEX \<IndexName\> ON \<TableName\> (\<columns\>) USING CLUSTER \<SearchClusterName\> with \<SpecificJson\>;

\<SearchClusterName\>: The Crossdata Cluster that support FullText search. 

Example
===========

Prepare the Environment:
::
      ATTACH CLUSTER test ON DATASTORE Cassandra WITH OPTIONS {'Hosts': '[localhost]', 'Port': 9042};
      ATTACH CONNECTOR CassandraConnector TO test WITH OPTIONS {'DefaultLimit': '1000'};
      ATTACH CLUSTER elCluster ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[localhost]', 'Native Ports': '[9300]', 'Restful Ports': '[9200]', 'Cluster Name':'esCluster'};
      ATTACH CONNECTOR elasticsearchconnector TO elCluster WITH OPTIONS {};
      
      CREATE CATALOG xd WITH {'settings' : '{"number_of_replicas" : "0","number_of_shards" : "1","analysis" : {"analyzer" : {"raw" :
      {"tokenizer" : "keyword"},"normalized" : {"filter" : ["lowercase"],"tokenizer" : "keyword"},"english" : {"filter" : ["lowercase",
      "english_stemmer"],"tokenizer" : "standard"}},"filter" : {"english_stemmer" : {"type" : "stemmer","language" : "english"}}}}'};
      
      use xd;
      
      CREATE TABLE movie ON CLUSTER test (id text PRIMARY KEY,awards text, plot text, title text);

Create the Index:
::
    CREATE GLOBAL INDEX myIndex ON movie (title, plot, awards) using cluster elCluster with {"title":'["english","normalized"]', "plot":'["english","normalized"]', "awards":'["english","normalized"]'};


Insert some values:
::
      insert into movie (id, awards, plot, title) VALUES ("tt0986233", "41 wins & 29 nominations.", "Irish republican Bobby Sands leads the inmates of a Northern Irish prison in a hunger strike.", "Hunger"); 
      insert into movie (id, awards, plot, title) VALUES ("tt0260688", "20 wins & 2 nominations.", "Ali, Kwita, Omar and Boubker are street kids. The daily dose of glue sniffing represents their only escape from reality. Since they left Dib and his gang, they have been living on the ", "Ali Zaoua: Prince of the Streets"); 
      insert into movie (id, awards, plot, title) VALUES ("tt0181875", "Won 1 Oscar. Another 57 wins & 92 nominations.", "A high-school boy is given the chance to write a story for Rolling Stone Magazine about an up-and-coming rock band as he accompanies it on their concert tour.", "Almost Famous"); 
      insert into movie (id, awards, plot, title) VALUES ("tt0144084", "4 wins & 7 nominations.", "A wealthy New York investment banking executive hides his alternate psychopathic ego from his co-workers and friends as he escalates deeper into his illogical, gratuitous fantasies.", "American Psycho"); 
      insert into movie (id, awards, plot, title) VALUES ("tt0249462", "Nominated for 3 Oscars. Another 57 wins & 59 nominations.", "A talented young boy becomes torn between his unexpected love of dance and the disintegration of his family.", "Billy Elliot"); 
    
Do some queries with indexed fields:
::
      select title from movie where fuzzy("title", "amerikan", 0.6);
      select title from movie where contains("title plot", "hunger", 1);
      select title from movie where contains("awards plot", "won",1);
    
