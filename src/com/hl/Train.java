package com.hl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.hl.test.StopWordsHandler;
import com.hl.util.spliter.cn.mmseg4j.example.Complex;

public class Train implements Serializable {  
  
    /** 
     *  
     */  
    private static final long serialVersionUID = 1L;  
  
    public final static String SERIALIZABLE_PATH = "E:\\doc\\hanson\\Train.ser";  
    // 训练集的位置  
    private String trainPath = "E:\\doc\\hanson";  
  
    // 类别序号对应的实际名称  
    private Map<String, String> classMap = new HashMap<String, String>();  
  
    // 类别对应的txt文本数  
    private Map<String, Integer> classP = new ConcurrentHashMap<String, Integer>();  
  
    // 所有文本数  
    private AtomicInteger actCount = new AtomicInteger(0);  
  
      
  
    // 每个类别对应的词典和频数  
    private Map<String, Map<String, Double>> classWordMap = new ConcurrentHashMap<String, Map<String, Double>>();  
  
    // 分词器  
    private transient Complex participle;  
  
    private static Train trainInstance = new Train();  
  
    public static Train getInstance() {  
        trainInstance = new Train();  
  
        // 读取序列化在硬盘的本类对象  
        FileInputStream fis;  
        try {  
            File f = new File(SERIALIZABLE_PATH);  
            if (f.length() != 0) {  
                fis = new FileInputStream(SERIALIZABLE_PATH);  
                ObjectInputStream oos = new ObjectInputStream(fis);  
                trainInstance = (Train) oos.readObject();  
                trainInstance.participle = new Complex();  
            } else {  
                trainInstance = new Train();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return trainInstance;  
    }  
  
    private Train() {  
        this.participle = new Complex();  
    }  
  
    public String readtxt(String path) {  
        BufferedReader br = null;  
        StringBuilder str = null;  
        try {  
            br = new BufferedReader(new FileReader(path));  
  
            str = new StringBuilder();  
  
            String r = br.readLine();  
  
            while (r != null) {  
                str.append(r);  
                r = br.readLine();  
  
            }  
  
            return str.toString();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } finally {  
            if (br != null) {  
                try {  
                    br.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            str = null;  
            br = null;  
        }  
  
        return "";  
    }  
    
    /**
     * 去掉停用词
     * @param text 给定的文本
     * @return 去停用词后结果
     */
     public String[] DropStopWords(String[] oldWords)
     {
         Vector<String> v1 = new Vector<String>();
         for(int i=0;i<oldWords.length;++i)
         {
             if(StopWordsHandler.IsStopWord(oldWords[i])==false)
             {//不是停用词
                 v1.add(oldWords[i]);
             }
         }
         String[] newWords = new String[v1.size()];
         v1.toArray(newWords);
         return newWords;
     }
    
    /** 
     * 训练数据 
     * @throws IOException 
     */  
    public void realTrain() throws IOException {  
        // 初始化  
        classMap = new HashMap<String, String>();  
        classP = new HashMap<String, Integer>();  
        actCount.set(0);  
        classWordMap = new HashMap<String, Map<String, Double>>();  
        
        
        File rootFile = new File(trainPath);
        if(rootFile.isDirectory()){
        	File [] files = rootFile.listFiles();
        	for (int i = 0; i < files.length; i++) {
        		if(files[i].isDirectory())
        		 classMap.put(files[i].getName(), files[i].getName()); 
			}
        }
        // classMap.put("C000007", "汽车");  
//        classMap.put("C000008", "财经");  
//        classMap.put("C000010", "IT");  
//        classMap.put("C000013", "健康");  
//        classMap.put("C000014", "体育");  
//        classMap.put("C000016", "旅游");  
//        classMap.put("C000020", "教育");  
//        classMap.put("C000022", "招聘");  
//        classMap.put("C000023", "文化");  
//        classMap.put("C000024", "军事");  
  
        // 计算各个类别的样本数  
        Set<String> keySet = classMap.keySet();  
  
        // 所有词汇的集合,是为了计算每个单词在多少篇文章中出现，用于后面计算df  
        final Set<String> allWords = new HashSet<String>();  
  
        // 存放每个类别的文件词汇内容  
        final Map<String, List<String[]>> classContentMap = new ConcurrentHashMap<String, List<String[]>>();  
  
        Complex participle = new Complex();  
        for (String classKey : keySet) {  
        	System.out.println("train:"+classKey);
            Map<String, Double> wordMap = new HashMap<String, Double>();  
            File f = new File(trainPath + File.separator + classKey);  
            File[] files = f.listFiles(new FileFilter() {  
  
                @Override  
                public boolean accept(File pathname) {  
                    if (pathname.getName().endsWith(".txt")) {  
                        return true;  
                    }  
                    return false;  
                }  
  
            });  
  
            // 存储每个类别的文件词汇向量  
            List<String[]> fileContent = new ArrayList<String[]>();  
            if (files != null) {  
                for (File txt : files) {  
                    String content = readtxt(txt.getAbsolutePath());  
                    // 分词  
                    String[] word_arr = participle.segWords(content, " ").split(" "); 
                    word_arr = DropStopWords(word_arr);
                    fileContent.add(word_arr);  
                    // 统计每个词出现的个数  
                    for (String word : word_arr) {  
                        if (wordMap.containsKey(word)) {  
                            Double wordCount = wordMap.get(word);  
                            wordMap.put(word, wordCount + 1);  
                        } else {  
                            wordMap.put(word, 1.0);  
                        }  
                          
                    }  
                }  
            }  
  
            // 每个类别对应的词典和频数  
            classWordMap.put(classKey, wordMap);  
  
            // 每个类别的文章数目  
            classP.put(classKey, files.length);  
            actCount.addAndGet(files.length);  
            classContentMap.put(classKey, fileContent);  
  
        }  
  
          
  
          
  
        // 把训练好的训练器对象序列化到本地 （空间换时间）  
        FileOutputStream fos;  
        try {  
            fos = new FileOutputStream(SERIALIZABLE_PATH);  
            ObjectOutputStream oos = new ObjectOutputStream(fos);  
            oos.writeObject(this);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
  
    /** 
     * 分类 
     *  
     * @param text 
     * @return 返回各个类别的概率大小 
     * @throws IOException 
     */  
    public Map<String, Double> classify(String text) throws IOException {  
        // 分词，并且去重  
        String[] text_words = participle.segWords(text, " ").split(" "); 
        text_words = DropStopWords(text_words);
        Map<String, Double> frequencyOfType = new HashMap<String, Double>();  
        Set<String> keySet = classMap.keySet();  
        for (String classKey : keySet) {  
            double typeOfThis = 1.0;  
            Map<String, Double> wordMap = classWordMap.get(classKey);  
            for (String word : text_words) {  
                Double wordCount = wordMap.get(word);  
                int articleCount = classP.get(classKey);  
  
                /* 
                 * Double wordidf = idfMap.get(word); if(wordidf==null){ 
                 * wordidf=0.001; }else{ wordidf = Math.log(actCount / wordidf); } 
                 */  
  
                // 假如这个词在类别下的所有文章中木有，那么给定个极小的值 不影响计算  
                double term_frequency = (wordCount == null) ? ((double) 1 / (articleCount + 1))  
                        : (wordCount / articleCount);  
  
                // 文本在类别的概率 在这里按照特征向量独立统计，即概率=词汇1/文章数 * 词汇2/文章数 。。。  
                // 当double无限小的时候会归为0，为了避免 *10  
  
                typeOfThis = typeOfThis * term_frequency;  
                typeOfThis = ((typeOfThis == 0.0) ? Double.MIN_VALUE  
                        : typeOfThis);  
//                 System.out.println(typeOfThis+" : "+term_frequency+" :  "+actCount);  
            }  
  
            typeOfThis = ((typeOfThis == 1.0) ? 0.0 : typeOfThis);  
  
            // 此类别文章出现的概率  
            double classOfAll = classP.get(classKey) / actCount.doubleValue();  
  
            // 根据贝叶斯公式 $(A|B)=S(B|A)*S(A)/S(B),由于$(B)是常数，在这里不做计算,不影响分类结果  
            frequencyOfType.put(classKey, typeOfThis * classOfAll);  
        }  
  
        return frequencyOfType;  
    }  
  
    public void pringAll() {  
        Set<Entry<String, Map<String, Double>>> classWordEntry = classWordMap  
                .entrySet();
        for (Entry<String, Map<String, Double>> ent : classWordEntry) {  
            System.out.println("类别： " + ent.getKey());  
            Map<String, Double> wordMap = ent.getValue();  
            Set<Entry<String, Double>> wordMapSet = wordMap.entrySet();  
            for (Entry<String, Double> wordEnt : wordMapSet) {  
                System.out.println(wordEnt.getKey() + ":" + wordEnt.getValue());  
            }  
        }
    }  
  
    public Map<String, String> getClassMap() {  
        return classMap;  
    }  
  
    public void setClassMap(Map<String, String> classMap) {  
        this.classMap = classMap;  
    }  
    
    
    public static void main(String[] args) {
    	Train train = Train.getInstance();
    	// 训练，训练好模型之后序列化到磁盘就不用再次训练了
    	try {
//    		train.realTrain();
    		StringBuilder sb = new StringBuilder();
        	try {
        		InputStreamReader isReader =new InputStreamReader(new FileInputStream("E:\\doc\\class\\"+"新建文本文档.txt"),"UTF-8");
        		BufferedReader reader = new BufferedReader(isReader);
        		String aline;
        		while ((aline = reader.readLine()) != null)
        		{
        			sb.append(aline + " ");
        		}
        		isReader.close();
        		reader.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            
            String text = sb.toString();
			Map<String, Double> resultMap = train.classify(text);
			double max = 0.0;
	        String classWord = "";
			for (String key : resultMap.keySet()) {
				double v = resultMap.get(key);
				if(max<v){
            		max = v;
            		classWord = key;
            	}
				System.out.println(key+":"+v);
			}
			System.out.println("推测属于:[ "+classWord+" ]领域");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}