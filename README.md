SOLR requires 2 main files solarconfig.xml where HDFS location is mentioned schema.xml contains fields which should be indexed in solr.

Go to solr admin page and see to create new collection. New collection requires installdir, data, schema and config.xml

Please see find the information about the batch indexing with tweeets http://blog.cloudera.com/blog/2013/06/quickstart-vm-now-with-real-time-big-data/#comments
~/datasets/batch-tweets.sh

Also find the example in hortonworks to know more about how solrconfig and schema are defined http://hortonworks.com/hadoop-tutorial/searching-data-solr/

------------------------------------- Instructions -----------------------------------------------------

Follow all the instructions mentioned below

Dont forget classpath uploaded here. SOmetimes you get 404 error if libraries are missing especially apache-solar-client.jars ALso mention name of collection in the solr url.

Go through the batchtweets and see how the collections and schema are created with shell script. config xml is present but the schema file is mentioned in the command jar at morphlines. --morphline-file /usr/share/doc/search*/examples/solr-nrt/test-morphlines/tutorialReadAvroContainer.conf Install dir in the batch script has config folder.

--------------------------------------How to run java map reduce----------------------------------------

Please run the solrindexer to create entries in batch_tweets collection. Java map reduce writes it in that collection
you can see the results getting added in batch_tweets collection in hue.

script file consists of how the installation, data and collection is created.
