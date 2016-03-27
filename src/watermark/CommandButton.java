package watermark;

import javax.swing.JButton;
/**
 * 实现了命令接口的按钮
 * @author CSDN's Cannel_2020
 *
 */
public class CommandButton extends JButton {

	private static final long serialVersionUID = 1L;
	/**
	 * 有个按钮类型的枚举ButtonStyle
	 */
	protected enum ButtonStyle{
		selectImage("选择图片"),
		selectSavepath("选择存放路径"),
		preview("预览效果"),
		batching("批量添加"),
		moreSetting("更多设置"),
		drirect("添加水印");
		private String name;
		ButtonStyle(String name){
			this.name = name;
		}
		public String getButtonName(){
			return name;
		}
	}
	/**
	 * @param style 有个按钮类型的枚举ButtonStyle
	 */
	public CommandButton(ButtonStyle style){
		super(style.getButtonName());
		addActionListener(new ButtonAction(style));
	}
}
