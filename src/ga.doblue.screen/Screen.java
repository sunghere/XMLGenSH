package src.ga.doblue.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by SungHere on 2017-09-08.
 */
public class Screen extends JFrame {

    private JFileChooser jfc;
    private JPanel topPanel;
    private JLabel jLabel;
    private JTextField jf;

    private JTextArea ja;
    private JScrollPane jp;

    private JButton geneBtn;
    private JButton resetBtn;
    private JButton findBtn;
    private JButton clearBtn;


    private JPanel leftPanel;

    private ActionListener fileFind;

    public void init() {

        this.setName("McCube.XMLGeneSH");
        this.setTitle("McCube.XMLGeneSH");

        topPanel = new JPanel(new GridLayout(1, 0));
        jLabel = new JLabel("경로 : ");
        findBtn = new JButton("Find..");

        findBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (jfc.showOpenDialog(Screen.this) == JFileChooser.APPROVE_OPTION) {
                    // showopendialog 열기 창을 열고 확인 버튼을 눌렀는지 확인
                    jf.setText(jfc.getSelectedFile().toString());
                }

            }
        });
        jf = new JTextField();
        jf.setEditable(false);


        topPanel.add(jLabel);
        topPanel.add(jf);
        topPanel.add(findBtn);

        this.add(topPanel, BorderLayout.NORTH);

        //---------------파일선택
        jfc = new JFileChooser();
        jfc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML Document", ".xml"));


        ja = new JTextArea();
        jp = new JScrollPane(ja);

        ja.setEditable(false);
        this.add(jp);

        leftPanel = new JPanel();

        leftPanel.setLayout(new GridLayout(0, 1, 10, 20));

        geneBtn = new JButton("변환");


        resetBtn = new JButton("Reset");
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jf.setText("");
            }
        });

        clearBtn = new JButton("Clear");


        leftPanel.add(geneBtn);
        leftPanel.add(resetBtn);

        this.add(leftPanel, BorderLayout.EAST);


    }


    public Screen() {
        setSize(400, 300);

        init();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScreenUtil.setLocation(this);

        this.setVisible(true);

    }


}
