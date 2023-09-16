package http;

import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest<T extends TaskManagerTest<HTTPTaskManager>> {
    private KVServer server;
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            manager = Managers.getDefault(historyManager);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = new Task("description1", "name1", Status.NEW, Instant.now(), 1);
        Task task2 = new Task("description2", "name2", Status.NEW, Instant.now(), 2);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllTasks(), list);
    }

    @Test
    public void shouldLoadEpics() {
        Epic epic1 = new Epic("description1", "name1", Status.NEW, Instant.now(), 3);
        Epic epic2 = new Epic("description2", "name2", Status.NEW, Instant.now(), 4);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllEpics(), list);
    }

    @Test
    public void shouldLoadSubtasks() {
        Epic epic1 = new Epic("description1", "name1", Status.NEW, Instant.now(), 5);
        Subtask subtask1 = new Subtask("description1", "name1", Status.NEW, epic1.getId()
                , Instant.now(), 6);
        Subtask subtask2 = new Subtask("description2", "name2", Status.NEW, epic1.getId(),
                Instant.now(), 7);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllSubtasks(), list);
    }

}