package jp.projects.miya.hadoop_solr_indexer;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

public class SolrjPopulator {
  public static void main(String[] args) throws IOException, SolrServerException {
    HttpSolrServer server = new HttpSolrServer("http://localhost:8983/solr/batch_tweets");
    for(int i=0;i<1000;++i) {
      SolrInputDocument doc = new SolrInputDocument();
      doc.addField("id", "100000001");
      doc.addField("user_name", "Asmath");
      doc.addField("user_screen_name", "book-" + i);
      doc.addField("user_statuses_count", "12");
      server.add(doc);
      if(i%100==0) server.commit();  // periodically flush
    }
    server.commit();
  }
}
