package com.example.homework41.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.homework41.App;
import com.example.homework41.Model.Model;
import com.example.homework41.OnClick;
import com.example.homework41.R;
import com.example.homework41.adapter.ProfileAdapter;
import com.example.homework41.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ProfileAdapter adapter;
    private boolean isChanged = false;
    private int position;
    Model model;
    private List<Model> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new ProfileAdapter();
        adapter.addList(App.getDataBase().newsDao().getAll());
    }

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                open(null);
            }
        });

        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list = App.getDataBase().newsDao().getSearch(editable.toString());
                adapter.addList(list);
            }
        });

        binding.recycleView.setAdapter(adapter);
        list = App.getDataBase().newsDao().sortAll();
        adapter.addList(list);

        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Model model = (Model) result.getSerializable("model");
                Log.e("Home", "text = " + model.getTitle());
                if (isChanged) adapter.updateItem(model, position);
                else adapter.addItem(model);

            }
        });
        binding.recycleView.setAdapter(adapter);


        adapter.setOnClickListener(new OnClick() {
            @Override
            public void onClick(int position) {
                model = adapter.getItem(position);
                isChanged = true;
                open(model);
                HomeFragment.this.position = position;
            }

            @Override
            public void onLongClick(int position) {
                new AlertDialog.Builder(getContext()).setTitle("Delete").setMessage("Вы уверены что хотите удалить?").
                        setNegativeButton("Отмена", null).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        model = adapter.getItem(position);
                        adapter.deleteItem(position);
                        App.getDataBase().newsDao().deleteTask(model);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    private void open(Model model) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putSerializable("update", model);
        navController.navigate(R.id.newsFragment, bundle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort){
            adapter.setData(App.getDataBase().newsDao().sort());
//            binding.recycleView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }


}