package cn.yzl.translate;

import cn.yzl.translate.ui.SelectUI;
import cn.yzl.utils.FileUtil;
import cn.yzl.utils.MessagesCenter;
import cn.yzl.utils.StringUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * 中文自动翻译为英文插件
 * Created by YZL on 2017/4/25.
 */
public class Translate extends AnAction {

    Editor editor;
    private String selectedText;
    private AnActionEvent event;
    private String resultText;
    private Project mProject;
    private Document document;

    @Override
    public void actionPerformed(AnActionEvent event) {
        try {
            initData(event);

            if (selectedText == null || selectedText.equals("")) {
                return;
            }

            Modle modle = TranslateUtil.translateToEn(selectedText);

            resultText = modle.getTranslation().get(0);
            if (resultText == null) {
                return;
            }
            if (StringUtil.isContainChinese(selectedText)) {
                //译成英文的情况
                showSeletUI();
            } else {
                showPop(modle.toString());
            }
        } catch (NullPointerException e) {
            MessagesCenter.showErrorMessage("翻译失败", "插件错误");
        } catch (Exception e1) {
            e1.printStackTrace();
            MessagesCenter.showErrorMessage("翻译失败", "插件错误");
        }
    }

    /**
     * 保存基本变量
     * @param event
     */
    private void initData(AnActionEvent event) {
        this.event = event;
        editor = event.getData(PlatformDataKeys.EDITOR);
        selectedText = editor.getSelectionModel().getSelectedText();
        mProject = event.getData(PlatformDataKeys.PROJECT);
        document = editor.getDocument();
    }

    /**
     * 显示选择dialog
     */
    private void showSeletUI() {
        SelectUI ui = new SelectUI(resultText, text -> changeSelectText(text));
        ui.setVisible(true);
    }

    /**
     * 缓存真实的替换文字
     */
    private String realText;
    /**
     * 中文翻译后
     * 替换文字
     */
    private void changeSelectText(String text) {
        realText = text;
        Runnable runnable = () -> document.replaceString(editor.getSelectionModel().getSelectionStart(),
                editor.getSelectionModel().getSelectionEnd(), realText);
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
        editor.getSelectionModel().removeSelection();
    }

    /**
     * 显示英文翻译解释
     * @param text
     */
    public void showPop(String text) {
        //翻译成中文
        JBPopupFactory jbPopupFactory = JBPopupFactory.getInstance();
        jbPopupFactory.createHtmlTextBalloonBuilder(text,
                null,
                new JBColor(new Color(186, 238, 186),
                        new Color(73, 117, 73)),
                null)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(jbPopupFactory.guessBestPopupLocation(editor),
                        Balloon.Position.below);
    }
}
