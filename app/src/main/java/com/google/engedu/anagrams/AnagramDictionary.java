/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;





public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private HashSet<String> wordsSet;
    private HashMap<String, ArrayList<String>> lettersToWords; //[cat]
    private Random random = new Random();
    private ArrayList<String> wordList;

    public AnagramDictionary(Reader reader) throws IOException {
        wordList = new ArrayList<String>();
        wordsSet = new HashSet<>();
        lettersToWords = new HashMap<>();
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordsSet.add(word);
            String sorted = sortLetters(word); // cat > act

//            if(lettersToWords.containsKey(sorted)){
//                lettersToWords.get(sorted).add(word);  // [cat,act]
//            }else{
//                ArrayList<String> al = new ArrayList<>();
//                al.add(word); // [cat]
//                lettersToWords.put(sorted, al);  //  [] , [act : [cat]]
//            }

            if (!lettersToWords.containsKey(sorted)) {
                lettersToWords.put(sorted, new ArrayList<String>());
            }
            lettersToWords.get(sorted).add(word);


        }
    }

    public boolean isGoodWord(String word, String base) {
        // wordset.contains(words) , move on
            // !word.(base)
        if(wordsSet.contains(word) && (!word.contains(base))){ //
            return true;
        }

        return false;
    }


    public String sortLetters(String word) {
        word = word.toLowerCase();
        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);
    }


    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        String sortedTargetWord = sortLetters(targetWord);

        int targetWordLength = targetWord.length();

        for(String word : wordList){
            if (word.length() != targetWordLength) {
                continue;
            }
            if(sortLetters(word).equals(sortedTargetWord)){
                result.add(word);
            }
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();


        for(int i = 0; i < 26; i++) {
            int letterIndex = i + 97;
            char extra = (char)i;
            String wordPlus = word + extra;

            String sortedWord = sortLetters(wordPlus);

            result.addAll(lettersToWords.get(sortedWord));


        }
/*
        for(int i = 97; i <= 122; i++){
            result.addAll(getAnagrams(word + (char) i));
        }
*/
        return result;
    }

    public String pickGoodStarterWord() {
        int size = wordList.size();
        int randomInt = (int)(Math.random() * size);
        for (int i = randomInt; i < size + randomInt; i++) {
            String word = wordList.get(i % size);
            String sorted = sortLetters(word);
            if (lettersToWords.get(sorted).size() >= MIN_NUM_ANAGRAMS) {
                return word;
            }
        }
        throw new Error();
    }
}
