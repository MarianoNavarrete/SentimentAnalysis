import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

/*
 * SD2x Homework #3
 * Implement the methods below according to the specification in the assignment description.
 * Please be sure not to change the method signatures!
 */
public class Analyzer {
	

	public static List<Sentence> readFile(String filename) throws IOException {
		File archivo = null;  
		FileReader fr = null;
	    BufferedReader br = null;
	    if(filename==null) {
	    	List<Sentence> n= new LinkedList<Sentence>();
	    	return n;
	    }
	    archivo = new File (filename);
	    try {
        fr = new FileReader (archivo);
        br = new BufferedReader(fr);
        } catch(Exception e) {
        	List<Sentence> n= new LinkedList<Sentence>();
	    	return n;
        }
        String linea;
        int number = 0;
        String phrase = "";
        List<Sentence> phrases = new LinkedList<Sentence>();
        if(br.readLine()==null) {
        	return phrases;
        }
        while((linea=br.readLine())!=null) {
        	if(linea!=null) {
	        	if(linea.substring(0,1).equals("-")) {
	        		if(isFormated(linea,0)) {
	        			number = Integer.parseInt(linea.substring(0, 2));
		        		phrase = linea.substring(2,linea.length()-1);
		        		phrase = phrase.replaceFirst("^\\s+", "");
		        		Sentence obj = new Sentence(number, phrase );
		            	phrases.add(obj);
	        		}
	        	} else {
	        		if(isFormated(linea,1))  {
	        			number = Integer.parseInt(linea.substring(0, 1));	
		        		phrase = linea.substring(1,linea.length()-1);
		        		phrase = phrase.replaceFirst("^\\s+", "");
		        		Sentence obj = new Sentence(number, phrase );
		            	phrases.add(obj);
	        		}
	        	}
        	}
        	
        }
        
		return phrases; 

	}
	public static boolean isFormated(String phrase, int version) {
		boolean isTrue = false;
		if(version == 0) {
        	if(phrase.substring(0,2).matches("-?\\d+(\\.\\d+)?")) {
        		int number = Integer.parseInt(phrase.substring(0,2));
        		if(number >=-2 && number <=2) {
        			if(phrase.substring(2,3).equals(" ")) {
        				return true;
        			}
        		}
        	}
			
		} else {
			if(phrase.substring(0,1).matches("-?\\d+(\\.\\d+)?")) {
        		int number = Integer.parseInt(phrase.substring(0,2));
        		if(number >=-2 && number <=2) {
        			if(phrase.substring(1,2).equals(" ")) {
        				return true;
        			}
        		}
        	}
		}
		return isTrue;
	}
	
	/*
	 * Implement this method in Part 2
	 */
	public static Set<Word> allWords(List<Sentence> sentences) {
		if(sentences==null) {
			Set<Word> empty= new HashSet<Word>();
			return empty;
		} else if(sentences.isEmpty()) {
			Set<Word> empty= new HashSet<Word>();
			return empty;
		}
		//LinkedList<Word> words = new LinkedList<Word>();
		HashMap<String, Word> words= new HashMap<String, Word>();
		for(Sentence phrase: sentences) {
			if(phrase!=null) {
				String[] separatePhrase = phrase.text.split(" ");
				for(String word: separatePhrase) {
					word = formatLetter(word);
					if(!words.containsKey(word)) {
						Word obj = new Word(word);
						obj.increaseTotal(phrase.getScore());
						words.put(word, obj);
					} else {
						Word obj = words.get(word);
						obj.increaseTotal(phrase.score);
						words.put(word, obj);
					}
				}
			}
		}
		Set<Word> wordsSet = new HashSet<Word>(words.values());
		return wordsSet;
	}
	/*
	 *Format the letters, remove extra characters and put to lowercase 
	 */
	public static String formatLetter(String word) {
		String format = word;
		if(word.contains("'")) {
			format = word.substring(0,word.indexOf("'"));
		}
		format = format.toLowerCase();
		return format;
	}
	
	/*
	 * Implement this method in Part 3
	 */
	public static Map<String, Double> calculateScores(Set<Word> words) {
		Map<String, Double> scores = new HashMap<String, Double>();
		if(words==null) {
			return scores;
		}else if(words.isEmpty()) {
			return scores;
		}
		for(Word oneWord: words) {
			if(oneWord!=null) {
				scores.put(oneWord.getText(), oneWord.calculateScore());
			}
		}
		
		return scores; // this line is here only so this code will compile if you don't modify it

	}
	
	/*
	 * Implement this method in Part 4
	 */
	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {
		if(sentence==null) {
			return 0;
		}
		if(wordScores==null) {
			return 0;
		}else if(wordScores.isEmpty()) {
			return 0;
		}
		String[] separateSentece = sentence.split("");
		double average = 0;
		for(String word: separateSentece) {
			word = formatLetter(word);
			if(!word.substring(0,1).matches("[a-zA-Z]")) {
				return 0;
			}
			if(wordScores.containsKey(word)) {
				average+=wordScores.get(word);
			}
		}
		return average; 

	}
	
	/*
	 * This method is here to help you run your program. Y
	 * You may modify it as needed.
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Please specify the name of the input file");
			System.exit(0);
		}
		String filename = args[0];
		System.out.print("Please enter a sentence: ");
		Scanner in = new Scanner(System.in);
		String sentence = in.nextLine();
		in.close();
		List<Sentence> sentences = Analyzer.readFile(filename);
		Set<Word> words = Analyzer.allWords(sentences);
		Map<String, Double> wordScores = Analyzer.calculateScores(words);
		allWords(sentences);

		double score = Analyzer.calculateSentenceScore(wordScores, sentence);
		System.out.println("The sentiment score is " + score);
	}
}
