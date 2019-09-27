package com.ooftf.demo

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

import java.util.concurrent.ConcurrentHashMap

class TimeListener implements TaskExecutionListener, BuildListener {
    private times = []
    Map<String,Long> startMap = new ConcurrentHashMap<>()
    @Override
    void beforeExecute(Task task) {
        printf("beforeExecute::"+task.getName())
        startMap.put(task.getName(),System.currentTimeMillis())
    }

    @Override
    void afterExecute(Task task, TaskState taskState) {
        printf("afterExecute::"+task.getName())
        def ms = System.currentTimeMillis() - startMap.get(task.getName())
        times.add([ms, task.path])
        task.project.logger.warn "${task.path} spend ${ms}ms"
    }

    @Override
    void buildFinished(BuildResult result) {
        println "Task spend time:"
        for (time in times) {
            if (time[0] >= 50) {
                printf "%7sms  %s\n", time
            }
        }
    }

    @Override
    void buildStarted(Gradle gradle) {}

    @Override
    void projectsEvaluated(Gradle gradle) {}

    @Override
    void projectsLoaded(Gradle gradle) {}

    @Override
    void settingsEvaluated(Settings settings) {}
}