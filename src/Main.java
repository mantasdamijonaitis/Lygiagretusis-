import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final String FILE_PATH = "./src/resources/IFF-5-4_DamijonaitisMantas_L1a_dat.txt";
    private static final int ARRAY_SIZE = 5;
    private static final String[] THREAD_TITLES = { "Pirma gija", "Antra gija", "Trečia gija", "Ketvirta gija", "Penkta gija"};

    public static void main(String[] args) {
        try {
            Student[][] studentArray = readStudentsFromFile();
            Student[] resultArray = new Student[ARRAY_SIZE * ARRAY_SIZE];
            for (int i = 0; i < ARRAY_SIZE; i++){
                new ArrayMixThread(studentArray[i], resultArray, THREAD_TITLES[i]).run();
            }
            for (int i = 0; i < ARRAY_SIZE * ARRAY_SIZE; i++){
                System.out.println(resultArray[i]);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static class Student {
        public String name;
        public String surname;
        public int year;
        public double weight;
        public int numberInClass;
        public String threadName;

        public Student(String line) {
            String[] splitted = line.split("\\s+");
            this.name = splitted[0];
            this.surname = splitted[1];
            this.year = Integer.parseInt(splitted[2]);
            this.weight = Double.parseDouble(splitted[3]);
            this.numberInClass = Integer.parseInt(splitted[4]);
            this.threadName = "";
        }

        public String toString() {
            return this.name + ' ' + this.surname + ' ' + this.weight + ' ' + this.numberInClass + ' ' + this.threadName;
        }

    }

    private static Student[][] readStudentsFromFile() throws IOException {
        Student[][] studentArray = new Student[ARRAY_SIZE][ARRAY_SIZE];
        int index = 0;
        int internalIndex = 0;
        for (String line : Files.lines(Paths.get(FILE_PATH)).toArray(String[]::new)){
            if (line.trim().isEmpty()) {
                index++;
                internalIndex = 0;
                continue;
            }
            studentArray[index][internalIndex] = new Student(line);
            internalIndex++;
        }
        return studentArray;
    }

    private static class ArrayMixThread implements Runnable {

        private Student[] inputList;
        private Student[] resultList;
        private final String threadName;

        public ArrayMixThread (Student[] inputList, Student[] resultList, String threadName){
            this.inputList = inputList;
            this.resultList = resultList;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            for (int i = 0; i < inputList.length; i++){
                int firstNotNullArrayIndex = getFirstNullArrayIndex(resultList);
                if (firstNotNullArrayIndex != -1){
                    inputList[i].threadName = this.threadName;
                    resultList[firstNotNullArrayIndex] = inputList[i];
                }
            }
        }

        private int getFirstNullArrayIndex (Student[] array){
            for (int i = 0; i < array.length; i++){
                if (array[i] == null){
                    return i;
                }
            }
            return -1;
        }

    }

}
