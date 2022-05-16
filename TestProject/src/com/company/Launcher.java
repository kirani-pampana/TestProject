package com.company;

public class Launcher {

    public static void main (String[] args) {
        String checkString = args[0];
        Boolean status = validateString(checkString);

        System.out.println("Is parenthesis in given String are properly aligned ? " + status);
    }

    static  Boolean  validateString (String checkString) {
        int len = checkString.length();
        int leftParenthesisCnt = 0;
        int rightParenthisisCnt = 0;

        for (int i=0; i < len ; i++) {
            if (checkString.charAt(i) == '(' ) leftParenthesisCnt++;
            if (checkString.charAt(i) == ')' ) rightParenthisisCnt++;
        }

        if (leftParenthesisCnt == rightParenthisisCnt) return true;
        else return false;


    }
}
