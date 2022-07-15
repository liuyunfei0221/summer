package com.blue.auth.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new security question
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class SecurityQuestionInsertParam implements Serializable, Asserter {

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

    @Override
    public void asserts() {
        if (isBlank(this.question) || isBlank(this.answer))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "question or answer can't be blank");

        int length = this.question.length();
        if (length < SEC_QUESTION_LEN_MIN.value || length > SEC_QUESTION_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "question length can't be less than " + SEC_QUESTION_LEN_MIN.value
                    + " or greater than " + SEC_QUESTION_LEN_MAX.value);

        length = this.answer.length();
        if (length < SEC_ANSWER_LEN_MIN.value || length > SEC_ANSWER_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "answer length can't be less than " + SEC_ANSWER_LEN_MIN.value
                    + " or greater than " + SEC_ANSWER_LEN_MAX.value);
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
