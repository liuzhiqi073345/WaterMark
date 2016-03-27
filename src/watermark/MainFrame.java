package watermark;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import watermark.CommandButton.ButtonStyle;
/**
 * 主界面
 * @author CSDN's Cannel_2020
 *
 */

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private String toward[] = { "右下", "左下", "右上", "左上", "正中", "左上->右下",
			"左下->右下" };
	private String fontstyle[] = {"普通","粗体","斜体","粗体&斜体"};
	private int fontstyles[] = {Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD | Font.ITALIC};
	private String fontcolor[] = {"蓝色","青色","绿色","红色","灰色","深灰色","浅灰色",
			"洋红色","桔黄色","粉红色","黄色","黑色","白色"};
	private String scales[] = {"不缩放", "25%", "50%", "75%","125%","150%","175%","200%"};
	private float imgScales[] = {1.0f, 0.25f, 0.5f, 0.75f, 1.25f, 1.5f, 1.75f, 2.0f};
	private Color colors[] = {Color.blue, Color.cyan, Color.green, Color.red, Color.gray,
			Color.darkGray, Color.lightGray, Color.magenta, Color.orange, Color.pink,
			Color.yellow, Color.black, Color.white};
	private JComboBox fontCB, towardCB, fontstyleCB, fontcolorCB, scaleCB;
	private JTextField filepathTF, savepathTF, markTF, fontsizeTF;
	private JSlider alphaSlider;
	private Font font = new Font("微软雅黑", Font.PLAIN, 14);
	private Border border = new BevelBorder(BevelBorder.RAISED);// 简单的双线斜面边框
	private int mainFrameWidth = 550;// MainFrame的宽度
	private int mainFrameHeight = 410;// MainFrame的高度
	private JButton selectPathBT, batchingBT;
	private float alpha = 0.5f;
	private JProgressBar progressBar;

	//关于组件外观的枚举
	protected enum UIClassName {
		systemLookAndFeel(UIManager.getSystemLookAndFeelClassName()), 
		motifLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"), 
		crossPlatformLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()), 
		metalLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		private String className;

		UIClassName(String className) {
			this.className = className;
		}

		public String getUIClassName() {
			return className;
		}
	}

	//饿汉式 
	private static final MainFrame mainFrame = new MainFrame();
	static MainFrame getInstance() {
		return mainFrame;
	}
	private MainFrame() {
		// 设置外观
		setUI(UIClassName.systemLookAndFeel);
		setTitle("添加水印-当前状态：单个文件");
		setSize(mainFrameWidth, mainFrameHeight);
		Common.setCentered(this);// 居中显示
		setLayout(new BorderLayout());

		JPanel panel = getNorthPanel();
		add(panel, BorderLayout.NORTH);

		panel = getCenterPanel();
		add(panel, BorderLayout.CENTER);

		panel = getSouthPanel();
		add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		// 将当前窗口创建成拖放源
		new DropTarget(this, DnDConstants.ACTION_COPY,
				new ImageDropTargetListener());
	}

	private JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(border, "文件存取",
				Font.LAYOUT_LEFT_TO_RIGHT, Font.LAYOUT_LEFT_TO_RIGHT, font));

		GridBagConstraints gbc = new GridBagConstraints();
		// 组件的显示区域大于它所请求的显示区域的大小时使用此字段。HORIZONTAL：在水平方向而不是垂直方向上调整组件大小。
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// insets组件与其显示区域边缘之间间距的最小量
		gbc.insets = new Insets(5, 10, 5, 10);
		// 指定包含组件的显示区域开始边的"单元格"，其中行的第一个单元格为 gridx=0。
		gbc.gridx = 0;
		gbc.weightx = 1;
		filepathTF = new JTextField();
		panel.add(filepathTF, gbc);

		savepathTF = new JTextField();
		panel.add(savepathTF, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		
		selectPathBT = makeButton(CommandButton.ButtonStyle.selectImage, panel, gbc);
		makeButton(CommandButton.ButtonStyle.selectSavepath, panel, gbc);
		Common.setComponentsFont(panel, font);
		return panel;

	}

	private JPanel getCenterPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new TitledBorder(border, "设置",
				Font.LAYOUT_LEFT_TO_RIGHT, Font.LAYOUT_LEFT_TO_RIGHT, font));

		GridBagConstraints gbc = new GridBagConstraints();
		// 组件的显示区域大于它所请求的显示区域的大小时使用此字段。HORIZONTAL：在水平方向而不是垂直方向上调整组件大小。
		gbc.fill = GridBagConstraints.HORIZONTAL;
		// insets组件与其显示区域边缘之间间距的最小量
		gbc.insets = new Insets(5, 10, 5, 10);
		gbc.fill = GridBagConstraints.BOTH;
		
		
		gbc.gridwidth = 1;
		JLabel label;
		label = new JLabel("选择字体:", JLabel.RIGHT);
		panel.add(label, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER; // 结束当前行
		fontCB = new JComboBox(Common.getAvailableFontFamilyNames());
		panel.add(fontCB, gbc);

		gbc.weightx = 1.0;// 系统会将额外的空间按照其权重比例分布到每一列
		gbc.gridwidth = 1;

		label = new JLabel("字体大小:", JLabel.RIGHT);
		panel.add(label, gbc);

		fontsizeTF = new JTextField("15");
		panel.add(fontsizeTF, gbc);

		label = new JLabel("水印位置:", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		towardCB = new JComboBox(toward);
		panel.add(towardCB, gbc);
		
		//TODO
		gbc.gridwidth = 1;
		label = new JLabel("字体样式:", JLabel.RIGHT);
		panel.add(label, gbc);

		fontstyleCB = new JComboBox(fontstyle);
		panel.add(fontstyleCB, gbc);

		label = new JLabel("水印颜色:", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		fontcolorCB = new JComboBox(fontcolor);
		panel.add(fontcolorCB, gbc);
		//TODO
		gbc.weightx = 1.0;// 系统会将额外的空间按照其权重比例分布到每一列
		gbc.gridwidth = 1;
		label = new JLabel("透明度:", JLabel.RIGHT);
		panel.add(label, gbc);
		alphaSlider = new JSlider(JSlider.HORIZONTAL);
		alphaSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				alpha = (float)alphaSlider.getValue()/100;
			}
		});
		panel.add(alphaSlider, gbc);

		label = new JLabel("缩放大小:", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		scaleCB = new JComboBox(scales);
		panel.add(scaleCB, gbc);
		
		gbc.gridwidth = 1;
		label = new JLabel("水印内容：", JLabel.RIGHT);
		panel.add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		markTF = new JTextField("From CSDN Cannel_2020's blog");
		panel.add(markTF, gbc);
		
		
		gbc.gridwidth = 1;
		label = new JLabel("执行进度:", JLabel.RIGHT);
		panel.add(label, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		progressBar = new JProgressBar(0, 1);  
        //设置 stringPainted 属性的值  
        //该属性确定进度条是否应该呈现进度字符串。  
        progressBar.setStringPainted(true);  
        panel.add(progressBar, gbc);
		Common.setComponentsFont(panel, font);
		return panel;
	}
	private JPanel getSouthPanel() {
		JPanel panel = new JPanel();
		makeButton(CommandButton.ButtonStyle.preview, panel);
		batchingBT = makeButton(CommandButton.ButtonStyle.batching, panel);
		makeButton(CommandButton.ButtonStyle.moreSetting, panel);
		makeButton(CommandButton.ButtonStyle.drirect, panel);
		Common.setComponentsFont(panel, font);
		return panel;
	}
	/**
	 * 制按钮 两个参数
	 * @param style 枚举ButtonStyle中的一个值
	 * @param panel 放置该按钮的JPanel
	 * @return JButton
	 */
	private JButton makeButton(ButtonStyle style, JPanel panel) {
		return makeButton(style, panel, null);
	}
	/**
	 * 制按钮 三个参数
	 * @param style 枚举ButtonStyle中的一个值
	 * @param panel 放置该按钮的JPanel
	 * @param constraints 布局
	 * @return JButton
	 */
	private JButton makeButton(ButtonStyle style, JPanel panel, Object constraints) {
		JButton button = new CommandButton(style);
		if (constraints == null) {
			panel.add(button);
		} else {
			panel.add(button, constraints);
		}
		return button;
	}
	
	/**
	 * 设置外观
	 * @param className 枚举UIClassName
	 */
	protected void setUI(UIClassName className) {
		try {
			UIManager.setLookAndFeel(className.getUIClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取所要添加水印的文件名或文件夹名
	 * @return 
	 */
	public String getFilepath() {
		return filepathTF.getText();
	}
	/**
	 * 获取添加水印后保存文件的路径
	 * @return 
	 */
	public String getSavePath() {
		return savepathTF.getText();
	}
	/**
	 * @return 所要添加的水印的字体
	 */
	public Font getWaterMarkFont() {
		return new Font(getFontname(), getFontstyle(), getFontsize());
	}
	/**
	 * @return 所要添加的水印的内容
	 */
	public String getMark() {
		return markTF.getText();
	}
	/**
	 * @return 放置将要添加水印的文件路径的JTextField
	 */
	public JTextField getFilepathTF() {
		return filepathTF;
	}
	/**
	 * @return 放置将要添加水印后保存路径的JTextField
	 */
	public JTextField getSavepathTF() {
		return savepathTF;
	}
	/**
	 * @return 水印的颜色
	 */
	public Color getFontcolor() {
		return colors[fontcolorCB.getSelectedIndex()];
	}
	/**
	 * @return 水印的位置
	 */
	public int getToward() {
		return towardCB.getSelectedIndex();
	}
	/**
	 * @return 水印所选择的字体名字
	 */
	private String getFontname() {
		return String.valueOf(fontCB.getSelectedItem());
	}
	/**
	 * @return 水印所使用的字体样式
	 */
	private int getFontstyle() {
		return fontstyles[fontstyleCB.getSelectedIndex()];
	}
	/**
	 * @return 水印所使用的字体大小
	 */
	private int getFontsize() {
		return (int) (Integer.parseInt(fontsizeTF.getText())*getScale());
	}
	/**
	 * @param text 新的按钮的名字
	 */
	public void setSelectPathBtText(String text){
		selectPathBT.setText(text);
	}
	/**
	 * @param text 新的按钮的名字
	 */
	public void setBatchingBtText(String text){
		batchingBT.setText(text);
	}
	/**
	 * 
	 * @return 水印透明度
	 */
	public float getWaterMarkAlpha() {
		return alpha ;
	}
	/**
	 * 
	 * @return 放缩比例
	 */
	public float getScale() {
		return imgScales[scaleCB.getSelectedIndex()];
	}
	/**
	 * 
	 * @return 进度条
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
