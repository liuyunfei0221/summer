package com.blue.auth.model;

import java.io.Serializable;

/**
 * params for insert a new security question
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class SecurityQuestionInsertParam implements Serializable {

    private static final long serialVersionUID = 7060883955532299247L;

    /**
     * question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

    public SecurityQuestionInsertParam() {
    }

    public SecurityQuestionInsertParam(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "SecurityQuestionInsertParam{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

}
