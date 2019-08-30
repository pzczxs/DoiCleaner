package cn.edu.bjut.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.bjut.models.DoiStatus;
import cn.edu.bjut.models.Reference;
import cn.edu.bjut.utils.StringUtils;

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

public abstract class AbstractCleaner {
	private static final String RECORD_DELIMITER = "; "; 
	private static final String DOI_DELIMITER = ", "; 
	
	protected Map<String, List<Reference>> id2Refs; 
	
	public AbstractCleaner() {
		this.id2Refs = new LinkedHashMap<String, List<Reference>>(); 
	}
	
	/**
	 * Clean all of files in the specified directory. 
	 * 
	 * @param
	 * 		dirName: directory name
	 * 
	 * */
	public void clean(String dirName) {
		File dir = new File(dirName); 
		
		for (File f: dir.listFiles()) {
			if (f.isFile()) {
				clean(f); 
			} else if (f.isDirectory()) {
				try {
					clean(f.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public abstract void clean(File f); 
	
	/**
	 * Pre-processing doi.
	 * 
	 * @param
	 * 		doi: original doi
	 * @return
	 * 		pre-processed doi
	 * */
	private DoiStatus trimDoi(String doi) {
		String newDoi = doi.toUpperCase().replaceAll("\\s+", ""); 
		
		Pattern patPrefix = Pattern.compile("^(?:D[0|O]I/?|HTTP://DX.D[0|O]I.[0|O]RG/|[0|O]RG/|[:|/]|\\d+\\.HTTP://DX.D[0|O]I.[0|O]RG/?)+(.*)"); 
		Pattern patSuffix = Pattern.compile("(.*?)(?:/-/DCSUPPLEMENTAL|/SUPPINF[0|O]|(\\.)?/|[;|.| ]?PMID:[\\d]+|[:|/|.]|[;|.| ]?PMCID:PMC[\\d]+|[(|.|;]+EPUB|(\\(\\d{4}\\))?[(|\\[]EPUBAHEADOFPRINT[)|\\]]?(\\(\\d{4}\\))?|[.|;| ]?ARTICLEPUBLISHEDONLINE.*\\d{4}|[.|(]*HTTP://.*)$");
		Pattern patYear = Pattern.compile("(.*?)(\\(\\d{4}[\\)]?)$"); 
		
		Matcher matPrefix = patPrefix.matcher(newDoi); 
		if (matPrefix.find()) {
			newDoi = matPrefix.group(1); 
		} 
		
		Matcher matSuffix = patSuffix.matcher(newDoi); 
		if (matSuffix.find()) {
			newDoi = matSuffix.group(1); 
		} 
		
		Matcher matYear = patYear.matcher(newDoi); 
		if (matYear.find()) {
			newDoi = matYear.group(1); 
		}
		
		newDoi = newDoi.replaceAll("\\\\", "").replaceAll("__", "_").replaceAll("\\.\\.", "."); 
		newDoi = newDoi.replaceAll("<.*?>.*?</.*?>", "").replaceAll("<.*?/>", ""); 
		
		if (!newDoi.equals("") && newDoi.matches("10\\..*?/.*?")) {
			if (newDoi.matches(".*?[-|_]$")) {
				return new DoiStatus(newDoi, false); 
			}
			
			if (StringUtils.isMatch(newDoi)) {
				return new DoiStatus(newDoi, true); 
			} else if (StringUtils.isMatch(newDoi.substring(0, newDoi.length() - 1))) {
				return new DoiStatus(newDoi.substring(0, newDoi.length() - 1), true); 
			} 
		}
		
		return new DoiStatus(newDoi, false); 
	}
	
	private String joinDoi(String articleId, String reference, String[] dois) {
		Set<String> doiSet = new LinkedHashSet<String>(); 
		
		// remove duplicates
		for (String doi: dois) {
			DoiStatus ds = trimDoi(doi); 
			if (ds.isStatus()) {
				doiSet.add(ds.getDoi()); 
			}
		}
		
		if (doiSet.size() == 0) {
			return null; 
		}
		
		StringBuilder sb = new StringBuilder(); 
		
		List<String> doiList = new ArrayList<String>(doiSet); 
		for (int i = 0; i < doiList.size() - 1; i++) {
			sb.append(doiList.get(i) + DOI_DELIMITER); 
		}
		sb.append(doiList.get(doiList.size() - 1)); 
		
		return sb.toString(); 
	}
	
	/**
	 * Save reference list to database.
	 * 
	 * @param 
	 * 		articleId: UT
	 * 		referenceList: reference list
	 * */
	protected void cleaning(String articleId, String referenceList) {
		String[] refs = referenceList.split(RECORD_DELIMITER);

		for (String r : refs) {
			int position = r.indexOf(", DOI");
			if (position != -1) { // found
				String text = r.substring(0, position).trim();
				String doiOriginal = r.substring(position + ", DOI".length()).trim();

				if (doiOriginal.startsWith("[") && doiOriginal.endsWith("]")) { // multiple dois
					String doi = doiOriginal.substring(1, doiOriginal.length() - 1);
					doi = joinDoi(articleId, r, doi.split(DOI_DELIMITER)); 
					
					String[] dois = (doi != null)? doi.split(DOI_DELIMITER): null; 
					
					Reference ref = (doi != null && dois.length == 1)? new Reference(text, doiOriginal, doi):
						new Reference(text, doiOriginal, doi, true); 
					addReference(articleId, ref); 
				} else {
					DoiStatus ds = trimDoi(doiOriginal); 
					
					Reference ref = (ds.isStatus())? new Reference(text, doiOriginal, ds.getDoi()):
						new Reference(text, doiOriginal, ds.getDoi(), true); 
					addReference(articleId, ref); 
				}
			} else {
				if (r.trim().equals("")) {
					continue;
				}
				
				Reference ref = new Reference(r.trim(), null, null); 
				addReference(articleId, ref); 
			}
		}
	}
	
	/**
	 * Add one cited reference to id2Refs.
	 * 
	 * @param 
	 * 		articleId: UT
	 * 		ref: Reference
	 * */
	private void addReference(String articleId, Reference ref) {
		List<Reference> citedRefs = id2Refs.containsKey(articleId)? id2Refs.get(articleId): 
			new LinkedList<Reference>(); 
		citedRefs.add(ref); 
		id2Refs.put(articleId, citedRefs); 
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		for (Map.Entry<String, List<Reference>> entry: id2Refs.entrySet()) {
			sb.append(entry.getKey() + ":\n"); 
			for (Reference ref: entry.getValue()) {
				sb.append("\t" + ref + "\n"); 
			}
		}
		
		return sb.toString(); 
	}
	
	public void print(final File f) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(f));
			print(writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public void print(final Writer w) {
		try {
			for (Map.Entry<String, List<Reference>> entry: id2Refs.entrySet()) {
				w.write(entry.getKey() + ":\n");
				for (Reference ref: entry.getValue()) {
					w.write("\t" + ref + "\n"); 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
