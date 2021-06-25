import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.lang.*;
import java.io.RandomAccessFile;
class HuffNode{
	int data;
	char c;
	HuffNode left;
	HuffNode right;
}
class MyComparator implements Comparator<HuffNode>{
	public int compare(HuffNode x, HuffNode y) {
		return x.data-y.data;
	}
}
class Functions{
	
	
	String getFile(String k) {
		int len=k.length(),ij=len-1;
		while(Character.isLetter(k.charAt(ij)) || k.charAt(ij)=='.') {
			ij--;
		}
		String n=k.substring(0,ij+1);
		return n;
		
	}
	
	
	String compress(String k) throws IOException {
		HashMap<Integer,Integer> hash=new HashMap<>();
		HuffNode root=new HuffNode();
		try {
			hash=count(k);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hash=sortByValue(hash);
		root=CreateTree(hash);
		
		HashMap<Character,String> hm=new HashMap<>();
		hm=printCode(root, "",hm);
		
		
		String n=getFile(k);
		FileOutputStream fos2;
		fos2 = new FileOutputStream(new File(n+"output.txt"));
	
		
		int b;String line;
		
		BufferedReader reader=new BufferedReader(new FileReader(k));
		while((line=reader.readLine())!=null) {
			String line2=new String(line.getBytes(),"UTF8");
			String a=Encode(line2,hm);
			while(a.length()%8 !=0) {
				a+="0";
			}
			fos2.write(getByteByString(a));
		}
		
		FileWriter fr = new FileWriter(n+"hashmap.txt");
		BufferedWriter be=new BufferedWriter(fr);
		for(char c:hm.keySet()) {
			be.write(c+":"+hm.get(c)+"\n");
		}
		be.close();
		
		return n+"output.txt";
		
		
	}
	HashMap<Integer,Integer> count(String name) throws IOException{
		HashMap<Integer,Integer> hash=new HashMap<>();
		BufferedReader reader =new BufferedReader(new FileReader(name));
		int n=0;
		while(true) {
			String line=reader.readLine();
			if(line==null) {
				break;
			}
			String line2=new String(line.getBytes(),"UTF8");
			for(int i=0;i<line2.length();i++) {
				char c=line2.charAt(i);
				int value=hash.getOrDefault((int)c,0);
				hash.put((int)c,value+1);
				System.out.println(hash);
			}
			n=n+1;
			hash.put((int)'\n',n);
			
		}
		reader.close();
		/*for(int key:hash.keySet()) {
			System.out.println((char)key +": "+hash.get(key));
		}*/
		return hash;
	}
	
	HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Integer> > list =
               new LinkedList<Map.Entry<Integer, Integer> >(hm.entrySet());
  
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() {
            public int compare(Map.Entry<Integer, Integer> o1, 
                               Map.Entry<Integer, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
          
        // put data from sorted list to hashmap 
        HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
	HuffNode CreateTree(HashMap<Integer,Integer> h) {
		PriorityQueue<HuffNode> q=new PriorityQueue<HuffNode>(h.size(),new MyComparator());
		for(int key:h.keySet()) {
			HuffNode hn=new HuffNode();
			hn.c=(char)key;
			//System.out.println(hn.c);
			hn.data=h.get(key);
			//System.out.println(hn.data);
			hn.left=null;
			hn.right=null;
			q.add(hn);
		}
		HuffNode root=null;
		while (q.size()>1) {
			HuffNode x=q.peek();
			q.poll();
			HuffNode y=q.peek();
			q.poll();
			HuffNode f= new HuffNode();
			f.data= x.data+ y.data;
			f.c=0;
			f.left =x;
			f.right=y;
			root=f;
			q.add(f);
			
		}
		return root;
		
	}
	
	HashMap<Character,String> printCode(HuffNode root,String s,HashMap<Character,String> hm) {
		if(root!=null &&root.left==null && root.right==null) {
			System.out.println(root.c+"  : "+s);
			hm.put(root.c, s);
			return hm;
		}
			printCode(root.left,s+"0",hm);
			printCode(root.right,s+"1",hm);
			return hm;
	}


	String Encode(String line,HashMap<Character,String> hm) {
		String res="";
		for(char c: line.toCharArray()) {
			res=res+hm.get(c);
		}
		res=res+hm.get('\n');
		return res;
	}

	public byte[] getByteByString(String binaryString) {
	    int splitSize = 8;

	    if(binaryString.length() % splitSize == 0){
	        int index = 0;
	        int position = 0;

	        byte[] resultByteArray = new byte[binaryString.length()/splitSize];
	        StringBuilder text = new StringBuilder(binaryString);

	        while (index < text.length()) {
	            String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
	            Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
	            resultByteArray[position] = byteAsInt.byteValue();
	            index += splitSize;
	            position ++;
	        }
	        return resultByteArray;
	    }
	    else{
	        System.out.println("Cannot convert binary string to byte[], because of the input length. '" +binaryString+"' % 8 != 0");
	        return null;
	    }
	}
	
		
	String Decompress(String k) throws IOException {
		String n=getFile(k);
		
		HashMap<String,Character> hm1=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(n+"\\hashmap.txt"));//
		String line;
		int i=0;
		String line2="";
		while((line=br.readLine())!=null) {
			//System.out.println(line);
			try {
			hm1.put(line.substring(2),line.charAt(0));
			}
			catch(Exception e) {
				if((line=br.readLine())!=null) {
					hm1.put(line.substring(1),'\n');
				}
					
			}
		}

		
		FileInputStream in=new FileInputStream(k);
		BufferedWriter  fos2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(n+"input.txt")),"UTF-8"));
		int c;
		String result,res;result=res="";
		while((c=in.read())!=-1) {
			String val=Integer.toBinaryString(c);
			while(val.length()<8)
				val="0"+val;
			result=result+val;
			i=0;
			int j=0;
			res="";
			while(i<result.length()) {
				res=res+result.charAt(i);
				if(hm1.containsKey(res)) {
					if(hm1.get(res)=='\n') {
						result="";
					}
					fos2.write(hm1.get(res));
					j=i+1;
					res="";
				}
				i++;
			}
			if(result!="")
			result=result.substring(j);
	}
		fos2.close();
		return n+"input.txt";

	}
	
}
  

public class Compress2 {
	  
	static String k="C:\\Users\\Kiran\\Desktop\\E3 SEM1\\kiran.txt";
	
	public static void main(String args[]) throws IOException {
		Functions f=new Functions();
		System.out.println(f.compress(k));
		System.out.println(f.Decompress("C:\\\\Users\\\\Kiran\\\\Desktop\\\\E3 SEM1\\\\output.txt"));
		
		
	}
}
