package com.eagle.ui.demo;

import   java.awt.BorderLayout; 
import   java.awt.Dimension; 
import   java.awt.Toolkit; 
import   java.awt.event.ActionEvent; 
import   java.awt.event.ActionListener; 

import   javax.swing.AbstractAction; 
import   javax.swing.Icon; 
import   javax.swing.ImageIcon; 
import   javax.swing.JComboBox; 
import   javax.swing.JFrame; 
import   javax.swing.JLabel; 
import   javax.swing.JMenu; 
import   javax.swing.JMenuBar; 
import   javax.swing.JPanel; 
import   javax.swing.JTextPane; 
import   javax.swing.JToolBar; 
import   javax.swing.KeyStroke; 
import   javax.swing.border.BevelBorder; 

public   class   ToolBarDemo0   extends   JPanel   { 

	private static final long serialVersionUID = -2802175909169436545L;

	public   JTextPane   pane; 

    public   JMenuBar   menuBar; 

    public   JToolBar   toolBar; 

    String   fonts[]   =   {   "Serif ",   "SansSerif ",   "Monospaced ",   "Dialog ", 
            "DialogInput "   }; 

    public   ToolBarDemo0()   { 
        menuBar   =   new   JMenuBar(); 

        //   Create   a   set   of   actions   to   use   in   both   the   menu   and   toolbar 
        DemoAction   leftJustifyAction   =   new   DemoAction( "Left ",   new   ImageIcon( 
                "../logo/diary/new.png"),   "Left   justify   text ",   'L'); 
        DemoAction   rightJustifyAction   =   new   DemoAction( "Right ",   new   ImageIcon( 
                "../logo/diary/new.png"),   "Right   justify   text ",   'R'); 
        DemoAction   centerJustifyAction   =   new   DemoAction( "Center ", 
                new   ImageIcon( "../logo/diary/new.png"),   "Center   justify   text ",   'M'); 
        DemoAction   fullJustifyAction   =   new   DemoAction( "Full ",   new   ImageIcon( 
                "../logo/diary/new.png"),   "Full   justify   text ",   'F'); 

        JMenu   formatMenu   =   new   JMenu( "Justify "); 
        formatMenu.add(leftJustifyAction); 
        formatMenu.add(rightJustifyAction); 
        formatMenu.add(centerJustifyAction); 
        formatMenu.add(fullJustifyAction); 

        menuBar.add(formatMenu); 

        toolBar   =   new   JToolBar( "Formatting "); 
        toolBar.add(leftJustifyAction); 
        toolBar.add(rightJustifyAction); 
        toolBar.add(centerJustifyAction); 
        toolBar.add(fullJustifyAction); 

        toolBar.addSeparator(); 
        JLabel   label   =   new   JLabel( "Font "); 
        toolBar.add(label); 

        toolBar.addSeparator(); 
        JComboBox   combo   =   new   JComboBox(fonts); 
        combo.addActionListener(new   ActionListener()   { 
            public   void   actionPerformed(ActionEvent   e)   { 
                try   { 
                    pane.getStyledDocument().insertString( 
                            0, 
                            "Font   [ " 
                                    +   ((JComboBox)   e.getSource()) 
                                            .getSelectedItem()   +   "]   chosen!\n ", 
                            null); 
                }   catch   (Exception   ex)   { 
                    ex.printStackTrace(); 
                } 
            } 
        }); 
        toolBar.add(combo); 

        //     Disable   one   of   the   Actions 
        fullJustifyAction.setEnabled(false); 
    } 

    public   static   void   main(String   s[])   { 

        ToolBarDemo0   example   =   new   ToolBarDemo0(); 
        example.pane   =   new   JTextPane(); 
        example.pane.setPreferredSize(new   Dimension(250,   250)); 
        example.pane.setBorder(new   BevelBorder(BevelBorder.LOWERED)); 
        example.toolBar.setMaximumSize(example.toolBar.getSize()); 

        JFrame   frame   =   new   JFrame( "Menu   Example "); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setJMenuBar(example.menuBar); 
        frame.getContentPane().add(example.toolBar,   BorderLayout.NORTH); 
        frame.getContentPane().add(example.pane,   BorderLayout.CENTER); 
        frame.pack(); 
        frame.setVisible(true); 
    } 

    class   DemoAction   extends   AbstractAction   { 

		private static final long serialVersionUID = 4728504496726187057L;

		public   DemoAction(String   text,   Icon   icon,   String   description, 
                char   accelerator)   { 
            super(text,   icon); 
            putValue(ACCELERATOR_KEY,   KeyStroke.getKeyStroke(accelerator, 
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())); 
            putValue(SHORT_DESCRIPTION,   description); 
        } 

        public   void   actionPerformed(ActionEvent   e)   { 
            try   { 
                pane.getStyledDocument().insertString(0, 
                        "Action   [ "   +   getValue(NAME)   +   "]   performed!\n ",   null); 
            }   catch   (Exception   ex)   { 
                ex.printStackTrace(); 
            } 
        } 
    } 
} 
