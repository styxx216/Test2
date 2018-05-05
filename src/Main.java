import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static String listOfFiles = "";
    private static void fetchChild(File file) {

        if (file.isDirectory()) {

            File[] children = file.listFiles();

            for (File child : children) {
                // Рекурсивный (Recursive)
                listOfFiles+=(child.getAbsolutePath()+"\n");

                fetchChild(child);
            }
        }

    }
    public static void main(String args[]) throws IOException {


        String path;

        System.out.println(" Введите путь к каталогу:");
        Scanner in = new Scanner(System.in);
        path = in.nextLine();
        boolean isFirstTime = true;



        File dir = new File(path);
        if(!dir.exists()){
            System.out.println("директории не существует");
        }

        File list = new File("list.txt");
        if(!list.exists()){
            list.createNewFile();
            isFirstTime = true;
        }
        else{
            isFirstTime = false;
        }


        String listOfDeleted = "";
        String listOfCreated = "";
        String listOfMoved = "";
        if(isFirstTime){
            System.out.println("\n_______________________\nэто первый запуск программы\n_______________________\n");
            FileWriter fileWriter = new FileWriter(list);
            listOfFiles = "";
            fetchChild(dir);
            fileWriter.write(listOfFiles);
            fileWriter.flush();
            fileWriter.close();

        }
        else{
            System.out.println("\n_______________________\nэто второй запуск программы\n_______________________\n");
            File newpath = new File(path);
            FileReader fileReader = new FileReader(list);
            String pathOfFile = "";
            String name ="";
            String name2 ="";
            Scanner scan = new Scanner(fileReader);
            String newListOfFiles = "";
            while (scan.hasNextLine()) {
                newListOfFiles+=(scan.nextLine()+"\n");
            }
            listOfFiles = "";
            fetchChild(newpath);

            Scanner newScan = new Scanner(listOfFiles);
            scan = new Scanner(newListOfFiles);
            //поиск удаленных и перемещенных файлов
            while (scan.hasNextLine()) {
                pathOfFile = scan.nextLine();
                File temp = new File(pathOfFile);
                if(!temp.exists()){
                    name = temp.getName();
                    boolean isDeleted = true;
                    newScan = new Scanner(listOfFiles);
                    while (newScan.hasNextLine()) {
                        File file = new File(newScan.nextLine());
                        name2 = file.getName();
                        if(name.equals(name2)){
                            listOfMoved+=(name+"\n");
                            isDeleted = false;
                            break;
                        }
                    }
                    if(isDeleted){
                        listOfDeleted+=(name+"\n");
                    }

                }
            }
            scan = new Scanner(fileReader);
            newScan = new Scanner(listOfFiles);
            //поиск созданных файлов
            while (newScan.hasNextLine()){
                pathOfFile = newScan.nextLine();
                File temp = new File(pathOfFile);
                name = temp.getName();
                boolean isCreated = true;
                scan = new Scanner(newListOfFiles);
                while (scan.hasNextLine()) {
                    File file = new File(scan.nextLine());
                    name2 = file.getName();
                    if(name.equals(name2)){
                        isCreated = false;
                        break;
                    }
                }
                if(isCreated){
                    listOfCreated+=(name+"\n");
                }
            }
            fileReader.close();
            list.delete();

            System.out.println("Созданные файлы и папки:\n"+listOfCreated);
            System.out.println("Удаленные файлы и папки:\n"+listOfDeleted);
            System.out.println("Перемещенные файлы и папки:\n"+listOfMoved);
        }

    }
}
