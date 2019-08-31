# DoiCleaner
A cleaning method of DOI names of cited references in Web of Science (WoS). 

## 1. Introduction
### 1.1 Description
With the establishment of digital object identifier (DOI) system in 1997, DOIs have been assigned uniquely to many digital objects. The DOI name is a case-insensitive alphanumeric string, and consists of two parts separated by a forward slash: (a) a prefix beginning with the numeral 10 assigned by IDF or by DOI registration agencies, and (b) a suffix assigned by the registrants.

Although comprehensive bibliographic databases, such as Scopus and Web of Science, largely promote the development of scientometrics and informetrics, they are not free of errors. So far, errors have been found to happen to many fields of publications, including the DOI field.

By definition, each DOI name should be unique and must identify one and only one entity. However, DOI errors present challenges for the data collection from different sources in order to avoid unwanted duplicate entries. In addition, it remains unknown that whether there are other types of DOI errors, how often each type of errors occur, and whether it is possible to automatically correct these errors.

Various DOI errors of cited references in the WoS database are deeply analyzed and a cleaning approach is put forward to alleviate the extent of DOI errors of cited references by [Xu et al. (2019)](https://doi.org/10.1007/s11192-019-03162-4). This tool implements the cleaning approach. Please refer to [Xu et al. (2019)](https://doi.org/10.1007/s11192-019-03162-4) for more details. 

### 1.2 News, Comments, and Bug Reports.
We highly appreciate any suggestion, comment, and bug report.

### 1.3 License
DoiCleaner is a free JAVA tool; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

DoiCleaner is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with DoiCleaner; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.

## 2. How to Use DoiCleaner

### 2.1 Command Line & Input Parameters
```
> java -jar DoiCleaner.jar -dirName dir_name -outFileName out_fname
    -dirName dir_name: the directory containing the bibliographic data in CSV format from Web of Science.
    -outFileName out_fname: the output file name. 
```
### 2.2 Examples
```
> java -jar DoiCleaner.jar -dirName data -outFileName dois.txt
```

### 2.3 Data Format
Input data format: CSV format from Web of Science database

Output data format: 
```
WoS ID: 
\TAB cited reference information (not including DOI name) \TAB original DOI name \TAB cleaned DOI name [\TAB flag] 
......
WoS ID:
\TAB cited reference information (not including DOI name) \TAB original DOI name \TAB cleaned DOI name [\TAB flag] 
......
```

If the cleaned DOI name is illegal, or there are still multiple DOI names after cleaning, ```flag``` is set to ```true```. Otherwise, ```\TAB flag``` disappears. Please check the 2nd example in the following for more details. If a cited reference is not attached any DOI name, it is just copied directly (cf. the 3rd one in the following). 

```
WOS:000418594700004:
	Achtman M, 2012, PLOS PATHOG, V8	10.1371/journal.ppat.1002776	10.1371/JOURNAL.PPAT.1002776
	Clark CG, 2003, J CLIN MICROBIOL, V41, P27	[10.1128/JCM.41.1.27-33.2003, 10.1128/JCM>41.1.27-33.2003]	10.1128/JCM.41.1.27-33.2003, 10.1128/JCM>41.1.27-33.2003	true
	Ferrari R, 2011, J INFECT DEV COUNTR, V5, P496
```
### 2.4 Additional Information

This tool is written by [XU, Shuo](http://54xushuo.net/wiki/) from [Beijing University of Technology](http://www.bjut.edu.cn). If you find this toolbox useful, please cite MLS-SVR as follows:

Shuo Xu, Liyuan Hao, Xin An, Dongsheng Zhai, and Hongshen Pang, 2019. [Types of DOI Errors of Cited References in Web of Science with a Cleaning Method](https://doi.org/10.1007/s11192-019-03162-4). *Scientometrics*, Vol. 120, No. 3, pp. 1427-1437. 

For any question, please contact XU, Shuo at <xushuo@bjut.edu.cn> OR <pzczxs@gmail.com>.

## 3. References
[1] Shuo Xu, Liyuan Hao, Xin An, Dongsheng Zhai, and Hongshen Pang, 2019. [Types of DOI Errors of Cited References in Web of Science with a Cleaning Method](https://doi.org/10.1007/s11192-019-03162-4). *Scientometrics*, Vol. 120, No. 3, pp. 1427-1437. 
