package com.armavi_bsd.robotispnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispnew.databinding.ActivityComplainSubmitBinding;
import com.armavi_bsd.robotispnew.dialogs.ComplainTypeDialogFragment;
import com.armavi_bsd.robotispnew.dialogs.CustomerDialogFragment;
import com.armavi_bsd.robotispnew.dialogs.EmployeeDialogFragment;
import com.armavi_bsd.robotispnew.dialogs.SuccessDialog;
import com.armavi_bsd.robotispnew.dialogs.WarningDialog;
import com.armavi_bsd.robotispnew.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispnew.urlStorage.URLStorage;
import com.armavi_bsd.robotispnew.util.Pref;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplainSubmit extends AppCompatActivity {

    URLStorage urlStorage = new URLStorage();
    Pref pref = new Pref();
    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    private List<String> selectedIds = new ArrayList<>();
    SharedPreferences sharedPreferences;
    CustomerDialogFragment dialog = new CustomerDialogFragment();
    ComplainTypeDialogFragment dialogComplainType = new ComplainTypeDialogFragment();
    EmployeeDialogFragment dialogEmployeeFragment = new EmployeeDialogFragment();
    private ActivityComplainSubmitBinding binding;
    String priorityValStore, smsCheckValStore = "", employeeCheckValStore;
    String userID;
    String sub_solve_by = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplainSubmitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(),MODE_PRIVATE);
        userID = sharedPreferences.getString(pref.getPrefUserID(), "");


        binding.customerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnCustomerSelectedListener((agId, agName) -> {
                    // Set selected values in TextViews
                    binding.customerSelect.setText(agName);
                    binding.customerIDTxt.setText(agId);
                });
                dialog.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });
        binding.complainTypeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComplainType.setOnCustomerSelectedListener((id, template) -> {
                    // Set selected values in TextViews
                    binding.complainTypeSelect.setText(template);
                    binding.complainTypeIDText.setText(id);
                });
                dialogComplainType.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });

        binding.employeeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmployeeFragment.setOnCustomerSelectedListener((id, employeeName) -> {
                    // Set selected values in TextViews
                    binding.employeeSelect.setText(employeeName);
                    binding.employeeIdText.setText(id);
                });
                dialogEmployeeFragment.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });

        binding.employeeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmployeeFragment.setOnCustomerSelectedListener((id, employeeName) -> {
                    // Set selected values in TextViews
//                    binding.employeeSelect.setText(employeeName);
                    addChip(employeeName, id);
                });
                dialogEmployeeFragment.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });

        if(binding.smsCheck.isChecked()){
            smsCheckValStore = "1";
        } else {
            smsCheckValStore = "";
        }

        if(binding.employeeCheck.isChecked()){
            employeeCheckValStore = "1";
        }else {
            employeeCheckValStore = "";
        }

        binding.spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    priorityValStore = "1";
                }else if(position == 1){
                    priorityValStore = "2";
                } else if (position == 2) {
                    priorityValStore = "3";
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.complainPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.smsCheck.isChecked()){
                    smsCheckValStore = "1";
                } else {
                    smsCheckValStore = "";
                }

                if(binding.employeeCheck.isChecked()){
                    employeeCheckValStore = "1";
                }else {
                    employeeCheckValStore = "";
                }

                if(binding.customerSelect.getText().toString().trim().isEmpty()){
                    WarningDialog.warningDialog(ComplainSubmit.this, "Select Customer");
                }else if (binding.complainTypeSelect.getText().toString().trim().isEmpty()) {
                    WarningDialog.warningDialog(ComplainSubmit.this, "Select Complain Type");
                } else if (binding.complainDetailsText.getText().toString().trim().isEmpty()) {
                    WarningDialog.warningDialog(ComplainSubmit.this, "Type complain Details");
                } else if (binding.complainNoteText.getText().toString().trim().isEmpty()) {
                    WarningDialog.warningDialog(ComplainSubmit.this, "Type complain Note");
                } else if (binding.employeeSelect.getText().toString().trim().isEmpty()) {
                    WarningDialog.warningDialog(ComplainSubmit.this, "Select Employee");
                }  else {
                    submitComplain();
                }
            }
        });
    }
    private void addChip(String employeeName, String id) {
        if (selectedIds.contains(id)) {
            Toast.makeText(this, employeeName + " is already selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Chip chip = new Chip(this);
        chip.setText(employeeName);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            binding.chipGroup.removeView(chip);
            selectedIds.remove(id);
            updateSelectedIdsTextView();
        });

        binding.chipGroup.addView(chip);
        selectedIds.add(id);
        updateSelectedIdsTextView();
    }

    private void updateSelectedIdsTextView() {
        binding.selectedIdsTextView.setText(String.join(",", selectedIds));
    }

    private void submitComplain() {
        sub_solve_by = binding.selectedIdsTextView.getText().toString().trim();
//
        String urlSubmitComplain = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getComplainSubmitEndpoint();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSubmitComplain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the API
                        Log.d("Volley Response", response.toString());
                        if(response.toString().equals("1")){
                            SuccessDialog.successDialog(ComplainSubmit.this, "Complain posted successfully.");
                            navigationWithEndState.navigateToActivity(ComplainSubmit.this, Complain.class);
                        } else if (response.toString().equals("0")) {
                            WarningDialog.warningDialog(ComplainSubmit.this, "Error!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any error that occurs during the request
                        Toast.makeText(ComplainSubmit.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a map to hold the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("details", binding.complainDetailsText.getText().toString().trim());
                params.put("note", binding.complainNoteText.getText().toString().trim());
                params.put("complain_type", binding.complainTypeIDText.getText().toString().trim());
                params.put("customer_id", binding.customerIDTxt.getText().toString().trim());
                params.put("priority", priorityValStore);
                params.put("employee_id", binding.employeeIdText.getText().toString());
                params.put("customer_sms", smsCheckValStore);
                params.put("employee_sms", employeeCheckValStore);
                params.put("user_id", userID);
                params.put("sub_solve_by", sub_solve_by);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        RequestQueue requestQueueSendAlert = Volley.newRequestQueue(this);
        requestQueueSendAlert.add(stringRequest);
    }
    void toastFunc(String tMessage){
        Toast.makeText(getApplicationContext(), tMessage, Toast.LENGTH_SHORT).show();
    }

}