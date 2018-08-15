package com.kreators.mybizkios.report;

import android.util.Log;
import android.view.Gravity;

import com.kreators.mybizkios.report.model.BestProductModel;
import com.kreators.mybizkios.report.model.CellModel;
import com.kreators.mybizkios.report.model.ColumnHeaderModel;
import com.kreators.mybizkios.report.model.RevenueModel;
import com.kreators.mybizkios.report.model.RowHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class TableViewModel
{
    // View Types
    public static final int GENDER_TYPE = 1;
    public static final int MONEY_TYPE = 2;

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    private static List<Integer> aligns;

    public int getCellItemViewType(int column) {

        switch (column) {
            case 5:
                // 5. column header is gender.
                return GENDER_TYPE;
            case 8:
                // 8. column header is Salary.
                return MONEY_TYPE;
            default:
                return 0;
        }
    }

    public static int getColumnTextAlign(int column) {
        return aligns.get(column);

    }

    private List<ColumnHeaderModel> createColumnHeaderModelList(List<String> header) {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        for(String s : header) list.add(new ColumnHeaderModel(s));


        /*list.add(new ColumnHeaderModel("Id"));
        list.add(new ColumnHeaderModel("Name"));
        list.add(new ColumnHeaderModel("Nickname"));
        list.add(new ColumnHeaderModel("Email"));
        list.add(new ColumnHeaderModel("Birthday"));
        list.add(new ColumnHeaderModel("Sex"));
        list.add(new ColumnHeaderModel("Age"));
        list.add(new ColumnHeaderModel("Job"));
        list.add(new ColumnHeaderModel("Salary"));
        list.add(new ColumnHeaderModel("CreatedAt"));
        list.add(new ColumnHeaderModel("UpdatedAt"));
        list.add(new ColumnHeaderModel("Address"));
        list.add(new ColumnHeaderModel("Zip Code"));
        list.add(new ColumnHeaderModel("Phone"));
        list.add(new ColumnHeaderModel("Fax"));*/
        return list;
    }

    private List<List<CellModel>> createCellModelList(List<Object> userList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service

        for (int i = 0; i < userList.size(); i++) {
            /*User user = userList.get(i);

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, user.id));          // "Id"
            list.add(new CellModel("2-" + i, user.name));        // "Name"
            list.add(new CellModel("3-" + i, user.nickname));    // "Nickname"
            list.add(new CellModel("4-" + i, user.email));       // "Email"
            list.add(new CellModel("5-" + i, user.birthdate));   // "BirthDay"
            list.add(new CellModel("6-" + i, user.gender));      // "Gender"
            list.add(new CellModel("7-" + i, user.age));         // "Age"
            list.add(new CellModel("8-" + i, user.job));         // "Job"
            list.add(new CellModel("9-" + i, user.salary));      // "Salary"
            list.add(new CellModel("10-" + i, user.created_at)); // "CreatedAt"
            list.add(new CellModel("11-" + i, user.updated_at)); // "UpdatedAt"
            list.add(new CellModel("12-" + i, user.address));    // "Address"
            list.add(new CellModel("13-" + i, user.zipcode));    // "Zip Code"
            list.add(new CellModel("14-" + i, user.mobile));     // "Phone"
            list.add(new CellModel("15-" + i, user.fax));        // "Fax"*/

            List<CellModel> list = new ArrayList<>();
            switch(userList.get(i).getClass().getName())
            {
                case "com.kreators.mybizkios.report.model.RevenueModel":
                    RevenueModel revModel = (RevenueModel)userList.get(i);
                    if(revModel.getDocdate() != null)
                        list.add(new CellModel("1-" + i, revModel.getDocdate()));
                    list.add(new CellModel("2-" + i, revModel.getCode()));
                    list.add(new CellModel("3-" + i, revModel.getName()));
                    list.add(new CellModel("4-" + i, revModel.getPaymentType()));
                    list.add(new CellModel("5-" + i, revModel.getPaymentMethod()));
                    list.add(new CellModel("6-" + i, revModel.getSubtotal()));
                    list.add(new CellModel("7-" + i, revModel.getGrandTotal()));
                    list.add(new CellModel("8-" + i, revModel.getPaidTotal()));
                    break;
                case "com.kreators.mybizkios.report.model.BestProductModel":
                    BestProductModel besModel = (BestProductModel)userList.get(i);
                    list.add(new CellModel("1-" + i, besModel.getItemName()));
                    list.add(new CellModel("2-" + i, besModel.getQty()));
                    list.add(new CellModel("3-" + i, besModel.getTotalTrans()));
                    list.add(new CellModel("4-" + i, besModel.getPrice()));
                    list.add(new CellModel("5-" + i, besModel.getTotalPrice()));
                    break;
                default:
                    break;
            }

            // Add
            lists.add(list);
        }

        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }


    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }


    public void generateListForTableView(List<Object> users, List<String> header, List<Integer> aligns) {
        this.aligns = aligns;
        mColumnHeaderModelList = createColumnHeaderModelList(header);
        mCellModelList = createCellModelList(users);
        mRowHeaderModelList = createRowHeaderList(users.size());
    }
}
