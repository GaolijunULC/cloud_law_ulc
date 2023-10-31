package com.clyl.cloudlaw.util;

public class ImageUtil {

    /**
     * 获取文件后缀
     * @param fileName 文件名称
     * @return 文件后缀
     */
    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 根据文件扩展名判断文件是否图片格式
     * @param fileName 文件名称
     * @return 是否是图片
     */
    public static boolean isImage(String fileName) {
        String extension = getFileExtension(fileName);
        String[] imageExtension = new String[]{".jpeg", ".jpg", ".png"};
        for (String e : imageExtension){
            if (extension.toLowerCase().equals(e)) {
                return true;
            }
        }
        return false;
    }
}