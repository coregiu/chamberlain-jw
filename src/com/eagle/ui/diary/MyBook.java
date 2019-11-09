package com.eagle.ui.diary;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.*;

import javax.swing.*;
import javax.swing.undo.*;

import org.apache.log4j.Logger;

import com.eagle.action.DataBakAction;

/**
 * 我的日记本
 * 
 * @author szhang
 * 
 */
public class MyBook extends JTextArea {
	private static final long serialVersionUID = -6586871395053625200L;
	private static Logger log = Logger.getLogger(MyBook.class);

	private UndoManager undoManager = new UndoManager();

	private boolean bolder;

	private boolean italic;

	private boolean underline;

	private String color;

	private int fontType;

	private int fontSize;
	
	@SuppressWarnings("unchecked")
	public MyBook(){
		try{
			//this.setText("asdfasdfasdfasDF");
			this.setLineWrap(true);
			Font f = getMyFont();//new Font(DiaryPanel.fontTable.get(this.getFontType()), Font.BOLD|Font.ITALIC, this.getFontSize());		
			this.setFont(f);
			this.setForeground(Color.getColor("color", Integer.parseInt(this.getColor())));
		}catch(Exception e){
			log.error(e);
		}
	}

	/**
	 * "撤销"的处理方法
	 */
	public void undo() {
		boolean canUndo = this.undoManager.canUndo(); // 判断是否可以撤销
		if (canUndo) {
			this.undoManager.undo(); // 执行撤销操作
		}
	}

	/**
	 * "重做"的处理方法
	 */
	public void redo() {
		boolean canRedo = this.undoManager.canRedo(); // 判断是否可以重做
		if (canRedo) {
			this.undoManager.redo(); // 执行重做操作
		}
	}

	/**
	 * 设置撤销与重做菜单项的状态
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}

	public boolean isBolder() {
		return bolder;
	}

	public void setBolder(boolean bolder) {
		this.bolder = bolder;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontType() {
		return fontType;
	}

	public void setFontType(int fontType) {
		this.fontType = fontType;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public void setUndoManager(UndoManager undoManager) {
		this.undoManager = undoManager;
	}
	
	public Font getMyFont() throws Exception{
		//加载格式
		DataBakAction action = new DataBakAction();
		Hashtable<String, String> tb = action.getConfigHash();
		this.setBolder(Boolean.parseBoolean(tb.get("bolder")));
		this.setItalic(Boolean.parseBoolean(tb.get("italic")));
		this.setUnderline(Boolean.parseBoolean(tb.get("underline")));
		this.setColor(tb.get("color"));
		this.setFontType(Integer.parseInt(tb.get("font")));
		this.setFontSize(Integer.parseInt(tb.get("size")));
		/*Font f;
		if(this.isBolder()&&this.isItalic()&&this.isUnderline()){
			f = new Font(DiaryPanel.fontTable.get(this.getFontType()), Font.BOLD|Font.ITALIC|Font., this.getFontSize());	
		}*/
		HashMap<TextAttribute, Object> hm = new HashMap<TextAttribute, Object>(); 
		if(this.isUnderline()){
			/*AttributedString ats = new AttributedString(this.getText());
			ats.addAttribute(TextAttribute.UNDERLINE,
				      TextAttribute.UNDERLINE_LOW_TWO_PIXEL,0,this.getText().length());
			Graphics2D   g2= this.getText().createGraphics(); ;
            
            //retrive   the   charater   iterator   from   the   attributed   string 
			AttribCharIterator attribCharIterator=attribString.getIterator(); 
            
            //create   a   font   render   contex   object   and   text   layout 
            FontRenderContext   frc=new   FontRenderContext(null,false,false); 
            TextLayout   layout=new   TextLayout(attribCharIterator,frc); 
            
            //draw   the   string 
            layout.draw(g2,20,100); 
*/
		}
		if(this.isItalic()){
			hm.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE); // 是否斜体
		}
		if(this.isBolder()){
			hm.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD); // 是否加粗
		}
		hm.put(TextAttribute.SIZE, this.getFontSize());    // 定义字号 
		hm.put(TextAttribute.FAMILY, DiaryPanel.fontTable.get(this.getFontType()));   // 定义字型
		//hm.put(TextAttribute.FOREGROUND, Color.RED);    // 定义字号 
		Font font = new Font(hm);    // 生成字号为12，字体为宋体，字形带有下划线的字体 

		return font;
	}
}
