package com.e.trivia.data;

import com.e.trivia.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished (ArrayList<Question> questionArrayList);
}
