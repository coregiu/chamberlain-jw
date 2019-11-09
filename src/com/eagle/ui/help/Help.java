package com.eagle.ui.help;

import java.awt.Dimension;

import javax.swing.*;
import com.eagle.data.ConfBean;
import com.eagle.data.ImageData;
import com.eagle.prop.*;

public class Help extends JDialog{
	private static final long serialVersionUID = 7951991789530817522L;
	private JPanel plMain;
	
	public Help(JFrame f){		
		super(f, "关于我的管家", true);
		plMain = (JPanel)this.getContentPane();
		body();
		UIProp.setUIScreen(this, new Dimension(400,280));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void body(){
		Dimension dimen ;
		JButton label = new JButton();
		StringBuffer listBuff = new StringBuffer();
		listBuff.append("<html>");
		listBuff.append("<div align=\"center\">" );
		listBuff.append("<B><FONT COLOR=\"BLUE\" SIZE=\"5\">Production: "+NameProp.TITLE+"</FONT><Br></B><hr><B>" );
		listBuff.append("<FONT COLOR=\"BLUE\">");
		listBuff.append("Version: "+ConfBean.VERSION+"<Br><Br> ");
		listBuff.append("Create by 张赛 <Br><Br> " );
		listBuff.append("Contact: chinajszhangsai@qq.com <Br><Br>");
		listBuff.append("CopyRight © 2009-2010 <BR><BR>");
		if("1".equals(ConfBean.type)){		
			listBuff.append("献给我最亲最爱的老婆！</B>");
		}
		listBuff.append("</FONT>");
		listBuff.append("</div>" );
		listBuff.append("</html>");
		dimen = new Dimension(360, 220);
		label.setIcon(ImageData.eagle);
		label.setText(listBuff.toString());
		label.setEnabled(false);
		label.setPreferredSize(dimen);
		JScrollPane scrollPane1 = new JScrollPane(label); 
		scrollPane1.setPreferredSize(new Dimension(380,230));
		plMain.add(scrollPane1);
	}
}
