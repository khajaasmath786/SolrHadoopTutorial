package jp.projects.miya.hadoop_solr_indexer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SolrIndexer extends Configured implements Tool {
	/**
	 *
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SolrIndexer.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.hadoop.util.Tool#run(java.lang.String[])
	 */
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 1) {
			this.printUsage();
			return -1;
		} else {
			Configuration conf = this.getConf();
			if (conf.get(Constants.CSV_HEADER_PROPERTY) == null
				|| conf.get(Constants.SOLR_URL) == null
				|| conf.get(Constants.SEND_BUFFER_SIZE) == null
				|| conf.get(Constants.REQUEST_THREAD_COUNT) == null) {
				this.printUsage();
				
				// Run this example from eclipse using Run As Java with aruguments as -D csv.headers=id,user_name,user_screen_name -D request.solr.url=http://localhost:8983/solr/batch_tweets request.bufsize=100 -D request.threads=5 input
				
				
				//$ export LIBJARS=/usr/lib/*.jar,usr/lib/solr/bib/*.jar
				//-libjars &lt;/usr/lib/solr;/usr/lib/solr/solr-solrj-4.4.0-search-1.3.0.jar
				//hadoop jar hadoop-solr-indexer-0.0.1-SNAPSHOT.jar jp.projects.miya.hadoop_solr_indexer.SolrIndexer -libjars &lt;/usr/lib/solr&gt;/usr/lib/solr/solr-solrj-4.4.0-search-1.3.0.jar -D csv.headers=id,name,comment -D request.solr.url=http://localhost:8983/solr -D request.bufsize=100 -D request.threads=5 input
//hadoop jar hadoop-solr-indexer-0.0.1-SNAPSHOT.jar jp.projects.miya.hadoop_solr_indexer.SolrIndexer -libjars /usr/lib/solr/solr-solrj-4.4.0-search-1.3.0.jar -D csv.headers=id,name,comment -D request.solr.url=http://localhost:8983/solr -D request.bufsize=100 -D request.threads=5 input
				return -1;
			}
			HttpSolrServer server = new HttpSolrServer(conf.get(Constants.SOLR_URL));

			SolrIndexer.LOG.info(conf.get(Constants.SOLR_URL) + " ping ...");
			SolrIndexer.LOG.info("Before Pinging");
			//server.ping();
			SolrIndexer.LOG.info("After Pinging");
			Job job = new Job(conf);
			//hadoop jar hadoop-solr-indexer-0.0.1-SNAPSHOT.jar jp.projects.miya.hadoop_solr_indexer.SolrIndexer -libjars /usr/lib/solr/ -D csv.headers=id,name,comment -D request.solr.url=http://http://localhost:8983/solr -D request.bufsize=100 -D request.threads=5 input

			
			job.setJarByClass(SolrIndexer.class);
			// job.setSomeProperty(...);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(NullOutputFormat.class);
			FileInputFormat.setInputPaths(job, new Path(args[0]));

			job.setMapperClass(IndexMapper.class);

			job.setNumReduceTasks(0);

			job.setMapOutputKeyClass(NullWritable.class);
			job.setMapOutputValueClass(NullWritable.class);
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(NullWritable.class);

			SolrIndexer.LOG.info(conf.get(Constants.SOLR_URL) + " start indexer");
			
			if (job.waitForCompletion(true)) {
				SolrIndexer.LOG.info(conf.get(Constants.SOLR_URL) + " indexes optimize ...");
				
				server.optimize();
				
				SolrIndexer.LOG.info(conf.get(Constants.SOLR_URL) + " indexes build done.");
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	private void printUsage() {
		System.err.printf(
				"Usage: %s [generic options] -D " + Constants.CSV_HEADER_PROPERTY + "=<header-list>" +
						"-D " + Constants.SOLR_URL + "=<solr url>" +
						"-D " + Constants.SEND_BUFFER_SIZE + "=<send buffer size>" +
						"-D " + Constants.REQUEST_THREAD_COUNT + "=<thread count>" +
						" <input dir>\n",
				getClass().getSimpleName());
		ToolRunner.printGenericCommandUsage(System.err);
	}

	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new SolrIndexer(), args);

		System.exit(exitCode);
	}
}