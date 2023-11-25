package example.demodic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class Dictionary {
    private static Hashtable<String, Word> dictionary = new Hashtable<>();
    private static List<String> listWord = new ArrayList<>();

    public static void insertFromFile(String path) throws FileNotFoundException {
        //
        dictionary.clear();
        listWord.clear();
        //
        File file = new File(path);
        Scanner sc = new Scanner(file);
        String wordTarget = new String();
        StringBuilder wordDescription= new StringBuilder();
        Word word = new Word();
        boolean check = false;
        while (sc.hasNext()) {
            String string = sc.nextLine();
            if (string.contains("@")) {
                //da duoc add vao roi:
                if (dictionary.get(wordTarget) != null) {
                    check = false;
                }
                if (check) {
                    //add new word:
                    word.setDescription(wordDescription.toString());
                    dictionary.put(wordTarget, word);
                    listWord.add(wordTarget);
                } else {
                    //thiet lap tu moi:
                    check = true;
                    //
                }
                //reset
                wordTarget = new String();
                wordDescription = new StringBuilder();
                word = new Word();
                //
                String[] split = string.split(" /", 2);
                //
                wordTarget = split[0].substring(1);
                word.setWord(wordTarget);
                wordDescription.append(string).append("\n");
            } else {
                wordDescription.append(string).append("\n");
            }
        }
        sc.close();
    }

    private static String getOpenTag(int val) {
        String openTag = new String();

        switch (val) {
            case 1: // word
                openTag = "<u><b><font size = \"5px\"><font color = \"#dd2c00\"><font face = \"Helvetica\">";
                break;
            case 2: // pronounce
                openTag = "<font size = \"4px\"><font color = \"#dd2c00\"><font face = \"Verdana\">";
                break;
            case 3: // part of speech, startWith("*  ")
                openTag = "<u><b><font size = \"4px\"><font color = \"#dd2c00\"><font face = \"Arial\">";
                break;
            case 4: // meaning, startWith("- ")
                openTag = "<font size = \"4px\"><font color = \"#dd2c00\">&#11031;&nbsp;<font face = \"Arial\">";
                break;
            case 5: // example, starWith("= ");
                openTag = "<font size = \"4px\"><font color = \"#b71c1c\"><font face = \"Arial\">&ensp;";
                break;
            case 6: // meaning of ex ("+")
                openTag = "<font size = \"4px\"><font color = \"#e53935\"><font face = \"Arial\">&ensp;";
                break;
            case 7: // meaning of ("!"), thanh ngu tuc ngu
                openTag = "<font size = \"4px\"><font color = \"#e53935\">&emsp;&#129082;<font face = \"Arial\">";
                break;
            case 8: // thanh ngu tuc ngu (!)
                openTag = "<font size = \"4px\"><font color = \"#b71c1c\">&ensp;&#11208;<font face = \"Arial\">";
        }

        return openTag;
    }

    public static String getStyle() {
        StringBuilder style = new StringBuilder();

        style.append("<style>\n");
        //underLine:
        style.append("u {\n");
        style.append("text-underline-offset: 2px;\n");
        style.append("text-decoration-color: black;\n");
        style.append("}\n");
        //lineHeight:
        style.append("font {\n");
        style.append("line-height: 1.4;\n");
        style.append("}\n");
        //color:
        style.append("body {\n");
        style.append("background-color: ffe668;\n");
        style.append("border: solid;\n");
        style.append("border-color: dd2c00;\n");
        style.append("border-radius: 10px;\n");
        style.append("padding: 20px;\n");
        style.append("}\n");
        //webView:
        style.append("::-webkit-scrollbar {\n");
        style.append("background-color:#FFE668;\n");
        style.append("width: 10px;\n");
        style.append("}\n");

        style.append("::-webkit-scrollbar-track {\n");
        style.append("background-color:#FFE668;\n");
        style.append("border-radius: 0em;\n");
        style.append("border-radius:2em;\n");
        style.append("margin-top:8px;\n");
        style.append("margin-bottom: 8px;\n");
        style.append("}\n");

        style.append("::-webkit-scrollbar-thumb {\n");
        style.append("background-color:dd2c00;\n");
        style.append("border-radius: 2em;\n");
        style.append("}\n");

        style.append("::-webkit-scrollbar-thumb:hover {\n");
        style.append("background: fa7d00;\n");
        style.append("}\n");

        style.append("</style>\n");

        return style.toString();
    }

    private static String getCloseTag(int val) {
        String closeTag = new String();

        switch (val) {
            case 1, 3:
                closeTag = "</font></font></font></b></u>";
                break;
            case 2:
                closeTag = "</font></font></font>";
                break;
            case 4, 5, 6, 7, 8:
                closeTag = "</font></font></font>\n<br>\n";
                break;
        }

        return closeTag;
    }

    public static String convertToHtml(String word) {
        if (word == null) {
            return new String();
        }
        String wordDescription = dictionary.get(word).getDescription();
        //Scanner read from String word:
        Scanner scanner = new Scanner(wordDescription);
        //StringBuilder html build a String to VebView:
        StringBuilder html = new StringBuilder();
        //check isAddingMeaningOf (!)
        boolean isAddingMeaningOfProverb = false;
        //
        html.append(getStyle());
        //
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.contains("@")) {
                //split word and pronounce
                String[] splitArr = line.split(" /", 2);
                String wordTarget = splitArr[0].substring(1);
                String pronounce = null;
                //check containPronounce
                if (splitArr.length > 1) {
                    pronounce = " /" + splitArr[1];
                }
                //add wordTarget to html
                html.append(getOpenTag(1));
                html.append(wordTarget);
                html.append(getCloseTag(1));
                //add pronounce to html
                if (pronounce != null) {
                    html.append("\n");
                    html.append(getOpenTag(2));
                    html.append(pronounce);
                    html.append(getCloseTag(2));
                }
                // endLine
                html.append("\n").append("<br>").append("\n");
                //
                isAddingMeaningOfProverb = false;
                //
            } else if (line.startsWith("*  ")) {
                html.append(getOpenTag(3));
                html.append(line.substring(3));
                html.append(getCloseTag(3));
                html.append("\n").append("<br>").append("\n");
                //
                isAddingMeaningOfProverb = false;
                //
            } else if (line.startsWith("- ")) {
                if (isAddingMeaningOfProverb) {
                    html.append(getOpenTag(7));
                    html.append(line.substring(2));
                    html.append(getCloseTag(7));
                } else {
                    html.append(getOpenTag(4));
                    html.append(line.substring(2));
                    html.append(getCloseTag(4));
                }
            } else if (line.startsWith("!")) {
                html.append(getOpenTag(8));
                html.append(line.substring(1));
                html.append(getCloseTag(8));
                //
                isAddingMeaningOfProverb = true;
                //
            } else if (line.startsWith("=")) {
                String[] splitArr = line.substring(1).split("\\+ ", 2);
                html.append(getOpenTag(5));
                html.append(splitArr[0]);
                html.append(getCloseTag(5));
                if (splitArr.length > 1) {
                    html.append(getOpenTag(6));
                    html.append(splitArr[1]);
                    html.append(getCloseTag(6));
                }
            }
        }

        return html.toString();
    }

    public static String convertToHtmlPreview(String word) {
        if (word == null) {
            return new String();
        }
        String wordDescription = word;
        //Scanner read from String word:
        Scanner scanner = new Scanner(wordDescription);
        //StringBuilder html build a String to VebView:
        StringBuilder html = new StringBuilder();
        //check isAddingMeaningOf (!)
        boolean isAddingMeaningOfProverb = false;

        //
        html.append(getStyle());
        //
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.contains("@")) {
                //split word and pronounce
                String[] splitArr = line.split(" /", 2);
                String wordTarget = splitArr[0].substring(1);
                String pronounce = null;
                //check containPronounce
                if (splitArr.length > 1) {
                    pronounce = " /" + splitArr[1];
                }
                //add wordTarget to html
                html.append(getOpenTag(1));
                html.append(wordTarget);
                html.append(getCloseTag(1));
                //add pronounce to html
                if (pronounce != null) {
                    html.append("\n");
                    html.append(getOpenTag(2));
                    html.append(pronounce);
                    html.append(getCloseTag(2));
                }
                // endLine
                html.append("\n").append("<br>").append("\n");
                //
                isAddingMeaningOfProverb = false;
                //
            } else if (line.startsWith("*  ")) {
                html.append(getOpenTag(3));
                html.append(line.substring(3));
                html.append(getCloseTag(3));
                html.append("\n").append("<br>").append("\n");
                //
                isAddingMeaningOfProverb = false;
                //
            } else if (line.startsWith("- ")) {
                if (isAddingMeaningOfProverb) {
                    html.append(getOpenTag(7));
                    html.append(line.substring(2));
                    html.append(getCloseTag(7));
                } else {
                    html.append(getOpenTag(4));
                    html.append(line.substring(2));
                    html.append(getCloseTag(4));
                }
            } else if (line.startsWith("!")) {
                html.append(getOpenTag(8));
                html.append(line.substring(1));
                html.append(getCloseTag(8));
                //
                isAddingMeaningOfProverb = true;
                //
            } else if (line.startsWith("=")) {
                String[] splitArr = line.substring(1).split("\\+ ", 2);
                html.append(getOpenTag(5));
                html.append(splitArr[0]);
                html.append(getCloseTag(5));
                if (splitArr.length > 1) {
                    html.append(getOpenTag(6));
                    html.append(splitArr[1]);
                    html.append(getCloseTag(6));
                }
            }
        }

        return html.toString();
    }

    public static void exportToFile(String path) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file);
        for (int i = 0; i < listWord.size(); i++) {
            fileWriter.write(getWord(listWord.get(i)));
        }
        fileWriter.write("@");
        fileWriter.close();
    }

    public static boolean containWord(String word) {
        if (word == null) {
            return false;
        }
        if (listWord.contains(word)) {
            return true;
        }
        return false;
    }
    public static boolean insertNewWord(String word, String description) {
        if (containWord(word)) {
            return false;
        }
        //
        listWord.add(word);
        Word newWord = new Word();
        newWord.setWord(word);
        newWord.setDescription(description);
        dictionary.put(word, newWord);
        return true;
    }

    public static boolean fixWord(String word, String description) {
        if (!containWord(word)) {
            return false;
        }
        Word newWord = new Word();
        newWord.setWord(word);
        newWord.setDescription(description);
        dictionary.put(word, newWord);
        return true;
    }

    public static String convertToTextForm(String line, int val) {
        String text = new String();

        switch (val) {
            case 1: //word
                text = "@" + line;
                break;
            case 2: //loai tu
                text = "*  " + line;
                break;
            case 3: // nghia
                text = "- " + line;
                break;
            case 4: // vidu;
                text = "=" + line;
        }

        return text;
    }

    public static boolean removeWord(String word) {
        if (!containWord(word)) {
            return false;
        }
        listWord.remove(word);
        dictionary.remove(word);
        return true;
    }

    public static boolean backUp() {
        dictionary.clear();
        listWord.clear();
        try {
            insertFromFile("back_up_data\\dictionary_back_up.txt");
            exportToFile(UserManagement.getCurrentUser().getFilePath());
        } catch (FileNotFoundException f) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean loadToNewUserData(String path) {
        dictionary.clear();
        listWord.clear();
        try {
            insertFromFile("back_up_data\\dictionary_back_up.txt");
            exportToFile(path);
        } catch (FileNotFoundException f) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String getWord(String word) {
        return dictionary.get(word).getDescription();
    }

    public static ArrayList<String> findRecommendWord(String key) {
        ArrayList<String> sameWord = new ArrayList<>();

        if (key.length() == 0 || key == null) {
            return sameWord;
        }

        for (int i = 0; i < listWord.size(); i++) {
            if (listWord.get(i).indexOf(key) == 0) {
                sameWord.add(listWord.get(i));
            }
        }

        return sameWord;
    }

}
