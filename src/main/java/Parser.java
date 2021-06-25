import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Parser {

    public static ArrayList<Note> parse(String fileName) {
        // инициализируем потоки
        String result = "";
        InputStream inputStream = null;
        HSSFWorkbook workBook = null;
        try {
            inputStream = new FileInputStream(fileName);
            workBook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Note> NoteList = new ArrayList<Note>(); // Список, в котором будут хранится все экземляры класса "Записи", т.е все события

        int amountSheets = workBook.getNumberOfSheets(); // Количество листов в xls файле
        for (int i=0;i<amountSheets;i++) {
            // разбираем первый лист входного файла на объектную модель
            Sheet sheet = workBook.getSheetAt(i);
            Iterator<Row> it = sheet.iterator();
            // проходим по всему листу
            while (it.hasNext()) {
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                Cell firstCell = row.getCell(0);
                 try{
                     firstCell.getNumericCellValue(); // Если первая ячейка имеет не числовое значение, следовательно это не запись, пропускаем её
                     if (firstCell.toString()=="") continue;
                 }
                 catch(Exception e)
                {
                    continue;
                }
                 // Инициализация экземпляра класса "Запись" значением ячеек из файла, из строковых значений убираются лишние пробелы, переносы строк и разрывы
                Note note = new Note(row.getCell(1).getStringCellValue().replaceAll("[\\s]{2,}", " ").replace("\n", "").replace("\r", "").trim(),
                        row.getCell(2).getStringCellValue().replaceAll("[\\s]{2,}", " ").replace("\n", "").replace("\r", "").trim(),
                        row.getCell(3).getStringCellValue().replaceAll("[\\s]{2,}", " ").replace("\n", "").replace("\r", "").trim());
                 NoteList.add(note); // Добавляем текущую запись в список
            }
        }
        return NoteList;
    }

 /*   public static void main(String... args) throws InvalidFormatException, IOException {
        // получаем файл в формате xls
        ArrayList <Note> NoteList = parse("C:\\Users\\EIGHTH\\Desktop\\IPRACTICE\\practice\\src\\main\\resources\\КП 2021 30 апреля.xls");
        for (Note note:NoteList)
        {
            note.print(); // Вывод всех записей, | - разделитель для значений полей экземляров
        }
    } */
}



