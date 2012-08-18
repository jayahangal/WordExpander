import edu.smu.tspell.wordnet.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * java SentimentBuilder airplane
 */
public class SentimentBuilder
{
	static HashMap<String, Integer> ranks = new HashMap<String, Integer>(); 
	static Collection<String> second_level = new HashSet<String>();
 	static Comparator<String> SYN_ORDER = new Comparator<String>() {
                public int compare (String s1, String s2) {
                   int s1_rank = second_level.contains(s1) ? 2 : 1;
                   int s2_rank = second_level.contains(s2) ? 2 : 1;
		   System.out.println("s1-s2:" + (s1_rank - s2_rank));
                   return (s1_rank - s2_rank);
                }

                public boolean equals(Object obj) {
                        return true;
                }
        };
	public static void main(String[] args)
	{
		Collection<String> syns;
		if (args.length == 0)
		{
			System.err.println("You must specify " +
					"a word form for which to retrieve synsets.");
			System.exit(1);
		}
		//  Concatenate the command-line arguments
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
				buffer.append((i > 0 ? " " : "") + args[i]);
		}
		String wordForm = buffer.toString();
		syns = getSyns(wordForm);

		System.out.println("Original list of synonyms:\n" + syns);
		if (syns.size() > 0) {
		     Iterator<String> iter1 = syns.iterator();
		     while (iter1.hasNext()) {	
			String synWord = iter1.next();
		     	Collection<String> synsyns = getSyns(synWord);
			//System.out.println("synsyns :" + synsyns);
		     	/*Iterator<String> iter2 = synsyns.iterator();
		     	while (iter2.hasNext()) {	
			String synWord2 = iter2.next();
		     	    Set<String> syns3 = getSyns(synWord2);
			    //System.out.println("synsyns for word:" + synWord + " : " + synsyns);
				if (!syns3.contains(wordForm)) { //remove from the set
					iter2.remove();
					System.out.println("Removing synonym:" + synWord2);
				}
			} */
			second_level.addAll(synsyns);
			//System.out.println("synsyns added:" + synsyns);
		    }
		    Collection syns_all = new ArrayList(syns);
		    //Collection seconds = new ArrayList(second_level);
		    //syns_all.addAll(seconds);
		    Iterator<String> iter = second_level.iterator();
		    while(iter.hasNext()) {
			String syn = iter.next();
			if (!syns_all.contains(syn)) {
			    syns_all.add(syn);
			}
		    }
			
		    System.out.println("\nExpanded list of synonyms:\n" + syns_all);
		}
	}

        static Collection getSyns (String wordForm) {
		Collection<String> sn = new HashSet<String>();
			WordNetDatabase database = WordNetDatabase.getFileInstance();
			Synset[] synsets = database.getSynsets(wordForm);
			//  Display the word forms and definitions for synsets retrieved
			if (synsets.length > 0)
			{
			/*	System.out.println("The following synsets contain '" +
						wordForm + "' or a possible base form " +
						"of that text:");
			*/
				for (int i = 0; i < synsets.length; i++)
				{
					String[] wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++)
					{
						sn.add(wordForms[j]);
					}
				}
				//System.out.println("synonyms:" + sn);
			} else {
				System.err.println("No synsets exist that contain " +
						"the word form '" + wordForm + "'");
			}
		return sn;
	}

        static class SynCompartor implements Comparator<String> {
		public int compare (String s1, String s2) {
		   int s1_rank = second_level.contains(s1) ? 2 : 1;
		   int s2_rank = second_level.contains(s2) ? 2 : 1;
		   return (s1_rank - s2_rank);
		}
		
		public boolean equals(Object obj) {
			return true;
		}
	}

}
