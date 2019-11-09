package com.eagle.ui.datechooser;

import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class Test2 extends javax.swing.JFrame {
    public Test2() {
        initComponents();
    }
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        tfDate = new javax.swing.JTextField();
        btnDate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("\u65e5\u671f\u9009\u62e9\u63a7\u4ef6");
        jLabel1.setFont(new java.awt.Font("������", 0, 12));
        jLabel1.setText("\u65e5\u671f:");

        tfDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDateFocusGained(evt);
            }
        });
        tfDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfDateMouseClicked(evt);
            }
        });

        btnDate.setFont(new java.awt.Font("������", 0, 12));
        btnDate.setText("\u9009\u62e9");
        btnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateActionPerformed(evt);
            }
        });
        btnDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDateMouseClicked(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jTextArea1.setText("\u77e5\u8bc6\u70b9\u548c\u6280\u5de7:\n1.\u638c\u63e1\u5728NetBeans\u4e0b\u8c03\u7528\u6b64\u65e5\u671f\u9009\u62e9\u63a7\u4ef6\u7684\u65b9\u6cd5.\n2.\u638c\u63e1\u65e5\u671f\u9009\u62e9\u63a7\u4ef6\u7684\u5b9a\u4f4d\u4e0e\u4e8b\u4ef6\u89e6\u53d1.\n3.\u638c\u63e1\u672c\u5730\u65f6\u95f4\u683c\u5f0f\u5316\u8f93\u51fa\u7684\u65b9\u6cd5.");
        jScrollPane1.setViewportView(jTextArea1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tfDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnDate)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(tfDate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnDate))
                .add(111, 111, 111)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfDateMouseClicked
            dateChooser.showChooser(btnDate, evt.getX() - DateChooser.width, evt.getY() + 15);
            if(dateChooser.getDate() != null)
                    tfDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate()));
    }//GEN-LAST:event_tfDateMouseClicked

    private void tfDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDateFocusGained

    }//GEN-LAST:event_tfDateFocusGained

    private void btnDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDateMouseClicked
         dateChooser.showChooser(btnDate, evt.getX() - DateChooser.width, evt.getY());
         if(dateChooser.getDate() != null)
                    tfDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate()));   
    }//GEN-LAST:event_btnDateMouseClicked

    private void btnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateActionPerformed

    }//GEN-LAST:event_btnDateActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Test2.setDefaultLookAndFeelDecorated(true);
                new Test2().setVisible(true);
            }
        });
    }
    
    private DateChooser dateChooser = new DateChooser(this);
    private javax.swing.JButton btnDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField tfDate;
}