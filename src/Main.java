import adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import http.KVServer;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        // Спринт 8
        KVServer server;
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            TaskManager httpTaskManager = Managers.getDefault(historyManager);

            Task task1 = new Task(
                    "Разработать лифт до луны", "Космолифт",
                    Status.NEW,
                    Instant.now(),
                    1
            );
            httpTaskManager.createTask(task1);

            Epic epic1 = new Epic(
                    "Посадить дерево",
                    "Дерево",
                    Status.NEW,
                    Instant.now(),
                    2
            );
            httpTaskManager.createEpic(epic1);

            Subtask subtask1 = new Subtask(
                    "Купить семена",
                    "Семена",
                    Status.NEW,
                    epic1.getId(),
                    Instant.now(),
                    3
            );
            httpTaskManager.createSubtask(subtask1);


            httpTaskManager.getTaskById(task1.getId());
            httpTaskManager.getEpicById(epic1.getId());
            httpTaskManager.getSubtaskById(subtask1.getId());

            System.out.println("Печать всех задач");
            System.out.println(gson.toJson(httpTaskManager.getAllTasks()));
            System.out.println("Печать всех эпиков");
            System.out.println(gson.toJson(httpTaskManager.getAllEpics()));
            System.out.println("Печать всех подзадач");
            System.out.println(gson.toJson(httpTaskManager.getAllSubtasks()));
            System.out.println("Загруженный менеджер");
            System.out.println(httpTaskManager);
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
