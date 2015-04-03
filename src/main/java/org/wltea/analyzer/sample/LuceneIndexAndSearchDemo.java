 package org.wltea.analyzer.sample;
 
 import java.io.IOException;
 import java.io.PrintStream;
 import org.apache.lucene.analysis.Analyzer;
 import org.apache.lucene.document.Document;
 import org.apache.lucene.document.Field.Store;
 import org.apache.lucene.document.StringField;
 import org.apache.lucene.document.TextField;
 import org.apache.lucene.index.CorruptIndexException;
 import org.apache.lucene.index.DirectoryReader;
 import org.apache.lucene.index.IndexReader;
 import org.apache.lucene.index.IndexWriter;
 import org.apache.lucene.index.IndexWriterConfig;
 import org.apache.lucene.index.IndexWriterConfig.OpenMode;
 import org.apache.lucene.queryparser.classic.ParseException;
 import org.apache.lucene.queryparser.classic.QueryParser;
 import org.apache.lucene.search.IndexSearcher;
 import org.apache.lucene.search.Query;
 import org.apache.lucene.search.ScoreDoc;
 import org.apache.lucene.search.TopDocs;
 import org.apache.lucene.store.Directory;
 import org.apache.lucene.store.LockObtainFailedException;
 import org.apache.lucene.store.RAMDirectory;
 import org.apache.lucene.util.Version;
 import org.wltea.analyzer.lucene.IKAnalyzer;
 
 public class LuceneIndexAndSearchDemo
 {
   public static void main(String[] args)
   {
     String fieldName = "text";
 
     String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";
 
     Analyzer analyzer = new IKAnalyzer(true);
 
     Directory directory = null;
     IndexWriter iwriter = null;
     IndexReader ireader = null;
     IndexSearcher isearcher = null;
     try
     {
       directory = new RAMDirectory();
 
       IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40, analyzer);
       iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
       iwriter = new IndexWriter(directory, iwConfig);
 
       Document doc = new Document();
       doc.add(new StringField("ID", "10000", Field.Store.YES));
       doc.add(new TextField(fieldName, text, Field.Store.YES));
       iwriter.addDocument(doc);
       iwriter.close();
 
       ireader = DirectoryReader.open(directory);
       isearcher = new IndexSearcher(ireader);
 
       String keyword = "中文分词工具包";
 
       QueryParser qp = new QueryParser(Version.LUCENE_40, fieldName, analyzer);
       qp.setDefaultOperator(QueryParser.AND_OPERATOR);
       Query query = qp.parse(keyword);
       System.out.println("Query = " + query);
 
       TopDocs topDocs = isearcher.search(query, 5);
       System.out.println("命中：" + topDocs.totalHits);
 
       ScoreDoc[] scoreDocs = topDocs.scoreDocs;
       for (int i = 0; i < topDocs.totalHits; i++) {
         Document targetDoc = isearcher.doc(scoreDocs[i].doc);
         System.out.println("内容：" + targetDoc.toString());
       }
     }
     catch (CorruptIndexException e) {
       e.printStackTrace();
 
       if (ireader != null) {
         try {
           ireader.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
       if (directory != null)
         try {
           directory.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
     }
     catch (LockObtainFailedException e)
     {
       e.printStackTrace();
 
       if (ireader != null) {
         try {
           ireader.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
       if (directory != null)
         try {
           directory.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
     }
     catch (IOException e)
     {
       e.printStackTrace();
 
       if (ireader != null) {
         try {
           ireader.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
       if (directory != null)
         try {
           directory.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
     }
     catch (ParseException e)
     {
       e.printStackTrace();
 
       if (ireader != null) {
         try {
           ireader.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
       if (directory != null)
         try {
           directory.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
     }
     finally
     {
       if (ireader != null) {
         try {
           ireader.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
       if (directory != null)
         try {
           directory.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
     }
   }
 }