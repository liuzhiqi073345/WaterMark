package watermark;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageDropTargetListener extends DropTargetAdapter {
	private List<File> fileList;;
	private MainFrame mainFrame;
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent event) {
		// 接受复制操作
		event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		// 获取拖放的内容
		Transferable transferable = event.getTransferable();
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		
		// 遍历拖放内容里的所有数据格式
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor d = flavors[i];
			try {
				// 如果拖放内容的数据格式是文件列表
				if (d.equals(DataFlavor.javaFileListFlavor)) {
					// 取出拖放操作里的文件列表
					fileList = (List<File>) transferable
							.getTransferData(d);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 强制拖放操作结束，停止阻塞拖放源
			event.dropComplete(true);
		}

		mainFrame = MainFrame.getInstance();
		if(fileList == null || fileList.size() == 0){//不存在任何文件，返回
			System.out.println("纠结：拖入较多时却为空！");
			return;
		}
		List<File> imgFiles= getImageFiles(fileList);
		if(imgFiles.size() == 0){//不存在图片文件，返回
			return;
		}
		File file = getImageFiles(fileList).get(0);
		String filename = file.getAbsolutePath();
		if(imgFiles.size() == 1){
			mainFrame.getFilepathTF().setText(filename);
			mainFrame.getSavepathTF().setText(filename.substring(0, filename.lastIndexOf("\\"))
					+ Common.getNewFileorDirName(filename));
		}else if(imgFiles.size() > 1){
			mainFrame.getFilepathTF().setText(filename.substring(0, filename.lastIndexOf("\\")));
			mainFrame.getSavepathTF().setText(filename.substring(0, filename.lastIndexOf("\\"))
					+ "\\加水印后");
			ButtonAction.array = getBufferImages(imgFiles);
			ButtonAction.isDrag = true;
			ButtonAction.isSingle = false;
		}
		BufferedImage buffImg = addWatermark(file);
		new PreviewImage(buffImg);
	}
	/**
	 * @param filepath 图片的绝对路径
	 * @return 图像加水印之后的BufferedImage对象
	 */
	private BufferedImage addWatermark(File file) {
		Color fontColor = mainFrame.getFontcolor();
		String mark = mainFrame.getMark();
		int toward = mainFrame.getToward();
		Font font = mainFrame.getWaterMarkFont();
		float alpha = mainFrame.getWaterMarkAlpha();
		float scale = mainFrame.getScale();
		BufferedImage buffImg = ImageTool.watermark(file, font, fontColor,
				toward, mark, alpha, scale);
		return buffImg;
	}
	/**
	 * 过滤掉非图片文件
	 * @param fileList 所有的文件
	 * @return 图片文件的List<File>
	 */
	private List<File> getImageFiles(List<File> fileList){
		List<File> imgFileList = new ArrayList<File>();
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			if (!file.isDirectory()) {
				String filename = file.getName();
				if(Common.isImageFile(filename)){
					imgFileList.add(file);
				}
			}
		}
		return imgFileList;
	}
	private ArrayList<FileBean> getBufferImages(List<File> imgFileList){
		ArrayList<FileBean> array = new ArrayList<FileBean>();
		for (int i = 0; i < imgFileList.size(); i++) {
			String filename = imgFileList.get(i).getName();
			// 对图像加水印
			BufferedImage buffImg = addWatermark(imgFileList.get(i));
			array.add(new FileBean(filename, buffImg));
		}
		return array;
	}
}