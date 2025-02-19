package com.codesoom.assignment.service;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Task Service의")
class TaskServiceTest {

    public static final String TASK_TITLE = "Test Title";
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new TaskRepository();
        taskService = new TaskService(taskRepository);
        Task task = new Task(1L, TASK_TITLE);
        taskService.createTask(task);
    }

    @Nested
    @DisplayName("getTaskList 메소드는")
    class Describe_getTaskList {
        @Test
        @DisplayName("할 일 리스트를 조회한다.")
        void getTasks() {
            assertThat(taskService.getTaskList()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {
        @Nested
        @DisplayName("유효한 Id를 조회하면")
        class Context_with_task {
            @Test
            @DisplayName("할 일을 찾는다.")
            void getTaskWithValidId() {
                assertThat(taskService.getTask(1L).getTitle()).isEqualTo(TaskServiceTest.TASK_TITLE);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 Id를 조회하면")
        class Context_with_task_not_valid_id {
            @Test
            @DisplayName("TaskNotFound 예외를 던진다.")
            void getTaskWithInValidId() {
                assertThatThrownBy(() -> taskService.getTask(100000L))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {

        int oldSize;
        int newSize;
        Task newTask;

        @BeforeEach
        void createSetUp() {
            oldSize = taskService.getTaskList().size();
            newTask = new Task(2L, "second");
        }

        @Test
        @DisplayName("새로운 할 일을 등록한다.")
        void createTask() {
            taskService.createTask(newTask);
            newSize = taskService.getTaskList().size();

            Task findNewTask = taskService.getTask(2L);

            assertThat(newSize - oldSize).isEqualTo(1);
            assertThat(findNewTask).isNotNull();
            assertThat(findNewTask.getId()).isEqualTo(newTask.getId());
            assertThat(findNewTask.getTitle()).isEqualTo(newTask.getTitle());
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {

        String newTitle;
        Task source;

        @BeforeEach
        void updateSetUp() {
            newTitle = "New Title";
            source = new Task(null, newTitle);
        }

        @Test
        @DisplayName("할 일을 수정한다.")
        void updateTask() {
            taskService.updateTask(1L, source);
            Task task = taskService.getTask(1L);
            assertThat(task.getTitle()).isEqualTo(newTitle);
        }
    }
}
