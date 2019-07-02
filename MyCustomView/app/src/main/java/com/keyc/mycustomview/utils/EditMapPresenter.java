package com.keyc.mycustomview.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.keyc.mycustomview.bean.NodeModel;
import com.keyc.mycustomview.bean.TreeModel;
import com.keyc.mycustomview.listener.EditMapContract;

import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by owant on 21/03/2017.
 */
public class EditMapPresenter implements EditMapContract.Presenter {

    private EditMapContract.View mView;

    private boolean mIsCreate;
    private String mFilePath;
    private String mDefaultFilePath;
    private String mFileName;
    private TreeModel<String> mTreeModel;

    private TreeModel<String> mOldTreeModel;

    private String[] mOwantFilesArray;

    public EditMapPresenter(EditMapContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mIsCreate = true;
        mFileName = mView.getDefaultPlanStr();
        mView.showLoadingFile();
    }

    @Override
    public void onRecycle() {
        mOwantFilesArray = null;
        mTreeModel = null;
    }

    @Override
    public void setLoadMapPath(String path) {
        // 获取到是否是编辑文件
        // 文件的名字
        // 文件路径下的owant file lists
        mIsCreate = false;

        Log.i("owant file path=%s", path);
        mFilePath = path;

        String saveFileName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        mFileName = saveFileName;

        refreshOwantFilesLists();
    }

    @Override
    public void createDefaultTreeModel() {
        NodeModel<String> plan = new NodeModel<>(mView.getDefaultPlanStr());
        mTreeModel = new TreeModel<>(plan);
        mView.setTreeViewData(mTreeModel);
        refreshOwantFilesLists();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void refreshOwantFilesLists() {
        if (!TextUtils.isEmpty(mFilePath)) {
            File editFilePath = new File(mFilePath);
            String[] lists = editFilePath.getParentFile().list();

            //设置存在的owant集合
            sortFiles(lists);

        } else {
            // 默认文件路径
            mDefaultFilePath = mView.getDefaultSaveFilePath();
            File file = new File(mDefaultFilePath);
            if (!file.exists()) {
                file.mkdirs();
                Log.i("TAG", "create default file path=%s");
            }
            Log.i("defaultFilePath=%s", mDefaultFilePath);
            if (file.exists()) {
                String[] lists = file.list();
                Log.i("TAG", "lists=" + lists);
//                sortFiles(lists);
            } else {
                Log.i("TAG", "defaultPath is empty!");
            }
        }
    }

    private void sortFiles(String[] lists) {
        ArrayList<String> owantFiles = new ArrayList<>();
        for (String fileName : lists) {
            if (fileName.endsWith(".owant")) {
                Log.i("file=%s", fileName);
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                if (!TextUtils.isEmpty(fileName)) {
                    owantFiles.add(fileName);
                }

                //编辑模式，不能修改文件名字
                if (!mIsCreate) {
                    owantFiles.remove(fileName);
                }
            }
        }

        if (owantFiles.size() > 0) {
            mOwantFilesArray = owantFiles.toArray(new String[owantFiles.size()]);
            if (AppConstants.CONFIG_DEBUG) {
                for (String str : mOwantFilesArray) {
                    Log.i("TAG", "mOwantFilesArray is empty! str=%s" +  str);
                }
            }
        }

    }

    @Override
    public void readOwantFile() {
        //读取owant文件
        if (!TextUtils.isEmpty(mFilePath)) {
            try {
                OwantFileCreate owantFileCreate = new OwantFileCreate();
                Log.i("filePath=%s", mFilePath);
                Object o = owantFileCreate.readContentObject(mFilePath);
                TreeModel<String> tree = (TreeModel<String>) o;
                mTreeModel = tree;
                mView.setTreeViewData(mTreeModel);

                mIsCreate = false;
                //拷贝一份
                mOldTreeModel = (TreeModel<String>) mTreeModel.deepClone();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvalidClassException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addNote() {
        mView.showAddNoteDialog();
    }

    @Override
    public void addSubNote() {
        mView.showAddSubNoteDialog();
    }

    @Override
    public void editNote() {
        mView.showEditNoteDialog();
    }

    @Override
    public void focusMid() {
        mView.focusingMid();
    }

    @Override
    public void saveFile() {
        //TODO 进行判断是否改变了文本
        //只有在编剧模式下才进行判断其他的跳过
        boolean equals = false;
        if (!mIsCreate) {
            //进行判断
            equals = isEqualsOldTreeModel();
        }

        if (equals) {
            Log.i("no change =%s", "true");
            mView.finishActivity();
        } else {
            Log.i("change =%s", "false");
            mView.showSaveFileDialog(mFilePath);
        }
    }

    private boolean isEqualsOldTreeModel() {
        boolean equals = false;
        TreeModel<String> temp = mTreeModel;
        TreeModel<String> compareTemp = mOldTreeModel;

        StringBuffer tempBuffer = new StringBuffer();
        Stack<NodeModel<String>> stack = new Stack<>();
        NodeModel<String> rootNode = temp.getRootNode();
        stack.add(rootNode);
        while (!stack.isEmpty()) {
            NodeModel<String> pop = stack.pop();
            tempBuffer.append(pop.value);
            LinkedList<NodeModel<String>> childNodes = pop.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                stack.add(item);
            }
        }

        StringBuffer compareTempBuffer = new StringBuffer();
        Stack<NodeModel<String>> stackThis = new Stack<>();
        NodeModel<String> rootNodeThis = compareTemp.getRootNode();
        stackThis.add(rootNodeThis);
        while (!stackThis.isEmpty()) {
            NodeModel<String> pop = stackThis.pop();
            compareTempBuffer.append(pop.value);
            LinkedList<NodeModel<String>> childNodes = pop.getChildNodes();
            for (NodeModel<String> item : childNodes) {
                stackThis.add(item);
            }
        }

        if (compareTempBuffer.toString().equals(tempBuffer.toString())) {
            equals = true;
        }
        return equals;
    }

    @Override
    public void doSaveFile(String fileName) {
        OwantFileCreate owantFileCreate = new OwantFileCreate();
        owantFileCreate.createOwantMapsDirectory();
        owantFileCreate.createTempDirectory();

        Conf conf = new Conf();
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        conf.date = simpleDateFormat.format(time);
        conf.app_version = mView.getAppVersion();
        conf.android_version = AndroidUtil.getAndroidSystemVersion();
        conf.map_name = mTreeModel.getRootNode().getValue();
        owantFileCreate.writeConf(conf);

        owantFileCreate.writeContent(mTreeModel);

//        //如果是创建模式
//        if (mIsCreate) {
//            owantFileCreate.makeOwantFile(mTreeModel.getRootNode().getValue());
//        } else {
//            owantFileCreate.makeOwantFile(mFileName);
//        }
        owantFileCreate.makeOwantFile(fileName);
        owantFileCreate.deleteTemp();
    }

    @Override
    public void setTreeModel(TreeModel<String> treeModel) {
        mTreeModel = treeModel;
        mView.setTreeViewData(mTreeModel);
    }

    @Override
    public TreeModel<String> getTreeModel() {
        return mTreeModel;
    }

    @Override
    public List getOwantLists() {
        List list = new ArrayList();
        Log.i("TAG", "isCreate=%s" + mIsCreate);
        if (mOwantFilesArray != null) {
            for (String s : mOwantFilesArray) {
                list.add(s);
                Log.i("exist file=%s", s);
            }
        } else {
            Log.i("TAG", "mOwantFilesArray is empty");
        }
        return list;
    }

    @Override
    public String getSaveInput() {
        if (mIsCreate) {
            return mTreeModel.getRootNode().getValue();
        } else {
            return mFileName;
        }
    }

}
