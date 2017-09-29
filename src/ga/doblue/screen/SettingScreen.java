package src.ga.doblue.screen;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Vector;

/**
 * Created by SungHere on 2017-09-27.
 */
public class SettingScreen extends JDialog {

    private static Setting setting;
    private JScrollPane sp;
    private JPanel btnPanel;
    private JTable listTable;
    private JButton okBtn;
    private JButton addBtn;
    private JButton deleteBtn;

    private Screen parent;

    @Override
    public void dispose() {
        super.dispose();
        parent.setClose();
    }

    public SettingScreen(Screen parent, Setting setting) {
        super(parent, "Setting", false);
        this.parent = parent;
        setSize(200, 300);
        this.setting = setting;

        init();


        ScreenUtil.setLocation(this, (int) parent.getLocation().getX() + parent.getWidth(), (int) parent.getLocation().getY());

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void init() {

        btnPanel = new JPanel(new GridLayout(0, 3));

        add(btnPanel, BorderLayout.NORTH);

        btnPanel.add(okBtn);
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);

        /* 컬럼 초기화*/
        Vector<String> tablecolum = new Vector<>();
        tablecolum.add("key");
        tablecolum.add("value");

        /*테이블 초기화*/
        DefaultTableModel defaultTableModel = new DefaultTableModel(setting.getData(), tablecolum) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }


        };
        listTable = new JTable(defaultTableModel);
        // 테이블 가운데정렬
        // DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
        DefaultTableCellRenderer tRender = new DefaultTableCellRenderer();

        // DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
        tRender.setHorizontalAlignment(SwingConstants.CENTER);

        // 정렬할 테이블의 ColumnModel을 가져옴
        TableColumnModel tcm = listTable.getColumnModel();

        // 반복문을 이용하여 테이블을 가운데 정렬로 지정
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            tcm.getColumn(i).setCellRenderer(tRender);
        }

        tcm = listTable.getColumnModel();

        // 반복문을 이용하여 테이블을 가운데 정렬로 지정
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            tcm.getColumn(i).setCellRenderer(tRender);
        }
        sp = new JScrollPane(listTable);


        add(sp);
    }

}
