package com.gym.app.examstuff;

import java.util.List;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

public interface ExamView {

    void showTasks(List<Task> tasks);

    void showProgress();

    void showError(Throwable error);

    void showConnected(boolean conencted);

    void deleteSuccess(int id);

    void deleteError(int id, Throwable e);
}
