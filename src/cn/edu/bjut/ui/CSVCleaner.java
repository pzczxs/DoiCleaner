package cn.edu.bjut.ui;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/*
 * A cleaning method of DOI names of cited references in Web of Science.  
 * 
 * References: 
 * [1] Shuo Xu, Liyuan Hao, Xin An, Dongsheng Zhai, and Hongshen Pang, 2019. Types of 
 * DOI Errors of Cited References in Web of Science with a Cleaning Method. Scientometrics, 
 * Vol. 120, No. 3, pp. 1427-1437. DOI: https://doi.org/10.1007/s11192-019-03162-4 
 * 
 * Author: XU, Shuo (pzczxs@gmail.com)
 * */

public class CSVCleaner extends AbstractCleaner {
	
	public CSVCleaner() {
		super(); 
	}
	
	/**
	 * Clean the data in a specified file. 
	 * 
	 * @param
	 * 		f: a File object
	 * 
	 * */
	@Override
	public void clean(File f) {
		Reader reader = null; 
		try {
			reader = new FileReader(f); 
			Iterable<CSVRecord> records = CSVFormat.TDF.withQuote(' ').withFirstRecordAsHeader().parse(reader); 
			
			for (CSVRecord record: records) {
				String articleId = record.get("UT").trim().toUpperCase(); 
				String referenceList = record.get("CR").trim();
				
				cleaning(articleId, referenceList); 
			} // for
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void usage(String programName) {
		System.out.println(programName + " v1.0: Cleaning DOI names of cited references.");
		System.out.println("Author: XU, Shuo (pzczxs@gmail.com)");
		System.out.println("Usage: java -jar " + programName + " -dirName dir_name -outFileName out_fname");
		System.exit(0);
	}
	
	private static Map<String, String> parse(String[] args) {
		Map<String, String> params = new HashMap<String, String>(); 
		
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i]; 
			
			if (arg.startsWith("-")) {
				params.put(arg.substring(1), args[++i]); 
			}
		}
		
		return params; 
	}
	
	public static void main(final String[] args) {
		final String programName = "DoiCleaner.jar"; 
		if (args.length != 4) {
			usage(programName);
		}
		
		Map<String, String> params = parse(args); 
		
		String dirName = params.get("dirName"); 
		
		AbstractCleaner cleaner = new CSVCleaner(); 
		cleaner.clean(dirName);
		cleaner.print(new File(params.get("outFileName"))); 
	}
}
