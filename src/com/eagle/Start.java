package com.eagle;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.*;
import javax.swing.plaf.metal.*;
import com.eagle.ui.*;
import com.jgoodies.looks.*;
import com.jgoodies.looks.demo.*;
import com.jgoodies.looks.plastic.*;

/**
 * 启动类ϵͳ����
 * */
public class Start{
	protected Start() {
		try {
			new ServerSocket(26452);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "当前系统已有一个实例在运行！");
			return;
		}
    	/*界面配置*/
        configureUI();
        /*打开主界面*/
        new SplashWindow(new MainFrame());
    }
	/**
	 * 启动主方法ϵͳ�����
	 * */
	public static void main(String args[]){
		try{
            /*启动系统*/
            new Start();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error,please ask for the administrator!");
            e.printStackTrace();
        }
	}

    private static Settings createDefaultSettings() {
        Settings set;
        set = Settings.createDefault();
        return set;
    }
    /**
    *  系统界面美化
    */
    public void configureUI() {
    	//UIManager.put("ToolTip.hideAccelerator", Boolean.FALSE);
        com.jgoodies.looks.demo.Settings settings = createDefaultSettings();
        Options.setDefaultIconSize(new Dimension(18,18));    //设置图像的大小
        Options.setUseNarrowButtons(settings.isUseNarrowButtons());       //设置按钮大小
        // Global options
        Options.setTabIconsEnabled(settings.isTabIconsEnabled());    //让标签Enabled(激活)
        UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY,
        settings.isPopupDropShadowEnabled());                  //关于阴影部分的设置(例如按钮是可选择的时候——即按下不会马上起来)
        // Swing Settings
        LookAndFeel selectedLaf = settings.getSelectedLookAndFeel();     //初始化
        if (selectedLaf instanceof PlasticLookAndFeel) {                   //如果选择的是包中的实例
            PlasticLookAndFeel.setPlasticTheme(settings.getSelectedTheme());            //设置主题
            PlasticLookAndFeel.setTabStyle(settings.getPlasticTabStyle());         //设置标签的风格
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(                 //设置高对比度的颜色
            settings.isPlasticHighContrastFocusEnabled());
        } else if (selectedLaf.getClass() == MetalLookAndFeel.class) {       //如果初始的类型和MetalLookAndFeel相同
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());      //则 MetalLookAndFeel 将把它默认的主题作为当前的主题
        }

        // Work around caching in MetalRadioButtonUI
        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);

        try {
            UIManager.setLookAndFeel(selectedLaf);           //设置当前的LookAndFeel放入面板管理器
        } catch (Exception e) {
            System.out.println("Can't change L&F: " + e);
        }
    }
}