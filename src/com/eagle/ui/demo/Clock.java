package com.eagle.ui.demo;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.applet.*;

public class Clock extends JFrame {
	private static final long serialVersionUID = 1L;

	panel p;

	Clock() throws MalformedURLException {

		super("小闹钟");

		p = new panel();
		setSize(450, 250);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(p, BorderLayout.CENTER);

	}

	class panel extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		JTextField text1;

		JTextField text2;

		JButton button;

		JLabel l1;

		JLabel l2;

		panel() {
			l1 = new JLabel("时");
			l2 = new JLabel("分");
			text1 = new JTextField(6);
			text2 = new JTextField(6);
			button = new JButton("开始");

			add(text1);
			add(l1);
			add(text2);
			add(l2);
			add(button);
			button.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			packer pack = new packer();
			pack.start();

		}

	}

	class packer extends Thread {

		int hours;

		int miu;

		@SuppressWarnings("deprecation")
		public void run() {

			while (true) {
				Calendar t = Calendar.getInstance();
				hours = t.get(Calendar.HOUR_OF_DAY);
				miu = t.get(Calendar.MINUTE);
				System.out.println(hours + " " + miu);
				if (hours == Integer.parseInt(p.text1.getText())
						&& miu == Integer.parseInt(p.text2.getText())) {

					URL cb;
					File f = new File("F:\\09-spaceprivate\\my chamberlain\\media\\不接你电话.wav");// 音乐文件路径
					try {
						cb = f.toURL();
						AudioClip aau;
						aau = Applet.newAudioClip(cb);
						aau.play();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(300000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws MidiUnavailableException,
			InvalidMidiDataException, IOException {

		new Clock();

	}

}
