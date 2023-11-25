import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*  Реализовать приложение сканер сети, приложение должно на вход получать ip адрес
и проводить сканирование сети, находить активные устройства и получать список
доступных портов. Информацию выводить в консоль и запрашивать у пользователя
сохранение в файл формата txt или csv.   */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите IP-адрес для сканирования: ");
        String ipAddress = scanner.nextLine();
        System.out.println("Начало сканирования...");

        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);

            if (inetAddress.isReachable(5000)) {
                System.out.println("Устройство " + ipAddress + " доступно.");
                List<Integer> openPorts = scanPorts(ipAddress);
                System.out.println("Открытые порты для " + ipAddress + ": " + openPorts);
                System.out.print("Хотите сохранить результаты в файл? (да/нет): ");
                String saveToFile = scanner.nextLine().toLowerCase();

                if (saveToFile.equals("да")) {
                    System.out.print("Введите путь и имя файла (например, scan_results): ");
                    String filePath = scanner.nextLine();
                    System.out.print("Выберите формат файла (txt/csv): ");
                    String fileFormat = scanner.nextLine().toLowerCase();
                    saveResultsToFile(ipAddress, openPorts, filePath, fileFormat);
                    System.out.println("Результаты сохранены в файл: " + filePath + "." + fileFormat);
                }
            } else {
                System.out.println("Устройство " + ipAddress + " не доступно.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> scanPorts(String ipAddress) {
        System.out.println("Сканирование портов для " + ipAddress + "...");
        List<Integer> openPorts = new ArrayList<>();

        for (int port = 1; port <= 1024; port++) {
            try {
                Socket socket = new Socket(ipAddress, port);
                System.out.println("Порт " + port + " открыт.");
                openPorts.add(port);
                socket.close();
            } catch (IOException e) {

            }
        }

        return openPorts;
    }

    private static void saveResultsToFile(String ipAddress, List<Integer> openPorts, String filePath, String fileFormat) {
        try (FileWriter writer = new FileWriter(filePath + "." + fileFormat)) {
            writer.write("Результаты сканирования для " + ipAddress + ":\n");

            if (fileFormat.equals("txt")) {
                writer.write("Открытые порты: " + openPorts.toString());
            } else if (fileFormat.equals("csv")) {
                writer.write("Открытые порты\n");
                for (int port : openPorts) {
                    writer.write(port + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}