
/**
 * Write a description of TestVigenereBreaker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import edu.duke.*;

public class TestVigenereBreaker {
   
public void testSlicer() {

VigenereBreaker vb = new VigenereBreaker();
 
String sliced = vb.sliceString("abcdefghijklm", 1, 3);

System.out.println(sliced);
    
}
  
public void testDecrypt() {
FileResource fr = new FileResource();
String message = fr.asString();  
VigenereBreaker vb = new VigenereBreaker(); 
String decrypted = vb.breakVigenereKnown(message,38, "english");    
System.out.println(decrypted);
}
  
public void testbreakVigenereUnknownKey() {
FileResource fr = new FileResource();
String message = fr.asString();  
VigenereBreaker vb = new VigenereBreaker(false);
String decrypted = vb.breakVigenereUnknownLang(message);
System.out.println(decrypted);
}

public void testBreakKnownLength() {
FileResource fr = new FileResource();
String message = fr.asString();
VigenereBreaker vb = new VigenereBreaker(true);
String language = "english";
String decrypted = vb.breakVigenereKnown(message, 38, language); 
HashSet<String> dictionary = vb.getDictionary(language);
int num = vb.countWords (decrypted, dictionary);
System.out.println("valid words in " + language + " is " +num);
System.out.println(decrypted);
}

public void testMostCommonCharIn() {
String language = "french";
VigenereBreaker vb = new VigenereBreaker(true); 
HashSet<String> dictionary = vb.getDictionary(language);
char mostcommon = vb.mostCommonCharIn(dictionary);
System.out.println("Most common letter in " + language + " is " + mostcommon);    
}

public void testbreakVigenereUnknownLang() {
FileResource fr = new FileResource();
String message = fr.asString();    
VigenereBreaker vb = new VigenereBreaker(true);
String decrypted = vb.breakVigenereUnknownLang(message);
System.out.println(decrypted);
    
}

public void testBreakVigenereKnownLang(){
FileResource fr = new FileResource();
String message = fr.asString();    
VigenereBreaker vb = new VigenereBreaker(true);
String decrypted = vb.breakVigenereKnownLang(message, "german");
System.out.println(decrypted);
}
}
