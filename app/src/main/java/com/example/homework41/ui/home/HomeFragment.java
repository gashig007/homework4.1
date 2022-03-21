package com.example.homework41.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.homework41.Model;
import com.example.homework41.OnClick;
import com.example.homework41.R;
import com.example.homework41.adapter.ProfileAdapter;
import com.example.homework41.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ProfileAdapter adapter;
    private boolean isChanged = false;
    private int position;


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
                Model model = adapter.getItem(position);
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
                        adapter.deleteItem(position);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ProfileAdapter();
    }
}