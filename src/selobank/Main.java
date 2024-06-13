package selobank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main {
    private static Database database = new Database();
    private static JFrame frame;
    private static JPanel mainPanel;

    public static void main(String[] args) {
        database.loadDataFromFile("Files/Data");

        frame = Frame(800, 600);
        JLabel title = Label("Banka uygulamasına hoş geldiniz", 23);

        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel label1 = Label("Hesap ID:", 20);
        JTextField id = TextField(20);
        JLabel label2 = Label("Şifre:", 20);
        JPasswordField sifre = PasswordField(20);
        loginPanel.add(label1);
        loginPanel.add(id);
        loginPanel.add(label2);
        loginPanel.add(sifre);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton girisYap = Button("Giriş Yap", 23);
        JButton yeniHesapOlustur = Button("Yeni Hesap Oluştur", 23);
        JButton cikisYap = Button("Çıkış Yap", 23);
        buttonPanel.add(girisYap);
        buttonPanel.add(yeniHesapOlustur);
        buttonPanel.add(cikisYap);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        girisYap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = id.getText();
                String sifreStr = new String(sifre.getPassword());

                if (idStr.isEmpty() || sifreStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun.");
                    return;
                }

                if (idStr.length() != 7 || !isNumeric(idStr)) {
                    JOptionPane.showMessageDialog(frame, "Hesap ID 7 haneli olmalıdır ve sadece rakam içermelidir.");
                    return;
                }

                int hesapID = Integer.parseInt(idStr);

                if (sifreStr.length() != 4 || !isNumeric(sifreStr)) {
                    JOptionPane.showMessageDialog(frame, "Şifre 4 haneli olmalıdır ve sadece rakam içermelidir.");
                    return;
                }

                int sifreInt = Integer.parseInt(sifreStr);

                Account account = null;
                for (Account acc : database.getAccounts()) {
                    if (acc != null && acc.getHesapID() == hesapID && acc.getSifre() == sifreInt) {
                        account = acc;
                        break;
                    }
                }

                if (account != null) {
                    mainPanel.removeAll();
                    menuGoster(account);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Hesap bulunamadı veya şifre hatalı.");
                }
            }
        });

        yeniHesapOlustur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yeniHesapOlustur();
            }
        });

        cikisYap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.saveDataToFile("Files/Data"); 
                System.exit(0);
            }
        });
    }

    private static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private static JButton Button(String string, int i) {
        JButton button = new JButton(string);
        button.setFont(new Font("Arial", Font.PLAIN, i));
        return button;
    }

    private static JPasswordField PasswordField(int i) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setColumns(i);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        return passwordField;
    }

    private static JTextField TextField(int i) {
        JTextField textField = new JTextField();
        textField.setColumns(i);
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        return textField;
    }

    private static JLabel Label(String string, int i) {
        JLabel label = new JLabel(string);
        label.setFont(new Font("Arial", Font.PLAIN, i));
        return label;
    }

    private static JFrame Frame(int i, int j) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(i, j);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private static void menuGoster(Account account) {
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // 
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton bakiyeSorgulamaBtn = Button("Bakiye Sorgulama", 23);
        JButton paraYatirBtn = Button("Para Yatır", 23);
        JButton paraCekBtn = Button("Para Çek", 23);
        JButton paraTransferBtn = Button("Para Transferi", 23);
        JButton islemGecmisiBtn = Button("İşlem Geçmişi", 23);
        JButton cikisBtn = Button("Çıkış Yap", 23); 

        menuPanel.add(bakiyeSorgulamaBtn);
        menuPanel.add(paraYatirBtn);
        menuPanel.add(paraCekBtn);
        menuPanel.add(paraTransferBtn);
        menuPanel.add(islemGecmisiBtn);
        menuPanel.add(cikisBtn); 

        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        bakiyeSorgulamaBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Güncel bakiye: " + account.getBakiye() + " TL");
            }
        });

        paraYatirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String miktarStr = JOptionPane.showInputDialog("Yatırmak istediğiniz miktarı girin:");
                if (miktarStr != null && !miktarStr.isEmpty() && isNumeric(miktarStr)) {
                    double miktar = Double.parseDouble(miktarStr);
                    account.paraYatir(miktar);
                    JOptionPane.showMessageDialog(frame, miktar + " TL hesabınıza yatırıldı. Güncel bakiye: " + account.getBakiye());
                } else {
                    JOptionPane.showMessageDialog(frame, "Geçersiz miktar.");
                }
                database.saveDataToFile("Files/Data");
            }
        });

        paraCekBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String miktarStr = JOptionPane.showInputDialog("Çekmek istediğiniz miktarı girin:");
                if (miktarStr != null && !miktarStr.isEmpty() && isNumeric(miktarStr)) {
                    double miktar = Double.parseDouble(miktarStr);
                    if (account.paraCek(miktar)) {
                        JOptionPane.showMessageDialog(frame, miktar + " TL hesabınızdan çekildi. Güncel bakiye: " + account.getBakiye());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Yetersiz bakiye.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Geçersiz miktar.");
                }
                database.saveDataToFile("Files/Data");
            }
        });

        paraTransferBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hedefHesapIDStr = JOptionPane.showInputDialog("Transfer etmek istediğiniz hesabın ID'sini girin:");
                if (hedefHesapIDStr != null && !hedefHesapIDStr.isEmpty() && isNumeric(hedefHesapIDStr)) {
                    int hedefHesapID = Integer.parseInt(hedefHesapIDStr);
                    Account hedefHesap = database.getAccountByID(hedefHesapID);
                    if (hedefHesap != null) {
                        String miktarStr = JOptionPane.showInputDialog("Transfer etmek istediğiniz miktarı girin:");
                        if (miktarStr != null && !miktarStr.isEmpty() && isNumeric(miktarStr)) {
                            double miktar = Double.parseDouble(miktarStr);
                            if (account.paraTransferi(hedefHesap, miktar)) {
                                JOptionPane.showMessageDialog(frame, miktar + " TL " + hedefHesapID + " numaralı hesaba transfer edildi.");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Yetersiz bakiye.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Geçersiz miktar.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Hedef hesap bulunamadı.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Geçersiz hesap ID.");
                }
                database.saveDataToFile("Files/Data");
            }
        });

        islemGecmisiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String islemGecmisi = account.getİşlemGeçmişi();
                JOptionPane.showMessageDialog(frame, "İşlem Geçmişi:\n" + islemGecmisi);
                database.saveDataToFile("Files/Data");
            }
        });

        cikisBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.saveDataToFile("Files/Data"); 
                System.exit(0); 
            }
        });
    }


    private static void yeniHesapOlustur() {
        JFrame yeniHesapFrame = Frame(600, 400); 
        JLabel title = Label("Yeni Hesap Oluşturma", 23);

        JPanel yeniHesapPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        yeniHesapPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField isimField = TextField(20);
        JTextField soyIsimField = TextField(20);
        JTextField bakiyeField = TextField(20);
        JTextField tcNoField = TextField(20);
        JPasswordField sifreField = PasswordField(20);

        yeniHesapPanel.add(Label("İsim:", 20));
        yeniHesapPanel.add(isimField);
        yeniHesapPanel.add(Label("Soyisim:", 20));
        yeniHesapPanel.add(soyIsimField);
        yeniHesapPanel.add(Label("Bakiye:", 20));
        yeniHesapPanel.add(bakiyeField);
        yeniHesapPanel.add(Label("TC Kimlik No:", 20));
        yeniHesapPanel.add(tcNoField);
        yeniHesapPanel.add(Label("Şifre:", 20));
        yeniHesapPanel.add(sifreField);

        JButton kaydetBtn = Button("Kaydet", 23);
        yeniHesapPanel.add(new JLabel()); 
        yeniHesapPanel.add(kaydetBtn);

        yeniHesapFrame.add(title, BorderLayout.NORTH);
        yeniHesapFrame.add(yeniHesapPanel, BorderLayout.CENTER);
        yeniHesapFrame.setVisible(true);

        kaydetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isim = isimField.getText();
                String soyIsim = soyIsimField.getText();
                String bakiyeStr = bakiyeField.getText();
                String tcNoStr = tcNoField.getText();
                String sifreStr = new String(sifreField.getPassword());

                if (isim.isEmpty() || soyIsim.isEmpty() || bakiyeStr.isEmpty() || tcNoStr.isEmpty() || sifreStr.isEmpty()) {
                    JOptionPane.showMessageDialog(yeniHesapFrame, "Lütfen tüm alanları doldurun.");
                    return;
                }

                if (sifreStr.length() != 4 || !isNumeric(sifreStr)) {
                    JOptionPane.showMessageDialog(yeniHesapFrame, "Şifre 4 haneli olmalıdır ve sadece rakam içermelidir.");
                    return;
                }

                if (!isNumeric(bakiyeStr) || Double.parseDouble(bakiyeStr) < 0) {
                    JOptionPane.showMessageDialog(yeniHesapFrame, "Bakiye pozitif bir sayı olmalıdır.");
                    return;
                }

                if (tcNoStr.length() != 11 || !isNumeric(tcNoStr)) {
                    JOptionPane.showMessageDialog(yeniHesapFrame, "TC Kimlik No 11 haneli olmalıdır ve sadece rakam içermelidir.");
                    return;
                }

                long tcNo = Long.parseLong(tcNoStr);
                if (database.tcNoExists(tcNo)) {
                    JOptionPane.showMessageDialog(yeniHesapFrame, "Bu TC Kimlik No ile zaten bir hesap var.");
                    return;
                }

                int hesapID = database.generateUniqueID();
                double bakiye = Double.parseDouble(bakiyeStr);
                int sifre = Integer.parseInt(sifreStr);

                Account newAccount = new Account(isim, soyIsim, hesapID, tcNo, bakiye, sifre);
                database.addAccount(newAccount);

                JOptionPane.showMessageDialog(yeniHesapFrame, "Hesap başarıyla oluşturuldu. Hesap ID'niz: " + hesapID);
                database.saveDataToFile("Files/Data");
                yeniHesapFrame.dispose();
            }
        });

    }
}
