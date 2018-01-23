package com.gym.app.examstuff;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

public class ExamResponse {

    @SerializedName("tasks")
    public List<Task> mTasks;
    @SerializedName("page")
    public int mPage;
}
