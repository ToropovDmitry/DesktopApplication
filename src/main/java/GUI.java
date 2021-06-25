import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

public class GUI extends JFrame {
    JTextField textField;
    FirebaseApp app = null;

    public GUI() {
        super("Adm");
        setBounds(200, 200, 600, 250);
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel contents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textField = new JTextField(50);
        JButton restoreButton = new JButton("Открыть");
        restoreButton.setFocusable(false);

        restoreButton.addActionListener(new ActionListener()  { // Открыть
            public void actionPerformed(ActionEvent e) {
                openDirectory();
            }
        });

        JButton addDatabaseButton = new JButton("Добавить в БД");
        addDatabaseButton.setFocusable(false);

        JButton showDatabaseButton = new JButton("Вывести содержимое БД");
        showDatabaseButton.setFocusable(false);

        JTextArea eventsList = new JTextArea(10, 52);
        eventsList.setFont(new Font("Dialog", Font.PLAIN, 14));
        eventsList.setTabSize(10);

        addDatabaseButton.addActionListener(new ActionListener()  { // Добавить в БД
            public void actionPerformed(ActionEvent e) {
                File xlsFile = new File(textField.getText());
                if (!xlsFile.exists())
                {
                    textField.setText("");
                    JOptionPane.showMessageDialog(null, "Заданного файла не существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    FileInputStream serviceAccount = null;
                    try {
                        if (app == null) {
                            serviceAccount = new FileInputStream("C:\\Users\\Enot\\Desktop\\sport-events-43-firebase-adminsdk-1lhsq-8a02f854bf.json");
                            FirebaseOptions options = new FirebaseOptions.Builder()
                                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                    .setDatabaseUrl("https://sport-events-43-default-rtdb.firebaseio.com")
                                    .build();
                            app = FirebaseApp.initializeApp(options);
                        }
                        DatabaseReference eventsDataBase = FirebaseDatabase.getInstance().getReference();
                        ArrayList<Note> NoteList = Parser.parse(xlsFile.getAbsolutePath());
                        eventsDataBase.removeValueAsync();
                        for (Note event : NoteList) {
                            eventsDataBase.push().setValueAsync(event);
                            eventsList.append("Название мероприятия: " + event.getName() + "\n" + "Дата проведения: " + event.getDateString() + "\n" + "Город: " + event.getLocation() + "\n\n");
                        }
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "Не удалось пройти аутентификацию.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        showDatabaseButton.addActionListener(new ActionListener()  { // Чтение данных из БД
            public void actionPerformed(ActionEvent e) {
                    FileInputStream serviceAccount = null;
                    try {
                        if (app == null) {
                            serviceAccount = new FileInputStream("C:\\Users\\Enot\\Desktop\\sport-events-43-firebase-adminsdk-1lhsq-8a02f854bf.json");
                            FirebaseOptions options = new FirebaseOptions.Builder()
                                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                    .setDatabaseUrl("https://sport-events-43-default-rtdb.firebaseio.com")
                                    .build();
                            app = FirebaseApp.initializeApp(options);
                        }
                        DatabaseReference eventsDataBase = FirebaseDatabase.getInstance().getReference();
                        eventsDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    Note event = singleSnapshot.getValue(Note.class);
                                    eventsList.append("Название мероприятия: " + event.getName() + "\n" + "Дата проведения: " + event.getDateString() + "\n" + "Город: " + event.getLocation() + "\n\n");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                JOptionPane.showMessageDialog(null, databaseError.getMessage(), "Ошибка чтения", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "Не удалось пройти аутентификацию.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
            }
        });
        contents.add(textField);
        contents.add(restoreButton);
        contents.add(addDatabaseButton);
        contents.add(showDatabaseButton);
        contents.add(new JScrollPane(eventsList));
        setContentPane(contents);
    }

    private void openDirectory() {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter1 = new FileNameExtensionFilter(".xls", "xls");
            fileChooser.setFileFilter(filter1);

            int i = fileChooser.showDialog(this, "Open");
            File fileName = fileChooser.getSelectedFile();
            if (fileName != null) {
                if (!fileName.getName().endsWith("xls")) { JOptionPane.showMessageDialog(null, "Выбран не xls файл!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else  textField.setText(fileName.getAbsolutePath());
            }
    }
}
