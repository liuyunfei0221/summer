package com.blue.auth.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.base.BlueNumericalValue.*;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new security question
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class SecurityQuestionInsertParam implements Serializable, Asserter {

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
        if (isBlank(this.question))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "question can't be blank");
        int length = this.question.length();
        if (length < SEC_QUESTION_LEN_MIN.value || length > SEC_QUESTION_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "question length can't be less than " + SEC_QUESTION_LEN_MIN.value
                    + " or greater than " + SEC_QUESTION_LEN_MAX.value);

        if (isBlank(this.answer))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "answer can't be blank");
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
