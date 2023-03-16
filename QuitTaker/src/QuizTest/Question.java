/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author steam
 */
    public class Question {

        private String question;
        private String[] ans = new String[5];


        String getQuestion() {
            return question;
        }

        String getAns(int i) {
            if (i >= 5) {
                return ans[0];
            }
            return ans[i];
        }

        void setQuestion(String q) {
            this.question = q;
        }

        void setAns(String a, int i) {
            this.ans[i] = a;
        }
        
         

    }

  
