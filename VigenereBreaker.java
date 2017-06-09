import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
private String[] languages;
private HashMap<String, HashSet<String>> dictionaries;
private HashMap<Integer, String> decryptedByLang;
private char[] mostcommon;
private String alphabet;

public VigenereBreaker() {
alphabet = "abcdefghijklmnopqrstuvwxyz"; 
languages = new String[] {"English"};
mostcommon = new char[languages.length];
decryptedByLang = new HashMap<Integer, String>();
dictionaries = new HashMap<String, HashSet<String>>();
readDictionary();
}

public VigenereBreaker(boolean allLanguages) {
alphabet = "abcdefghijklmnopqrstuvwxyz"; 
if(allLanguages==true) {languages = new String[] {"Danish", "Dutch", "English", "French", "German", "Italian", "Portuguese", "Spanish"};} else {languages = new String[] {"English"};}
mostcommon = new char[languages.length];
decryptedByLang = new HashMap<Integer, String>();
dictionaries = new HashMap<String, HashSet<String>>();
readDictionary();
}

 
public String sliceString(String message, int whichSlice, int totalSlices) {
StringBuilder SB = new StringBuilder();

for(int k=whichSlice; k < message.length(); k+= totalSlices){
            char chr = message.charAt(k);
            SB.append(chr);
                       }
String returnST = SB.toString();  
return returnST;
    }

public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for(int i=0; i < klength; i++)
        {String slice = sliceString(encrypted, i, klength);
        key[i] = cc.getKey(slice);
               
    }

    return key;
}
 
private int getLangNo(String language) {
int langNo = -1;
language = language.toLowerCase();
for (int i=0; i < languages.length; i++){
String currentlanguage = languages[i].toLowerCase();
if (language.equals(currentlanguage)) {langNo = i;}   
}
return langNo;   
    
}

   
public String breakVigenereKnown (String message, int klength, String language) {
    int langnum = getLangNo(language);
    if(langnum!=-1){
    int[] key = tryKeyLength(message, klength, mostcommon[langnum]);
    System.out.print("Key is ");
    for (int i=0; i < key.length; i++)
    {System.out.print(key[i]+" ");}
    VigenereCipher vc = new VigenereCipher(key);
    String decrypted = vc.decrypt(message);
    return decrypted;}
    else {String error = "error"; return error;}
    }

public String breakVigenereUnknownLang (String message) {
String decrypted = breakForAllLanguage (message);
return decrypted;
    }
  
public String breakVigenereKnownLang (String message, String language) {
int langnum = getLangNo(language);
if(langnum!=-1) {
String decrypted = breakForLanguage(message, langnum);
return decrypted;}
else {String error = "error"; return error;}

}
    
private HashSet<String> addDictionary(FileResource langfile) { 
HashSet<String> dict = new HashSet<String>();
for (String line : langfile.lines()) {
dict.add(line.toLowerCase()); }
return dict;    
}
    
private void readDictionary() {
for (int i = 0; i < languages.length; i++) {
    FileResource langfile = new FileResource("dictionaries/"+languages[i]);
    System.out.println("Now now loading " +languages[i] + " dictionary");
    HashSet<String> langdictionary = addDictionary(langfile);    
    dictionaries.put(languages[i],langdictionary);
    HashSet<String> dictionary = getDictionary(languages[i]);
    mostcommon[i] = mostCommonCharIn(dictionary);
}        
}

public int countWords (String message, HashSet<String> dictionary) {
int count = 0;    
String[] tester = message.split("\\W+");
for (int i = 0; i < tester.length; i++) {
if(dictionary.contains(tester[i].toLowerCase())) {count +=1;}
}

return count;
}

private String breakForLanguage (String encrypted, int language){
HashSet<String> dictionary = dictionaries.get(languages[language]);
int[] keyCheck = new int[101];
String[] decrypted = new String[101];
for (int i=1; i < 101; i++) {
int[] keyi = tryKeyLength(encrypted, i, mostcommon[language]);
VigenereCipher vc = new VigenereCipher(keyi);
decrypted[i] = vc.decrypt(encrypted);
keyCheck[i] = countWords (decrypted[i], dictionary);
}
int max = 0;
int maxkey = 0;
for (int i=0; i < keyCheck.length; i++) {
if (keyCheck[i] > max) {max = keyCheck[i]; maxkey = i;}
}
System.out.println("valid words is " +max);
System.out.println("keylength is " + maxkey);
return decrypted[maxkey];   
}

private String breakForAllLanguage (String encrypted) {
HashMap<Integer, Integer> langwords = new HashMap<Integer, Integer>();
for (int i = 0; i < languages.length; i++)
{int[] keyCheck = new int[101];
String[] decrypted = new String[101];
HashSet<String> dictionary = dictionaries.get(languages[i]); 
System.out.println("Now checking " +languages[i]);
String decrypt = breakForLanguage (encrypted, i);
decryptedByLang.put(i, decrypt);
int max = 0;
int maxkey = 0;
int langscore = countWords(decrypt, dictionary);
langwords.put(i, langscore);
}
int bestlang = 0;
int max = 0;
for (Integer s : langwords.keySet()) {
int langnum = langwords.get(s);
if (langnum > max) {max = langnum; bestlang = s;}
}

System.out.println("Best language is " +languages[bestlang]);
System.out.println("Most valid words is " + langwords.get(bestlang));
HashSet<String> bestdictionary = dictionaries.get(languages[bestlang]); 
char bestmostcommon = mostcommon[bestlang];
String bestdecrypt = decryptedByLang.get(bestlang);
return bestdecrypt;
}

public HashSet<String> getDictionary(String language){
int langnum = getLangNo(language);
if(langnum!=-1){
HashSet<String> dictionary = dictionaries.get(languages[langnum]);
return dictionary; }
else {
HashSet<String> dictionary = dictionaries.get(languages[0]);
System.out.println("Language not found, returning " + languages[0] + " dictionary instead");
return dictionary; }
    }

public char mostCommonCharIn(HashSet<String> dictionary) {
int[] counts = new int[26];
for (String s : dictionary) {
for (int i = 0; i < s.length(); i ++) {
char l = s.charAt(i);
int letterpos = alphabet.indexOf(l);
if (letterpos!=-1) {counts[letterpos] +=1;}
}  }

int max = 0;
int maxlet = 0;
for (int i=0; i < counts.length; i++) {
if (counts[i] > max) {max = counts[i]; maxlet = i;}    
}

char mostcommon = alphabet.charAt(maxlet);
return mostcommon;
 
}}
