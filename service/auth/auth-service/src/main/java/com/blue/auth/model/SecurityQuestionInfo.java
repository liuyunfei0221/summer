package com.blue.auth.model;

import java.io.Serializable;

/**
 * security question info for rest
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class SecurityQuestionInfo implements Serializable {

    private static final long serialVersionUID = -4932066899508751611L;

    /**
     * question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

    public SecurityQuestionInfo() {
    }

    public SecurityQuestionInfo(String question, String answer) {
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
        return "SecurityQuestionInfo{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

}
